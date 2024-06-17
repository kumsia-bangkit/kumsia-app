package com.dicoding.kumsiaapp.view.organization.event

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.data.local.UserPreferences
import com.dicoding.kumsiaapp.data.local.dataStore
import com.dicoding.kumsiaapp.data.remote.request.EventRequestDTO
import com.dicoding.kumsiaapp.data.remote.response.EventsItem
import com.dicoding.kumsiaapp.databinding.ActivityUpdateEventPreferencesBinding
import com.dicoding.kumsiaapp.utils.reduceFileImage
import com.dicoding.kumsiaapp.utils.uriToFile
import com.dicoding.kumsiaapp.view.organization.OrganizationActivity
import com.dicoding.kumsiaapp.viewmodel.EventViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModelFactory
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class UpdateEventPreferencesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateEventPreferencesBinding
    private val eventViewModel: EventViewModel by lazy {
        ViewModelProvider(this)[EventViewModel::class.java]
    }
    private lateinit var token: String

    private val listOfGender = mutableListOf<String>()
    private val listOfReligion = mutableListOf<String>()
    private val listOfInterests = mutableListOf<String>()
    private val listOfCity = mutableListOf<String>()

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateEventPreferencesBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val pref = UserPreferences.getInstance(application.dataStore)
        val sessionViewModel = ViewModelProvider(this, SessionViewModelFactory(pref))[SessionViewModel::class.java]

        sessionViewModel.getUserToken().observe(this) {
            token = it!!
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.addCityChip.setOnClickListener {
            addCityDialog()
        }

        eventViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        eventViewModel.eventItemData.observe(this) {
            it.getContentIfNotHandled()?.let { data ->
                showToast("Event draft is successfully updated!")
                val intent = Intent(this, OrganizationDetailEventActivity::class.java)
                intent.putExtra(OrganizationDetailEventActivity.EVENT_DATA, data)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
                finish()
            } ?: run {
                showToast("Failed to update event draft!")
            }
        }

        binding.eventGenderGroup.setOnCheckedStateChangeListener{ chipGroup, checkedIds ->
            listOfGender.clear()
            checkedIds.forEach {
                val chip = chipGroup.findViewById<Chip>(it)
                listOfGender.add(chip.text.toString())
            }
        }

        binding.eventReligionGroup.setOnCheckedStateChangeListener { chipGroup, checkedIds ->
            listOfReligion.clear()
            checkedIds.forEach {
                val chip = chipGroup.findViewById<Chip>(it)
                listOfReligion.add(chip.text.toString())
            }
        }

        binding.eventInterestGroup.setOnCheckedStateChangeListener { chipGroup, checkedIds ->
            listOfInterests.clear()
            checkedIds.forEach {
                val chip = chipGroup.findViewById<Chip>(it)
                listOfInterests.add(chip.text.toString())
            }
        }

        val previousEventData = intent.getParcelableExtra<EventsItem>(UpdateEventActivity.PREVIOUS_DATA)
        setPreferencesData(previousEventData)

        binding.saveButton.setOnClickListener {
            val eventData = intent.getParcelableExtra<EventRequestDTO>(UpdateEventActivity.EVENT_DATA)
            val imageUri = intent.getParcelableExtra<Uri>(UpdateEventActivity.IMAGE_FILE)
            val imageMultipart: MultipartBody.Part?

            eventData?.genderPreference = listOfGender
            eventData?.religionPreference = listOfReligion
            eventData?.hobbyPreference = listOfInterests
            eventData?.cityPreference = listOfCity

            // Convert event data into json format
            val gson = Gson()
            val jsonData = gson.toJson(eventData)
            val requestBody = jsonData.toRequestBody("text/plain".toMediaType())

            if (imageUri == null) {
                eventViewModel.updateEvent(token, previousEventData?.eventId!!, null, requestBody)
            } else {
                val imageFile = uriToFile(imageUri, this).reduceFileImage()
                val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                imageMultipart = MultipartBody.Part.createFormData(
                    "file",
                    imageFile.name,
                    requestImageFile
                )
                eventViewModel.updateEvent(token, previousEventData?.eventId!!, imageMultipart, requestBody)
            }

        }
    }

    private fun setPreferencesData(previousEventData: EventsItem?) {
        previousEventData?.genderPreference?.forEach {
            listOfGender.add(it!!)
        }

        previousEventData?.religionPreference?.forEach {
            listOfReligion.add(it!!)
        }

        previousEventData?.hobbyPreference?.forEach {
            listOfInterests.add(it!!)
        }

        previousEventData?.cityPreference?.forEach {
            addNewChip(it!!)
        }

        addGenderChips(previousEventData?.genderPreference)
        addReligionChips(previousEventData?.religionPreference)
        addInterestsChips(previousEventData?.hobbyPreference)
    }

    private fun addGenderChips(list: List<String?>?) {
        val interestsArray = resources.getStringArray(R.array.gender_array)
        val interestsList = interestsArray.toList()
        val chipGroup = binding.eventGenderGroup

        interestsList.forEachIndexed { index, interest ->
            val chip =
                layoutInflater.inflate(R.layout.item_chip_preference, chipGroup, false) as Chip
            chip.text = interest
            chip.id = index
            chip.isClickable = true
            chip.isCheckable = true
            chip.textAlignment = View.TEXT_ALIGNMENT_CENTER
            chip.layoutParams = ChipGroup.LayoutParams(
                ChipGroup.LayoutParams.WRAP_CONTENT,
                ChipGroup.LayoutParams.WRAP_CONTENT
            )

            chip.isChecked = list?.contains(interest)!!
            chipGroup.addView(chip)
        }
    }

    private fun addReligionChips(list: List<String?>?) {
        val interestsArray = resources.getStringArray(R.array.religion_array)
        val interestsList = interestsArray.toList()
        val chipGroup = binding.eventReligionGroup

        interestsList.forEachIndexed { index, interest ->
            val chip =
                layoutInflater.inflate(R.layout.item_chip_preference, chipGroup, false) as Chip
            chip.text = interest
            chip.id = index
            chip.isClickable = true
            chip.isCheckable = true
            chip.textAlignment = View.TEXT_ALIGNMENT_CENTER
            chip.layoutParams = ChipGroup.LayoutParams(
                ChipGroup.LayoutParams.WRAP_CONTENT,
                ChipGroup.LayoutParams.WRAP_CONTENT
            )

            chip.isChecked = list?.contains(interest)!!
            chipGroup.addView(chip)
        }
    }

    private fun addInterestsChips(list: List<String?>?) {
        val interestsArray = resources.getStringArray(R.array.interests_array)
        val interestsList = interestsArray.toList()
        val chipGroup = binding.eventInterestGroup

        interestsList.forEachIndexed { index, interest ->
            val chip =
                layoutInflater.inflate(R.layout.item_chip_preference, chipGroup, false) as Chip
            chip.text = interest
            chip.id = index
            chip.isClickable = true
            chip.isCheckable = true
            chip.textAlignment = View.TEXT_ALIGNMENT_CENTER
            chip.layoutParams = ChipGroup.LayoutParams(
                ChipGroup.LayoutParams.WRAP_CONTENT,
                ChipGroup.LayoutParams.WRAP_CONTENT
            )

            chip.isChecked = list?.contains(interest)!!
            chipGroup.addView(chip)
        }
    }

    private fun addCityDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(
            R.layout.dialog_searchable_spinner)
        dialog.window?.setLayout(1000, 800)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        // Initialize and assign variable
        val editText: EditText = dialog.findViewById(R.id.edit_text)
        val listView: ListView = dialog.findViewById(R.id.list_view)

        val adapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(
            this,
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.city_array)
        )


        listView.adapter = adapter
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        listView.onItemClickListener =
            OnItemClickListener { _, _, position, _ ->
                addNewChip(adapter.getItem(position).toString())
                dialog.dismiss()
            }

    }

    private fun addNewChip(chipText: String) {
        val chipGroup = binding.eventCityGroup
        val chip =
            layoutInflater.inflate(R.layout.item_chip_preference, chipGroup, false) as Chip
        chip.text = chipText
        chip.id = View.generateViewId()
        chip.isClickable = false
        chip.isCheckable = true
        chip.isChecked = true
        chip.textAlignment = View.TEXT_ALIGNMENT_CENTER
        chip.layoutParams = ChipGroup.LayoutParams(
            ChipGroup.LayoutParams.WRAP_CONTENT,
            ChipGroup.LayoutParams.WRAP_CONTENT
        )
        chip.isCloseIconVisible = true
        chip.setCloseIconResource(R.drawable.baseline_close_24)
        chip.setOnCloseIconClickListener {
            listOfCity.remove(chipText)
            chipGroup.removeView(chip)
        }

        listOfCity.add(chipText)
        chipGroup.addView(chip, 0)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
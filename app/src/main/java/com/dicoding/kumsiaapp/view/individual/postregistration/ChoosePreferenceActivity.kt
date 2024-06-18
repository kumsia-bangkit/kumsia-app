package com.dicoding.kumsiaapp.view.individual.postregistration

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.dicoding.kumsiaapp.data.remote.request.UpdateUserProfileDTO
import com.dicoding.kumsiaapp.databinding.ActivityChoosePreferenceBinding
import com.dicoding.kumsiaapp.utils.JwtDecoder
import com.dicoding.kumsiaapp.utils.reduceFileImage
import com.dicoding.kumsiaapp.utils.uriToFile
import com.dicoding.kumsiaapp.view.individual.IndividualActivity
import com.dicoding.kumsiaapp.view.organization.OrganizationActivity
import com.dicoding.kumsiaapp.viewmodel.ProfileViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModelFactory
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class ChoosePreferenceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChoosePreferenceBinding
    private val profileViewModel: ProfileViewModel by lazy {
        ViewModelProvider(this)[ProfileViewModel::class.java]
    }
    private val listOfGender = mutableListOf<String>()
    private val listOfReligion = mutableListOf<String>()
    private val listOfInterests = mutableListOf<String>()
    private val listOfCity = mutableListOf<String>()

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChoosePreferenceBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userData = intent.getParcelableExtra<UpdateUserProfileDTO>(USER_DATA)
        val imageUri = intent.getParcelableExtra<Uri>(IMAGE_URI)
        val token = intent.getStringExtra(TOKEN)

        val pref = UserPreferences.getInstance(application.dataStore)
        val sessionViewModel = ViewModelProvider(this, SessionViewModelFactory(pref))[SessionViewModel::class.java]

        profileViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        profileViewModel.tokenData.observe(this) {
            it.getContentIfNotHandled()?.let { data ->
                val claims = JwtDecoder.decode(data.accessToken!!)
                val name = claims.getClaim("name").asString()
                val role = claims.getClaim("role").asString()
                val isNewUser = claims.getClaim("is_new_user").asBoolean() ?: true
                sessionViewModel.saveSession(data.accessToken, name!!, role!!, isNewUser)

                val intent = Intent(this, IndividualActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)

                // Flag to show whether the user update their data or skip the process
                if (imageUri != null ) {
                    showToast("Data is successfully updated!")
                }
            } ?: run {
                showToast("Failed to update organization data!")
            }
        }

        binding.addCityChip.setOnClickListener {
            addCityDialog()
        }
        addGenderChips()
        addReligionChips()
        addInterestsChips()

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

        binding.saveButton.setOnClickListener {
            val imageFile = uriToFile(imageUri!!, this).reduceFileImage()
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "file",
                imageFile.name,
                requestImageFile
            )

            userData?.genderPreference = listOfGender
            userData?.religionPreference = listOfReligion
            userData?.hobbyPreference = listOfInterests
            userData?.cityPreference = listOfCity

            val gson = Gson()
            val jsonData = gson.toJson(userData)
            val requestBody = jsonData.toRequestBody("text/plain".toMediaType())

            profileViewModel.updateUserProfile(token!!, requestBody, imageMultipart)
        }

        binding.skipButton.setOnClickListener {
            val gson = Gson()
            val jsonData = gson.toJson(userData)
            val requestBody = jsonData.toRequestBody("text/plain".toMediaType())

            profileViewModel.updateUserProfile(token!!, requestBody, null)
        }
    }

    private fun addGenderChips() {
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

            chipGroup.addView(chip)
        }
    }

    private fun addReligionChips() {
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

            chipGroup.addView(chip)
        }
    }

    private fun addInterestsChips() {
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

    companion object {
        const val USER_DATA = "user_data"
        const val IMAGE_URI = "image_uri"
        const val TOKEN = "token_user"
    }
}
package com.dicoding.kumsiaapp.view.organization.event

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
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
import com.bumptech.glide.Glide
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.data.local.UserPreferences
import com.dicoding.kumsiaapp.data.local.dataStore
import com.dicoding.kumsiaapp.data.remote.request.EventRequestDTO
import com.dicoding.kumsiaapp.data.remote.response.EventsItem
import com.dicoding.kumsiaapp.databinding.ActivityUpdateSubmittedEventBinding
import com.dicoding.kumsiaapp.utils.DateFormatter
import com.dicoding.kumsiaapp.viewmodel.EventViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModelFactory
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class UpdateSubmittedEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateSubmittedEventBinding
    private val eventViewModel: EventViewModel by lazy {
        ViewModelProvider(this)[EventViewModel::class.java]
    }
    private lateinit var token: String

    private var city: String? = null
    private lateinit var eventType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUpdateSubmittedEventBinding.inflate(layoutInflater)
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

        eventViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        eventViewModel.eventItemData.observe(this) {
            it.getContentIfNotHandled()?.let { data ->
                showToast("Event is successfully updated!")
                val intent = Intent(this, OrganizationDetailEventActivity::class.java)
                intent.putExtra(OrganizationDetailEventActivity.EVENT_DATA, data)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
                finish()
            } ?: run {
                showToast("Failed to update event!")
            }
        }

        val eventData = intent.getParcelableExtra<EventsItem>(UpdateEventActivity.EVENT_DATA)
        setEventData(eventData!!)
        setRadioButton(eventData.type)

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.citySpinner.setOnClickListener {
            showCityDialog()
        }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == binding.online.id) {
                setRadioButton("Online")
            } else {
                setRadioButton("Offline")
            }
        }

        binding.saveButton.setOnClickListener {
            val descriptionField = binding.edDescription.text.toString().trim()
            val locationField = binding.edLocation.text.toString().trim()
            val meetingLinkField = binding.edEventLink.text.toString().trim()
            var eventDTO : EventRequestDTO? = null

            if (binding.offline.isChecked) {
                if (descriptionField.isEmpty() || locationField.isEmpty() ||  city == null) {
                    showToast(resources.getString(R.string.input_can_t_be_empty))
                } else {
                    eventDTO = EventRequestDTO(
                        name = eventData.name,
                        location = locationField,
                        type = eventType,
                        eventStart = eventData.eventStart,
                        city = city,
                        link = meetingLinkField,
                        description = descriptionField,
                        attendeeCriteria = eventData.attendeeCriteria,
                        contact = eventData.contactVarchar,
                        capacity = eventData.capacity,
                        genderPreference = eventData.genderPreference,
                        religionPreference = eventData.religionPreference,
                        hobbyPreference = eventData.hobbyPreference,
                        cityPreference = eventData.cityPreference
                    )
                }
            } else {
                if (descriptionField.isEmpty() || meetingLinkField.isEmpty()) {
                    showToast(resources.getString(R.string.input_can_t_be_empty))
                } else {
                    eventDTO = EventRequestDTO(
                        name = eventData.name,
                        location = locationField,
                        type = eventType,
                        eventStart = eventData.eventStart,
                        city = "",
                        link = meetingLinkField,
                        description = descriptionField,
                        attendeeCriteria = eventData.attendeeCriteria,
                        contact = eventData.contactVarchar,
                        capacity = eventData.capacity,
                        genderPreference = eventData.genderPreference,
                        religionPreference = eventData.religionPreference,
                        hobbyPreference = eventData.hobbyPreference,
                        cityPreference = eventData.cityPreference
                    )
                }
            }

            val gson = Gson()
            val jsonData = gson.toJson(eventDTO)
            val requestBody = jsonData.toRequestBody("text/plain".toMediaType())

            eventViewModel.updateEvent(token, eventData.eventId!!, null, requestBody)
        }
    }

    private fun showCityDialog() {
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
            OnItemClickListener { _, _, position, _ -> // when item selected from list
                binding.citySpinner.text = adapter.getItem(position).toString()
                city = adapter.getItem(position).toString()
                dialog.dismiss()
            }
    }

    private fun setRadioButton(type: String?) {
        if (type == "Online") {
            eventType = "Online"
            binding.online.isChecked = true
            binding.offline.isChecked = false
            binding.edLocation.visibility = View.GONE
            binding.locationTitle.visibility = View.GONE
            binding.citySpinner.visibility = View.GONE
            binding.cityTitle.visibility = View.GONE
            binding.edEventLink.visibility = View.VISIBLE
            binding.eventLinkTitle.visibility = View.VISIBLE
        } else {
            eventType = "Offline"
            binding.offline.isChecked = true
            binding.online.isChecked = false
            binding.edLocation.visibility = View.VISIBLE
            binding.locationTitle.visibility = View.VISIBLE
            binding.citySpinner.visibility = View.VISIBLE
            binding.cityTitle.visibility = View.VISIBLE
            binding.edEventLink.visibility = View.GONE
            binding.eventLinkTitle.visibility = View.GONE
        }
    }

    private fun setEventData(data: EventsItem) {
        city = data.city

        // Set view data
        if (data.profilePicture != null) {
            Glide.with(this).load(data.profilePicture).into(binding.imageView)
        }
        binding.edEventName.text = SpannableStringBuilder(data.name)
        binding.edDatetime.text = SpannableStringBuilder(DateFormatter.formatDate(data.eventStart!!))
        binding.edCapacity.text = SpannableStringBuilder(data.capacity.toString())
        binding.edContact.text = SpannableStringBuilder(data.contactVarchar)
        binding.edDescription.text = SpannableStringBuilder(data.description)
        binding.edAttendeeCriteria.text = SpannableStringBuilder(data.attendeeCriteria)
        binding.edLocation.text = SpannableStringBuilder(data.location)
        binding.citySpinner.text = SpannableStringBuilder(data.city)
        binding.edEventLink.text = SpannableStringBuilder(data.link)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
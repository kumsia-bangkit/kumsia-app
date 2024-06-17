package com.dicoding.kumsiaapp.view.organization.event

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
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
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.data.remote.request.EventRequestDTO
import com.dicoding.kumsiaapp.databinding.ActivityAddEventBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class AddEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEventBinding
    private var currentImageUri: Uri? = null
    private var eventDateAndTime: String? = null
    private var city: String? = null
    private lateinit var eventType: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(com.dicoding.kumsiaapp.R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        eventType = if (binding.online.isChecked) "Online" else "Offline"

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.dateTimeButton.setOnClickListener {
            pickDateTime()
        }

        binding.radioGroup.setOnCheckedChangeListener {_, checkedId ->
            if (checkedId == binding.online.id) {
                setRadioButton("Online")
            } else {
                setRadioButton("Offline")
            }
        }

        binding.citySpinner.setOnClickListener {
            showCityDialog()
        }

        binding.addPhotoButton.setOnClickListener {
            startGallery()
        }

        binding.nextButton.setOnClickListener {
            val nameField = binding.edEventName.text.toString().trim()
            val capacityField = binding.edCapacity.text.toString().toIntOrNull()
            val contactGroupField = binding.edContact.text.toString().trim()
            val descriptionField = binding.edDescription.text.toString().trim()
            val attendeeField = binding.edAttendeeCriteria.text.toString().trim()
            val locationField = binding.edLocation.text.toString().trim()
            val meetingLinkField = binding.edEventLink.text.toString().trim()

            if (binding.offline.isChecked) {
                if (currentImageUri == null || eventDateAndTime == null || nameField.isEmpty() || capacityField == null || contactGroupField.isEmpty() || descriptionField.isEmpty()
                    || attendeeField.isEmpty() || locationField.isEmpty() || city == null) {
                    showToast(resources.getString(R.string.input_can_t_be_empty))
                } else {
                    val eventDTO = EventRequestDTO(
                        name = nameField,
                        location = locationField,
                        type = eventType,
                        eventStart = eventDateAndTime,
                        city = city,
                        link = meetingLinkField,
                        description = descriptionField,
                        attendeeCriteria = attendeeField,
                        contact = contactGroupField,
                        capacity = capacityField
                    )

                    val intent = Intent(this, AddEventPreferencesActivity::class.java)
                    intent.putExtra(EVENT_DATA, eventDTO)
                    intent.putExtra(IMAGE_FILE, currentImageUri)
                    startActivity(intent)
                }
            } else {
                if (currentImageUri == null || eventDateAndTime == null || nameField.isEmpty() || capacityField == null || contactGroupField.isEmpty() || descriptionField.isEmpty()
                    || attendeeField.isEmpty() || meetingLinkField.isEmpty()) {
                    showToast(resources.getString(R.string.input_can_t_be_empty))
                } else {
                    val eventDTO = EventRequestDTO(
                        name = nameField,
                        location = locationField,
                        type = eventType,
                        eventStart = eventDateAndTime,
                        city = "",
                        link = meetingLinkField,
                        description = descriptionField,
                        attendeeCriteria = attendeeField,
                        contact = contactGroupField,
                        capacity = capacityField
                    )

                    val intent = Intent(this, AddEventPreferencesActivity::class.java)
                    intent.putExtra(EVENT_DATA, eventDTO)
                    intent.putExtra(IMAGE_FILE, currentImageUri)
                    startActivity(intent)
                }
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            showToast(getString(R.string.no_media_is_selected))
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.imageView.setImageURI(it)
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

    private fun pickDateTime() {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(this, { _, year, month, day ->
            TimePickerDialog(this, { _, hour, minute ->
                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(year, month, day, hour, minute)

                val timestamp = pickedDateTime.timeInMillis
                val textFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
                val date = textFormat.format(timestamp)
                binding.edDatetime.text = SpannableStringBuilder(date)

                val isoDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                isoDateFormat.timeZone = TimeZone.getTimeZone("UTC")

                eventDateAndTime = isoDateFormat.format(Date(timestamp))
            }, startHour, startMinute, android.text.format.DateFormat.is24HourFormat(this)).show()
        }, startYear, startMonth, startDay).show()
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EVENT_DATA = "eventDTO"
        const val IMAGE_FILE = "imageFile"
    }
}
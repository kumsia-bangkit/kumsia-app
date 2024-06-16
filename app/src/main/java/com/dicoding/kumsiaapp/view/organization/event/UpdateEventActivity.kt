package com.dicoding.kumsiaapp.view.organization.event

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.databinding.ActivityUpdateEventBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class UpdateEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateEventBinding
    private lateinit var eventDateAndTime: String
    private lateinit var city: String
    private lateinit var eventStatus: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateEventBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.dateTimeButton.setOnClickListener {
            pickDateTime()
        }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == binding.online.id) {
                eventStatus = "Online"
                binding.offline.isChecked = false
                binding.edLocation.visibility = View.GONE
                binding.locationTitle.visibility = View.GONE
                binding.citySpinner.visibility = View.GONE
                binding.citySpinner.visibility = View.GONE
                binding.cityTitle.visibility = View.GONE
                binding.edEventLink.visibility = View.VISIBLE
                binding.eventLinkTitle.visibility = View.VISIBLE
            } else {
                eventStatus = "Offline"
                binding.online.isChecked = false
                binding.edLocation.visibility = View.VISIBLE
                binding.locationTitle.visibility = View.VISIBLE
                binding.citySpinner.visibility = View.VISIBLE
                binding.citySpinner.visibility = View.VISIBLE
                binding.cityTitle.visibility = View.VISIBLE
                binding.edEventLink.visibility = View.GONE
                binding.eventLinkTitle.visibility = View.GONE
            }
        }

        binding.citySpinner.setOnClickListener {
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

        binding.nextButton.setOnClickListener {
            val intent = Intent(this, AddEventPreferencesActivity::class.java)
            startActivity(intent)
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
            }, startHour, startMinute, DateFormat.is24HourFormat(this)).show()
        }, startYear, startMonth, startDay).show()
    }
}
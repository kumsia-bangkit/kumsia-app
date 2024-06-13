package com.dicoding.kumsiaapp.view.auth

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.databinding.ActivityIndividualRegisterBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class IndividualRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIndividualRegisterBinding
    private lateinit var dateOfBirth: String
    private lateinit var gender: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIndividualRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.signInLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.dobButton.setOnClickListener {
            pickDate()
        }

        binding.genderSpinner.setOnClickListener {
            showGenderDialog()
        }
    }

    private fun pickDate() {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, year, month, day ->
            val pickedDateTime = Calendar.getInstance()
            pickedDateTime.set(year, month, day)

            val timestamp = pickedDateTime.timeInMillis
            val textFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            val date = textFormat.format(timestamp)
            binding.edDob.text = SpannableStringBuilder(date)

            val dataFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            dateOfBirth = dataFormat.format(Date(timestamp))
        }, startYear, startMonth, startDay).show()
    }

    private fun showGenderDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(
            R.layout.dialog_searchable_spinner)
        dialog.window?.setLayout(1000, 800)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        // Initialize and assign variable
        val textView: TextView = dialog.findViewById(R.id.text_view)
        textView.text = getString(R.string.choose_the_gender)

        val editText: EditText = dialog.findViewById(R.id.edit_text)
        editText.hint = getString(R.string.search_the_gender)

        val listView: ListView = dialog.findViewById(R.id.list_view)

        val adapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(
            this,
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.gender_array)
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
            AdapterView.OnItemClickListener { _, _, position, _ ->
                binding.genderSpinner.text = adapter.getItem(position).toString()
                gender = adapter.getItem(position).toString()
                dialog.dismiss()
            }
    }
}
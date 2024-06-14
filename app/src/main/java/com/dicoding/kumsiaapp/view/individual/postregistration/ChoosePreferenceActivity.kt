package com.dicoding.kumsiaapp.view.individual.postregistration

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.dicoding.kumsiaapp.databinding.ActivityChoosePreferenceBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class ChoosePreferenceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChoosePreferenceBinding
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

        binding.addCityChip.setOnClickListener {
            addCityDialog()
        }

        addGenderChips()
        addReligionChips()
        addInterestsChips()
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

        // Initialize and assign variable
        val editText: EditText = dialog.findViewById(R.id.edit_text)
        val listView: ListView = dialog.findViewById(R.id.list_view)

        val adapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(
            this,
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.courses_array)
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
            chipGroup.removeView(chip)
        }

        chipGroup.addView(chip, 0)
    }
}
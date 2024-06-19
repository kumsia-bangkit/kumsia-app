package com.dicoding.kumsiaapp.view.individual.profile

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.data.remote.request.UpdateUserProfileDTO
import com.dicoding.kumsiaapp.data.remote.response.UserDTO
import com.dicoding.kumsiaapp.databinding.ActivityEditIndividualProfileBinding
import com.dicoding.kumsiaapp.utils.DateFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EditIndividualProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditIndividualProfileBinding
    private var currentImageUri: Uri? = null
    private lateinit var dateOfBirth: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditIndividualProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        this.window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )

        val userData = intent.getParcelableExtra<UserDTO>(USER_DATA)
        setUserData(userData)

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.citySpinner.setOnClickListener {
            showCityDialog()
        }

        binding.dobButton.setOnClickListener {
            pickDate()
        }

        binding.religionSpinner.setOnClickListener {
            showDialog("Religion")
        }

        binding.citySpinner.setOnClickListener {
            showDialog("City")
        }

        binding.addPhotoButton.setOnClickListener {
            startGallery()
        }

        binding.nextButton.setOnClickListener {
            val nameField = binding.edRegisterName.text.toString().trim()
            val usernameField = binding.edRegisterUsername.text.toString().trim()
            val emailField = binding.edRegisterEmail.text.toString().trim()
            val genderField = binding.genderSpinner.text.toString().trim()
            val dobField = binding.edDob.text.toString().trim()
            val oldPasswordField = binding.edConfigmEditPassword.text.toString().trim()

            // Optional fields
            val newPasswordField = binding.edEditPassword.text.toString().trim()
            val religionField = binding.religionSpinner.text.toString().trim()
            val cityField = binding.citySpinner.text.toString().trim()
            val contact = binding.edContact.text.toString().trim()
            val guardianContact = binding.edGuardianContact.text.toString().trim()

            if (nameField.isEmpty() || usernameField.isEmpty() || emailField.isEmpty() || genderField.isEmpty() ||
                dobField.isEmpty() || oldPasswordField.isEmpty()) {
                    binding.errorMessage.let {
                        it.text = getString(R.string.required_fields_can_t_be_empty)
                        it.visibility = View.VISIBLE
                    }
                }
            else {
                binding.errorMessage.visibility = View.GONE
                val userDTO = UpdateUserProfileDTO(
                    username = usernameField,
                    name = nameField,
                    email = emailField,
                    contact = contact.ifEmpty { null },
                    guardianContact = guardianContact.ifEmpty { null },
                    religion = religionField.ifEmpty { null },
                    gender = genderField,
                    dob = dateOfBirth,
                    city = cityField,
                    newPassword = newPasswordField,
                    password = oldPasswordField,
                    genderPreference = userData?.user?.preferenceGender,
                    religionPreference = userData?.user?.preferenceReligion,
                    cityPreference = userData?.user?.preferenceCity,
                    hobbyPreference = userData?.user?.preferenceHobby,
                )

                val intent = Intent(this, EditPreferencesActivity::class.java)
                intent.putExtra(EditPreferencesActivity.USER_DATA, userDTO)
                intent.putExtra(EditPreferencesActivity.IMAGE_FILE, currentImageUri)
                startActivity(intent)
            }
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

    private fun showDialog(content: String) {
        val dialog = Dialog(this)
        dialog.setContentView(
            R.layout.dialog_searchable_spinner)
        dialog.window?.setLayout(1000, 800)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        // Initialize and assign variable
        val editText: EditText = dialog.findViewById(R.id.edit_text)
        val listView: ListView = dialog.findViewById(R.id.list_view)
        val adapter: ArrayAdapter<Any?> = if (content == "City") {
            ArrayAdapter<Any?>(
                this,
                android.R.layout.simple_list_item_1,
                resources.getStringArray(R.array.city_array)
            )
        } else {
            ArrayAdapter<Any?>(
                this,
                android.R.layout.simple_list_item_1,
                resources.getStringArray(R.array.religion_array)
            )
        }


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
                if (content == "City") {
                    binding.citySpinner.text = adapter.getItem(position).toString()
                } else {
                    binding.religionSpinner.text = adapter.getItem(position).toString()
                }
                dialog.dismiss()
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
                dialog.dismiss()
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
            binding.profileChange.setImageURI(it)
        }
    }

    private fun setUserData(userData: UserDTO?) {
        dateOfBirth = userData?.user?.dob!!

        if (userData.user.profilePicture != null) {
            Glide.with(this)
                .load(userData.user.profilePicture)
                .into(binding.profileChange)
        }

        binding.apply {
            edRegisterName.text = SpannableStringBuilder(userData.user.name)
            edRegisterUsername.text = SpannableStringBuilder(userData.user.username)
            edRegisterEmail.text = SpannableStringBuilder(userData.user.email)
            genderSpinner.text = SpannableStringBuilder(userData.user.gender)
            edDob.text = SpannableStringBuilder(DateFormatter.formatDOB(userData.user.dob))

            // Optional field (need to check null values)
            if (!userData.user.religion.isNullOrEmpty()) {
                religionSpinner.text = SpannableStringBuilder(userData.user.religion)
            }
            if (!userData.user.city.isNullOrEmpty()) {
                citySpinner.text = SpannableStringBuilder(userData.user.city)
            }
            if (!userData.user.contact.isNullOrEmpty()) {
                edContact.text = SpannableStringBuilder(userData.user.contact)
            }
            if (!userData.user.guardianContact.isNullOrEmpty()) {
                edGuardianContact.text = SpannableStringBuilder(userData.user.guardianContact)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val USER_DATA = "user_data"
    }
}
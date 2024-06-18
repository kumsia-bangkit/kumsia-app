package com.dicoding.kumsiaapp.view.individual.postregistration

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
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
import androidx.lifecycle.ViewModelProvider
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.data.local.UserPreferences
import com.dicoding.kumsiaapp.data.local.dataStore
import com.dicoding.kumsiaapp.data.remote.request.UpdateUserProfileDTO
import com.dicoding.kumsiaapp.data.remote.response.OrganizationDTO
import com.dicoding.kumsiaapp.data.remote.response.UserDTO
import com.dicoding.kumsiaapp.databinding.ActivityCompleteIndividualDataBinding
import com.dicoding.kumsiaapp.viewmodel.AuthViewModel
import com.dicoding.kumsiaapp.viewmodel.ProfileViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModelFactory

class CompleteIndividualDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompleteIndividualDataBinding
    private val profileViewModel: ProfileViewModel by lazy {
        ViewModelProvider(this)[ProfileViewModel::class.java]
    }
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }
    private var currentImageUri: Uri? = null
    private var userData: UserDTO? = null
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompleteIndividualDataBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        val pref = UserPreferences.getInstance(application.dataStore)
        val sessionViewModel = ViewModelProvider(this, SessionViewModelFactory(pref))[SessionViewModel::class.java]

        sessionViewModel.getUserToken().observe(this) {
            token = it!!
            authViewModel.getUserData(token)
        }

        authViewModel.userData.observe(this) {
            it.getContentIfNotHandled()?.let { data ->
                userData = data
            }
        }

        binding.skipButton.setOnClickListener {
            binding.errorMessage.visibility = View.GONE
            val userDTO = UpdateUserProfileDTO(
                username = userData?.user?.username,
                name = userData?.user?.name,
                email = userData?.user?.email,
                contact = null,
                guardianContact = null,
                religion = null,
                gender = userData?.user?.gender,
                dob = userData?.user?.dob,
                city = null,
                newPassword = "",
                password = ""
            )

            val intent = Intent(this, ChoosePreferenceActivity::class.java)
            intent.putExtra(ChoosePreferenceActivity.USER_DATA, userDTO)
            intent.putExtra(ChoosePreferenceActivity.IMAGE_URI, currentImageUri)
            intent.putExtra(ChoosePreferenceActivity.TOKEN, token)
            startActivity(intent)
        }

        binding.addPhotoButton.setOnClickListener {
            startGallery()
        }

        binding.religionSpinner.setOnClickListener {
            showDialog("Religion")
        }

        binding.citySpinner.setOnClickListener {
            showDialog("City")
        }

        binding.saveButton.setOnClickListener {
            val religionField = binding.religionSpinner.text.toString().trim()
            val cityField = binding.citySpinner.text.toString().trim()
            val contactField = binding.edContact.text.toString().trim()
            val guardianContactField = binding.edGuardianContact.text.toString().trim()

            if (currentImageUri == null || religionField.isEmpty() || cityField.isEmpty() || contactField.isEmpty()) {
                binding.errorMessage.let {
                    it.text = resources.getString(R.string.input_cant_be_empty)
                    it.visibility = View.VISIBLE
                }
            } else {
                binding.errorMessage.visibility = View.GONE
                val userDTO = UpdateUserProfileDTO(
                    username = userData?.user?.username,
                    name = userData?.user?.name,
                    email = userData?.user?.email,
                    contact = contactField,
                    guardianContact = guardianContactField,
                    religion = religionField,
                    gender = userData?.user?.gender,
                    dob = userData?.user?.dob,
                    city = cityField,
                    newPassword = "",
                    password = ""
                )

                val intent = Intent(this, ChoosePreferenceActivity::class.java)
                intent.putExtra(ChoosePreferenceActivity.USER_DATA, userDTO)
                intent.putExtra(ChoosePreferenceActivity.IMAGE_URI, currentImageUri)
                intent.putExtra(ChoosePreferenceActivity.TOKEN, token)
                startActivity(intent)
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
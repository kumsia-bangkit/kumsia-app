package com.dicoding.kumsiaapp.view.organization.postregistration

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
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
import com.dicoding.kumsiaapp.data.remote.request.UpdateOrgProfileDTO
import com.dicoding.kumsiaapp.data.remote.response.OrganizationDTO
import com.dicoding.kumsiaapp.databinding.ActivityCompleteOrganizationDataBinding
import com.dicoding.kumsiaapp.utils.JwtDecoder
import com.dicoding.kumsiaapp.utils.reduceFileImage
import com.dicoding.kumsiaapp.utils.uriToFile
import com.dicoding.kumsiaapp.view.organization.OrganizationActivity
import com.dicoding.kumsiaapp.view.organization.event.OrganizationDetailEventActivity
import com.dicoding.kumsiaapp.viewmodel.AuthViewModel
import com.dicoding.kumsiaapp.viewmodel.ProfileViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModelFactory
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class CompleteOrganizationDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompleteOrganizationDataBinding
    private val profileViewModel: ProfileViewModel by lazy {
        ViewModelProvider(this)[ProfileViewModel::class.java]
    }
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }
    private var currentImageUri: Uri? = null
    private var orgData: OrganizationDTO? = null
    private lateinit var token: String

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompleteOrganizationDataBinding.inflate(layoutInflater)
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
            authViewModel.getOrganizationData(it)
        }

        authViewModel.organizationData.observe(this) {
            it.getContentIfNotHandled()?.let { data ->
                orgData = data
            }
        }

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

                val intent = Intent(this, OrganizationActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)

                showToast("Data is successfully updated!")
            } ?: run {
                showToast("Failed to update organization data!")
            }
        }

        binding.addPhotoButton.setOnClickListener {
            startGallery()
        }

        binding.saveButton.setOnClickListener {
            val contactField = binding.edContact.text.toString().trim()
            val descriptionField = binding.edDescription.text.toString().trim()

            if (currentImageUri == null || contactField.isEmpty() || descriptionField.isEmpty()) {
                binding.errorMessage.let {
                    it.text = resources.getString(R.string.input_cant_be_empty)
                    it.visibility = View.VISIBLE
                }
            } else {
                binding.errorMessage.visibility = View.GONE

                val orgDTO = UpdateOrgProfileDTO(
                    name = orgData?.user?.name,
                    username = orgData?.user?.username,
                    email = orgData?.user?.email,
                    description = descriptionField,
                    contact = contactField
                )

                val gson = Gson()
                val jsonData = gson.toJson(orgDTO)
                val requestBody = jsonData.toRequestBody("text/plain".toMediaType())

                val imageFile = uriToFile(currentImageUri!!, this).reduceFileImage()
                val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                val imageMultipart = MultipartBody.Part.createFormData(
                    "file",
                    imageFile.name,
                    requestImageFile
                )

                profileViewModel.updateOrganizationProfile(token, requestBody, imageMultipart)
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
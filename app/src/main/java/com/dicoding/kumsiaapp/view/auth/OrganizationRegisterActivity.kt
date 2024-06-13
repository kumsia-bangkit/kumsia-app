package com.dicoding.kumsiaapp.view.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.data.remote.request.OrganizationRegisterDTO
import com.dicoding.kumsiaapp.databinding.ActivityOrganizationRegisterBinding
import com.dicoding.kumsiaapp.view.organization.OrganizationActivity
import com.dicoding.kumsiaapp.viewmodel.AuthViewModel

class OrganizationRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrganizationRegisterBinding
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrganizationRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.signUpButton.setOnClickListener {
            // 1. Check if all input is not empty
            // 2. Check if the email and password format is valid
            // 3. Submit

            val nameField = binding.edRegisterName.text.toString().trim()
            val usernameField = binding.edRegisterUsername.text.toString().trim()
            val emailField = binding.edRegisterEmail.text.toString().trim()
            val passwordField = binding.edRegisterPassword.text.toString().trim()

            if (usernameField.isEmpty() || nameField.isEmpty() || emailField.isEmpty() || passwordField.isEmpty()) {
                binding.errorMessage.let {
                    it.text = resources.getString(R.string.input_cant_be_empty)
                    it.visibility = View.VISIBLE
                }
            } else if (binding.edRegisterEmail.error != null || binding.edRegisterPassword.error != null) {
                binding.errorMessage.let {
                    it.text = resources.getString(R.string.input_is_invalid)
                    it.visibility = View.VISIBLE
                }
            } else {
                binding.errorMessage.visibility = View.GONE
                val registerDTO = OrganizationRegisterDTO(
                    nameField,
                    usernameField,
                    emailField,
                    passwordField
                )
                authViewModel.registerForOrganization(registerDTO)

                authViewModel.isLoading.observe(this) {
                    showLoading(it)
                }

                authViewModel.registerMessage.observe(this) {
                    it.getContentIfNotHandled()?.let { message ->
                        if (message == "Account is successfully registered") {
                            showToast(message)

                            val intent = Intent(this@OrganizationRegisterActivity, LoginActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                        } else {
                            showToast(message)
                        }
                    }
                }
            }

        }

        // Temporary button to check the fragment
        binding.signInLink.setOnClickListener {
            val intent = Intent(this@OrganizationRegisterActivity, OrganizationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
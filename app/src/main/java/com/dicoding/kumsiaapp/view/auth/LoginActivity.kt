package com.dicoding.kumsiaapp.view.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.data.local.UserPreferences
import com.dicoding.kumsiaapp.data.local.dataStore
import com.dicoding.kumsiaapp.data.remote.response.LoginResponseDTO
import com.dicoding.kumsiaapp.databinding.ActivityLoginBinding
import com.dicoding.kumsiaapp.utils.JwtDecoder
import com.dicoding.kumsiaapp.view.individual.IndividualActivity
import com.dicoding.kumsiaapp.view.individual.postregistration.CompleteIndividualDataActivity
import com.dicoding.kumsiaapp.view.organization.OrganizationActivity
import com.dicoding.kumsiaapp.view.organization.postregistration.CompleteOrganizationDataActivity
import com.dicoding.kumsiaapp.viewmodel.AuthViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserPreferences.getInstance(application.dataStore)
        val sessionViewModel = ViewModelProvider(this, SessionViewModelFactory(pref))[SessionViewModel::class.java]

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.registerLink.setOnClickListener {
            val intent = Intent(this, PreRegistrationActivity::class.java)
            startActivity(intent)
        }

        binding.signInButton.setOnClickListener {
            val usernameField = binding.edLoginUsername.text.toString().trim()
            val passwordField = binding.edLoginPassword.text.toString().trim()

            if (usernameField.isEmpty() || passwordField.isEmpty()) {
                binding.errorMessage.let {
                    it.text = resources.getString(R.string.input_cant_be_empty)
                    it.visibility = View.VISIBLE
                }
            } else if (binding.edLoginPassword.error != null) {
                binding.errorMessage.let {
                    it.text = resources.getString(R.string.input_is_invalid)
                    it.visibility = View.VISIBLE
                }
            } else {
                binding.errorMessage.visibility = View.GONE
                authViewModel.login(usernameField, passwordField)

                authViewModel.isLoading.observe(this) {
                    showLoading(it)
                }

                authViewModel.loginResponse.observe(this) {
                    it.getContentIfNotHandled()?.let { message ->
                        if (message is LoginResponseDTO) {
                            val claims = JwtDecoder.decode(message.accessToken!!)

                            val name = claims.getClaim("name").asString()
                            val role = claims.getClaim("role").asString()
                            val isNewUser = claims.getClaim("is_new_user").asBoolean() ?: true

                            sessionViewModel.saveSession(message.accessToken, name!!, role!!, isNewUser)
                            showToast(getString(R.string.successfully_logged_in))

                            if (role == "organization") {
                                if (isNewUser) {
                                    val intent = Intent(this, CompleteOrganizationDataActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    startActivity(intent)
                                } else {
                                    val intent = Intent(this, OrganizationActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    startActivity(intent)
                                }
                            } else {
                                if (isNewUser) {
                                    val intent = Intent(this, CompleteIndividualDataActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    startActivity(intent)
                                } else {
                                    val intent = Intent(this, IndividualActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    startActivity(intent)
                                }
                            }

                        } else {
                            showToast(message as String)
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
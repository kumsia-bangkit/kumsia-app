package com.dicoding.kumsiaapp.view.individual.profile

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.data.remote.response.UserDTO
import com.dicoding.kumsiaapp.databinding.ActivityEditIndividualProfileBinding

class EditIndividualProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditIndividualProfileBinding
    private var currentImageUri: Uri? = null

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

        val userData = intent.getParcelableExtra<UserDTO>(USER_DATA)
        setUserData(userData)
    }

    private fun setUserData(userData: UserDTO?) {

    }

    companion object {
        const val USER_DATA = "user_data"
    }
}
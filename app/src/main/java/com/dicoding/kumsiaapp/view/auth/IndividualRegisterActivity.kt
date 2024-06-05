package com.dicoding.kumsiaapp.view.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.databinding.ActivityIndividualRegisterBinding

class IndividualRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIndividualRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIndividualRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}
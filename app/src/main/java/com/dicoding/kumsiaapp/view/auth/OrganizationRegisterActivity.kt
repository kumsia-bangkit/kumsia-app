package com.dicoding.kumsiaapp.view.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.databinding.ActivityOrganizationRegisterBinding
import com.dicoding.kumsiaapp.view.organization.OrganizationActivity

class OrganizationRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrganizationRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrganizationRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        // Temporary button to check the fragment
        binding.signUpButton.setOnClickListener {
            val intent = Intent(this@OrganizationRegisterActivity, OrganizationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }
}
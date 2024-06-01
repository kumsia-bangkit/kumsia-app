package com.dicoding.kumsiaapp.view.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.dicoding.kumsiaapp.R

class PreRegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_registration)

        val backButtonLogin = findViewById<ImageView>(R.id.back_button)
        backButtonLogin.setOnClickListener {
            finish()
        }
    }
}
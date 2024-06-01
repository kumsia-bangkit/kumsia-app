package com.dicoding.kumsiaapp.view.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.view.MainActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val backButtonLogin = findViewById<ImageView>(R.id.back_button)
        backButtonLogin.setOnClickListener {
            finish()
        }

        val registrationButtonLogin = findViewById<TextView>(R.id.register_link)
        registrationButtonLogin.setOnClickListener {
            val intent = Intent(this, PreRegistrationActivity::class.java)
            startActivity(intent)
        }
    }
}
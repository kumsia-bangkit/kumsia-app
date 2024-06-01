package com.dicoding.kumsiaapp.view.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.dicoding.kumsiaapp.R
import org.w3c.dom.Text

class PreRegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_registration)

        val backButtonPreRegistration = findViewById<ImageView>(R.id.back_button)
        backButtonPreRegistration.setOnClickListener {
            finish()
        }

        val loginButtonPreRegistration = findViewById<TextView>(R.id.register_link)
        loginButtonPreRegistration.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val individualRegisterButton = findViewById<CardView>(R.id.individual_role_card)
        individualRegisterButton.setOnClickListener {
            val intent = Intent(this, IndividualRegisterActivity::class.java)
            startActivity(intent)
        }

        val organizationRegisterButton = findViewById<CardView>(R.id.organization_role_card)
        organizationRegisterButton.setOnClickListener {
            val intent = Intent(this, OrganizationRegisterActivity::class.java)
            startActivity(intent)
        }

    }
}
package com.dicoding.kumsiaapp.view.organization.postregistration

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.databinding.ActivityCompleteOrganizationDataBinding
import com.dicoding.kumsiaapp.view.organization.OrganizationActivity

class CompleteOrganizationDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompleteOrganizationDataBinding
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

        binding.skipButton.setOnClickListener {
            val intent = Intent(this, OrganizationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }
}
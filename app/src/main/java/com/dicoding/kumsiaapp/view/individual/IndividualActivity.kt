package com.dicoding.kumsiaapp.view.individual

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.databinding.ActivityIndividualBinding
import com.dicoding.kumsiaapp.databinding.ActivityOrganizationBinding
import com.dicoding.kumsiaapp.view.individual.home.IndividualHomeFragment
import com.dicoding.kumsiaapp.view.organization.event.OrganizationEventFragment
import com.dicoding.kumsiaapp.view.organization.home.OrganizationHomeFragment
import com.dicoding.kumsiaapp.view.organization.profile.OrganizationProfileFragment

class IndividualActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIndividualBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityIndividualBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.bottomNavigationView.setOnApplyWindowInsetsListener(null)
        binding.bottomNavigationView.setPadding(0,0,0,0)
        replaceFragment(IndividualHomeFragment())
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> replaceFragment(IndividualHomeFragment())
                //R.id.profile -> replaceFragment(OrganizationProfileFragment())
                //R.id.events -> replaceFragment(OrganizationEventFragment())
                else -> {}
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
package com.dicoding.kumsiaapp.view.organization

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.databinding.ActivityOrganizationBinding
import com.dicoding.kumsiaapp.view.organization.event.OrganizationEventFragment
import com.dicoding.kumsiaapp.view.organization.home.OrganizationHomeFragment
import com.dicoding.kumsiaapp.view.organization.profile.OrganizationProfileFragment

class OrganizationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrganizationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOrganizationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        val intentData = intent?.extras?.getInt(FRAGMENT_POSITION, 0)
        if (intentData != null) {
            when (intentData) {
                0 -> {
                    binding.bottomNavigationView.selectedItemId = R.id.home
                    replaceFragment(OrganizationHomeFragment())
                }

                1 -> {
                    binding.bottomNavigationView.selectedItemId = R.id.events
                    replaceFragment(OrganizationEventFragment())
                }
                2 -> {
                    binding.bottomNavigationView.selectedItemId = R.id.profile
                    replaceFragment(OrganizationProfileFragment())
                }
            }
        } else {
            replaceFragment(OrganizationHomeFragment())
        }

        binding.bottomNavigationView.setOnApplyWindowInsetsListener(null)
        binding.bottomNavigationView.setPadding(0,0,0,0)

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> replaceFragment(OrganizationHomeFragment())
                R.id.profile -> replaceFragment(OrganizationProfileFragment())
                R.id.events -> replaceFragment(OrganizationEventFragment())
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

    companion object {
        const val FRAGMENT_POSITION = "fragment_position"
    }
}
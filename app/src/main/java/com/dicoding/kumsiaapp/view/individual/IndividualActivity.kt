package com.dicoding.kumsiaapp.view.individual

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.databinding.ActivityIndividualBinding
import com.dicoding.kumsiaapp.view.individual.event.IndividualEventFragment
import com.dicoding.kumsiaapp.view.individual.friends.FriendsFragment
import com.dicoding.kumsiaapp.view.individual.home.IndividualHomeFragment
import com.dicoding.kumsiaapp.view.individual.profile.IndividualProfileFragment
import com.dicoding.kumsiaapp.view.organization.home.OrganizationHomeFragment

class IndividualActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIndividualBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIndividualBinding.inflate(layoutInflater)
        enableEdgeToEdge()
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
                R.id.friends -> replaceFragment(FriendsFragment())
                R.id.profile -> replaceFragment(IndividualProfileFragment())
                R.id.events -> replaceFragment(IndividualEventFragment())
                else -> {}
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerIndividual, fragment)
            .commit()
    }
}
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
import com.dicoding.kumsiaapp.view.organization.OrganizationActivity

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

        val intentData = intent?.extras?.getInt(OrganizationActivity.FRAGMENT_POSITION, 0)
        if (intentData != null) {
            when(intentData) {
                0 -> {
                    binding.bottomNavigationView.selectedItemId = R.id.home
                    replaceFragment(IndividualHomeFragment())
                }

                1 -> {
                    binding.bottomNavigationView.selectedItemId = R.id.friends
                    replaceFragment(FriendsFragment())
                }
                2 -> {
                    binding.bottomNavigationView.selectedItemId = R.id.events
                    replaceFragment(IndividualEventFragment())
                }
                3 -> {
                    binding.bottomNavigationView.selectedItemId = R.id.profile
                    replaceFragment(IndividualProfileFragment())
                }
            }
        } else {
            replaceFragment(IndividualHomeFragment())
        }

        binding.bottomNavigationView.setOnApplyWindowInsetsListener(null)
        binding.bottomNavigationView.setPadding(0,0,0,0)

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

    companion object {
        const val FRAGMENT_POSITION = "fragment_position"
    }
}
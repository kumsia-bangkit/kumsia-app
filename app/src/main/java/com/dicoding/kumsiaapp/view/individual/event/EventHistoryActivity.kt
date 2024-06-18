package com.dicoding.kumsiaapp.view.individual.event

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.data.remote.response.Event
import com.dicoding.kumsiaapp.databinding.ActivityEventHistoryBinding
import com.dicoding.kumsiaapp.utils.EventPagerAdapter
import com.dicoding.kumsiaapp.view.individual.IndividualActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class EventHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventHistoryBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val eventPagerAdapter = EventPagerAdapter(this, Event("ayam", "12 Januari 2022", "Online"))
        binding.viewPager.adapter = eventPagerAdapter
        TabLayoutMediator(
            binding.tabs, binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        binding.backButton.setOnClickListener {
            val intent = Intent(
                this@EventHistoryActivity,
                IndividualActivity::class.java
            )
            intent.putExtra(IndividualActivity.FRAGMENT_POSITION, 2)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(
            this@EventHistoryActivity,
            IndividualActivity::class.java
        )
        intent.putExtra(IndividualActivity.FRAGMENT_POSITION, 2)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.joined_event, R.string.liked_event)
    }
}
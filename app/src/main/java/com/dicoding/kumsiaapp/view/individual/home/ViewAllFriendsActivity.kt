package com.dicoding.kumsiaapp.view.individual.home

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.data.local.UserPreferences
import com.dicoding.kumsiaapp.data.local.dataStore
import com.dicoding.kumsiaapp.data.remote.response.FriendsListResponseDTO
import com.dicoding.kumsiaapp.databinding.ActivityViewAllFriendsBinding
import com.dicoding.kumsiaapp.utils.FriendListAdapter
import com.dicoding.kumsiaapp.viewmodel.FriendsViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModelFactory

class ViewAllFriendsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewAllFriendsBinding
    private val friendsViewModel: FriendsViewModel by lazy {
        ViewModelProvider(this)[FriendsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityViewAllFriendsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val pref = UserPreferences.getInstance(application.dataStore)
        val sessionViewModel = ViewModelProvider(this, SessionViewModelFactory(pref))[SessionViewModel::class.java]

        sessionViewModel.getUserToken().observe(this) {
            friendsViewModel.getAllFriends(it!!)
        }

        friendsViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        friendsViewModel.friendsData.observe(this) {
            provideFriendsData(it)
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun provideFriendsData(friends: FriendsListResponseDTO) {
        val layoutManager = LinearLayoutManager(this)
        binding.rvFriends.layoutManager = layoutManager

        val adapter = FriendListAdapter()
        adapter.submitList(friends.friends)
        binding.rvFriends.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
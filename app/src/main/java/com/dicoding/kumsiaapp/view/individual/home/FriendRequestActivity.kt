package com.dicoding.kumsiaapp.view.individual.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
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
import com.dicoding.kumsiaapp.databinding.ActivityFriendRequestBinding
import com.dicoding.kumsiaapp.utils.FriendRequestAdapter
import com.dicoding.kumsiaapp.viewmodel.FriendsViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModelFactory

class FriendRequestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFriendRequestBinding
    private val friendsViewModel: FriendsViewModel by lazy {
        ViewModelProvider(this)[FriendsViewModel::class.java]
    }
    private lateinit var token: String
    val adapter = FriendRequestAdapter(this)
    private var currentPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFriendRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val pref = UserPreferences.getInstance(application.dataStore)
        val sessionViewModel = ViewModelProvider(this, SessionViewModelFactory(pref))[SessionViewModel::class.java]

        sessionViewModel.getUserToken().observe(this) {
            token = it!!
            friendsViewModel.getAllFriendRequest(token)
        }

        friendsViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        friendsViewModel.isSuccessAccept.observe(this) {
            it.getContentIfNotHandled().let {success ->
                if (success!!) {
                    showToast("Friend request accepted.")
                    removeItemFromAdapter(currentPosition)
                } else {
                    showToast("Failed to accept friend request.")
                }
            }
        }

        friendsViewModel.isSuccessReject.observe(this) {
            it.getContentIfNotHandled().let { success ->
                if (success!!) {
                    showToast("Friend request rejected.")
                    removeItemFromAdapter(currentPosition)
                } else {
                    showToast("Failed to reject friend request.")
                }
            }
        }

        friendsViewModel.friendRequestsData.observe(this) {
            provideFriendRequests(it)
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun provideFriendRequests(data: FriendsListResponseDTO) {
        if (data.friends!!.isEmpty()) {
            showEmptyMessage(true)
        } else {
            showEmptyMessage(false)
            val layoutManager = LinearLayoutManager(this)
            binding.rvFriendsReq.layoutManager = layoutManager

            adapter.submitList(data.friends)
            binding.rvFriendsReq.adapter = adapter
        }
    }

    private fun removeItemFromAdapter(position: Int) {
        val currentList = adapter.currentList.toMutableList()
        currentList.removeAt(position)
        adapter.submitList(currentList)
    }

    fun acceptFriendRequest(friendId: String, position: Int) {
        currentPosition = position
        friendsViewModel.acceptFriendRequest(friendId, token)
        showLoading(false)
    }

    fun rejectFriendRequest(friendId: String, position: Int) {
        currentPosition = position
        friendsViewModel.rejectFriendRequest(friendId, token)
        showLoading(false)
    }

    private fun showEmptyMessage(isEmpty: Boolean) {
        binding.noFriendRequests.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
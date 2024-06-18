package com.dicoding.kumsiaapp.view.organization.event

import android.os.Bundle
import android.view.View
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
import android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.data.local.UserPreferences
import com.dicoding.kumsiaapp.data.local.dataStore
import com.dicoding.kumsiaapp.data.remote.response.CommentsItem
import com.dicoding.kumsiaapp.data.remote.response.EventsItem
import com.dicoding.kumsiaapp.databinding.ActivityEventCommentBinding
import com.dicoding.kumsiaapp.utils.CommentAdapter
import com.dicoding.kumsiaapp.utils.EventAdapter
import com.dicoding.kumsiaapp.viewmodel.EventViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModelFactory

class EventCommentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventCommentBinding
    private val eventViewModel: EventViewModel by lazy {
        ViewModelProvider(this)[EventViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventCommentBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        this.window.setSoftInputMode(SOFT_INPUT_ADJUST_PAN)
        val isJoined = intent.getBooleanExtra(IS_JOINED, false)

        val pref = UserPreferences.getInstance(application.dataStore)
        val sessionViewModel = ViewModelProvider(this, SessionViewModelFactory(pref))[SessionViewModel::class.java]

        sessionViewModel.getUserRole().observe(this) {
            if (it == "organization" || !isJoined) {
                binding.apply {
                    edAddComment.visibility = View.GONE
                    addCommentButton.visibility = View.GONE
                }
            }
        }

        val eventId = intent.getStringExtra(EVENT_ID)
        eventViewModel.getAllComments(eventId!!)

        eventViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        eventViewModel.commentData.observe(this) {
            if (it != null && it.comments?.isNotEmpty()!!) {
                showEmptyMessage(false)
                provideComments(it.comments)
            } else {
                showEmptyMessage(true)
            }
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun showEmptyMessage(isEmpty: Boolean) {
        binding.noComments.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun provideComments(data: List<CommentsItem?>) {
        if (data.isEmpty()) {
            showEmptyMessage(true)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        val adapter = CommentAdapter()
        adapter.submitList(data)
        binding.recyclerView.adapter = adapter
    }

    companion object {
        const val EVENT_ID = "event_id"
        const val IS_JOINED = "is_joined"
    }
}
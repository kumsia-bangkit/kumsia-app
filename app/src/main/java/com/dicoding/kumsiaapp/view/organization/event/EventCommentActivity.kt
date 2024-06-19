package com.dicoding.kumsiaapp.view.organization.event

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
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
import com.dicoding.kumsiaapp.data.remote.request.CommentRequestDTO
import com.dicoding.kumsiaapp.data.remote.response.CommentResponseDTO
import com.dicoding.kumsiaapp.data.remote.response.CommentsItem
import com.dicoding.kumsiaapp.databinding.ActivityEventCommentBinding
import com.dicoding.kumsiaapp.utils.CommentAdapter
import com.dicoding.kumsiaapp.utils.JwtDecoder
import com.dicoding.kumsiaapp.viewmodel.AuthViewModel
import com.dicoding.kumsiaapp.viewmodel.EventViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModelFactory

class EventCommentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventCommentBinding
    private val eventViewModel: EventViewModel by lazy {
        ViewModelProvider(this)[EventViewModel::class.java]
    }
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }
    private lateinit var token: String
    private var commentData: CommentResponseDTO? = null
    private var profilePicture: String? = null
    private lateinit var name: String
    private lateinit var userIdFromJwt: String
    private var isUpdated: Boolean = false

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
        this.window.setSoftInputMode(
            SOFT_INPUT_ADJUST_PAN
        )
        val isJoined = intent.getBooleanExtra(IS_JOINED, false)

        val pref = UserPreferences.getInstance(application.dataStore)
        val sessionViewModel = ViewModelProvider(this, SessionViewModelFactory(pref))[SessionViewModel::class.java]

        sessionViewModel.getUserToken().observe(this) {
            token = it!!
            userIdFromJwt = JwtDecoder.decode(it).getClaim("sub").asString()!!
            authViewModel.getUserData(token)
        }

        sessionViewModel.getUserName().observe(this) {
            name = it!!
        }

        sessionViewModel.getUserRole().observe(this) {
            if (it == "organization" || !isJoined) {
                binding.apply {
                    edAddComment.visibility = View.GONE
                    addCommentButton.visibility = View.GONE
                }
            }
        }

        authViewModel.userData.observe(this) {
            it.getContentIfNotHandled().let { data ->
                profilePicture = data?.user?.profilePicture!!
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
                commentData = it

                if (!isUpdated) {
                    provideComments(it.comments!!)
                } else {
                    reProvideComments(commentData?.comments!!, commentData?.comments?.size!!)
                }
            } else {
                commentData = it
                showEmptyMessage(true)
            }
        }

        binding.addCommentButton.setOnClickListener {
            if (binding.edAddComment.text.toString().trim().isNotEmpty()) {
                val id = intent.getStringExtra(EVENT_ID)

                val comment = CommentRequestDTO(
                    eventId = id,
                    commentText = binding.edAddComment.text.toString().trim()
                )

                val commentsItem = CommentsItem(
                   userPicture = profilePicture,
                    commentText = binding.edAddComment.text.toString().trim(),
                    userName = name,
                    userId = userIdFromJwt
                )

                isUpdated = true

                eventViewModel.createComment(token, comment)
                val temporaryList = (commentData?.comments ?: emptyList()) + listOf(commentsItem)
                commentData?.comments = temporaryList
                reProvideComments(temporaryList, temporaryList.size)

                binding.edAddComment.text.clear()
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
        showLoading(false)

        if (data.isEmpty()) {
            showEmptyMessage(true)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.recyclerView.addItemDecoration(itemDecoration)

        val adapter = CommentAdapter()
        adapter.submitList(data)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.scrollToPosition(data.size - 1)
    }

    private fun reProvideComments(data: List<CommentsItem?>, position: Int) {
        showLoading(false)

        if (data.size == 1) {
            showEmptyMessage(false)
            val layoutManager = LinearLayoutManager(this)
            binding.recyclerView.layoutManager = layoutManager

            val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
            binding.recyclerView.addItemDecoration(itemDecoration)
        }

        val adapter = CommentAdapter()
        binding.recyclerView.adapter = adapter
        adapter.submitList(data)
        binding.recyclerView.scrollToPosition(position - 1)
    }

    companion object {
        const val EVENT_ID = "event_id"
        const val IS_JOINED = "is_joined"
    }
}
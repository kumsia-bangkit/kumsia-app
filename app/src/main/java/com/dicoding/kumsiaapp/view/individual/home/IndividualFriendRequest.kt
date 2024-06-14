package com.dicoding.kumsiaapp.view.individual.home

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.data.remote.response.FriendRequest
import com.dicoding.kumsiaapp.utils.FriendReqAdapter

class IndividualFriendRequest : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_req)

        val listOfFriendReq = mutableListOf(
            FriendRequest("Jake Peralta", "ted.mosby"),
            FriendRequest("Amy Santiago", "amy.santiago"),
            FriendRequest("Rosa Diaz", "have.you.met.ted"),
            FriendRequest("Jake Peralta", "ted.mosby"),
            FriendRequest("Amy Santiago", "amy.santiago"),
            FriendRequest("Rosa Diaz", "have.you.met.ted"),
        )
        val rvFriendReq = findViewById<RecyclerView>(R.id.rvFriendsReq)
        val layoutManager = LinearLayoutManager(this)
        rvFriendReq.layoutManager = layoutManager

        val adapter = FriendReqAdapter()
        adapter.submitList(listOfFriendReq)
        rvFriendReq.adapter = adapter

        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener() {
            finish()
        }
    }
}
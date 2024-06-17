package com.dicoding.kumsiaapp.view.individual

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.dicoding.kumsiaapp.R

class UserDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val passName:String = intent.getStringExtra("passName").toString()
        val passUsername:String = intent.getStringExtra("passUsername").toString()
        val isFriend:Int = intent.getIntExtra("isFriend", 0)
        val addFriendButton = findViewById<Button>(R.id.add_friend_button)
        val name = findViewById<TextView>(R.id.name)
        name.text = passName
        val username = findViewById<TextView>(R.id.username)
        if (passUsername != "null") {
            username.text = passUsername
        }
        addFriendButton.isVisible = isFriend != 1


        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener() {
            finish()
        }
    }
}
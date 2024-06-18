package com.dicoding.kumsiaapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.data.local.UserPreferences
import com.dicoding.kumsiaapp.data.local.dataStore
import com.dicoding.kumsiaapp.view.individual.IndividualActivity
import com.dicoding.kumsiaapp.view.individual.postregistration.CompleteIndividualDataActivity
import com.dicoding.kumsiaapp.view.organization.OrganizationActivity
import com.dicoding.kumsiaapp.view.organization.postregistration.CompleteOrganizationDataActivity
import com.dicoding.kumsiaapp.viewmodel.SessionViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModelFactory

class SplashActivity : AppCompatActivity() {

    private var isLogin: Boolean = false
    private var role: String? = null
    private var isNewUser: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val pref = UserPreferences.getInstance(application.dataStore)
        val sessionViewModel = ViewModelProvider(this, SessionViewModelFactory(pref))[SessionViewModel::class.java]

        sessionViewModel.getUserToken().observe(this) {
            if (it != null) {
                isLogin = true
            }
        }

        sessionViewModel.getUserRole().observe(this) {
            if (it != null) {
                role = it
            }
        }

        sessionViewModel.getIsNewUser().observe(this) {
            if (it != null) {
                isNewUser = it
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            if (isLogin) {
                if (role == "organization") {
                    if (isNewUser!!) {
                        val intent = Intent(
                            this@SplashActivity,
                            CompleteOrganizationDataActivity::class.java
                        )
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(
                            this@SplashActivity,
                            OrganizationActivity::class.java
                        )
                        startActivity(intent)
                        finish()
                    }
                } else {
                    if (isNewUser!!) {
                        val intent = Intent(
                            this@SplashActivity,
                            CompleteIndividualDataActivity::class.java
                        )
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(
                            this@SplashActivity,
                            IndividualActivity::class.java
                        )
                        startActivity(intent)
                        finish()
                    }
                }
            } else {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, TIME_IN_MILLIS)
    }
    companion object {
        const val TIME_IN_MILLIS : Long = 2000
    }
}
package com.dicoding.kumsiaapp.view.organization.event

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.data.local.UserPreferences
import com.dicoding.kumsiaapp.data.local.dataStore
import com.dicoding.kumsiaapp.data.remote.response.EventsItem
import com.dicoding.kumsiaapp.databinding.ActivityOrganizationDetailEventBinding
import com.dicoding.kumsiaapp.utils.DateFormatter
import com.dicoding.kumsiaapp.viewmodel.SessionViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModelFactory


class OrganizationDetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrganizationDetailEventBinding
    private var eventData: EventsItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOrganizationDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val pref = UserPreferences.getInstance(application.dataStore)
        val sessionViewModel = ViewModelProvider(this, SessionViewModelFactory(pref))[SessionViewModel::class.java]
        sessionViewModel.getUserName().observe(this) {
            if (it != null) {
                binding.tvOrganizer.text = it
            }
        }

        handleIntentData(intent)

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.updateButton.setOnClickListener {
            val intent = Intent(this, UpdateEventActivity::class.java)
            intent.putExtra(UpdateEventActivity.EVENT_DATA, eventData)
            startActivity(intent)
        }

        binding.cancelButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to cancel this event?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    // Call API untuk cancel event
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

        binding.deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to delete this event?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    // Call API untuk delete event
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

        binding.submitButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to open this event?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    // Call API untuk submit event
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

        binding.seeCommentsButton.setOnClickListener {
            val intent = Intent(this, EventCommentActivity::class.java)
            intent.putExtra(EventCommentActivity.ORGANIZATION_ID, eventData?.eventId)
            startActivity(intent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntentData(intent)
    }

    private fun handleIntentData(intent: Intent) {
        eventData = intent.getParcelableExtra<EventsItem>(EVENT_DATA)
        setEventData(eventData)
    }

    private fun setEventData(data: EventsItem?) {
        Glide.with(this).load(data?.profilePicture).into(binding.eventImage)
        binding.likeButton.visibility = View.GONE
        binding.eventTitle.text = data?.name
        binding.tvItemDate.text = DateFormatter.formatDate(data?.eventStart!!)
        binding.eventType.text = data.type
        if (data.type == "Offline") {
            binding.eventType.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.grey))
            binding.tvLocation.text = buildString {
                append(data.location)
                append(", ")
                append(data.city)
            }
        } else {
            binding.eventType.chipBackgroundColor = ColorStateList.valueOf(
                ContextCompat.getColor(
                    baseContext,
                    android.R.color.holo_green_dark
                )
            )
            binding.tvLocationTitle.text = resources.getString(R.string.meeting_link)
            binding.tvLocation.text = data.link
        }

        binding.tvCapacityValue.text = data.capacity.toString()
        binding.tvDescription.text = data.description
        binding.tvAttendee.text = data.attendeeCriteria

        binding.tvGenderPreferences.text = stringOfPreferences(data.genderPreference)
        binding.tvReligionPreferences.text = stringOfPreferences(data.religionPreference)
        binding.tvInterestsPreferences.text = stringOfPreferences(data.hobbyPreference)
        binding.tvCityPreferences.text = stringOfPreferences(data.cityPreference)

        if (isWhatsAppLink(data.contactVarchar!!)) {
            binding.tvContact.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setData(Uri.parse(data.contactVarchar))
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "WhatsApp is not installed", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            binding.tvContact.text = data.contactVarchar
        }

        if (data.status == "Draft") {
            binding.updateButton.visibility = View.VISIBLE
            binding.deleteButton.visibility = View.VISIBLE
            binding.submitButton.visibility = View.VISIBLE

            binding.cancelButton.visibility = View.GONE
            binding.updateSubmittedButton.visibility = View.GONE
        } else if (data.status == "Open") {
            binding.updateButton.visibility = View.GONE
            binding.deleteButton.visibility = View.GONE
            binding.submitButton.visibility = View.GONE

            binding.cancelButton.visibility = View.VISIBLE
            binding.updateSubmittedButton.visibility = View.VISIBLE
        } else {
            binding.updateButton.visibility = View.GONE
            binding.deleteButton.visibility = View.GONE
            binding.submitButton.visibility = View.GONE
            binding.cancelButton.visibility = View.GONE
            binding.updateSubmittedButton.visibility = View.GONE
        }
    }

    private fun stringOfPreferences(data: List<String?>?): String {
        val preferences = StringBuilder()
        if (data!!.isEmpty()) return "-"

        for (i in data.indices) {
            preferences.append(data[i])
            if (i < data.size - 1) {
                preferences.append(", ")
            }
        }

        return preferences.toString()
    }

    private fun isWhatsAppLink(link: String): Boolean {
        val regex = "^https://chat\\.whatsapp\\.com/.+$"
        return link.matches(regex.toRegex())
    }

    companion object {
        const val EVENT_DATA = "event_data"
    }
}
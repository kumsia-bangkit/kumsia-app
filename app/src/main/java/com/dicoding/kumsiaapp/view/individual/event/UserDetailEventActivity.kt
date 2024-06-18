package com.dicoding.kumsiaapp.view.individual.event

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
import com.bumptech.glide.request.RequestOptions
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.data.local.UserPreferences
import com.dicoding.kumsiaapp.data.local.dataStore
import com.dicoding.kumsiaapp.data.remote.response.EventsItemUser
import com.dicoding.kumsiaapp.databinding.ActivityUserDetailEventBinding
import com.dicoding.kumsiaapp.utils.DateFormatter
import com.dicoding.kumsiaapp.view.organization.event.EventCommentActivity
import com.dicoding.kumsiaapp.viewmodel.EventViewModel
import com.dicoding.kumsiaapp.viewmodel.ProfileViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModelFactory

class UserDetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailEventBinding
    private val eventViewModel: EventViewModel by lazy {
        ViewModelProvider(this)[EventViewModel::class.java]
    }
    private val profileViewModel: ProfileViewModel by lazy {
        ViewModelProvider(this)[ProfileViewModel::class.java]
    }
    private var eventData: EventsItemUser? = null
    private var isLiked: Boolean = false
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailEventBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handleIntentData(intent)

        val pref = UserPreferences.getInstance(application.dataStore)
        val sessionViewModel = ViewModelProvider(this, SessionViewModelFactory(pref))[SessionViewModel::class.java]

        sessionViewModel.getUserToken().observe(this) {
            token = it!!
            profileViewModel.getOrganizationProfile(eventData?.organizationId!!, token)
        }

        profileViewModel.isLoading.observe(this) {
            showOrganizationName(!it)
            showOrganizationNameLoading(it)
        }

        profileViewModel.organizationData.observe(this) {
            it.getContentIfNotHandled()?.let { data ->
                showOrganizationName(true)
                binding.tvOrganizer.text = data.name
            }
        }

        eventViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.likeButton.setOnClickListener {
            if (isLiked) {
                eventViewModel.unlikeEvent(token, eventData?.eventId!!)
            } else {
                eventViewModel.likeEvent(token, eventData?.eventId!!)
            }

            eventViewModel.isSuccess.observe(this) {
                it.getContentIfNotHandled()?.let { success ->
                   if (success and isLiked) {
                       showToast("Event is successfully unliked!")
                       binding.likeButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_not_liked))
                       isLiked = false
                   } else if (success and !isLiked) {
                       showToast("Event is successfully liked!")
                       binding.likeButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_liked))
                       isLiked = true
                   } else {
                       showToast("Failed to like the event")
                   }

                }
            }
        }

        binding.joinButton.setOnClickListener {
            // If user has joined the event
            if (eventData?.joined!!) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Are you sure you want to unjoin this event?")
                    .setTitle("Confirm unjoin")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { _, _ ->
                        eventViewModel.unjoinEvent(token, eventData?.eventId!!)

                        eventViewModel.joinEventItemData.observe(this) {
                            it.getContentIfNotHandled()?.let { _ ->
                                showToast("Successfully unjoined the event")

                                val intent = Intent(this, EventHistoryActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            } else {
                eventViewModel.joinEvent(token, eventData?.eventId!!)

                eventViewModel.joinEventItemData.observe(this) {
                    it.getContentIfNotHandled()?.let { _ ->
                        showToast("Successfully joined the event")

                        val intent = Intent(this, EventHistoryActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }

        binding.seeCommentsButton.setOnClickListener {
            val intent = Intent(this, EventCommentActivity::class.java)
            intent.putExtra(EventCommentActivity.EVENT_ID, eventData?.eventId)
            intent.putExtra(EventCommentActivity.IS_JOINED, eventData?.joined)
            startActivity(intent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntentData(intent)
    }

    private fun handleIntentData(intent: Intent) {
        eventData = intent.getParcelableExtra<EventsItemUser>(EVENT_DATA)
        setEventData(eventData)
    }

    private fun setEventData(data: EventsItemUser?) {
        Glide.with(this)
            .load(data?.profiePicture)
            .apply(
                RequestOptions.placeholderOf(R.drawable.people_event).error(R.drawable.people_event)
            )
            .into(binding.eventImage)
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

        if (eventData?.liked!!) {
            binding.likeButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_liked))
        } else {
            binding.likeButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_not_liked))
        }
        isLiked = eventData?.liked!!

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
            binding.tvContact.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        if (data.joined!!) {
            binding.joinButton.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    baseContext,
                    android.R.color.holo_red_dark
                )
            )
            binding.joinButton.text = getString(R.string.unjoin)
        } else {
            binding.apply {
                ivContact.visibility = View.INVISIBLE
                tvContact.visibility = View.INVISIBLE
                tvContactTitle.visibility = View.INVISIBLE
            }
        }

        if (data.status == "Closed" || data.status == "Cancelled" || data.capacity == 0) {
            binding.joinButton.visibility = View.GONE
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

    private fun showOrganizationName(isEmpty: Boolean) {
        binding.tvOrganizer.visibility = if (isEmpty) View.VISIBLE else View.INVISIBLE
    }

    private fun showOrganizationNameLoading(isLoading: Boolean) {
        binding.progressBarOrgName.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    private fun isWhatsAppLink(link: String): Boolean {
        val regex = "^https://chat\\.whatsapp\\.com/.+$"
        return link.matches(regex.toRegex())
    }


    companion object {
        const val EVENT_DATA = "event_data"
    }
}
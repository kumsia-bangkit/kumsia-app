package com.dicoding.kumsiaapp.view.individual

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.data.local.UserPreferences
import com.dicoding.kumsiaapp.data.local.dataStore
import com.dicoding.kumsiaapp.data.remote.response.UserDetailResponseDTO
import com.dicoding.kumsiaapp.databinding.ActivityUserDetailBinding
import com.dicoding.kumsiaapp.utils.DateFormatter
import com.dicoding.kumsiaapp.viewmodel.FriendsViewModel
import com.dicoding.kumsiaapp.viewmodel.ProfileViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModelFactory
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    private val profileViewModel: ProfileViewModel by lazy {
        ViewModelProvider(this)[ProfileViewModel::class.java]
    }
    private val friendsViewModel: FriendsViewModel by lazy {
        ViewModelProvider(this)[FriendsViewModel::class.java]
    }
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userId = intent.getStringExtra(USER_ID)

        val pref = UserPreferences.getInstance(application.dataStore)
        val sessionViewModel = ViewModelProvider(this, SessionViewModelFactory(pref))[SessionViewModel::class.java]

        sessionViewModel.getUserToken().observe(this) {
            token = it!!
            profileViewModel.getUserProfile(userId!!, it)
        }

        profileViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        profileViewModel.userData.observe(this) {
            it.getContentIfNotHandled()?.let { data ->
                setUserData(data)
            }
        }

        friendsViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        friendsViewModel.isSuccess.observe(this) {
            it.getContentIfNotHandled()?.let { isSuccess ->
                if (isSuccess) {
                    binding.friendRequestButton.apply {
                        visibility = View.VISIBLE
                        text = context.getString(R.string.requested_as_friend)
                        backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.transparent))
                        setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.black)))
                        isEnabled = false
                        isClickable = false
                    }
                    showToast("Successfully sent friend request")
                } else {
                    showToast("Failed to send friend request")
                }
            }
        }

        binding.friendRequestButton.setOnClickListener {
            friendsViewModel.sendFriendRequest(userId!!, token)
            showLoading(false)
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun setUserData(data: UserDetailResponseDTO) {
        if (data.isFriends!! == 2) {
            binding.friendRequestButton.visibility = View.GONE
            binding.guardianContact.visibility = View.VISIBLE
            binding.guardianContactTitle.visibility = View.VISIBLE
            binding.personalContact.visibility = View.VISIBLE
            binding.personalContactTitle.visibility = View.VISIBLE
        } else if (data.isFriends == 1) {
            binding.friendRequestButton.apply {
                visibility = View.VISIBLE
                text = context.getString(R.string.requested_as_friend)
                backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.transparent))
                setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.black)))
                isEnabled = false
                isClickable = false
            }
        } else if (data.isFriends == 0){
            binding.friendRequestButton.visibility = View.VISIBLE
        }

        if (data.profilePicture != null) {
            Glide.with(this)
                .load(data.profilePicture)
                .into(binding.circleImageView)
        }

        binding.apply {
            name.text = data.name
            username.text = data.username
            email.text = data.email
            gender.text = data.gender
            religion.text = if (data.religion.isNullOrEmpty()) {
                "No data"
            } else {
                data.religion
            }
            dob.text = DateFormatter.formatDOB(data.dob!!)
            city.text = if (data.city.isNullOrEmpty()) {
                "No data"
            } else {
                data.city
            }
            personalContact.text =
                if (data.contact.isNullOrEmpty() && (data.isFriends == 0 || data.isFriends == 1)) {
                    personalContact.setTextColor(resources.getColor(R.color.gold, null))
                    "Contact information is hidden. Become friends to view."
                } else if (data.contact.isNullOrEmpty()) {
                    "No data"
                }  else {
                    data.contact
                }
            guardianContact.text =
                if (data.guardianContact.isNullOrEmpty() && (data.isFriends == 0 || data.isFriends == 1)) {
                    guardianContact.setTextColor(resources.getColor(R.color.gold, null))
                    "Contact information is hidden. Become friends to view."
                } else if (data.guardianContact.isNullOrEmpty()) {
                    "No data"
                }  else {
                    data.guardianContact
                }
        }

        if (data.preferenceCity?.isEmpty()!!) {
            binding.eventCityTitle.text = getString(R.string.no_city)
        } else {
            data.preferenceCity.forEach {
                addNewChip(it!!)
            }
        }

        if (data.preferenceReligion?.isEmpty()!!) {
            binding.eventReligionTitle.text = getString(R.string.no_religion)
        } else {
            addReligionChips(data.preferenceReligion)
        }

        if (data.preferenceGender?.isEmpty()!!) {
            binding.eventGenderTitle.text = getString(R.string.no_gender)
        } else {
            addGenderChips(data.preferenceGender)
        }

        if (data.preferenceHobby?.isEmpty()!!) {
            binding.eventInterestTitle.text = getString(R.string.no_interests)
        } else {
            addInterestsChips(data.preferenceHobby)
        }
    }

    private fun addGenderChips(list: List<String?>?) {
        val chipGroup = binding.eventGenderGroup

        list?.forEachIndexed { index, interest ->
            val chip =
                layoutInflater.inflate(R.layout.item_chip_preference, chipGroup, false) as Chip
            chip.text = interest
            chip.id = index
            chip.isClickable = false
            chip.isCheckable = true
            chip.isChecked = false
            chip.textAlignment = View.TEXT_ALIGNMENT_CENTER
            chip.layoutParams = ChipGroup.LayoutParams(
                ChipGroup.LayoutParams.WRAP_CONTENT,
                ChipGroup.LayoutParams.WRAP_CONTENT
            )
            chipGroup.addView(chip)
        }
    }

    private fun addReligionChips(list: List<String?>?) {
        val chipGroup = binding.eventReligionGroup

        list?.forEachIndexed { index, interest ->
            val chip =
                layoutInflater.inflate(R.layout.item_chip_preference, chipGroup, false) as Chip
            chip.text = interest
            chip.id = index
            chip.isClickable = false
            chip.isCheckable = true
            chip.textAlignment = View.TEXT_ALIGNMENT_CENTER
            chip.layoutParams = ChipGroup.LayoutParams(
                ChipGroup.LayoutParams.WRAP_CONTENT,
                ChipGroup.LayoutParams.WRAP_CONTENT
            )

            chip.isChecked = false
            chipGroup.addView(chip)
        }
    }

    private fun addInterestsChips(list: List<String?>?) {
        val chipGroup = binding.eventInterestGroup

        list?.forEachIndexed { index, interest ->
            val chip =
                layoutInflater.inflate(R.layout.item_chip_preference, chipGroup, false) as Chip
            chip.text = interest
            chip.id = index
            chip.isClickable = false
            chip.isCheckable = true
            chip.textAlignment = View.TEXT_ALIGNMENT_CENTER
            chip.layoutParams = ChipGroup.LayoutParams(
                ChipGroup.LayoutParams.WRAP_CONTENT,
                ChipGroup.LayoutParams.WRAP_CONTENT
            )

            chip.isChecked = false
            chipGroup.addView(chip)
        }
    }

    private fun addNewChip(chipText: String) {
        val chipGroup = binding.eventCityGroup
        val chip =
            layoutInflater.inflate(R.layout.item_chip_preference, chipGroup, false) as Chip
        chip.text = chipText
        chip.id = View.generateViewId()
        chip.isClickable = false
        chip.isCheckable = true
        chip.isChecked = false
        chip.textAlignment = View.TEXT_ALIGNMENT_CENTER
        chip.layoutParams = ChipGroup.LayoutParams(
            ChipGroup.LayoutParams.WRAP_CONTENT,
            ChipGroup.LayoutParams.WRAP_CONTENT
        )

        chipGroup.addView(chip, 0)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.apply {
                circleImageView.visibility = View.INVISIBLE
                name.visibility = View.INVISIBLE
                usernameTitle.visibility = View.INVISIBLE
                username.visibility = View.INVISIBLE
                emailTitle.visibility = View.INVISIBLE
                email.visibility = View.INVISIBLE
                personalContactTitle.visibility = View.INVISIBLE
                personalContact.visibility = View.INVISIBLE
                guardianContact.visibility = View.INVISIBLE
                guardianContactTitle.visibility = View.INVISIBLE
                genderTitle.visibility = View.INVISIBLE
                gender.visibility = View.INVISIBLE
                dob.visibility = View.INVISIBLE
                dobTitle.visibility = View.INVISIBLE
                religion.visibility = View.INVISIBLE
                religionTitle.visibility = View.INVISIBLE
                cityTitle.visibility = View.INVISIBLE
                city.visibility = View.INVISIBLE
                eventGenderTitle.visibility = View.INVISIBLE
                eventGenderGroup.visibility = View.INVISIBLE
                eventCityTitle.visibility = View.INVISIBLE
                eventCityGroup.visibility = View.INVISIBLE
                eventReligionTitle.visibility = View.INVISIBLE
                eventReligionGroup.visibility = View.INVISIBLE
                eventInterestTitle.visibility = View.INVISIBLE
                eventInterestGroup.visibility = View.INVISIBLE
                eventTypeTitle.visibility = View.INVISIBLE
                friendRequestButton.visibility = View.INVISIBLE
                view2.visibility = View.INVISIBLE
                progressBar.visibility = View.VISIBLE
            }
        } else {
            binding.apply {
                circleImageView.visibility = View.VISIBLE
                name.visibility = View.VISIBLE
                usernameTitle.visibility = View.VISIBLE
                username.visibility = View.VISIBLE
                emailTitle.visibility = View.VISIBLE
                email.visibility = View.VISIBLE
                personalContactTitle.visibility = View.VISIBLE
                personalContact.visibility = View.VISIBLE
                guardianContact.visibility = View.VISIBLE
                guardianContactTitle.visibility = View.VISIBLE
                genderTitle.visibility = View.VISIBLE
                gender.visibility = View.VISIBLE
                dob.visibility = View.VISIBLE
                dobTitle.visibility = View.VISIBLE
                religion.visibility = View.VISIBLE
                religionTitle.visibility = View.VISIBLE
                cityTitle.visibility = View.VISIBLE
                city.visibility = View.VISIBLE
                eventGenderTitle.visibility = View.VISIBLE
                eventGenderGroup.visibility = View.VISIBLE
                eventCityTitle.visibility = View.VISIBLE
                eventCityGroup.visibility = View.VISIBLE
                eventReligionTitle.visibility = View.VISIBLE
                eventReligionGroup.visibility = View.VISIBLE
                eventInterestTitle.visibility = View.VISIBLE
                eventInterestGroup.visibility = View.VISIBLE
                eventTypeTitle.visibility = View.VISIBLE
                friendRequestButton.visibility = View.VISIBLE
                view2.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val USER_ID = "user_id"
    }
}
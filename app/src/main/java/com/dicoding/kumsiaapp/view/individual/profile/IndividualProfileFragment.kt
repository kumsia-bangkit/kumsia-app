package com.dicoding.kumsiaapp.view.individual.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.data.local.UserPreferences
import com.dicoding.kumsiaapp.data.local.dataStore
import com.dicoding.kumsiaapp.data.remote.response.UserDTO
import com.dicoding.kumsiaapp.databinding.FragmentIndividualProfileBinding
import com.dicoding.kumsiaapp.utils.DateFormatter
import com.dicoding.kumsiaapp.view.MainActivity
import com.dicoding.kumsiaapp.viewmodel.AuthViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModelFactory
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class IndividualProfileFragment : Fragment() {

    private lateinit var binding: FragmentIndividualProfileBinding
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIndividualProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = UserPreferences.getInstance(requireContext().dataStore)
        val sessionViewModel = ViewModelProvider(requireActivity(), SessionViewModelFactory(pref))[SessionViewModel::class.java]

        sessionViewModel.getUserToken().observe(viewLifecycleOwner) {
            if (it != null) {
                authViewModel.getUserData(it)
            }
        }

        authViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        authViewModel.userData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { data ->
                setUserData(data)
            }
        }

        binding.editButton.setOnClickListener {
            val intent = Intent(requireActivity(), EditIndividualProfileActivity::class.java)
            startActivity(intent)
        }

        binding.signOutButton.setOnClickListener {
            sessionViewModel.deleteAllData()
            val intent = Intent(requireActivity(), MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }

    private fun setUserData(data: UserDTO) {
        if (data.user?.profilePicture != null) {
            Glide.with(requireContext())
                .load(data.user.profilePicture)
                .into(binding.circleImageView)
        }

        binding.apply {
            name.text = data.user?.name
            username.text = data.user?.username
            email.text = data.user?.email
            gender.text = data.user?.gender
            religion.text = data.user?.religion ?: "No data"
            dob.text = DateFormatter.formatDOB(data.user?.dob!!)
            city.text = data.user.city ?: "No data"
            personalContact.text = data.user.contact ?: "No data"
            guardianContact.text = data.user.guardianContact ?: "No data"
        }

        data.user?.preferenceCity?.forEach {
            addNewChip(it)
        }

        addGenderChips(data.user?.preferenceGender)
        addReligionChips(data.user?.preferenceReligion)
        addInterestsChips(data.user?.preferenceHobby)
    }

    private fun addGenderChips(list: List<String?>?) {
        val chipGroup = binding.eventGenderGroup

        list?.forEachIndexed { index, interest ->
            val chip =
                layoutInflater.inflate(R.layout.item_chip_preference, chipGroup, false) as Chip
            chip.text = interest
            chip.id = index
            chip.isClickable = true
            chip.isCheckable = true
            chip.isChecked = true
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
            chip.isClickable = true
            chip.isCheckable = true
            chip.textAlignment = View.TEXT_ALIGNMENT_CENTER
            chip.layoutParams = ChipGroup.LayoutParams(
                ChipGroup.LayoutParams.WRAP_CONTENT,
                ChipGroup.LayoutParams.WRAP_CONTENT
            )

            chip.isChecked = true
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
            chip.isClickable = true
            chip.isCheckable = true
            chip.textAlignment = View.TEXT_ALIGNMENT_CENTER
            chip.layoutParams = ChipGroup.LayoutParams(
                ChipGroup.LayoutParams.WRAP_CONTENT,
                ChipGroup.LayoutParams.WRAP_CONTENT
            )

            chip.isChecked = true
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
        chip.isChecked = true
        chip.textAlignment = View.TEXT_ALIGNMENT_CENTER
        chip.layoutParams = ChipGroup.LayoutParams(
            ChipGroup.LayoutParams.WRAP_CONTENT,
            ChipGroup.LayoutParams.WRAP_CONTENT
        )
        chip.isCloseIconVisible = true
        chip.setCloseIconResource(R.drawable.baseline_close_24)
        chip.setOnCloseIconClickListener {
            chipGroup.removeView(chip)
        }

        chipGroup.addView(chip, 0)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.apply {
                circleImageView.visibility = View.GONE
                name.visibility = View.GONE
                usernameTitle.visibility = View.GONE
                username.visibility = View.GONE
                emailTitle.visibility = View.GONE
                email.visibility = View.GONE
                personalContactTitle.visibility = View.GONE
                personalContact.visibility = View.GONE
                guardianContact.visibility = View.GONE
                guardianContactTitle.visibility = View.GONE
                genderTitle.visibility = View.GONE
                gender.visibility = View.GONE
                dob.visibility = View.GONE
                dobTitle.visibility = View.GONE
                religion.visibility = View.GONE
                religionTitle.visibility = View.GONE
                cityTitle.visibility = View.GONE
                city.visibility = View.GONE
                eventGenderTitle.visibility = View.GONE
                eventGenderGroup.visibility = View.GONE
                eventCityTitle.visibility = View.GONE
                eventCityGroup.visibility = View.GONE
                eventReligionTitle.visibility = View.GONE
                eventReligionGroup.visibility = View.GONE
                eventInterestTitle.visibility = View.GONE
                eventInterestGroup.visibility = View.GONE
                editButton.visibility = View.GONE
                eventTypeTitle.visibility = View.GONE
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
                editButton.visibility = View.VISIBLE
                eventTypeTitle.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }
    }
}
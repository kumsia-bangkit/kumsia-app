package com.dicoding.kumsiaapp.view.organization.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.kumsiaapp.data.local.UserPreferences
import com.dicoding.kumsiaapp.data.local.dataStore
import com.dicoding.kumsiaapp.data.remote.response.OrganizationDTO
import com.dicoding.kumsiaapp.databinding.FragmentOrganizationProfileBinding
import com.dicoding.kumsiaapp.view.MainActivity
import com.dicoding.kumsiaapp.viewmodel.AuthViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModelFactory

class OrganizationProfileFragment : Fragment() {

    private var _binding: FragmentOrganizationProfileBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrganizationProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = UserPreferences.getInstance(requireContext().dataStore)
        val sessionViewModel = ViewModelProvider(requireActivity(), SessionViewModelFactory(pref))[SessionViewModel::class.java]

        sessionViewModel.getUserToken().observe(viewLifecycleOwner) {
            if (it != null) {
                authViewModel.getOrganizationData(it)
            }
        }

        authViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        authViewModel.organizationData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { data ->
                setUserData(data)
            }
        }

        binding.signOutButton.setOnClickListener {
            sessionViewModel.deleteAllData()
            val intent = Intent(requireActivity(), MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }

    private fun setUserData(data: OrganizationDTO) {
        if (data.user?.profilePicture != null) {
            Glide.with(requireContext())
                .load(data.user.profilePicture)
                .into(binding.circleImageView)
        }

        binding.apply {
            name.text = data.user?.name
            username.text = data.user?.username
            email.text = data.user?.email
            contact.text = data.user?.contact ?: "No contact"
            description.text = data.user?.description ?: "No description"
        }
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
                contactTitle.visibility = View.INVISIBLE
                contact.visibility = View.INVISIBLE
                descriptionTitle.visibility = View.INVISIBLE
                description.visibility = View.INVISIBLE
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
                contactTitle.visibility = View.VISIBLE
                contact.visibility = View.VISIBLE
                descriptionTitle.visibility = View.VISIBLE
                description.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }
    }

}
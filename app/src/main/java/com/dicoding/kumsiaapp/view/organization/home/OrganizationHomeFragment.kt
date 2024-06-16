package com.dicoding.kumsiaapp.view.organization.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dicoding.kumsiaapp.data.local.UserPreferences
import com.dicoding.kumsiaapp.data.local.dataStore
import com.dicoding.kumsiaapp.data.remote.response.Event
import com.dicoding.kumsiaapp.data.remote.response.EventsItem
import com.dicoding.kumsiaapp.databinding.FragmentOrganizationHomeBinding
import com.dicoding.kumsiaapp.utils.EventAdapter
import com.dicoding.kumsiaapp.viewmodel.AuthViewModel
import com.dicoding.kumsiaapp.viewmodel.EventViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModelFactory

class OrganizationHomeFragment : Fragment() {

    private var _binding: FragmentOrganizationHomeBinding? = null
    private val binding get() = _binding!!
    private val eventViewModel: EventViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by activityViewModels()
    private var profilePicture: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrganizationHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = UserPreferences.getInstance(requireContext().dataStore)
        val sessionViewModel = ViewModelProvider(requireActivity(), SessionViewModelFactory(pref))[SessionViewModel::class.java]

        sessionViewModel.getUserName().observe(viewLifecycleOwner) {
            binding.orgName.text = it
        }

        sessionViewModel.getUserToken().observe(viewLifecycleOwner) {
            if (it != null) {
                eventViewModel.getAllEvents(it)
                authViewModel.getOrganizationData(it)
            }
        }

        authViewModel.organizationData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { data ->
                if (data.user?.profilePicture != null) {
                    profilePicture = data.user.profilePicture
                }
            }
        }

        eventViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        eventViewModel.eventData.observe(viewLifecycleOwner) {
            if (it != null && it.events?.isNotEmpty()!!) {
                showEmptyMessage(false)
                provideEvents(it.events)
            } else {
                showEmptyMessage(true)
            }
        }

        if (!profilePicture.isNullOrEmpty()) {
            Glide.with(requireActivity()).load(profilePicture).into(binding.profileImage)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyMessage(isEmpty: Boolean) {
        binding.noEventMessage.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun provideEvents(data: List<EventsItem?>) {
        val newData = data.filter {
            it?.status != "Draft"
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvEvents.layoutManager = layoutManager

        val adapter = EventAdapter()
        adapter.submitList(newData)
        binding.rvEvents.adapter = adapter
    }
}
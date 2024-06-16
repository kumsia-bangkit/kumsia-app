package com.dicoding.kumsiaapp.view.organization.event

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kumsiaapp.data.local.UserPreferences
import com.dicoding.kumsiaapp.data.local.dataStore
import com.dicoding.kumsiaapp.data.remote.response.EventsItem
import com.dicoding.kumsiaapp.databinding.FragmentOrganizationEventBinding
import com.dicoding.kumsiaapp.utils.EventAdapter
import com.dicoding.kumsiaapp.viewmodel.EventViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModelFactory

class OrganizationEventFragment : Fragment() {

    private var _binding: FragmentOrganizationEventBinding? = null
    private val binding get() = _binding!!
    private val eventViewModel: EventViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrganizationEventBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = UserPreferences.getInstance(requireContext().dataStore)
        val sessionViewModel = ViewModelProvider(requireActivity(), SessionViewModelFactory(pref))[SessionViewModel::class.java]

        sessionViewModel.getUserToken().observe(viewLifecycleOwner) {
            if (it != null) {
                eventViewModel.getAllEvents(it)
            }
        }

        eventViewModel.eventData.observe(viewLifecycleOwner) {
            if (it != null && it.events?.isNotEmpty()!!) {
                showEmptyMessage(false)
                provideEvents(it.events)
            } else {
                showEmptyMessage(true)
            }
        }

        eventViewModel.isLoading.observe(viewLifecycleOwner) {
            showEmptyMessage(!it)
            showLoading(it)
        }

        binding.addEventButton.setOnClickListener {
            val intent = Intent(requireActivity(), AddEventActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyMessage(isEmpty: Boolean) {
        binding.noDraftMessage.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun provideEvents(data: List<EventsItem?>) {
        val newData = data.filter {
            it?.status == "Draft"
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvEvents.layoutManager = layoutManager

        val adapter = EventAdapter()
        adapter.submitList(newData)
        binding.rvEvents.adapter = adapter
    }
}
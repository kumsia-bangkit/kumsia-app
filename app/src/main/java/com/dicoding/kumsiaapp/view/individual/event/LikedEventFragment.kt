package com.dicoding.kumsiaapp.view.individual.event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.data.local.UserPreferences
import com.dicoding.kumsiaapp.data.local.dataStore
import com.dicoding.kumsiaapp.data.remote.response.Event
import com.dicoding.kumsiaapp.data.remote.response.EventUserResponseDTO
import com.dicoding.kumsiaapp.data.remote.response.EventsItemUser
import com.dicoding.kumsiaapp.databinding.FragmentLikedEventBinding
import com.dicoding.kumsiaapp.utils.EventAdapter
import com.dicoding.kumsiaapp.utils.EventUserAdapter
import com.dicoding.kumsiaapp.viewmodel.EventViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModelFactory

class LikedEventFragment : Fragment() {

    private lateinit var binding: FragmentLikedEventBinding
    private val eventViewModel: EventViewModel by lazy {
        ViewModelProvider(this)[EventViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLikedEventBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = UserPreferences.getInstance(requireContext().dataStore)
        val sessionViewModel = ViewModelProvider(this, SessionViewModelFactory(pref))[SessionViewModel::class.java]

        sessionViewModel.getUserToken().observe(viewLifecycleOwner) {
            eventViewModel.getAllEventsForUser(it!!)
        }

        eventViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        eventViewModel.eventUserData.observe(viewLifecycleOwner) {
            if (!it.events.isNullOrEmpty()) {
                provideEvents(it.events)
            } else {
                showEmptyMessage(true)
            }
        }
    }

    private fun provideEvents(events: List<EventsItemUser?>?) {
        val newData = events?.filter {
            it?.liked == true
        }

        if (newData.isNullOrEmpty()) {
            showEmptyMessage(true)
        } else {
            showEmptyMessage(false)
            val layoutManager = LinearLayoutManager(requireActivity())
            binding.rvLikedEvents.layoutManager = layoutManager

            val adapter = EventUserAdapter()
            adapter.submitList(newData)
            binding.rvLikedEvents.adapter = adapter
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyMessage(isEmpty: Boolean) {
        binding.noLikedEvents.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

}
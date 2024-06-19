package com.dicoding.kumsiaapp.view.individual.event

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kumsiaapp.data.local.UserPreferences
import com.dicoding.kumsiaapp.data.local.dataStore
import com.dicoding.kumsiaapp.data.remote.response.EventsItemUser
import com.dicoding.kumsiaapp.databinding.FragmentIndividualEventBinding
import com.dicoding.kumsiaapp.utils.EventUserAdapter
import com.dicoding.kumsiaapp.viewmodel.EventViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModelFactory

class IndividualEventFragment : Fragment() {

    private lateinit var binding: FragmentIndividualEventBinding
    private val eventViewModel: EventViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIndividualEventBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = UserPreferences.getInstance(requireContext().dataStore)
        val sessionViewModel = ViewModelProvider(requireActivity(), SessionViewModelFactory(pref))[SessionViewModel::class.java]

        sessionViewModel.getUserToken().observe(viewLifecycleOwner) {
            if (it != null) {
                eventViewModel.getAllEventsForUser(it)
            }
        }

        eventViewModel.isLoading.observe(viewLifecycleOwner) {
            showEmptyMessage(!it)
            showLoading(it)
        }

        eventViewModel.eventUserData.observe(viewLifecycleOwner) {
            if (it != null && it.events?.isNotEmpty()!!) {
                showEmptyMessage(false)
                provideEvents(it.events)
            } else {
                showEmptyMessage(true)
            }
        }

        binding.myEventsIcon.setOnClickListener {
            val intent = Intent(requireActivity(), EventHistoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun provideEvents(events: List<EventsItemUser?>) {
        val newData = events.filter {
            it?.status == "Open"
        }
            .sortedBy {
                it?.eventStart
            }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvEvents.layoutManager = layoutManager

        val adapter = EventUserAdapter()
        adapter.submitList(newData)
        adapter.notifyItemInserted(adapter.itemCount - 1)
        binding.rvEvents.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyMessage(isEmpty: Boolean) {
        binding.noEventsMessage.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }
}
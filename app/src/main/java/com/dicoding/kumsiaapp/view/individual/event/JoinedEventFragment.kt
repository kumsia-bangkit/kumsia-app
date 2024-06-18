package com.dicoding.kumsiaapp.view.individual.event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kumsiaapp.data.remote.response.EventUserResponseDTO
import com.dicoding.kumsiaapp.data.remote.response.EventsItemUser
import com.dicoding.kumsiaapp.databinding.FragmentJoinedEventBinding
import com.dicoding.kumsiaapp.utils.EventAdapter
import com.dicoding.kumsiaapp.utils.EventUserAdapter

class JoinedEventFragment : Fragment() {

    private lateinit var binding: FragmentJoinedEventBinding
    private var eventData: EventUserResponseDTO? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJoinedEventBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            eventData = it.getParcelable("event")
        }

        if (eventData != null) {
            provideEvents(eventData?.events)
        } else {
            showEmptyMessage(true)
        }
    }

    private fun provideEvents(events: List<EventsItemUser?>?) {
        val newData = events?.filter {
            it?.joined!!
        }

        if (newData!!.isEmpty()) {
            showEmptyMessage(true)
        } else {
            showEmptyMessage(false)
            val layoutManager = LinearLayoutManager(requireActivity())
            binding.rvJoinedEvents.layoutManager = layoutManager

            val adapter = EventUserAdapter()
            adapter.submitList(newData)
            binding.rvJoinedEvents.adapter = adapter
        }

    }

    private fun showEmptyMessage(isEmpty: Boolean) {
        binding.noJoinedEvents.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

}
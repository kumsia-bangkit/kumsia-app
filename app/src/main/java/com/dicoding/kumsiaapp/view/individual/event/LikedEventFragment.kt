package com.dicoding.kumsiaapp.view.individual.event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.data.remote.response.Event
import com.dicoding.kumsiaapp.data.remote.response.EventUserResponseDTO
import com.dicoding.kumsiaapp.data.remote.response.EventsItemUser
import com.dicoding.kumsiaapp.databinding.FragmentLikedEventBinding
import com.dicoding.kumsiaapp.utils.EventAdapter
import com.dicoding.kumsiaapp.utils.EventUserAdapter

class LikedEventFragment : Fragment() {

    private lateinit var binding: FragmentLikedEventBinding
    private var eventData: EventUserResponseDTO? = null

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
            it?.liked!!
        }

        if (newData!!.isEmpty()) {
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

    private fun showEmptyMessage(isEmpty: Boolean) {
        binding.noLikedEvents.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

}
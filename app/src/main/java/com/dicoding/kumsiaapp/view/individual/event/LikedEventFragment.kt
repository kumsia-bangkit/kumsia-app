package com.dicoding.kumsiaapp.view.individual.event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.data.remote.response.Event
import com.dicoding.kumsiaapp.databinding.FragmentLikedEventBinding
import com.dicoding.kumsiaapp.utils.EventAdapter

class LikedEventFragment : Fragment() {

    private lateinit var binding: FragmentLikedEventBinding

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

        val listOfEvent = mutableListOf(
            Event("Bincang-Bincang Masak", "12 Oktober 2023", "Memasak"),
        )

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvLikedEvents.layoutManager = layoutManager

        val adapter = EventAdapter()
        adapter.submitList(listOfEvent)
        binding.rvLikedEvents.adapter = adapter
    }

}
package com.dicoding.kumsiaapp.view.organization.event

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kumsiaapp.data.remote.response.Event
import com.dicoding.kumsiaapp.databinding.FragmentOrganizationEventBinding
import com.dicoding.kumsiaapp.utils.EventAdapter

class OrganizationEventFragment : Fragment() {

    private var _binding: FragmentOrganizationEventBinding? = null
    private val binding get() = _binding!!

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

        val listOfEvent = mutableListOf(
            Event("Bincang-Bincang Masak", "12 Oktober 2023", "Memasak"),
            Event("Catur Bersama", "12 Oktober 2023", "Olahraga"),
            Event("Bincang-Bincang Masak", "12 Oktober 2023", "Memasak"),
            Event("Catur Bersama", "12 Oktober 2023", "Olahraga"),
        )

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvEvents.layoutManager = layoutManager

        val adapter = EventAdapter()
        adapter.submitList(listOfEvent)
        binding.rvEvents.adapter = adapter

        binding.addEventButton.setOnClickListener {
            val intent = Intent(requireActivity(), AddEventActivity::class.java)
            startActivity(intent)
        }
    }
}
package com.dicoding.kumsiaapp.view.organization.event

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dicoding.kumsiaapp.databinding.FragmentOrganizationEventBinding

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

        binding.addEventButton.setOnClickListener {
            val intent = Intent(requireActivity(), AddEventActivity::class.java)
            startActivity(intent)
        }
    }
}
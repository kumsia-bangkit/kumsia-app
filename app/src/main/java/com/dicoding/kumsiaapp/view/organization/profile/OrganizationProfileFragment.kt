package com.dicoding.kumsiaapp.view.organization.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dicoding.kumsiaapp.databinding.FragmentOrganizationProfileBinding

class OrganizationProfileFragment : Fragment() {

    private var _binding: FragmentOrganizationProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrganizationProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
}
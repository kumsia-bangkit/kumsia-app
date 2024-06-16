package com.dicoding.kumsiaapp.view.individual.event

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kumsiaapp.data.remote.response.Event
import com.dicoding.kumsiaapp.databinding.FragmentIndividualEventBinding
import com.dicoding.kumsiaapp.utils.EventAdapter

class IndividualEventFragment : Fragment() {

    private lateinit var binding: FragmentIndividualEventBinding

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

        binding.myEventsIcon.setOnClickListener {
            val intent = Intent(requireActivity(), EventHistoryActivity::class.java)
            startActivity(intent)
        }

    }
}
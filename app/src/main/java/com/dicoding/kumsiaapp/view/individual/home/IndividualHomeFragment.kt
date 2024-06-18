package com.dicoding.kumsiaapp.view.individual.home

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.data.local.UserPreferences
import com.dicoding.kumsiaapp.data.local.dataStore
import com.dicoding.kumsiaapp.data.remote.response.EventsItemUser
import com.dicoding.kumsiaapp.data.remote.response.FriendList
import com.dicoding.kumsiaapp.data.remote.response.FriendsItem
import com.dicoding.kumsiaapp.databinding.FragmentIndividualHomeBinding
import com.dicoding.kumsiaapp.utils.DateFormatter
import com.dicoding.kumsiaapp.utils.EventUserAdapter
import com.dicoding.kumsiaapp.utils.FriendListAdapter
import com.dicoding.kumsiaapp.view.individual.event.UserDetailEventActivity
import com.dicoding.kumsiaapp.viewmodel.AuthViewModel
import com.dicoding.kumsiaapp.viewmodel.EventViewModel
import com.dicoding.kumsiaapp.viewmodel.FriendsViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModelFactory
import java.util.Collections

class IndividualHomeFragment : Fragment() {

    private var _binding: FragmentIndividualHomeBinding? = null
    private val binding get() = _binding!!
    private val eventViewModel: EventViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by activityViewModels()
    private val friendsViewModel: FriendsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIndividualHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = UserPreferences.getInstance(requireContext().dataStore)
        val sessionViewModel = ViewModelProvider(requireActivity(), SessionViewModelFactory(pref))[SessionViewModel::class.java]

        sessionViewModel.getUserName().observe(viewLifecycleOwner) {
            binding.individualName.text = it
        }

        sessionViewModel.getUserToken().observe(viewLifecycleOwner) {
            if (it != null) {
                eventViewModel.getAllUserJoinedEvents(it)
                friendsViewModel.getAllFriends(it)
                authViewModel.getUserData(it)
            }
        }

        authViewModel.userData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { data ->
                if (data.user?.profilePicture != null) {
                    Glide.with(requireActivity()).load(data.user.profilePicture)
                        .into(binding.profileImage)
                }
            }
        }

        eventViewModel.isLoading.observe(viewLifecycleOwner) {
            showEmptyEventMessage(!it)
            showEventLoading(it)
        }

        eventViewModel.joinedEventUserData.observe(viewLifecycleOwner) {
            if (it != null && it.events?.isNotEmpty()!!) {
                showEmptyEventMessage(false)
                provideEvents(it.events)
            } else {
                showEmptyEventMessage(true)
            }
        }

        friendsViewModel.isLoading.observe(viewLifecycleOwner) {
            showEmptyFriendsMessage(!it)
            showFriendsLoading(it)
        }

        friendsViewModel.friendsData.observe(viewLifecycleOwner) {
            if (it != null && it.friends?.isNotEmpty()!!) {
                showEmptyFriendsMessage(false)
                provideFriends(it.friends)
            } else {
                binding.viewAllFriends.visibility = View.GONE
                showEmptyFriendsMessage(true)
            }
        }

        binding.viewAllFriends.setOnClickListener {
            // Ke halaman all friends (copy activity comment)
        }

        binding.friendRequest.setOnClickListener {
            val intent = Intent(requireActivity(), FriendRequestActivity::class.java)
            startActivity(intent)
        }
    }

    private fun provideEvents(events: List<EventsItemUser?>) {
        val newData = events.filter {
            it?.status == "Open"
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvEvents.layoutManager = layoutManager

        val adapter = EventUserAdapter()
        adapter.submitList(Collections.singletonList(newData.first()))
        binding.rvEvents.adapter = adapter
    }

    private fun provideFriends(data: List<FriendsItem?>) {
        binding.viewAllFriends.visibility = View.VISIBLE

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFriends.layoutManager = layoutManager

        val adapter = FriendListAdapter()
        adapter.submitList(data)
        binding.rvFriends.adapter = adapter
    }

    private fun showEventLoading(isLoading: Boolean) {
        binding.progressBarEvent.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyEventMessage(isEmpty: Boolean) {
        binding.noUpcomingEvent.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun showFriendsLoading(isLoading: Boolean) {
        binding.progressBarFriends.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyFriendsMessage(isEmpty: Boolean) {
        binding.noFriends.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }
}
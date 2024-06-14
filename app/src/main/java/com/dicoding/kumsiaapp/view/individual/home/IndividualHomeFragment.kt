package com.dicoding.kumsiaapp.view.individual.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kumsiaapp.data.remote.response.FriendList
import com.dicoding.kumsiaapp.databinding.FragmentIndividualHomeBinding
import com.dicoding.kumsiaapp.utils.FriendListAdapter

class IndividualHomeFragment : Fragment() {

    private var _binding: FragmentIndividualHomeBinding? = null
    private val binding get() = _binding!!

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

        val listOfFriend = mutableListOf(
            FriendList("Ted Mosby", "ted.mosby"),
            FriendList("Lily Aldrin", "amy.santiago"),
            FriendList("Barney Stinson", "have.you.met.ted"),
            FriendList("Robin Scherbatsky", "robin.from.canada"),
            FriendList("Marshall Eriksen", "marshallthelaawyer"),
        )

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFriends.layoutManager = layoutManager

        val adapter = FriendListAdapter()
        adapter.submitList(listOfFriend)
        binding.rvFriends.adapter = adapter

        // Flag check from friend req db, if theres friend req or not
        // Logic blabalbala if true, set icon with orange
        //val resourceId = resources.getIdentifier("@drawable/ic_friend_true", "drawable", requireActivity().packageName)
        //binding.friendRequest.setImageResource(resourceId)
        // Logic if falseee, set default icon
        //val resourceId = resources.getIdentifier("@drawable/ic_friend_false", "drawable", requireActivity().packageName)
        //binding.friendRequest.setImageResource(resourceId)

        binding.friendRequest.setOnClickListener() {
            val intent = Intent(activity, IndividualFriendRequest::class.java)
            startActivity(intent)
        }
    }
}
package com.dicoding.kumsiaapp.view.individual.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.kumsiaapp.data.remote.response.FriendList
import com.dicoding.kumsiaapp.databinding.FragmentIndividualHomeBinding
import com.dicoding.kumsiaapp.utils.FriendListAdapter
import com.dicoding.kumsiaapp.view.individual.UserDetailActivity

class IndividualHomeFragment : Fragment(), FriendListAdapter.OnItemClickListener {

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


        binding.friendRequest.setOnClickListener {
            val intent = Intent(requireActivity(), FriendRequestActivity::class.java)
            startActivity(intent)
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFriends.layoutManager = layoutManager
        val adapter = FriendListAdapter()
        adapter.setOnItemClickListener(this)
        adapter.submitList(listOfFriend)
        binding.rvFriends.adapter = adapter


    }

    override fun onItemClick(friend: FriendList, position: Int) {
        val intent = Intent(requireContext(), UserDetailActivity::class.java)
        intent.putExtra("passName", friend.name)
        intent.putExtra("passUsername", friend.username)
        //Flag as friend
        intent.putExtra("isFriend", 1)
        startActivity(intent)
    }
}
package com.dicoding.kumsiaapp.view.individual.friends

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.kumsiaapp.data.remote.response.FriendList
import com.dicoding.kumsiaapp.data.remote.response.FriendsRec
import com.dicoding.kumsiaapp.databinding.FragmentFriendsBinding
import com.dicoding.kumsiaapp.utils.FriendsRecAdapter
import com.dicoding.kumsiaapp.utils.GridSpacingItemDecoration
import com.dicoding.kumsiaapp.view.individual.UserDetailActivity

class FriendsFragment : Fragment(), FriendsRecAdapter.OnItemClickListener {

    private lateinit var binding: FragmentFriendsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listOfFriends = listOf(
            FriendsRec("Marianne Sheridan", "56 years old"),
            FriendsRec("Connell Sheridan", "56 years old"),
            FriendsRec("Marianne Sheridan", "56 years old"),
            FriendsRec("Marianne Sheridan", "56 years old"),
            FriendsRec("Marianne Sheridan", "56 years old"),
            FriendsRec("Marianne Sheridan", "56 years old"),
            FriendsRec("Marianne Sheridan", "56 years old"),
            FriendsRec("Connell Sheridan", "56 years old"),
            FriendsRec("Marianne Sheridan", "56 years old"),
            FriendsRec("Marianne Sheridan", "56 years old"),
            FriendsRec("Marianne Sheridan", "56 years old"),
            FriendsRec("Marianne Sheridan", "56 years old"),
        )

        val layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.rvFriendsRec.layoutManager = layoutManager
        binding.rvFriendsRec.addItemDecoration(GridSpacingItemDecoration(2, 40, true))

        val adapter = FriendsRecAdapter()
        adapter.setOnItemClickListener(this)
        adapter.submitList(listOfFriends)
        binding.rvFriendsRec.adapter = adapter
    }

    override fun onItemClick(friend: FriendsRec, position: Int) {
        val intent = Intent(requireContext(), UserDetailActivity::class.java)
        intent.putExtra("passName", friend.name)
        //Flag as friend
        intent.putExtra("isFriend", 0)
        startActivity(intent)
    }

}
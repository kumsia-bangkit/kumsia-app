package com.dicoding.kumsiaapp.view.individual.friends

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.kumsiaapp.data.local.UserPreferences
import com.dicoding.kumsiaapp.data.local.dataStore
import com.dicoding.kumsiaapp.data.remote.response.FriendsRec
import com.dicoding.kumsiaapp.data.remote.response.FriendsRecommendationDTO
import com.dicoding.kumsiaapp.databinding.FragmentFriendsBinding
import com.dicoding.kumsiaapp.utils.FriendsRecAdapter
import com.dicoding.kumsiaapp.utils.GridSpacingItemDecoration
import com.dicoding.kumsiaapp.viewmodel.FriendsViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModel
import com.dicoding.kumsiaapp.viewmodel.SessionViewModelFactory

class FriendsFragment : Fragment() {

    private lateinit var binding: FragmentFriendsBinding
    private val friendsViewModel: FriendsViewModel by activityViewModels()

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

        val layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.rvFriendsRec.layoutManager = layoutManager
        binding.rvFriendsRec.addItemDecoration(GridSpacingItemDecoration(2, 40, true))

        val pref = UserPreferences.getInstance(requireContext().dataStore)
        val sessionViewModel = ViewModelProvider(requireActivity(), SessionViewModelFactory(pref))[SessionViewModel::class.java]

        sessionViewModel.getUserToken().observe(viewLifecycleOwner) {
            friendsViewModel.getFriendsRecommendation(it!!)
        }

        friendsViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        friendsViewModel.recommendedFriendsData.observe(viewLifecycleOwner) {
            if (it.users.isNullOrEmpty()) {
                showEmptyMessage(true)
            } else {
                showEmptyMessage(false)
                provideRecommendations(it)
            }
        }
    }

    private fun provideRecommendations(friendsRec: FriendsRecommendationDTO) {
        val adapter = FriendsRecAdapter()
        adapter.submitList(friendsRec.users)
        binding.rvFriendsRec.adapter = adapter
    }

    private fun showEmptyMessage(isEmpty: Boolean) {
        binding.noRecommendationYet.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvFriendsRec.visibility = if (!isLoading) View.VISIBLE else View.GONE
    }

}
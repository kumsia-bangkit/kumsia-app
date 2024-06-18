package com.dicoding.kumsiaapp.utils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.kumsiaapp.data.remote.response.Event
import com.dicoding.kumsiaapp.data.remote.response.EventUserResponseDTO
import com.dicoding.kumsiaapp.view.individual.event.JoinedEventFragment
import com.dicoding.kumsiaapp.view.individual.event.LikedEventFragment

class EventPagerAdapter internal constructor(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    private var eventData: EventUserResponseDTO? = null
    override fun createFragment(position: Int): Fragment {
        val bundle = Bundle()
        if (position == 0) {
            val fragmentJoined = JoinedEventFragment()
            bundle.putParcelable("event", eventData)
            fragmentJoined.arguments = bundle
            return fragmentJoined
        } else {
            val fragmentLiked = LikedEventFragment()
            bundle.putParcelable("event", eventData)
            fragmentLiked.arguments = bundle
            return fragmentLiked
        }
    }

    fun updatePagerData(newData: EventUserResponseDTO) {
        eventData = newData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return 2
    }
}
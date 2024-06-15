package com.dicoding.kumsiaapp.utils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.kumsiaapp.data.remote.response.Event
import com.dicoding.kumsiaapp.view.individual.event.JoinedEventFragment
import com.dicoding.kumsiaapp.view.individual.event.LikedEventFragment

class EventPagerAdapter internal constructor(activity: AppCompatActivity, eventData: Event) : FragmentStateAdapter(activity) {

    private val event = eventData
    override fun createFragment(position: Int): Fragment {
        val bundle = Bundle()
        if (position == 0) {
            val fragmentJoined = JoinedEventFragment()
            bundle.putParcelable("event", event)
            fragmentJoined.arguments = bundle
            return fragmentJoined
        } else {
            val fragmentLiked = LikedEventFragment()
            bundle.putParcelable("event", event)
            fragmentLiked.arguments = bundle
            return fragmentLiked
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}
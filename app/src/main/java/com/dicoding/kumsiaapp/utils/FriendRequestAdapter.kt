package com.dicoding.kumsiaapp.utils

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.kumsiaapp.data.remote.response.FriendList
import com.dicoding.kumsiaapp.databinding.ItemFriendReqBinding

class FriendReqAdapter : androidx.recyclerview.widget.ListAdapter<FriendList, FriendReqAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemFriendReqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story)
    }

    class MyViewHolder(private val binding: ItemFriendReqBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(friendRequest: FriendList) {
            binding.individualName.text = friendRequest.name
            binding.individualUsername.text = friendRequest.username
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FriendList>() {
            override fun areItemsTheSame(oldItem: FriendList, newItem: FriendList): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FriendList, newItem: FriendList): Boolean {
                return oldItem == newItem
            }
        }
    }
}
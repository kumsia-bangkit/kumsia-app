package com.dicoding.kumsiaapp.utils

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.kumsiaapp.data.remote.response.FriendRequest
import com.dicoding.kumsiaapp.databinding.ItemFriendReqBinding

class FriendReqAdapter : androidx.recyclerview.widget.ListAdapter<FriendRequest, FriendReqAdapter.MyViewHolder>(
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
        fun bind(friendRequest: FriendRequest) {
            binding.individualName.text = friendRequest.name
            binding.individualUsername.text = friendRequest.username
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FriendRequest>() {
            override fun areItemsTheSame(oldItem: FriendRequest, newItem: FriendRequest): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FriendRequest, newItem: FriendRequest): Boolean {
                return oldItem == newItem
            }
        }
    }
}
package com.dicoding.kumsiaapp.utils

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.data.remote.response.FriendList
import com.dicoding.kumsiaapp.data.remote.response.FriendsItem
import com.dicoding.kumsiaapp.databinding.ItemFriendBinding


class FriendListAdapter : androidx.recyclerview.widget.ListAdapter<FriendsItem, FriendListAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story)
    }

    class MyViewHolder(private val binding: ItemFriendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(friendList: FriendsItem) {
            Glide.with(itemView.context)
                .load(friendList.profilePicture)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_profile).error(R.drawable.ic_profile)
                )
                .into(binding.profileImage)
            binding.individualName.text = friendList.name
            binding.individualUsername.text = friendList.username
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FriendsItem>() {
            override fun areItemsTheSame(oldItem: FriendsItem, newItem: FriendsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FriendsItem, newItem: FriendsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
package com.dicoding.kumsiaapp.utils

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.kumsiaapp.data.remote.response.FriendList
import com.dicoding.kumsiaapp.databinding.ItemFriendBinding


class FriendListAdapter : androidx.recyclerview.widget.ListAdapter<FriendList, FriendListAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        onItemClickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val friend = getItem(position)
        holder.bind(friend)
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(friend, position)
        }
    }

    class MyViewHolder(private val binding: ItemFriendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(friendList: FriendList) {
            binding.individualName.text = friendList.name
            binding.individualUsername.text = friendList.username
        }
    }

    interface OnItemClickListener {
        fun onItemClick(friend: FriendList, position: Int)
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
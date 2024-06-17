package com.dicoding.kumsiaapp.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.kumsiaapp.data.remote.response.FriendList
import com.dicoding.kumsiaapp.data.remote.response.FriendsRec
import com.dicoding.kumsiaapp.databinding.ItemFriendRecommendationBinding

class FriendsRecAdapter : androidx.recyclerview.widget.ListAdapter<FriendsRec, FriendsRecAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {

    private var onItemClickListener: FriendsRecAdapter.OnItemClickListener? = null

    fun setOnItemClickListener(listener: FriendsRecAdapter.OnItemClickListener?) {
        onItemClickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemFriendRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val friend = getItem(position)
        holder.bind(friend, onItemClickListener)
    }

    class MyViewHolder(private val binding: ItemFriendRecommendationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(friend: FriendsRec, listener: OnItemClickListener?) {
            binding.userAge.text = friend.date
            binding.userName.text = friend.name
            binding.detailButton.setOnClickListener {
                listener?.onItemClick(friend, adapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(friend: FriendsRec, position: Int)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FriendsRec>() {
            override fun areItemsTheSame(oldItem: FriendsRec, newItem: FriendsRec): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FriendsRec, newItem: FriendsRec): Boolean {
                return oldItem == newItem
            }
        }
    }
}
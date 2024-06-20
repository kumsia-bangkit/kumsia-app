package com.dicoding.kumsiaapp.utils

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.data.remote.response.FriendsItem
import com.dicoding.kumsiaapp.databinding.ItemFriendReqBinding
import com.dicoding.kumsiaapp.view.individual.UserDetailActivity
import com.dicoding.kumsiaapp.view.individual.home.FriendRequestActivity

class FriendRequestAdapter(private val activity: FriendRequestActivity) : androidx.recyclerview.widget.ListAdapter<FriendsItem, FriendRequestAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemFriendReqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, activity)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val friend = getItem(position)
        holder.bind(friend, position)
    }

    class MyViewHolder(private val binding: ItemFriendReqBinding, private val activity: FriendRequestActivity) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(friendRequest: FriendsItem, position: Int) {
            Glide.with(itemView.context)
                .load(friendRequest.profilePicture)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_profile).error(R.drawable.ic_profile)
                )
                .into(binding.profileImage)
            binding.individualName.text = friendRequest.name
            binding.individualUsername.text = friendRequest.username

            binding.profileImage.setOnClickListener {
                val intent = Intent(itemView.context, UserDetailActivity::class.java)
                intent.putExtra(UserDetailActivity.USER_ID, friendRequest.userId)
                itemView.context.startActivity(intent)
            }

            binding.acceptButton.setOnClickListener {
                val positionItem = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    activity.acceptFriendRequest(friendRequest.userId!!, positionItem)
                }
            }

            binding.rejectButton.setOnClickListener {
                val positionItem = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    activity.rejectFriendRequest(friendRequest.userId!!, positionItem)
                }
            }
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
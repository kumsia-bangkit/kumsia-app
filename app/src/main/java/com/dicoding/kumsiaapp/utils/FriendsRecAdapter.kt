package com.dicoding.kumsiaapp.utils

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.data.remote.response.UsersItem
import com.dicoding.kumsiaapp.databinding.ItemFriendRecommendationBinding
import com.dicoding.kumsiaapp.view.individual.UserDetailActivity

class FriendsRecAdapter : androidx.recyclerview.widget.ListAdapter<UsersItem, FriendsRecAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemFriendRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val friend = getItem(position)
        holder.bind(friend)
    }

    class MyViewHolder(private val binding: ItemFriendRecommendationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(friend: UsersItem) {
            Glide.with(itemView.context)
                .load(friend.profilePicture)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_profile).error(R.drawable.ic_profile)
                )
                .into(binding.circleImageView2)
            binding.userName.text = friend.name
            binding.userAge.text = DateFormatter.getAgeFromDate(friend.dob!!)

            binding.detailButton.setOnClickListener {
                val intent = Intent(itemView.context, UserDetailActivity::class.java)
                intent.putExtra(UserDetailActivity.USER_ID, friend.userId)
                itemView.context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UsersItem>() {
            override fun areItemsTheSame(oldItem: UsersItem, newItem: UsersItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: UsersItem, newItem: UsersItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
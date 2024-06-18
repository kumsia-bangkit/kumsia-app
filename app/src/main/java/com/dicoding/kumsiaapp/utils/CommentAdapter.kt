package com.dicoding.kumsiaapp.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.data.remote.response.CommentsItem
import com.dicoding.kumsiaapp.databinding.ItemCommentBinding

class CommentAdapter : androidx.recyclerview.widget.ListAdapter<CommentsItem, CommentAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val comment = getItem(position)
        holder.bind(comment)
    }

    class MyViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: CommentsItem) {
            Glide.with(itemView.context)
                .load(comment.userPicture)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_profile).error(R.drawable.ic_profile)
                )
                .into(binding.circleImageView2)

            binding.apply {
                userName.text = comment.userName
                tvComment.text = comment.commentText
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CommentsItem>() {
            override fun areItemsTheSame(oldItem: CommentsItem, newItem: CommentsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: CommentsItem, newItem: CommentsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
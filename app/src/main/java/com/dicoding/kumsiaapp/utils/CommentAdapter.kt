package com.dicoding.kumsiaapp.utils

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.data.remote.response.CommentsItem
import com.dicoding.kumsiaapp.databinding.ItemCommentBinding
import com.dicoding.kumsiaapp.view.individual.UserDetailActivity

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
                commentTime.text =
                    if (comment.createdAt == null) {
                        DateFormatter.commentDateFormat(DateFormatter.getCurrentDate())
                    } else {
                        DateFormatter.commentDateFormat(comment.createdAt)
                    }
            }

            binding.circleImageView2.setOnClickListener {
                val intent = Intent(itemView.context, UserDetailActivity::class.java)
                intent.putExtra(UserDetailActivity.USER_ID, comment.userId)
                itemView.context.startActivity(intent)
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
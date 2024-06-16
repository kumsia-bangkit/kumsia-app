package com.dicoding.kumsiaapp.utils

import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.kumsiaapp.R
import com.dicoding.kumsiaapp.data.remote.response.EventsItem
import com.dicoding.kumsiaapp.databinding.ItemEventBinding
import com.dicoding.kumsiaapp.view.organization.event.OrganizationDetailEventActivity

class EventAdapter : androidx.recyclerview.widget.ListAdapter<EventsItem, EventAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story)
    }

    class MyViewHolder(private val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: EventsItem) {
            binding.tvItemDate.text = DateFormatter.formatDate(event.eventStart!!)
            binding.eventTitle.text = event.name
            binding.eventType.text = event.type
            if (event.type == "Offline") {
                binding.eventType.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.grey))
            } else {
                binding.eventType.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, android.R.color.holo_green_dark))
            }

            binding.tvItemLikes.text = event.likeCount.toString()
            Glide.with(itemView.context)
                .load(event.profilePicture)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.people_event).error(R.drawable.people_event)
                ).into(binding.ivItemPhoto)

            binding.detailButton.setOnClickListener {
                val intent = Intent(itemView.context, OrganizationDetailEventActivity::class.java)
                intent.putExtra(OrganizationDetailEventActivity.EVENT_DATA, event)
                itemView.context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventsItem>() {
            override fun areItemsTheSame(oldItem: EventsItem, newItem: EventsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: EventsItem, newItem: EventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
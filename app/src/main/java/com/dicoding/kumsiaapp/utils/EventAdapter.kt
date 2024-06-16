package com.dicoding.kumsiaapp.utils

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
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
            binding.tvItemDate.text = event.eventStart
            binding.eventTitle.text = event.name
            binding.eventType.text = event.type
            binding.tvItemLikes.text = event.likeCount.toString()

            binding.detailButton.setOnClickListener {
                val intent = Intent(binding.root.context, OrganizationDetailEventActivity::class.java)
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
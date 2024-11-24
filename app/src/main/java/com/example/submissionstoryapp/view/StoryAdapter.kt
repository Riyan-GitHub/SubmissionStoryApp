package com.example.submissionstoryapp.view

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submissionstoryapp.R
import com.example.submissionstoryapp.data.remote.pref.ListStoryItem
import com.example.submissionstoryapp.databinding.CardItemBinding
import com.example.submissionstoryapp.view.detail.DetailActivity
import com.example.submissionstoryapp.view.detail.DetailActivity.Companion.STORY

class StoryAdapter : ListAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(private val binding: CardItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            Glide
                .with(binding.root.context)
                .load(story.photoUrl)
                .placeholder(R.drawable.baseline_photo_24)
                .error(R.drawable.baseline_photo_24)
                .into(binding.ivItemPhoto)
            binding.ivItemName.text = story.name
            binding.ivItemDescription.text = story.description

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(STORY, story)

                itemView.context.startActivity(intent)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val stories = getItem(position)
        holder.bind(stories)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
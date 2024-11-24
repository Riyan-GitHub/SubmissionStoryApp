package com.example.submissionstoryapp.view.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.submissionstoryapp.R
import com.example.submissionstoryapp.data.remote.pref.ListStoryItem
import com.example.submissionstoryapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val story = intent.getParcelableExtra<ListStoryItem>(STORY) as ListStoryItem

        Glide
            .with(this)
            .load(story.photoUrl)
            .placeholder(R.drawable.baseline_photo_24)
            .error(R.drawable.baseline_photo_24)
            .into(binding.tvDetailPhoto)
        binding.tvDetailName.text = story.name
        binding.tvDetailDescription.text = story.description
    }

    companion object {
        const val STORY = "story"
    }
}
package com.example.submissionstoryapp.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionstoryapp.R
import com.example.submissionstoryapp.data.remote.Result
import com.example.submissionstoryapp.databinding.ActivityMainBinding
import com.example.submissionstoryapp.view.StoryAdapter
import com.example.submissionstoryapp.view.ViewModelFactory
import com.example.submissionstoryapp.view.detail.AddStoryActivity
import com.example.submissionstoryapp.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private val storiesAdapter = StoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar?.show()

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        setupView()
        setupAction()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout_action -> {
                viewModel.logout()
                viewModel.removeEmail() // Hapus email dari DataStore
                Toast.makeText(this, "Email berhasil dihapus", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun setupView() {
        binding.rvStory.apply {
            adapter = storiesAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        viewModel.getAllStories().observe(this) { result ->
            when (result) {
                is Result.Error -> {
                    binding.linearProgressBar.visibility = View.GONE
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }

                is Result.Loading -> {
                    binding.linearProgressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.linearProgressBar.visibility = View.GONE
                    if (result.data.error == true) {
                        Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                    } else {
                        storiesAdapter.submitList(result.data.listStory)
                    }
                }
            }
        }
    }

    private fun setupAction() {
        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }
}
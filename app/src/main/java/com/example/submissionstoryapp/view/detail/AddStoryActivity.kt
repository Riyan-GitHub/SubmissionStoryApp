package com.example.submissionstoryapp.view.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.submissionstoryapp.R
import com.example.submissionstoryapp.data.remote.Result
import com.example.submissionstoryapp.databinding.ActivityAddStoryBinding
import com.example.submissionstoryapp.getImageUri
import com.example.submissionstoryapp.reduceFileImage
import com.example.submissionstoryapp.uriToFile
import com.example.submissionstoryapp.view.ViewModelFactory
import com.example.submissionstoryapp.view.main.MainActivity
import com.example.submissionstoryapp.view.main.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding

    private val viewModel by viewModels<MainViewModel> { ViewModelFactory.getInstance(this) }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            binding.addStoryImage.setImageURI(uri)
            viewModel.setCurrentImageUri(uri)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            binding.addStoryImage.setImageURI(viewModel.currentImageUri.value)
        } else {
            viewModel.setCurrentImageUri(null)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.show()
        supportActionBar?.title = getString(R.string.upload_story)

        viewModel.currentImageUri.observe(this) {
            binding.addStoryImage.setImageURI(it)
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.uploadButton.setOnClickListener { uploadStory() }
    }

    private fun uploadStory() {
        viewModel.currentImageUri.value?.let { uri ->
            lifecycleScope.launch {
                binding.progressBar.visibility = View.VISIBLE

                val imageFile = withContext(Dispatchers.IO) {
                    uriToFile(uri, this@AddStoryActivity).reduceFileImage()
                }

                Log.d("Image File", "showImage: ${imageFile.path}")
                val description = binding.edAddDescription.text.toString()

                viewModel.uploadStory(imageFile, description).observe(this@AddStoryActivity) { result ->
                    when (result) {
                        is com.example.submissionstoryapp.data.remote.Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@AddStoryActivity, result.error, Toast.LENGTH_SHORT).show()
                        }
                        is com.example.submissionstoryapp.data.remote.Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            if (result.data.error == true) {
                                Toast.makeText(this@AddStoryActivity, result.data.message, Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@AddStoryActivity, result.data.message, Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@AddStoryActivity, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                            }
                        }
                    }
                }
            }
        } ?: Toast.makeText(this, getString(R.string.upload_error), Toast.LENGTH_SHORT).show()
    }

    private fun startCamera() {
        viewModel.setCurrentImageUri(getImageUri(this))
        launcherIntentCamera.launch(viewModel.currentImageUri.value!!)
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}
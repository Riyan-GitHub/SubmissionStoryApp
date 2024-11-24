package com.example.submissionstoryapp.view.main

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.submissionstoryapp.data.pref.UserModel
import com.example.submissionstoryapp.data.remote.StoriesRepository
import com.example.submissionstoryapp.data.remote.UserRepository
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel(private val repository: UserRepository, private val storiesRepository: StoriesRepository) : ViewModel() {

    private var _currentImageUri = MutableLiveData<Uri?>()
    val currentImageUri: MutableLiveData<Uri?> = _currentImageUri

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getAllStories() = storiesRepository.getAllStories()


    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun removeEmail() {
        viewModelScope.launch {
            repository.removeEmail()
        }
    }

    fun uploadStory(imageFile: File, desc: String) = storiesRepository.uploadStory(imageFile, desc)

    fun setCurrentImageUri(uri: Uri?) {
        _currentImageUri.value = uri
    }
}
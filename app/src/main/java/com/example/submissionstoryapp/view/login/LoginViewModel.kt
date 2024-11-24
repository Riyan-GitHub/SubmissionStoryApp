package com.example.submissionstoryapp.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissionstoryapp.data.pref.UserModel
import com.example.submissionstoryapp.data.remote.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel (private val repository: UserRepository) : ViewModel() {
    fun saveSession(user: UserModel) {
        viewModelScope.launch { repository.saveSession(user) }
    }
    fun login(email: String, password: String) = repository.login(email, password)
}
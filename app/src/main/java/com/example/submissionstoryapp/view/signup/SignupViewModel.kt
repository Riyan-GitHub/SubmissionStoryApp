package com.example.submissionstoryapp.view.signup

import androidx.lifecycle.ViewModel
import com.example.submissionstoryapp.data.remote.UserRepository

class SignupViewModel(private val repository: UserRepository) : ViewModel() {
    fun signup(name: String, email: String, password: String) = repository.signup(name, email, password)
}
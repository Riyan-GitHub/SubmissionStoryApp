package com.example.submissionstoryapp.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.submissionstoryapp.data.remote.StoriesRepository
import com.example.submissionstoryapp.data.remote.UserRepository
import com.example.submissionstoryapp.di.Injection
import com.example.submissionstoryapp.view.login.LoginViewModel
import com.example.submissionstoryapp.view.main.MainViewModel
import com.example.submissionstoryapp.view.signup.SignupViewModel

class ViewModelFactory(private val userRepository: UserRepository, private val storiesRepository: StoriesRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository, storiesRepository) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(userRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(
                        Injection.provideRepository(context),
                        Injection.provideStoriesRepository(context)
                    )
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}
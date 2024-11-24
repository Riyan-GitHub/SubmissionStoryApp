package com.example.submissionstoryapp.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.submissionstoryapp.R
import com.example.submissionstoryapp.data.pref.UserModel
import com.example.submissionstoryapp.data.remote.Result
import com.example.submissionstoryapp.databinding.ActivityLoginBinding
import com.example.submissionstoryapp.view.ViewModelFactory
import com.example.submissionstoryapp.view.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val emailText = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEdit = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordText = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEdit = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)

        val together = AnimatorSet().apply {
            playTogether(login)
        }

        AnimatorSet().apply {
            playSequentially(title,
                message,
                emailText,
                emailEdit,
                passwordText,
                passwordEdit,
                together
            )
            start()
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            viewModel.login(email, password).observe(this) { result ->
                when (result) {
                    is com.example.submissionstoryapp.data.remote.Result.Error -> {
                        binding.linearProgressBar.visibility = View.GONE
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }
                    is com.example.submissionstoryapp.data.remote.Result.Loading -> {
                        binding.linearProgressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.linearProgressBar.visibility = View.GONE
                        if (result.data.error == true) {
                            Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                        } else {
                            result.data.loginResult?.let {
                                Toast.makeText(this,
                                    getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                                viewModel.saveSession(UserModel(email, it.token.toString(), true))
                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                }
            }
        }
    }
}
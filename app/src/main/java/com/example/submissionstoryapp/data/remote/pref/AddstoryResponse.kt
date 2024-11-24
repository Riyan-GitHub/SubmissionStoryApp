package com.example.submissionstoryapp.data.remote.pref

import com.google.gson.annotations.SerializedName

data class AddstoryResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

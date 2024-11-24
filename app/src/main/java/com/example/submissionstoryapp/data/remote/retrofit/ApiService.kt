package com.example.submissionstoryapp.data.remote.retrofit

import com.example.submissionstoryapp.data.remote.pref.AddstoryResponse
import com.example.submissionstoryapp.data.remote.pref.AllStoryResponse
import com.example.submissionstoryapp.data.remote.pref.LoginResponse
import com.example.submissionstoryapp.data.remote.pref.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): AddstoryResponse

    @GET("stories")
    suspend fun getAllStories(): AllStoryResponse
}
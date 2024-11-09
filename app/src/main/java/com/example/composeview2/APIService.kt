package com.example.composeview2

import retrofit2.http.GET

interface ApiService {
    @GET("users/cclli4")
    suspend fun getGithubProfile(): GithubProfile
}
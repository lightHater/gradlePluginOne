package com.example.textlinkdemo

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitService {

    companion object {
        const val  BASE_URL = "https://api.github.com/"
    }

    @GET("users/{user}")
    fun getUser(@Path("user")user: String): Call<TestUser>
}
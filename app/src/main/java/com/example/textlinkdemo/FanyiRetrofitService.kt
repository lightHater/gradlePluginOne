package com.example.textlinkdemo

import android.util.Log
import retrofit2.Call
import retrofit2.http.*
import rx.Observable

interface FanyiRetrofitService {

    @POST("translate?doctype=json&jsonversion=&keyfrom=&model=&mid=&imei=&vendor=&screen=&ssid=&network=&abtest=")
    @FormUrlEncoded
    fun getFanyi(@Field ("i") src: String, @Query("type")value: String): Call<FanyiResponse>

    @GET("data/{user}")
    fun test(@Path("user") user: String): Observable<FanyiResponse>

    public fun defaultMethod() {
        Log.d("wow", "default method")
    }

}
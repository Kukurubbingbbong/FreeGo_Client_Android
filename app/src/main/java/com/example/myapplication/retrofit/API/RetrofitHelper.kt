package com.example.myapplication.retrofit.API

import com.example.myapplication.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://3.34.32.20:8080")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getFridgeAPI() : FridgeAPI{
        return retrofit.create(FridgeAPI::class.java)
    }
    fun getUserAPI() : UserAPI{
        return retrofit.create(UserAPI::class.java)
    }
}
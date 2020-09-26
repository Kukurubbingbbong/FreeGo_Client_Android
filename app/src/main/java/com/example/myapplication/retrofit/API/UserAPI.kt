package com.example.myapplication.retrofit.API

import com.example.myapplication.retrofit.DTO.FirstData
import com.example.myapplication.retrofit.DTO.UserRequest
import retrofit2.Call
import retrofit2.http.*

interface UserAPI {
    @POST("/auth/register")
    fun register(
        @Body user : UserRequest
    ) : Call<FirstData>

    @GET("/auth/finduser/{id}")
    fun getUser(
        @Path("id") id: String
    ) : Call<FirstData>
    @POST("/auth/login/")
    fun login(
        @Body user : UserRequest
    ) : Call<FirstData>
}
package com.example.myapplication.retrofit.API

import com.example.myapplication.retrofit.DTO.BarCodeFoodInfo
import com.example.myapplication.retrofit.DTO.FirstData
import com.example.myapplication.retrofit.DTO.FoodData
import retrofit2.Call
import retrofit2.http.*

interface FridgeAPI {
    @GET("/material/show/{id}")
    fun getTable(
        @Path("id") id: String
    ) : Call<FoodData>

    @GET("/material/find/{id}")
    fun findFood(
        @Path("id") id : String,
        @Query("p_name") p_name : String
    ) : Call<FirstData>

    @GET("/material/late/{id}")
    fun findLateFood(
        @Path("id") id : String
    ) : Call<FoodData>

    @POST("/material/insert")
    fun insertFood(
        @Query("code") code : Int,
        @Field("id") id : String,
        @Field("p_name") p_name: String,
        @Field("p_number") p_number : Int,
        @Field("p_ex_date") p_ex_date : String
    ) : Call<FirstData>

    @PUT("/material/update")
    fun updateFood(
        @Field("id") id : String,
        @Field("p_name") p_name: String,
        @Field("p_number") p_number: Int
    ) : Call<FirstData>

    @DELETE("/delete")
    fun deleteFood(
        @Field("id") id : String,
        @Field("p_name") p_name: String
    ) : Call<FirstData>

    @GET("/code/lookupcode/{code}")
    fun barCodeShow(
        @Path("code") code : Int
    ) : Call<BarCodeFoodInfo>

}
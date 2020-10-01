package com.example.myapplication.retrofit.DTO

import com.google.gson.annotations.SerializedName

data class FoodData(
    val code : Int,
    val data : ArrayList<Food>,
    val message : String
)

data class Food(
    val id : String,
    @SerializedName("ex_date")
    val p_ex_date : String,
    val p_name : String,
    @SerializedName("number")
    val p_number : Int,
    var img_link : String? = null
)
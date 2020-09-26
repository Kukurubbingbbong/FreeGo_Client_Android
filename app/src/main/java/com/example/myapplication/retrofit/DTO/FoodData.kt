package com.example.myapplication.retrofit.DTO

data class FoodData(
    val code : Int,
    val data : ArrayList<Food>,
    val message : String
)

data class Food(
    val id : String,
    val p_ex_date : String,
    val p_name : String,
    val p_number : Int,
    val img_link : String
)
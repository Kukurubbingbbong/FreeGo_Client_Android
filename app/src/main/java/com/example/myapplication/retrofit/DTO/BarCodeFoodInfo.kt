package com.example.myapplication.retrofit.DTO

data class BarCodeFoodInfo(
    val code : Int,
    val data : ArrayList<String>? = null,
    val message : String
)
package com.example.myapplication.retrofit.DTO

data class Recipe(
    val code : Int,
    val data : RecipeData
)

data class RecipeData(
    val foodname : ArrayList<String>,
    val recipelink : ArrayList<String>
)
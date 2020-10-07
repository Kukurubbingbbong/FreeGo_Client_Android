package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplication.retrofit.API.RetrofitHelper
import com.example.myapplication.retrofit.DTO.Recipe
import kotlinx.android.synthetic.main.activity_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        Glide.with(applicationContext).load(intent.getStringExtra("img_link")).placeholder(R.drawable.loading).error(R.drawable.noimage).into(detailImage)
        var ingredient = intent.getStringExtra("p_name")

        when {
            ingredient.contains("으로 할 수 있는 요리 알려 줘") -> {
                ingredient = ingredient.split("으로 할 수 있는 요리 알려 줘")[0]
            }
            ingredient.contains("로 할 수 있는 요리 알려 줘") -> {
                ingredient = ingredient.split("로 할 수 있는 요리 알려 줘")[0]
            }
            ingredient.contains("으로 할 수 있는 음식 알려 줘") -> {
                ingredient = ingredient.split("으로 할 수 있는 음식 알려 줘")[0]
            }
            ingredient.contains("로 할 수 있는 음식 알려 줘") -> {
                ingredient = ingredient.split("로 할 수 있는 음식 알려 줘")[0]
            }
        }


        detailFoodTitle.text = ingredient

        var recipe = ArrayList<String>()

        if (ingredient != null) {
            RetrofitHelper().getFridgeAPI().getRecipe(ingredient).enqueue(object : Callback<Recipe>{
                override fun onFailure(call: Call<Recipe>, t: Throwable) {}

                override fun onResponse(call: Call<Recipe>, response: Response<Recipe>) {
                    if (response.isSuccessful) {
                        if(response.body()!!.code == 200) {
                            progressBar.visibility = View.GONE
                            recipeList.adapter = RecipeAdapter(this@DetailActivity, response.body()!!.data.foodname)
                            recipe = response.body()!!.data.recipelink
                            if(recipe.size == 0){
                                recipeList.setBackgroundColor(Color.GRAY)
                                noRecipe.alpha = 1f
                            } else {
                                noRecipe.visibility = View.GONE
                            }
                        }
                    }
                }

            })
        }



        recipeList.onItemClickListener =
            AdapterView.OnItemClickListener { adapterVIew, view, position, l ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(recipe[position]))
                startActivity(intent)
            }

    }
}
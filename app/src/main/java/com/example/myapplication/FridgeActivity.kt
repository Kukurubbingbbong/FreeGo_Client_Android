package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.retrofit.API.RetrofitHelper
import com.example.myapplication.retrofit.DTO.Food
import com.example.myapplication.retrofit.DTO.FoodData
import kotlinx.android.synthetic.main.activity_fridge.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FridgeActivity : AppCompatActivity() {


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fridge)

        val id = intent.getStringExtra("name")

        var dataList = ArrayList<Food>()



        if(id != ""){
            val service = RetrofitHelper().getFridgeAPI()

            service.getTable(id).enqueue(object : Callback<FoodData>{
                override fun onFailure(call: Call<FoodData>, t: Throwable) {
                    Log.d("ERROR",t.toString())
                }

                override fun onResponse(call: Call<FoodData>, response: Response<FoodData>) {
                    if(response.isSuccessful) {
                        Log.d("CODE", response.body()!!.code.toString())
                        if (response.body()!!.code == 200) {
                            dataList = response.body()!!.data
                            Log.d("MESSAGE", response.body()!!.message)
                            if(dataList.size == 0){
                                itemList.setBackgroundColor(Color.GRAY)
                                textView3.alpha = 1f
                            }
                            val adapter =
                                ItemListAdapter(this@FridgeActivity, dataList, R.layout.food_row,id)
                            itemList.adapter = adapter
                        }
                    }
                }
            })


        }
        itemList.setOnItemClickListener{ parent: AdapterView<*>, view: View, position: Int, l: Long ->
            val intent = Intent(this@FridgeActivity, DetailActivity::class.java)
            intent.putExtra("p_id", dataList[position].id)
            intent.putExtra("p_name",dataList[position].p_name)
            intent.putExtra("p_ex_date", dataList[position].p_ex_date)
            intent.putExtra("p_number",dataList[position].p_number)
            intent.putExtra("img_ling", dataList[position].img_link)
            startActivity(intent)
        }

        insertButton.setOnClickListener {
            val intent = Intent(this@FridgeActivity, SelectActivity::class.java)
            intent.putExtra("id",id)
            startActivity(intent)
        }
    }
}
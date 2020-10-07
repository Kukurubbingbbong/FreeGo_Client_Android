package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.embeddedtest.retrofit.DTO.InsertFoodBody
import com.example.myapplication.retrofit.API.RetrofitHelper
import com.example.myapplication.retrofit.DTO.FirstData
import kotlinx.android.synthetic.main.activity_insert_add.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class InsertAddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_add)

        val id = intent.getStringExtra("id")
        val cal = Calendar.getInstance()
        val foodName = intent.getStringExtra("name")
        val date = intent.getStringExtra("date")
        val format = SimpleDateFormat("yyyyMMdd")

        var monthc = ""
        if(cal.get(Calendar.MONTH) < 9){
            monthc = "0"
        }
        var day = ""
        if(cal.get(Calendar.DAY_OF_MONTH) < 10){
            day = "0"
        }
        val today = "" + cal.get(Calendar.YEAR) + monthc + (cal.get(Calendar.MONTH) + 1)+ day + cal.get(Calendar.DAY_OF_MONTH)

        insertFoodTitle.text = foodName

        val todayDate = format.parse(today)
        val ex_date = format.parse(date)
        var minus = ""
        if(todayDate.time > ex_date.time){
            minus = "+"
        }

        insertFoodDate.text = "D" + minus + ((todayDate.time - ex_date.time) / (24*60*60*1000))

        insertNextButton.setOnClickListener {
            RetrofitHelper().getFridgeAPI().insertFood("nocode", InsertFoodBody(id, foodName, insertFoodCount.text.toString().toInt(), date)).enqueue(object : Callback<FirstData>{
                override fun onFailure(call: Call<FirstData>, t: Throwable) {
                    Log.d("ERROR", t.toString())
                }

                override fun onResponse(call: Call<FirstData>, response: Response<FirstData>) {
                    if(response.isSuccessful){
                        if(response.body()!!.code == 200){
                            Toast.makeText(this@InsertAddActivity, "제품이 추가되었습니다", Toast.LENGTH_LONG).show()
                            finish()
                        } else {
                            if(response.body()!!.message == "already exist"){
                                Toast.makeText(this@InsertAddActivity,"이미 등록된 제품입니다", Toast.LENGTH_LONG).show()
                                finish()
                            }
                        }
                    }
                }

            })
        }

    }
}
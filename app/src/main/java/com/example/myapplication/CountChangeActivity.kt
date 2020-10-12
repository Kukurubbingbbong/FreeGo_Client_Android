package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplication.retrofit.API.RetrofitHelper
import com.example.myapplication.retrofit.DTO.FirstData
import com.example.myapplication.retrofit.DTO.UpdateFood
import kotlinx.android.synthetic.main.activity_count_change.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CountChangeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_change)

        Glide.with(this@CountChangeActivity).load(intent.getStringExtra("img_link")).placeholder(R.drawable.loading).error(R.drawable.noimage).into(updateImgFood)

        updateFoodText.text = intent.getStringExtra("p_name")

        updateFoodCount.setText(intent.getStringExtra("p_number"))

        val date: Array<String> = intent.getStringExtra("p_ex_date").split(" ".toRegex()).toTypedArray()

        val current_date = Date()
        @SuppressLint("SimpleDateFormat") val format =
            SimpleDateFormat("yyyy-MM-dd")
        val ex_month = date_check(date[2])
        val ex_date_String: String =
            date[3] + "-" + ex_month + "-" + date.get(1)
        val ex_date: Date
        var date_big = true
        var date_second: Long = 0
        try {
            ex_date = format.parse(ex_date_String)
            if (ex_date.time >= current_date.time) {
                date_second = ex_date.time - current_date.time
                date_second = date_second / (24 * 60 * 60 * 1000) + 1
                date_big = true
            } else if (ex_date.time < current_date.time) {
                date_second = current_date.time - ex_date.time
                date_second = date_second / (24 * 60 * 60 * 1000) + 1
                date_big = false
            }
            date_second = Math.abs(date_second)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        if (date_big) {
            updateFoodDate.text = "D-$date_second"
        } else {
            updateFoodDate.text = "D+$date_second"
        }

        updateNextBtn.setOnClickListener {
            if(updateFoodCount.text.isNotEmpty()) {
                RetrofitHelper().getFridgeAPI().updateFood(
                    UpdateFood(
                        intent.getStringExtra("p_id"),
                        intent.getStringExtra("p_name"),
                        updateFoodCount.text.toString().toInt()
                    )
                ).enqueue(object :
                    Callback<FirstData> {
                    override fun onFailure(call: Call<FirstData>, t: Throwable) {
                        Log.d("TAG", t.toString())
                    }

                    override fun onResponse(call: Call<FirstData>, response: Response<FirstData>) {
                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                if (response.body()!!.code == 200) {
                                    Toast.makeText(
                                        this@CountChangeActivity,
                                        "개수 변경에 성공하였습니다",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this@CountChangeActivity,
                                        "개수 변경에 실패하였습니다",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    }

                })
            } else {
                Toast.makeText(this@CountChangeActivity, "갯수를 적어주세요!", Toast.LENGTH_LONG).show()
            }
        }

    }
    private fun date_check(month: String): String {
        when (month) {
            "Jan" -> return "01"
            "Feb" -> return "02"
            "Mar" -> return "03"
            "Apr" -> return "04"
            "May" -> return "05"
            "Jun" -> return "06"
            "Jul" -> return "07"
            "Aug" -> return "08"
            "Sep" -> return "09"
            "Oct" -> return "10"
            "Nov" -> return "11"
            "Dec" -> return "12"
        }
        return ""
    }
}
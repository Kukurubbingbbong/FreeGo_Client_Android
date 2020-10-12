package com.example.myapplication

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.embeddedtest.retrofit.DTO.InsertFoodBody
import com.example.myapplication.retrofit.API.RetrofitHelper
import com.example.myapplication.retrofit.DTO.FirstData
import kotlinx.android.synthetic.main.activity_addfood.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class AddFoodActivity : AppCompatActivity() {
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addfood)

        val data = intent.getStringArrayListExtra("data")
        Glide.with(this@AddFoodActivity).load(data[4]).placeholder(R.drawable.loading).error(R.drawable.noimage).into(imgFood)
        if(data != null){
            foodText.text = data[1]
        }

        val cal = Calendar.getInstance()


        val format = SimpleDateFormat("yyyyMMdd")
        var month = ""
        if(cal.get(Calendar.MONTH) < 9){
            month = "0"
        }
        var day = ""
        if(cal.get(Calendar.DAY_OF_MONTH) < 10){
            day = "0"
        }

        val today = format.parse("" + cal.get(Calendar.YEAR) + month + (cal.get(Calendar.MONTH) + 1) + day + cal.get(
            Calendar.DAY_OF_MONTH))

        var dateString = ""
        foodDate.setOnClickListener {
            val dialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{ view, year, month, dayOfMonth ->
                var monthc = ""
                if(month < 9){
                    monthc = "0"
                }
                var day = ""
                if(dayOfMonth < 10){
                    day = "0"
                }
                dateString = "" + year + monthc + (month + 1) + day + dayOfMonth
                val exDate = format.parse(dateString)
                foodDate.text = "D-" + ((exDate.time - today.time) / (24*60*60*1000))
            },cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
            dialog.show()
        }

        nextBtn.setOnClickListener {
            if(foodCount.text.isEmpty()){
                Toast.makeText(this@AddFoodActivity, "음식 갯수를 적어주세요!", Toast.LENGTH_LONG).show()
            } else {
                if(dateString == ""){
                    Toast.makeText(this@AddFoodActivity, "유통기한을 설정해주세요!", Toast.LENGTH_LONG).show()
                } else {
                    RetrofitHelper().getFridgeAPI().insertFood(
                        intent.getStringExtra("barcode"),
                        InsertFoodBody(
                            intent.getStringExtra("id"),
                            data[1],
                            foodCount.text.toString().toInt(),
                            dateString
                        )
                    ).enqueue(object : Callback<FirstData> {
                        override fun onFailure(call: Call<FirstData>, t: Throwable) {}

                        override fun onResponse(
                            call: Call<FirstData>,
                            response: Response<FirstData>
                        ) {
                            if (response.isSuccessful) {
                                if (response.body()!!.code == 200) {
                                    Toast.makeText(
                                        this@AddFoodActivity,
                                        "제품 등록에 성공했습니다",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                    finish()

                                } else {
                                    if (response.body()!!.message == "already exist") {
                                        Toast.makeText(
                                            this@AddFoodActivity,
                                            "이미 등록된 제품입니다",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        finish()
                                    }
                                }
                            }
                        }
                    })
                }
            }
        }
    }
}
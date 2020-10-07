package com.example.myapplication

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.example.myapplication.retrofit.API.RetrofitHelper
import com.example.myapplication.retrofit.DTO.FirstData
import com.example.myapplication.retrofit.DTO.UserRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {

    var id : String = ""
    var pwd : String = ""

    val service = RetrofitHelper().getUserAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            getData()

            if(id != "" || pwd != "") {
                service.login(UserRequest(id,pwd)).enqueue(object :
                    Callback<FirstData> {
                    override fun onFailure(call: Call<FirstData>, t: Throwable) {
                        Log.d("LOGIN", t.toString())
                        Toast.makeText(this@SplashActivity, "인터넷 연결 없음\n인터넷을 연결해주세요!", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<FirstData>, response: Response<FirstData>) {
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                if (response.body()!!.code == 200) {
                                    val intent =
                                        Intent(this@SplashActivity, FridgeActivity::class.java)
                                    intent.putExtra("name", id)
                                    startActivity(intent)
                                }
                            }
                        }
                    }
                })
            } else {
                startActivity(Intent(this@SplashActivity, SelectSignActivity::class.java))
            }
            finish()
        },3000)

    }
    private fun getData(){
        val pref = this.getSharedPreferences("user", Activity.MODE_PRIVATE)
        id = pref.getString("ID","").toString()
        pwd = pref.getString("PWD", "").toString()
    }
}
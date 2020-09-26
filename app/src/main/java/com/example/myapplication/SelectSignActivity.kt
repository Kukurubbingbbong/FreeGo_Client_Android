package com.example.myapplication

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.myapplication.retrofit.API.RetrofitHelper
import com.example.myapplication.retrofit.DTO.FirstData
import com.example.myapplication.retrofit.DTO.UserRequest
import kotlinx.android.synthetic.main.activity_selectsign.*
import kotlinx.android.synthetic.main.activity_signin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectSignActivity : AppCompatActivity() {

    val service = RetrofitHelper().getUserAPI()

    var id : String = ""
    var pwd : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selectsign)

        getData()

        if(id != "" || pwd != "") {
            service.login(UserRequest(id,pwd)).enqueue(object :
                Callback<FirstData> {
                override fun onFailure(call: Call<FirstData>, t: Throwable) {
                    Log.d("LOGIN", t.toString())
                    Toast.makeText(this@SelectSignActivity, "로그인 중 오류발생", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<FirstData>, response: Response<FirstData>) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            if (response.body()!!.code == 200) {
                                val intent =
                                    Intent(this@SelectSignActivity, FridgeActivity::class.java)
                                intent.putExtra("name", IDText.text.toString())
                                startActivity(intent)
                            }
                        }
                    }
                }
            })
        }

        goSignIn.setOnClickListener{
            startActivity(Intent(this@SelectSignActivity, SignInActivity::class.java))
        }

        goSignUp.setOnClickListener {
            startActivity(Intent(this@SelectSignActivity, RegisterActivity::class.java))
        }
    }

    private fun getData(){
        val pref = this.getSharedPreferences("user", Activity.MODE_PRIVATE)
        id = pref.getString("ID","").toString()
        pwd = pref.getString("PWD", "").toString()
    }
}
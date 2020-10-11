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
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_signin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity : AppCompatActivity() {

    val service = RetrofitHelper().getUserAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        btnSignIn.setOnClickListener {
            if(IDText.text.toString().isNotEmpty() && PwdText.text.toString().isNotEmpty()){
                service.login(UserRequest(IDText.text.toString(), PwdText.text.toString())).enqueue(object : Callback<FirstData>{
                    override fun onFailure(call: Call<FirstData>, t: Throwable) {
                        Log.d("LOGIN", t.toString())
                        Toast.makeText(this@SignInActivity, "로그인 중 오류발생", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<FirstData>, response: Response<FirstData>) {
                        if(response.isSuccessful){
                            if(response.body() != null){
                                if(response.body()!!.code == 200){
                                    val intent = Intent(this@SignInActivity, WelcomeActivity::class.java)
                                    intent.putExtra("name",IDText.text.toString())
                                    saveData()
                                    startActivity(intent)
                                    finish()
                                } else {
                                    if(response.body()!!.message == "pwd is wrong") {
                                        Toast.makeText(
                                            this@SignInActivity,
                                            "비밀번호가 틀렸습니다.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        Log.d("TAG1", response.code().toString())
                                        IDText.text = null
                                        PwdText.text = null
                                    } else {
                                        Toast.makeText(this@SignInActivity, "아이디를 찾지 못했습니다.", Toast.LENGTH_LONG).show()
                                        Log.d("TAG1", response.code().toString())
                                        IDText.text = null
                                        PwdText.text = null
                                    }
                                }
                            }
                            else {
                                Toast.makeText(this@SignInActivity, "로그인에 실패했습니다", Toast.LENGTH_LONG).show()
                                Log.d("TAG1", response.code().toString())
                                IDText.text = null
                                PwdText.text = null
                            }
                        } else {
                            Toast.makeText(this@SignInActivity, "로그인에 실패했습니다", Toast.LENGTH_LONG).show()
                            Log.d("TAG2", response.code().toString())
                            IDText.text = null
                            PwdText.text = null
                        }
                    }
                })
            }
        }
    }

    fun saveData(){
        val pref = this.getSharedPreferences("user", Activity.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("ID",IDText.text.toString())
        editor.putString("PWD", PwdText.text.toString())
        editor.apply()
    }

}
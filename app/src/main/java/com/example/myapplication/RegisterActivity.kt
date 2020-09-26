package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.example.myapplication.retrofit.API.RetrofitHelper
import com.example.myapplication.retrofit.DTO.FirstData
import com.example.myapplication.retrofit.DTO.UserRequest
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    val TAG = "REGISTER"

    val userAPI = RetrofitHelper().getUserAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        checkPassword()

        btnSignUp.setOnClickListener {
            if(passwordCheckLayout.error == null && signUpID.text.toString().isNotEmpty() && signUpPwd.text.toString().isNotEmpty()){
                userAPI.register(UserRequest(signUpID.text.toString(), signUpPwd.text.toString())).enqueue(object : Callback<FirstData>{
                    override fun onFailure(call: Call<FirstData>, t: Throwable) {
                        Log.d(TAG,t.toString())
                        Toast.makeText(this@RegisterActivity, "회원가입 도중 서버 오류 발생",Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<FirstData>, response: Response<FirstData>) {
                        if(response.isSuccessful){
                            if(response.body() != null){
                                if(response.body()!!.code == 200){
                                    Toast.makeText(this@RegisterActivity, "회원가입에 성공했습니다", Toast.LENGTH_LONG).show()
                                    finish()
                                } else {
                                    Toast.makeText(this@RegisterActivity, "회원가입 도중 오류 발생",Toast.LENGTH_LONG).show()
                                    passwordCheckText.text = null
                                    signUpID.text = null
                                    signUpPwd.text = null
                                }
                            } else {
                                Toast.makeText(this@RegisterActivity, "회원가입에 실패했습니다", Toast.LENGTH_LONG).show()
                                Log.d(TAG, response.code().toString())
                                passwordCheckText.text = null
                                signUpID.text = null
                                signUpPwd.text = null
                            }
                        } else {
                            Toast.makeText(this@RegisterActivity, "회원가입에 실패했습니다", Toast.LENGTH_LONG).show()
                            Log.d(TAG, response.code().toString())
                            passwordCheckText.text = null
                            signUpID.text = null
                            signUpPwd.text = null
                        }
                    }

                })
            }
        }
    }

    private fun checkPassword(){
        passwordCheckText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(passwordCheckText.text.toString() != signUpPwd.text.toString()){
                    passwordCheckLayout.error = "비밀번호가 다릅니다!"
                } else {
                    passwordCheckLayout.error = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })
    }
}
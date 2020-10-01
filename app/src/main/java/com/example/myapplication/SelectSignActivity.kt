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





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selectsign)



        goSignIn.setOnClickListener{
            startActivity(Intent(this@SelectSignActivity, SignInActivity::class.java))
            finish()
        }

        goSignUp.setOnClickListener {
            startActivity(Intent(this@SelectSignActivity, RegisterActivity::class.java))
        }
    }


}
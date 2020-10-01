package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_select.*

class SelectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)
        btnBarcode.setOnClickListener {
            val id = intent.getStringExtra("id")
            val intent = Intent(this@SelectActivity, BarcodeActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
            finish()
        }

        btnInsert.setOnClickListener {
            val id = intent.getStringExtra("id")
            val intent = Intent(this@SelectActivity, InsertActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
            finish()
        }
    }
}
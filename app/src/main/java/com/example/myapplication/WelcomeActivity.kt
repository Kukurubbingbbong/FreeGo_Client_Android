package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val intent = intent
        name.text = intent.getStringExtra("name")

        register_end.setOnClickListener {
            val outIntent = Intent(this@WelcomeActivity, GuideActivity::class.java)
            startActivity(outIntent)
            finish()
            this.overridePendingTransition(R.anim.sliding_up, R.anim.stay)
        }

        textGroup.animate().alpha(1f).setDuration(1000).start()


    }
}
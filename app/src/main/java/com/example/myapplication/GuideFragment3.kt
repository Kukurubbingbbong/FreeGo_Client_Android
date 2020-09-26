package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView


class GuideFragment3 : Fragment() {
    private lateinit var guide3Button : Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_guide3, container, false)
        guide3Button = view.findViewById(R.id.guide3Button)

        guide3Button.setOnClickListener {
            startActivity(Intent(context,FridgeActivity::class.java))
            activity?.finish()
        }

        return view
    }


}
package com.beviswang.appplugin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater

class RepairMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(LayoutInflater.from(this).inflate(pResources.getLayout(R.layout.activity_main),null))
//        setContentView(LayoutInflater.from(this).inflate(R.layout.activity_main,null))
    }
}

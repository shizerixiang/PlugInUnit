package com.beviswang.pluginunit.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.beviswang.pluginunit.R

class RealActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_real)
    }
}
package com.beviswang.pluginunit

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class RealActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_real)
    }
}
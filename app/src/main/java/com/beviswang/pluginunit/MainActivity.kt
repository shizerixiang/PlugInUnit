package com.beviswang.pluginunit

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.beviswang.pluginlib.IReader
import dalvik.system.DexClassLoader
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mPluginClassLoader: ClassLoader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dexPath = getFileStreamPath("appplugin-debug.apk").absolutePath
        Log.e("1", "dexPath=$dexPath")
        val optDir = getDir("dex", Context.MODE_PRIVATE).absolutePath
        Log.e("1", "optDir=$optDir")

        mPluginClassLoader = DexClassLoader(dexPath, optDir, null, classLoader)

        btn1.setOnClickListener {
            showToast()
        }
    }

    private fun showToast() {
        val readerClass = mPluginClassLoader?.loadClass("com.beviswang.appplugin.Reader")
                ?: throw Exception("Null ClassLoader!")
        val reader: IReader = readerClass.newInstance() as IReader
        Toast.makeText(this@MainActivity, reader.getToastText(), Toast.LENGTH_LONG).show()
    }
}

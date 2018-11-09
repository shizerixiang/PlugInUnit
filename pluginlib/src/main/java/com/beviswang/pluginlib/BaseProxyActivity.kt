package com.beviswang.pluginlib

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

abstract class BaseProxyActivity : AppCompatActivity() {
    private lateinit var mProxyManager: IProxyManager
    private lateinit var mClassLoader: ClassLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mClassLoader = loadClassLoader()
        loadProxyManager()
        mProxyManager.onProxyCreated(savedInstanceState)
    }

    /** 加载 proxyManager */
    abstract fun loadProxyManager()

    /** 加载 ClassLoader */
    abstract fun loadClassLoader(): ClassLoader

    /**
     * 启动指定插件中的 Activity
     * @param className class 路径
     */
    abstract fun startPluginActivity(className: String)

    /** 加载代理 Activity 管理类 */
    fun loadProxyManagerActivity(className: String) {
        val mainProxyClazz = mClassLoader.loadClass(className)
        mProxyManager = mainProxyClazz.newInstance() as IProxyManager
        mProxyManager.setProxyActivity(this@BaseProxyActivity)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mProxyManager.onSaveProxyInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        mProxyManager.onRestoreProxyInstanceState(savedInstanceState)
    }

    override fun onRestart() {
        super.onRestart()
        mProxyManager.onProxyRestart()
    }

    override fun onStart() {
        super.onStart()
        mProxyManager.onProxyStart()
    }

    override fun onResume() {
        super.onResume()
        mProxyManager.onProxyResume()
    }

    override fun onPause() {
        super.onPause()
        mProxyManager.onProxyPause()
    }

    override fun onStop() {
        super.onStop()
        mProxyManager.onProxyStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mProxyManager.onProxyDestroy()
    }
}

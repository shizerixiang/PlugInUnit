package com.beviswang.pluginlib

import android.os.Bundle
import android.support.annotation.Nullable

/**
 * 生命周期代理接口
 * @author BevisWang
 */
interface IProxyManager {
    fun setProxyActivity(activity: BaseProxyActivity)

    fun onProxyCreated(@Nullable savedInstanceState: Bundle?)

    fun onSaveProxyInstanceState(outState: Bundle?){}

    fun onRestoreProxyInstanceState(savedInstanceState: Bundle?){}

    fun onProxyRestart(){}

    fun onProxyStart(){}

    fun onProxyResume(){}

    fun onProxyPause(){}

    fun onProxyStop(){}

    fun onProxyDestroy(){}
}
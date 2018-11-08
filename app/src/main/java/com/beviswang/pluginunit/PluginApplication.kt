package com.beviswang.pluginunit

import android.app.Application
import com.beviswang.pluginunit.activity.ProxyActivity
import com.beviswang.pluginunit.opt.HookOpt

class PluginApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        val hook = HookOpt(ProxyActivity::class.java,this)
        hook.hookSystemHandler()
        hook.hookAms()
    }
}
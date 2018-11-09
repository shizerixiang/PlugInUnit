package com.beviswang.pluginunit.activity

import android.content.Intent
import com.beviswang.pluginlib.BaseProxyActivity
import com.beviswang.pluginunit.START_PROXY_MANAGER_NAME
import com.beviswang.pluginunit.mPluginClassLoader

class ProxyActivity : BaseProxyActivity() {
    override fun loadClassLoader(): ClassLoader {
        return mPluginClassLoader
    }

    override fun loadProxyManager() {
        loadProxyManagerActivity(if (START_PROXY_MANAGER_NAME.isEmpty())
            "com.beviswang.appplugin.proxy.MainProxyManager" else START_PROXY_MANAGER_NAME)
        reset()
    }

    override fun startPluginActivity(className: String) {
        START_PROXY_MANAGER_NAME = className
        startActivity(Intent(this@ProxyActivity,
                classLoader.loadClass(javaClass.name)))
    }

    private fun reset(){
        START_PROXY_MANAGER_NAME = ""
    }
}

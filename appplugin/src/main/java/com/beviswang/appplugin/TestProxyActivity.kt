package com.beviswang.appplugin

import android.content.Intent
import com.beviswang.appplugin.proxy.MainProxyManager
import com.beviswang.pluginlib.BaseProxyActivity

class TestProxyActivity : BaseProxyActivity() {
    override fun loadClassLoader(): ClassLoader {
        pResources = resources
        return classLoader
    }

    override fun loadProxyManager() {
        loadProxyManagerActivity(if (START_PROXY_MANAGER_NAME.isEmpty())
            MainProxyManager::javaClass.name else START_PROXY_MANAGER_NAME)
        reset()
    }

    override fun startPluginActivity(className: String) {
        START_PROXY_MANAGER_NAME = className
        startActivity(Intent(this@TestProxyActivity,
                classLoader.loadClass(javaClass.name)))
    }

    private fun reset(){
        START_PROXY_MANAGER_NAME = ""
    }
}
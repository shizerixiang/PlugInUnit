package com.beviswang.pluginunit.activity

import com.beviswang.pluginlib.BaseProxyActivity
import com.beviswang.pluginunit.mPluginClassLoader

class ProxyActivity : BaseProxyActivity() {
    override fun loadClassLoader(): ClassLoader {
        return mPluginClassLoader
    }

    override fun loadProxyManager() {
        loadProxyManagerActivity(intent.getStringExtra(BaseProxyActivity.EXTRA_CLASS_NAME)
                ?: "com.beviswang.appplugin.proxy.MainProxyManager")
    }
}

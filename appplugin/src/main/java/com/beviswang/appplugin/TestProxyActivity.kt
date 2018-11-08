package com.beviswang.appplugin

import android.content.res.Resources
import com.beviswang.appplugin.proxy.MainProxyManager
import com.beviswang.pluginlib.BaseProxyActivity
import com.beviswang.pluginlib.IProxyManager

class TestProxyActivity : BaseProxyActivity() {
    override fun createResources(): Resources {
        return pResources
    }

    override fun loadProxyManager(): IProxyManager {
        val main = MainProxyManager()
        main.setProxyActivity(this)
        return main
    }
}
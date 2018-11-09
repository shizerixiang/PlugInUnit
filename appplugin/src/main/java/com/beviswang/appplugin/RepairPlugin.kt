package com.beviswang.appplugin

import android.content.Context
import android.content.res.Resources
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import com.beviswang.pluginlib.IRepairPlugin

/**
 * 插件程序入口
 * @author BevisWang
 */
class RepairPlugin : IRepairPlugin {
    override fun loadResources(res: Resources) {
        pResources = res
    }
}

var START_PROXY_MANAGER_NAME:String = ""

// 重新加载的资源
lateinit var pResources: Resources

fun getResourcesView(@LayoutRes layoutId: Int,context: Context): View {
    return LayoutInflater.from(context).inflate(pResources.getLayout(layoutId), null)
}
package com.beviswang.appplugin

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.beviswang.pluginlib.IRepairPlugin
import dalvik.system.DexClassLoader

/**
 * 插件程序入口
 * @author BevisWang
 */
class RepairPlugin : IRepairPlugin {

    override fun getPluginInstallMessage(): String {
        return "我是插件：${javaClass.name}，现在已经安装好插件！"
    }

    override fun setOnStartActivity(startActivity: (intent: Intent) -> Unit, context: Context) {
        start = startActivity
        start.invoke(Intent(context, RepairMainActivity::class.java))
    }

    override fun loadResources(context: Context, classLoader: ClassLoader, apkPath: String) {
        // TODO 在此加载所需资源(所有 R 文件相关都需要重新加载)
    }
}

var start: (intent: Intent) -> Unit = {}

inline fun <reified T : Activity> Context.startActivity(intent: Intent? = null) {
    val startIntent = intent ?: Intent(this, T::class.java)
    start.invoke(startIntent)
}
package com.beviswang.appplugin

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.beviswang.pluginlib.IRepairPlugin

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
}

var start: (intent: Intent) -> Unit = {}

inline fun <reified T : Activity> Context.startActivity(intent: Intent? = null) {
    val startIntent = intent ?: Intent(this, T::class.java)
    start.invoke(startIntent)
}
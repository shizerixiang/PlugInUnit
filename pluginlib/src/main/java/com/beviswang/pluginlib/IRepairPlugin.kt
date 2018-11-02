package com.beviswang.pluginlib

import android.content.Context
import android.content.Intent

/**
 * 插件入口接口
 * @author BevisWang
 */
interface IRepairPlugin {
    /** @return 获取插件安装信息 */
    fun getPluginInstallMessage():String

    /**
     * 在主程序中设置 Activity 启动回调
     * @param startActivity 启动回调方法
     */
    fun setOnStartActivity(startActivity:(intent:Intent)->Unit,context: Context)
}
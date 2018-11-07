package com.beviswang.pluginlib

import android.content.Context
import android.content.Intent
import android.content.res.Resources

/**
 * 插件入口接口
 * @author BevisWang
 */
interface IRepairPlugin {
    /** @return 获取插件安装信息 */
    fun getPluginInstallMessage(): String

    /**
     * 插件资源加载
     * @param res 该插件的资源文件
     */
    fun loadResources(res: Resources)

    /**
     * 在主程序中设置 Activity 启动回调
     * @param startActivity 启动回调方法
     */
    fun setOnStartActivity(startActivity: (intent: Intent) -> Unit, context: Context)
}
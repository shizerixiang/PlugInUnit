package com.beviswang.pluginlib

import android.content.Context
import android.content.Intent

/**
 * 插件入口接口
 * @author BevisWang
 */
interface IRepairPlugin {
    /** @return 获取插件安装信息 */
    fun getPluginInstallMessage(): String

    /**
     * 插件资源加载
     * @param context 主程序上下文
     * @param classLoader 主程序加载器
     * @param apkPath 当前插件存放位置
     */
    fun loadResources(context: Context, classLoader: ClassLoader, apkPath: String)

    /**
     * 在主程序中设置 Activity 启动回调
     * @param startActivity 启动回调方法
     */
    fun setOnStartActivity(startActivity: (intent: Intent) -> Unit, context: Context)
}
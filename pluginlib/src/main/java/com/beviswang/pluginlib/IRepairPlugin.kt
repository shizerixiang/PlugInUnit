package com.beviswang.pluginlib

import android.content.res.Resources

/**
 * 插件入口接口
 * @author BevisWang
 */
interface IRepairPlugin {
    /**
     * 插件资源加载
     * @param res 该插件的资源文件
     */
    fun loadResources(res: Resources)
}
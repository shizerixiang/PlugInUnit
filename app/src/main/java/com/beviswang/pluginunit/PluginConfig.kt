package com.beviswang.pluginunit

import android.content.Context
import android.content.res.Resources
import com.beviswang.pluginlib.IRepairPlugin
import com.beviswang.pluginlib.util.PluginHelper

var PLUGIN_APK_PATH: String = ""

lateinit var mPluginClassLoader: ClassLoader

lateinit var mResources: Resources

fun Context.loadResources() {
    val readerClass = mPluginClassLoader.loadClass(
            "com.beviswang.appplugin.RepairPlugin") ?: return
    val repairPlugin: IRepairPlugin = readerClass.newInstance() as IRepairPlugin
    mResources = PluginHelper.createResources(this, getFileStreamPath(
            "appplugin-debug.apk").absolutePath)
    repairPlugin.loadResources(mResources)
}
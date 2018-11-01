package com.beviswang.pluginunit.util

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources

object PluginHelper {
    /**
     * 创建 AssetManager
     * @param pluginAPKPath 插件 APK 路径
     * @return AssetManager?
     */
    private fun createAssetManager(pluginAPKPath: String): AssetManager? {
        try {
            val assetManager = AssetManager::class.java.newInstance()
            val assetManagerClazz = Class.forName("android.content.res.AssetManager")
            val addAssetPathMethod = assetManagerClazz.getDeclaredMethod("addAssetPath", String::class.java)
            addAssetPathMethod.isAccessible = true
            addAssetPathMethod.invoke(assetManager, pluginAPKPath)
            return assetManager
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 创建插件 Res 资源文件
     * @param context 上下文
     * @param pluginAPKPath 组件 APK 路径
     * @return Resources
     */
    fun createResources(context: Context, pluginAPKPath: String): Resources {
        val assetManager = createAssetManager(pluginAPKPath)
        val superRes = context.resources
        return Resources(assetManager, superRes.displayMetrics, superRes.configuration)
    }

    /**
     * 获取未安装组件包名信息
     * @param context 上下文
     * @param pluginAPKPath 组件 APK 路径
     * @return 组件包名信息
     */
    fun getUninstallAPKPackageName(context: Context, pluginAPKPath: String): String {
        val pm = context.packageManager
        val packageInfo = pm.getPackageArchiveInfo(pluginAPKPath, PackageManager.GET_ACTIVITIES)
                ?: return ""
        val appInfo = packageInfo.applicationInfo
        return appInfo.packageName
    }
}
package com.beviswang.pluginunit

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import dalvik.system.DexClassLoader
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mPluginClassLoader: ClassLoader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dexPath = getFileStreamPath("appplugin-debug.apk").absolutePath
        Log.e("1", "dexPath=$dexPath")
        val optDir = getDir("dex", Context.MODE_PRIVATE).absolutePath
        Log.e("1", "optDir=$optDir")

        // 第一个参数：是dex压缩文件的路径
        // 第二个参数：是dex解压缩后存放的目录
        // 第三个参数：是C/C++依赖的本地库文件目录,可以为null
        // 第四个参数：是上一级的类加载器
        mPluginClassLoader = DexClassLoader(dexPath, optDir, null, classLoader)

        btn1.setOnClickListener {
            img.setImageDrawable(getPluginImg(getUninstallAPKPackageName(
                    this@MainActivity, dexPath) + ".R\$drawable", "cover"))
        }
    }

    /**
     * 获取未安装组件包名信息
     * @param context 上下文
     * @param pluginAPKPath 组件 APK 路径
     * @return 组件包名信息
     */
    private fun getUninstallAPKPackageName(context: Context, pluginAPKPath: String): String {
        val pm = context.packageManager
        val packageInfo = pm.getPackageArchiveInfo(pluginAPKPath, PackageManager.GET_ACTIVITIES)
                ?: return ""
        val appInfo = packageInfo.applicationInfo
        return appInfo.packageName
    }

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
    private fun createResources(context: Context, pluginAPKPath: String): Resources {
        val assetManager = createAssetManager(pluginAPKPath)
        val superRes = context.resources
        return Resources(assetManager, superRes.displayMetrics, superRes.configuration)
    }

    /**
     * 获取组件的资源图片
     * @param resDirName 资源文件的包名 + .R$ + 资源文件夹名
     * @param resName 资源文件名，不需要后缀
     * @return Drawable?
     */
    private fun getPluginImg(resDirName: String, resName: String): Drawable? {
        try {
            val loadClazz = mPluginClassLoader?.loadClass(resDirName)
                    ?: throw Exception("Null resDirName!")
            val field = loadClazz.getDeclaredField(resName)
            field.isAccessible = true
            val resId = field.get(R.id::class.java) as Int
            val res = createResources(this@MainActivity,
                    getFileStreamPath("appplugin-debug.apk").absolutePath)
            return res.getDrawable(resId, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}

package com.beviswang.pluginunit

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.beviswang.pluginlib.IRepairPlugin
import com.beviswang.pluginlib.util.PluginHelper
import dalvik.system.DexClassLoader
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    private var mPluginClassLoader: ClassLoader? = null
    private var dexPath:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dexPath = getFileStreamPath("appplugin-debug.apk").absolutePath
        Log.e("1", "dexPath=$dexPath")
        val optDir = getDir("dex", Context.MODE_PRIVATE).absolutePath
        Log.e("1", "optDir=$optDir")

        // 第一个参数：是dex压缩文件的路径
        // 第二个参数：是dex解压缩后存放的目录
        // 第三个参数：是C/C++依赖的本地库文件目录,可以为null
        // 第四个参数：是上一级的类加载器
        mPluginClassLoader = DexClassLoader(dexPath, optDir, null, classLoader)

        btn1.setOnClickListener {
            img.setImageDrawable(getPluginImg(PluginHelper.getUninstallAPKPackageName(
                    this@MainActivity, dexPath) + ".R\$drawable", "cover"))
        }

        btn2.setOnClickListener {
//            showToast()
            startActivity<RealActivity>()
        }
    }

    private fun showToast() {
        val readerClass = mPluginClassLoader?.loadClass("com.beviswang.appplugin.RepairPlugin")
                ?: return
        val repairPlugin: IRepairPlugin = readerClass.newInstance() as IRepairPlugin
        repairPlugin.loadResources(PluginHelper.createResources(this@MainActivity,
                getFileStreamPath("appplugin-debug.apk").absolutePath))
        repairPlugin.setOnStartActivity({ intent -> startPluginActivity(intent) }, this@MainActivity)
//        Toast.makeText(this@MainActivity, repairPlugin.getPluginInstallMessage(), Toast.LENGTH_LONG).show()
    }

    private fun startPluginActivity(intent: Intent) {
        mPluginClassLoader?.loadClass(intent.component.className) ?: return
//        toast(intent.component.className)
        startActivity(intent)
    }

    /**
     * 获取组件的资源图片
     * @param resDirName 资源文件的包名 + .R$ + 资源文件夹名
     * @param resName 资源文件名，不需要文件名后缀
     * @return Drawable?
     */
    private fun getPluginImg(resDirName: String, resName: String): Drawable? {
        try {
            val loadClazz = mPluginClassLoader?.loadClass(resDirName)
                    ?: throw Exception("Null resDirName!")
            val field = loadClazz.getDeclaredField(resName)
            field.isAccessible = true
            val resId = field.get(R.id::class.java) as Int
            val res = PluginHelper.createResources(this@MainActivity,
                    getFileStreamPath("appplugin-debug.apk").absolutePath)
            return res.getDrawable(resId, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}

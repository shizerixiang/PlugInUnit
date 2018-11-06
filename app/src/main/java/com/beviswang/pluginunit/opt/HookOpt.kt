package com.beviswang.pluginunit.opt

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Message
import android.util.Log
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * Hook 操作类
 * @author BevisWang
 */
class HookOpt(private val proxyActivity: Class<*>, private val context: Context) {

    /** Activity 绕过清单文件检测 */
    @SuppressLint("PrivateApi")
    fun hookAms() {
        val activityManagerClassName: String
        val defaultFiledName: String
        // 针对不同版本的 API 进行兼容
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            activityManagerClassName = "android.app.ActivityManagerNative"
            defaultFiledName = "gDefault"
        } else {
            activityManagerClassName = "android.app.ActivityManager"
            defaultFiledName = "IActivityManagerSingleton"
        }
        try {
            // 一路反射，直到拿到 IActivityManager 对象
            val activityManagerNativeClazz = Class.forName(activityManagerClassName)
            val defaultFiled = activityManagerNativeClazz.getDeclaredField(defaultFiledName)
            defaultFiled.isAccessible = true
            val defaultValue = defaultFiled.get(null)
            // 反射 Singleton
            val singletonClazz = Class.forName("android.util.Singleton")
            val mInstance = singletonClazz.getDeclaredField("mInstance")
            mInstance.isAccessible = true
            // 拿到 ActivityManager 对象
            val iActivityManagerObj = mInstance.get(defaultValue)
            // 开始动态代理，用代理对象替换掉真实的 ActivityManager，瞒天过海
            val iActivityManagerIntercept = Class.forName("android.app.IActivityManager")
            val handler = AmsInvocationHandler(iActivityManagerObj)
            val proxy = Proxy.newProxyInstance(Thread.currentThread().contextClassLoader,
                    Array<Class<*>>(1, init = { iActivityManagerIntercept }), handler)
            // 现在替换掉整个对象
            mInstance.set(defaultValue, proxy)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // 兼容 AppCompatActivity
        hookCompatAms()
    }

    /** 兼容 AppCompatActivity 的清单文件检测 */
    @SuppressLint("PrivateApi")
    private fun hookCompatAms() {
        try {
            val activityThread = Class.forName("android.app.ActivityThread")
            val field = activityThread.getDeclaredField("sCurrentActivityThread")
            field.isAccessible = true
            val activityThreadObj = field.get(null)
            val getPackageManager = activityThreadObj.javaClass
                    .getDeclaredMethod("getPackageManager")
            val iPackageManager = getPackageManager.invoke(activityThreadObj)
            val handler = AmsCompatInvocationHandler(iPackageManager)

            val iPackageManagerIntercept = Class.forName("android.content.pm.IPackageManager")
            val proxy = Proxy.newProxyInstance(Thread.currentThread().contextClassLoader,
                    Array<Class<*>>(1, init = { iPackageManagerIntercept }), handler)
            // 获取 sPackageManager 属性
            val iPackageManagerField = activityThreadObj.javaClass.getDeclaredField("sPackageManager")
            iPackageManagerField.isAccessible = true
            iPackageManagerField.set(activityThreadObj, proxy)
        } catch (e: Exception) {
            Log.e("HookCompatAms", "HookCompatAms failed!!!")
            e.printStackTrace()
        }
    }

    /** Hook 系统 Handler，实现瞒天过海 */
    @SuppressLint("PrivateApi")
    fun hookSystemHandler() {
        val activityThreadClazz = Class.forName("android.app.ActivityThread")
        val currentActivityThreadMethod = activityThreadClazz.getDeclaredMethod("currentActivityThread")
        currentActivityThreadMethod.isAccessible = true
        // 获取主线程对象
        val activityThread = currentActivityThreadMethod.invoke(null)
        val mH = activityThreadClazz.getDeclaredField("mH")
        mH.isAccessible = true
        val handler = mH.get(activityThread) as Handler
        val mCallback = Handler::class.java.getDeclaredField("mCallback")
        mCallback.isAccessible = true
        mCallback.set(handler, ActivityThreadHandlerCallback(handler))
    }

    inner class AmsInvocationHandler(private val iActivityManagerObj: Any?) : InvocationHandler {
        override fun invoke(proxy: Any?, method: Method?, args: Array<Any>?): Any? {
            if (method == null) throw Exception("Null method exception!")
            if ("startActivity".contains(method.name) && args != null) {
                var intent: Intent? = null
                var indexArg = 0
                args.forEachIndexed { index, any ->
                    if (any is Intent) {
                        intent = any
                        indexArg = index
                    }
                }
                // 伪造 Intent
                val proxyIntent = Intent()
                proxyIntent.component = ComponentName(context, proxyActivity)
                // 在附加参数中混入真实 intent
                proxyIntent.putExtra("realIntent", intent)
                args[indexArg] = proxyIntent
            }
            return if (args != null) method.invoke(iActivityManagerObj, *args)
            else method.invoke(iActivityManagerObj)
        }
    }

    inner class AmsCompatInvocationHandler(private val iPackageManagerObj: Any?) : InvocationHandler {
        override fun invoke(proxy: Any?, method: Method?, args: Array<Any?>?): Any? {
            if (method == null) throw Exception("Null method exception!")
            if (args == null) return method.invoke(iPackageManagerObj)
            if ("resolveIntent".contains(method.name)) {
                var intent: Intent? = null
                var indexArg = 0
                args.forEachIndexed { index, any ->
                    if (any is Intent) {
                        intent = any
                        indexArg = index
                    }
                }
                // 伪造 Intent
                val proxyIntent = Intent()
                proxyIntent.component = ComponentName(context, proxyActivity)
                // 在附加参数中混入真实 intent
                proxyIntent.putExtra("realIntent", intent)
                args[indexArg] = proxyIntent
            }
            // AppCompatActivity 需要替换该方法传递的参数为代理 Activity (返回一个虚假信息)
            if ("getActivityInfo".contains(method.name)) {
                val componentName = ComponentName(context, proxyActivity)
                args[0] = componentName
            }
            return method.invoke(iPackageManagerObj, *args)
        }
    }

    /** 重写 Handler Callback，用于将通过清单验证的 Intent 偷换成真实 Intent */
    class ActivityThreadHandlerCallback(private val handler: Handler) : Handler.Callback {
        override fun handleMessage(msg: Message?): Boolean {
            if (msg == null) return false
            // 替换之前的 Intent
            if (msg.what == LAUNCH_ACTIVITY) {
                handlerLaunchActivity(msg)
            }
            handler.handleMessage(msg)
            return true
        }

        private fun handlerLaunchActivity(msg: Message) {
            val obj = msg.obj
            try {
                val intentFailed = obj.javaClass.getDeclaredField("intent")
                intentFailed.isAccessible = true
                val proxyIntent = intentFailed.get(obj) as Intent
                val realIntent = proxyIntent.getParcelableExtra<Intent>("realIntent")
                if (realIntent != null) proxyIntent.component = realIntent.component
            } catch (e: Exception) {
                Log.e("HandlerCallback", "Launch activity failed!!!")
            }
        }
    }

    companion object {
        private const val LAUNCH_ACTIVITY = 100
    }
}
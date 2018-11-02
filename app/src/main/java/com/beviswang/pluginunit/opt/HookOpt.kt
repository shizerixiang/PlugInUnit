package com.beviswang.pluginunit.opt

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * Hook 操作类
 * @author BevisWang
 */
class HookOpt(private val proxyActivity: Class<*>, private val context: Context) {

    @SuppressLint("PrivateApi")
    fun hookAms() {
        // 一路反射，直到拿到 IActivityManager 对象
        try {
            val activityManagerNativeClazz = Class.forName("android.app.ActivityManagerNative")
            val defaultFiled = activityManagerNativeClazz.getDeclaredField("gDefault")
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
    }

    private class AmsInvocationHandler(private val iActivityManagerObj: Any?) : InvocationHandler {
        override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
            if (method == null) throw Exception("Null method exception!")
            Log.i("HookOpt", method.name)
            if ("startActivity".contains(method.name)) {
                Log.e("HookOpt", "Activity 已经开始启动！")
                Log.e("HookOpt", "Hook 一下！！！")
            }
            return if (args != null) method.invoke(iActivityManagerObj, *args)
            else method.invoke(iActivityManagerObj)
        }
    }
}
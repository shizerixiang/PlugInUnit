package com.beviswang.appplugin

import com.beviswang.pluginlib.IReader

class Reader:IReader {
    override fun getToastText(): String {
        return "我是插件：${javaClass.canonicalName} 返回来的消息！"
    }
}
package com.beviswang.pluginunit.util


import android.content.Context

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * so库从sd卡拷贝到app的私有目录下，并进行比对验证和加载
 */

object LoadSoFileUtils {
    private val nameSO = "libSaveLib"

    /**
     * 加载 so 文件
     */
    fun loadSoFile(context: Context, fromPath: String): Int {
        val dir = context.getDir("jniLibs", Context.MODE_PRIVATE)
        val fromPathFile = "$fromPath/$nameSO.so"
        val isExist = File(fromPathFile)
        return if (!isLoadSoFile(dir, isExist.exists())) {
            copy(fromPath, dir.absolutePath)
        } else {
            0
        }
    }

    /**
     * 判断 so 文件是否存在
     */
    fun isLoadSoFile(dir: File, isExist: Boolean?): Boolean {
        val currentFiles: Array<File>? = dir.listFiles()
        var hasSoLib = false
        if (currentFiles == null) {
            return false
        }
        for (currentFile in currentFiles) {
            //判断里面是否存在这个库,以及sd也有这个库，那就删除，然后进行外面拷贝进去
            if (currentFile.name.contains(nameSO)) {
                hasSoLib = isExist!! && !currentFile.delete()
            }
        }
        return hasSoLib
    }

    fun copy(fromFile: String, toFile: String): Int {
        //要复制的文件目录
        val currentFiles: Array<File>?
        val root = File(fromFile)
        //如同判断SD卡是否存在或者文件是否存在,如果不存在则 return出去
        if (!root.exists()) {
            return -1
        }
        //如果存在则获取当前目录下的全部文件 填充数组
        currentFiles = root.listFiles()

        if (currentFiles == null) {
            return -1
        }
        //目标目录
        val targetDir = File(toFile)
        //创建目录
        if (!targetDir.exists()) {
            targetDir.mkdirs()
        }

        var statue = -1
        //遍历要复制该目录下的全部文件
        for (currentFile in currentFiles) {
            if (currentFile.isDirectory) {
                //如果当前项为子目录 进行递归
                copy(currentFile.path + "/", toFile + currentFile.name + "/")
            } else {
                //如果当前项为文件则进行文件拷贝
                if (currentFile.name.contains(".so")) {
                    statue = copySdcardFile(currentFile.path, toFile + File.separator + currentFile.name)
                }
            }
        }
        return statue
    }


    //文件拷贝
    //要复制的目录下的所有非子目录(文件夹)文件拷贝
    fun copySdcardFile(fromFile: String, toFile: String): Int {
        try {
            val fosfrom = FileInputStream(fromFile)
            val fosto = FileOutputStream(toFile)
            val baos = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var len = fosfrom.read(buffer)
            // 从内存到写入到具体文件
            while (len != -1) {
                len = fosfrom.read(buffer)
                baos.write(buffer, 0, len)
            }
            fosto.write(baos.toByteArray())
            // 关闭文件流
            baos.close()
            fosto.close()
            fosfrom.close()
            return 0
        } catch (ex: Exception) {
            return -1
        }

    }
}
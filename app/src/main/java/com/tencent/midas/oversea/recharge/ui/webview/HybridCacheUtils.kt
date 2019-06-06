package com.tencent.midas.oversea.recharge.ui.webview

import android.net.Uri
import android.os.Environment
import android.text.TextUtils

import com.google.gson.JsonObject
import com.google.gson.JsonParser

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * WebView缓存
 */
object HybridCacheUtils {

    //缓存主目录
    var DIR = ".app"
    val DIR_H5 = "h5"

    /**
     * 获取SD卡下的缓存目录
     * @return
     */
    // 获取SD卡根目录
    // 根目录下的缓存目录
    // 若主缓存目录不存在，创建
    val cacheDir: String?
        get() {
            val externalStorageDirectory = Environment.getExternalStorageDirectory()
            if (null == externalStorageDirectory || !externalStorageDirectory.exists()) {
                return null
            }

            val cacheDir = File(externalStorageDirectory, DIR)
            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }
            return cacheDir.absolutePath
        }

    @JvmOverloads
    fun convertUriToFilePath(uri: Uri?, needQueryParams: Boolean = true): String? {
        if (null == uri || TextUtils.isEmpty(uri.toString())) {
            return null
        }

        // 获取SD卡主缓存目录
        var cacheDir = cacheDir
        if (TextUtils.isEmpty(cacheDir)) {
            return null
        }

        val file = File(cacheDir, DIR_H5)
        if (!file.exists()) {
            file.mkdirs()
        }
        cacheDir = file.absolutePath

        // 取出host + path信息
        val filePathSB = StringBuilder(cacheDir)
        filePathSB.append(File.separator)
        val host = uri.host
        if (!TextUtils.isEmpty(host)) {
            filePathSB.append(host)
        }

        val path = uri.path
        if (!TextUtils.isEmpty(path)) {
            filePathSB.append(path)
        }

        if (needQueryParams) {
            // Query信息
            val query = uri.query
            if (!TextUtils.isEmpty(query)) {
                filePathSB.append(query)
            }
        }
        // host + path不为空
        return if (filePathSB.length > 0) {
            filePathSB.toString()
        } else null
    }

    /**
     * 网络下载，并缓存
     * @param uri
     * @param cacheFilePath
     * @param onDownloadListener
     */
    fun saveResource(uri: Uri, cacheFilePath: String?, onDownloadListener: OnDownloadListener?) {
        DownloadFileUtils.download(uri, cacheFilePath, onDownloadListener)
    }

    /**
     * 根据路径后缀判断文本类型
     * @param path
     * @return
     */
    fun getResourceType(path: String): String {
        if (path.endsWith(".css")) {
            return "text/css"
        } else if (path.endsWith(".js")) {
            return "application/x-javascript"
        } else if (path.endsWith(".png")) {
            return "image/png"
        } else if (path.endsWith(".jpg")) {
            return "image/jpeg"
        } else if (path.endsWith(".jpeg")) {
            return "image/jpeg"
        }
        return "TEXT/HTML"//默认
    }

    /**
     * 返回HTML页面的MimeType
     * 有些HttpURLConnection获取的contentType为“text/html;charset=UTF-8”时，页面显示为代码。设置为text/html时能显示网页
     * @param contentType
     * @return
     */
    fun getHtmlMimeType(contentType: String): String {
        var mimeType = "text/html"
        if (!TextUtils.isEmpty(contentType)) {
            if (contentType.contains(";")) {//如果contentType为“text/html;charset=UTF-8”这种形式
                val args = contentType.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                mimeType = args[0]
            } else {//contentType为“text/html”这种形式
                mimeType = contentType
            }
        }
        return mimeType
    }

    /**
     * 返回HTML页面的encoding
     * @param contentType
     * @return
     */
    fun getHtmlEncoding(contentType: String): String {
        var encoding = "UTF-8"
        if (!TextUtils.isEmpty(contentType)) {
            if (contentType.contains(";")) {//如果contentType为“text/html;charset=UTF-8”这种形式
                val args = contentType.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (args.size > 1 && args[1].trim { it <= ' ' }.contains("charset=")) {
                    encoding = args[1].substring(args[1].indexOf("=") + 1)//截取charset=后的encoding
                }
            }
        }
        return encoding
    }

    /**
     * 将InputStream中的字节保存到ByteArrayOutputStream中
     * @param inputStream
     * @return
     */
    fun inputStreamCache(inputStream: InputStream?): ByteArrayOutputStream? {
        var byteArrayOutputStream: ByteArrayOutputStream? = null
        if (inputStream == null) {
            return null
        }

        try {
            byteArrayOutputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var len = 0

            len = inputStream.read(buffer)
            while (len != -1){
                byteArrayOutputStream.write(buffer, 0, len)
                len = inputStream.read(buffer)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try{
                inputStream.close()
            }catch (e:IOException){}

            try{
                byteArrayOutputStream?.close()
            }catch (e:IOException){}
        }

        return byteArrayOutputStream
    }

    /**
     * 将缓存ByteArrayOutputStream中的内容转为InputStream
     * @param byteArrayOutputStream
     * @return
     */
    fun getInputStream(byteArrayOutputStream: ByteArrayOutputStream?): InputStream? {
        return if (byteArrayOutputStream == null) null
                    else ByteArrayInputStream(byteArrayOutputStream.toByteArray())
    }

    /**
     * 将缓存ByteArrayOutputStream中的内容转为JsonObject
     * @param byteArrayOutputStream
     * @return
     */
    fun getJsonObjectFromOutputStream(byteArrayOutputStream: ByteArrayOutputStream?): JsonObject? {
        if (byteArrayOutputStream == null) {
            return null
        }

        val content = byteArrayOutputStream.toString()
        var returnData: JsonObject? = null
        if (content.startsWith("{") && content.endsWith("}")) {
            returnData = JsonParser().parse(content).asJsonObject
        }
        return returnData
    }

    /**
     * 判断资源是否需要保存
     * @param uri
     * @return
     */
    fun needCache(uri: Uri?): Boolean {
        if (null == uri || TextUtils.isEmpty(uri.path)) {
            return false
        }

        val path = uri.path

        // 这些资源类型需要缓存
        if (path!!.endsWith(".css") || path.endsWith(".js") || path.endsWith(".png")
                || path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".gif")) {

            // "hm.gif","0.gif","v.gif" 不保存
            if (path.endsWith("0.gif") || path.endsWith("v.gif") || path.endsWith("hm.gif")) {
                return false
            }

            return TextUtils.isEmpty(uri.query)
        }
        return false
    }

    /**
     * 判断路径是否存在
     * @param path
     * @return
     */
    fun checkPathExist(path: String): Boolean {
        if (TextUtils.isEmpty(path)) {
            return false
        }

        val file = File(path)
        if (file.exists()) {
            // 如果文件存在，且缓存的文件大小为0，说明之前下载失败，删除文件
            if (file.length() <= 0) {
                file.delete()
                return false
            }
            // 文件存在，且大小大于0
            return true
        }
        return false
    }

    /**
     * 获取缓存文件，返回字符串文本
     * @param path
     * @return
     */
    fun getCacheFile(path: String): String? {
        if (TextUtils.isEmpty(path)) {
            return null
        }
        val file = File(path)
        if (file.exists()) {
            // 如果文件存在，且缓存的文件大小为0，说明之前下载失败，删除文件
            if (file.length() <= 0) {
                file.delete()
                return null
            }
            // 读文件
            var fis: FileInputStream? = null
            try {
                fis = FileInputStream(file)
                val buff = ByteArray(fis.available())
                fis.read(buff)
                return String(buff, Charset.forName("UTF-8"))
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    fis?.close()
                } catch (e: Exception) {}
            }
        }
        return null
    }

    /**
     * 判断SDCard是否可用
     */
    fun mountedSDCard(): Boolean {
        return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
    }

    /**
     * 根据系统时间、前缀、后缀产生一个文件
     */
    fun createFile(folder: File, prefix: String, suffix: String): File {
        if (!folder.exists() || !folder.isDirectory) folder.mkdirs()
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)
        val filename = prefix + dateFormat.format(Date(System.currentTimeMillis())) + suffix
        return File(folder, filename)
    }
}

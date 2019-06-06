package com.tencent.midas.oversea.recharge.ui.webview

import android.net.Uri
import android.text.TextUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

object DownloadFileUtils {

    private val sOkHttpClient = OkHttpClient()

    fun download(uri: Uri?, saveDir: String?, listener: OnDownloadListener?) {
        if (null == uri || TextUtils.isEmpty(uri.toString()) || TextUtils.isEmpty(saveDir)) {
            // 下载失败
            listener?.onFailed()
            return
        }

        val scheme = uri.scheme
        if (!TextUtils.equals(scheme, "http") && !TextUtils.equals(scheme, "https")) {
            // 下载失败
            listener?.onFailed()
            return
        }

        val request = Request.Builder().url(uri.toString()).build()
        sOkHttpClient.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                // 下载失败
                listener?.onFailed()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val file = File(saveDir)
                var fos: FileOutputStream? = null
                var inputStream: InputStream? = null

                try {
                    inputStream = response.body()!!.byteStream()
                    val parentFile = file.parentFile
                    if (null != parentFile && !parentFile.exists()) {
                        parentFile.mkdirs()
                    }
                    file.createNewFile()
                    fos = FileOutputStream(file)

                    val buf = ByteArray(1024)
                    var len = inputStream.read(buf)
                    while (len != -1){
                        fos.write(buf, 0, len)
                        len = inputStream.read(buf)
                    }
                    fos.flush()

                    // 下载成功
                    listener?.onSuccess()
                } catch (e: Exception) {
                    // 下载失败
                    listener?.onFailed()
                    file?.delete()
                } finally {
                    try {
                        inputStream?.close()
                    } catch (e: IOException) {
                    }

                    try {
                        fos?.close()
                    } catch (e: IOException) {
                    }
                }
            }
        })
    }
}

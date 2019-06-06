package com.tencent.midas.oversea.recharge.comm

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.backends.pipeline.PipelineDraweeController
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder

object ImageTool{

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            inSampleSize = 2
            while (height / inSampleSize > reqHeight && width / inSampleSize > reqWidth) {
                inSampleSize *= 2
            }
        }


        return inSampleSize
    }

    fun decodeBitmapFromResource(resources: Resources, resId: Int, reqWidth: Int, reqHeight: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, resId, options)

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(resources, resId, options)
    }


    fun frescoCompress(uri: Uri, view: SimpleDraweeView, width: Int, height: Int) {
        if(width <= 0 || height <= 0){
            return
        }

        val request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(ResizeOptions(width, height))
                .setProgressiveRenderingEnabled(true)//支持图片渐进式加载
                .setAutoRotateEnabled(true) //如果图片是侧着,可以自动旋转
                .build()

        val controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(view.controller)
                .setAutoPlayAnimations(true) //gif自动播放
                .build() as PipelineDraweeController

        view.controller = controller
    }
}
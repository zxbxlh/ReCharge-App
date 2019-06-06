package com.tencent.midas.oversea.recharge.ui.view

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tencent.midas.oversea.recharge.comm.UIUtils

class MDialog constructor(context: Context, themeResId: Int) : Dialog(context, themeResId) {

    //set layout id
    fun setLayout(layoutId: Int){
        val view = LayoutInflater.from(context).inflate(layoutId, null)
        setContentView(view)
        val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.width = context.resources.displayMetrics.widthPixels - UIUtils.dp2px(context, 20f)
        layoutParams.bottomMargin = UIUtils.dp2px(context, 10f)
        view.layoutParams = layoutParams
    }

    //set window animation
    fun setWindowAnimation(animationId: Int){
        window!!.setWindowAnimations(animationId)
    }

    fun setGravity(gravity: Int){
        window!!.setGravity(gravity)
    }
}

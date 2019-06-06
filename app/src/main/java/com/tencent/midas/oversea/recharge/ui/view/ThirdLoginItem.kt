package com.tencent.midas.oversea.recharge.ui.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.tencent.midas.oversea.recharge.R
import com.tencent.midas.oversea.recharge.comm.UIUtils

class ThirdLoginItem @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : LinearLayout(context, attrs, defStyle) {

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER
        val a = context.obtainStyledAttributes(attrs, R.styleable.ThirdLoginItem)
        val drawable = a.getDrawable(R.styleable.ThirdLoginItem_icon)
        val text = a.getString(R.styleable.ThirdLoginItem_text)
        val iconSize = a.getDimensionPixelSize(R.styleable.ThirdLoginItem_icon_size, UIUtils.dp2px(context, 48f))
        val textSize = a.getDimensionPixelSize(R.styleable.ThirdLoginItem_text_size, UIUtils.dp2px(context, 8f))
        a.recycle()

        val icon = ImageView(context)
        icon.scaleType = ImageView.ScaleType.CENTER_CROP
        icon.setImageDrawable(drawable)
        val iconLayoutParams = LinearLayout.LayoutParams(iconSize, iconSize)
        addView(icon, iconLayoutParams)

        val textView = TextView(context)
        textView.gravity = Gravity.CENTER
        textView.setPadding(0, UIUtils.dp2px(context, 2f), 0, UIUtils.dp2px(context, 2f))
        textView.setTextColor(ContextCompat.getColor(context, R.color.third_login_color))
        textView.text = text
        val textLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        addView(textView, textLayoutParams)
    }
}

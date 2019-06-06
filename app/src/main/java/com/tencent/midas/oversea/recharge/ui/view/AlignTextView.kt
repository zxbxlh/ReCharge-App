package com.tencent.midas.oversea.recharge.ui.view

import android.content.Context
import android.graphics.Canvas
import android.support.v7.widget.AppCompatTextView
import android.text.StaticLayout
import android.util.AttributeSet

import com.tencent.midas.oversea.recharge.R

class AlignTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var alignOnlyOneLine: Boolean = false

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AlignTextView)
        alignOnlyOneLine = typedArray.getBoolean(R.styleable.AlignTextView_alignOnlyOneLine, false)
        typedArray.recycle()
        setTextColor(currentTextColor)
    }

    override fun setTextColor(color: Int) {
        super.setTextColor(color)
        paint.color = color
    }

    override fun onDraw(canvas: Canvas) {
        val content = text
        if (content !is String) {
            super.onDraw(canvas)
            return
        }
        val layout = layout

        for (i in 0 until layout.lineCount) {
            val lineBaseline = layout.getLineBaseline(i) + paddingTop
            val lineStart = layout.getLineStart(i)
            val lineEnd = layout.getLineEnd(i)
            if (alignOnlyOneLine && layout.lineCount == 1) {//只有一行
                val line = content.substring(lineStart, lineEnd)
                val width = StaticLayout.getDesiredWidth(content, lineStart, lineEnd, paint)
                this.drawScaledText(canvas, line, lineBaseline.toFloat(), width)
            } else if (i == layout.lineCount - 1) {//最后一行
                canvas.drawText(content.substring(lineStart), paddingLeft.toFloat(), lineBaseline.toFloat(), paint)
                break
            } else {//中间行
                val line = content.substring(lineStart, lineEnd)
                val width = StaticLayout.getDesiredWidth(content, lineStart, lineEnd, paint)
                this.drawScaledText(canvas, line, lineBaseline.toFloat(), width)
            }
        }

    }

    private fun drawScaledText(canvas: Canvas, line: String, baseLineY: Float, lineWidth: Float) {
        if (line.length < 1) {
            return
        }
        var x = paddingLeft.toFloat()
        val forceNextLine = line[line.length - 1].toInt() == 10
        val length = line.length - 1
        if (forceNextLine || length == 0) {
            canvas.drawText(line, x, baseLineY, paint)
            return
        }

        val d = (measuredWidth.toFloat() - lineWidth - paddingLeft.toFloat() - paddingRight.toFloat()) / length

        for (i in 0 until line.length) {
            val c = line[i].toString()
            val cw = StaticLayout.getDesiredWidth(c, this.paint)
            canvas.drawText(c, x, baseLineY, this.paint)
            x += cw + d
        }
    }
}
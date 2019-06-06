package com.tencent.midas.oversea.recharge.ui.webview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar

/**
 * 单例/静态
 */
object AnimationUtils {

    //progressBar消失动画
    fun startDismissAnimation(mProgressBar: ProgressBar, progress: Int) {
        val anim = ObjectAnimator.ofFloat(mProgressBar, "alpha", 1.0f, 0.0f)
        anim.duration = 1000
        anim.interpolator = DecelerateInterpolator()// 减速

        anim.addUpdateListener { valueAnimator ->
            val fraction = valueAnimator.animatedFraction      // 0.0f ~ 1.0f
            val offset = 100 - progress
            mProgressBar.progress = (progress + offset * fraction).toInt()
        }

        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                // 动画结束
                mProgressBar.progress = 0
                mProgressBar.visibility = View.GONE
            }
        })
        anim.start()
    }

    //progressBar递增动画
    fun startProgressAnimation(mProgressBar: ProgressBar, currentProgress: Int, newProgress: Int) {
        val animator = ObjectAnimator.ofInt(mProgressBar, "progress", currentProgress, newProgress)
        animator.duration = 300
        animator.interpolator = DecelerateInterpolator()
        animator.start()
    }
}

package com.tencent.midas.oversea.recharge.ui.activity

import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.Toolbar
import android.transition.TransitionInflater
import android.view.Window
import com.tencent.midas.oversea.recharge.R
import me.yokeyword.fragmentation.SupportActivity

open class MBaseActivity : SupportActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //使用动画,在setContentViw之前
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        initActivityTransition()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar?.setNavigationOnClickListener { ActivityCompat.finishAfterTransition(this@MBaseActivity) }
    }

    private fun initActivityTransition() {
        val explode = TransitionInflater.from(this).inflateTransition(R.transition.explode)

        //退出时使用
        window.exitTransition = explode
        //进入时使用
        window.enterTransition = explode
        //再次进入时使用
        window.reenterTransition = explode
    }

    //finish self
    fun finishSelf(){
        ActivityCompat.finishAfterTransition(this)
    }
}

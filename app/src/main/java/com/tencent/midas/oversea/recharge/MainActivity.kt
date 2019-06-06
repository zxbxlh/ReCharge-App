package com.tencent.midas.oversea.recharge

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import com.tencent.midas.oversea.recharge.comm.MLog
import com.tencent.midas.oversea.recharge.ui.activity.MBaseActivity
import com.tencent.midas.oversea.recharge.ui.fragment.MainFragment

class MainActivity : MBaseActivity() {

    companion object{
        const val TAG = "MainActivity"

        fun start(activity: Activity){
            val intent = Intent(activity,MainActivity::class.java)
            activity.startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_container)
        initData()

        if (findFragment(MainFragment::class.java) == null) {
            loadRootFragment(R.id.main_container, MainFragment.newInstance())
        }
    }

    private fun initData() {
        //x5内核，避免网页中的视频出现闪烁
        window.setFormat(PixelFormat.TRANSLUCENT)
    }

    override fun onBackPressedSupport() {
        ActivityCompat.finishAfterTransition(this)

        //back to launcher
        val intent = Intent(Intent.ACTION_MAIN)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //get top fragment,pass the result
        val top = topFragment
        if (top != null && top is Fragment) {
            top.onActivityResult(requestCode, resultCode, data)
        }
    }
}

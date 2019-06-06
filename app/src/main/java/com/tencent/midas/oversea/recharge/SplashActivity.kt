package com.tencent.midas.oversea.recharge

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import com.tencent.midas.oversea.recharge.data.DataHandler
import com.tencent.midas.oversea.recharge.login.CommLoginManager
import com.tencent.midas.oversea.recharge.permission.PermissionItem
import com.tencent.midas.oversea.recharge.ui.view.MDialog
import java.util.ArrayList

class SplashActivity : AppCompatActivity() {
    private var mLogo: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //切回原有theme
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_splash)

        //展示动画
        mLogo = findViewById<View>(R.id.as_logo) as ImageView
        val set = AnimatorSet()
        set.playTogether(
                ObjectAnimator.ofFloat(mLogo, "scaleX", 0f, 1f)
                        .setDuration(500),
                ObjectAnimator.ofFloat(mLogo, "scaleY", 0f, 1f)
                        .setDuration(500)
        )
        set.start()


        /**
         * 知道该Activity接受fire base消息
         * fire base控制台发布的消息，如果从系统栏启动，消息存在extras中
         * Data message类型存在键值对
         */
        intent?.let {
            DataHandler.handleData(intent.extras)
        }

        //申请允许时权限，api>=23，才申请权
        if (Build.VERSION.SDK_INT >= 23) {
            val permissions = arrayOf(
                    "android.permission.WRITE_EXTERNAL_STORAGE",
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.READ_PHONE_STATE")
            requestPermissions(permissions, RC_REQUEST_PERMISSION)
        }
    }

    @TargetApi(23)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != RC_REQUEST_PERMISSION) {
            return
        }

        //处理申请结果
        val shouldShowRequestPermissionRationale = BooleanArray(permissions.size)
        for (i in permissions.indices) {
            shouldShowRequestPermissionRationale[i] = shouldShowRequestPermissionRationale(permissions[i])
        }

        this.onRequestPermissionsResult(permissions, grantResults, shouldShowRequestPermissionRationale)
    }

    @TargetApi(23)
    internal fun onRequestPermissionsResult(permissions: Array<String>, grantResults: IntArray, shouldShowRequestPermissionRationale: BooleanArray) {
        var granted = 0
        val permissionItems = ArrayList<PermissionItem>(permissions.size)

        for (i in permissions.indices) {

            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                val item = PermissionItem()
                item.permission = permissions[i]

                if (shouldShowRequestPermissionRationale[i]) {
                    //用户拒绝了权限
                    item.status = PermissionItem.RATIONAL
                } else {
                    //用户拒绝了权限，并选择了不再提示
                    item.status = PermissionItem.REJECT
                }

                permissionItems.add(item)
            } else {
                granted++
            }
        }

        if (granted == permissions.size) {
            permissionGranted()
        } else {
            permissionReject()
        }
    }

    //授权允许
    private fun permissionGranted() {
        launchActivity()
    }

    //授权拒绝
    private fun permissionReject() {
        val dialog = MDialog(this,R.style.BottomDialog)
        with(dialog){
            setCanceledOnTouchOutside(false)
            setLayout(R.layout.layout_dialog_permission)
            setWindowAnimation(R.style.BottomDialog_Animation)

            findViewById<View>(R.id.dp_sure)?.setOnClickListener {
                dialog.dismiss()
                this@SplashActivity.finish()
            }

            show()
        }
    }

    //启动Activity
    private fun launchActivity(){
        var intent:Intent

        //已登录，启动主activity;未登录，启动登录页
        if(CommLoginManager.isLogin){
            intent = Intent(this@SplashActivity, MainActivity::class.java)
        }else{
            intent = Intent(this@SplashActivity, CommLoginActivity::class.java)
        }

        startActivity(intent)
        overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out)
        this@SplashActivity.finish()
    }

    companion object {
        const val TAG = "SplashActivity"
        private const val RC_REQUEST_PERMISSION = 100
    }
}

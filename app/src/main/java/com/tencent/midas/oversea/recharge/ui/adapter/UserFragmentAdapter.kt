package com.tencent.midas.oversea.recharge.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.tencent.midas.oversea.recharge.CommLoginActivity
import com.tencent.midas.oversea.recharge.R
import com.tencent.midas.oversea.recharge.comm.MLog
import com.tencent.midas.oversea.recharge.data.UFCustomItem
import com.tencent.midas.oversea.recharge.login.CommLoginManager
import com.tencent.midas.oversea.recharge.ui.activity.MBaseActivity
import com.tencent.midas.oversea.recharge.ui.activity.TransactionActivity
import com.tencent.midas.oversea.recharge.ui.view.MDialog
import java.util.ArrayList

class UserFragmentAdapter(private val mActivity: MBaseActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {
    private val mCustomList = ArrayList<UFCustomItem>()

    init {
        mCustomList.add(UFCustomItem(R.mipmap.icons8_transaction_list, TRANSACTION))
        mCustomList.add(UFCustomItem(R.mipmap.icons8_settings, SETTINGS))
        mCustomList.add(UFCustomItem(R.mipmap.icons8_help, HELP))
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_LOGIN) {
            val layout = LayoutInflater.from(mActivity).inflate(R.layout.layout_user_info, viewGroup, false)
            return LoginHolder(layout)
        }

        //return custom
        val layout = LayoutInflater.from(mActivity).inflate(R.layout.layout_user_custom, viewGroup, false)
        return CustomHolder(layout)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        MLog.d(TAG, "position = $position")
        if (viewHolder is LoginHolder) {
            CommLoginManager.user?.let {
                with(viewHolder){
                    if(CommLoginManager.isLogin) {
                        userId.text = CommLoginManager.user!!.id
                        userName.text = CommLoginManager.user!!.name
                        userImage.setImageURI(CommLoginManager.user!!.logoUri)
                    }else{
                        userName.text = mActivity.getString(R.string.login_tips)
                        userId.visibility = View.INVISIBLE
                    }
                }
            }

            //ui_logo
            viewHolder.userImage.setOnClickListener(this)
        } else if(viewHolder is CustomHolder){
            with(viewHolder){
                text.text = mCustomList[position - 1].text
                layout.tag = mCustomList[position - 1].text
                icon.setImageResource(mCustomList[position - 1].iconResId)
            }

            //ResId：uc_layout
            viewHolder.layout.setOnClickListener(this)
        }
    }

    override fun getItemCount() = mCustomList.size + 1

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            TYPE_LOGIN
        } else {
            TYPE_CUSTOM
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            //user logo
            R.id.ui_logo->{
                //has login
                if(CommLoginManager.isLogin){
                    logOut()
                }else{
                    login()
                }
            }
            //custom item
            R.id.uc_layout->
                handleCustomClick(view.tag as String)
        }
    }

    //Activity跳转
    private fun handleCustomClick(tag: String) {
        if (TRANSACTION == tag) {
            TransactionActivity.start(mActivity)
        }
    }

    //login
    private fun login() {
        mActivity.finishSelf()
        CommLoginActivity.start(mActivity)
    }

    //log out
    private fun logOut() {
        val dialog1 = MDialog(mActivity, R.style.BottomDialog)
        with(dialog1){
            setLayout(R.layout.layout_user_loginout)
            setGravity(Gravity.BOTTOM)
            setWindowAnimation(R.style.BottomDialog_Animation)

            findViewById<View>(R.id.ul_logout_cancel).setOnClickListener {
                dismiss()
            }

            findViewById<View>(R.id.ul_logout_sure).setOnClickListener{
                CommLoginManager.logout()
                //refresh view
                notifyItemChanged(0)

                if(isShowing) {
                    dismiss()
                }
            }

            show()
        }
    }

    //未登陆Holder
    internal class LoginHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userImage = view.findViewById<SimpleDraweeView>(R.id.ui_logo)
        val userName = view.findViewById<TextView>(R.id.ui_title)
        val userId = view.findViewById<TextView>(R.id.ui_subTitle)
    }

    //通用item
    internal class CustomHolder(var layout: View) : RecyclerView.ViewHolder(layout) {
        var icon: ImageView = layout.findViewById(R.id.uc_icon)
        var text: TextView = layout.findViewById(R.id.uc_text)
    }

    companion object {
        const val TAG = "UserFragmentAdapter"
        const val TRANSACTION = "transaction"
        const val SETTINGS = "settings"
        const val HELP = "help"

        //View Type
        const val TYPE_LOGIN = 1
        const val TYPE_CUSTOM = 2
    }
}

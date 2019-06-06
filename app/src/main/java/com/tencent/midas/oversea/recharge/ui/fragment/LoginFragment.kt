package com.tencent.midas.oversea.recharge.ui.fragment

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.tencent.midas.oversea.recharge.R
import com.tencent.midas.oversea.recharge.login.CommLoginManager
import me.yokeyword.fragmentation.SupportFragment

class LoginFragment : SupportFragment(),View.OnClickListener {
    private val TAG = "LoginFragment"
    private lateinit var userNameET:EditText
    private lateinit var userPasswordET:EditText

    companion object{
        fun newInstance():LoginFragment{
            val args = Bundle()
            val fragment = LoginFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.layout_fragment_login,container,false)
        userNameET = layout.findViewById(R.id.fl_user)
        userPasswordET = layout.findViewById(R.id.fl_password)

        layout.findViewById<View>(R.id.fl_login).setOnClickListener(this)
        layout.findViewById<View>(R.id.fl_register).setOnClickListener(this)
        layout.findViewById<View>(R.id.fl_find_password).setOnClickListener(this)

        return layout
    }

    override fun onClick(v: View) {
        when (v.id){
            R.id.fl_login -> {
                val userName =  userNameET.text.toString()
                val userPassword = userPasswordET.text.toString()

                if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPassword)){
                    val snackBar = Snackbar.make(userPasswordET,"Empty username or password.", Snackbar.LENGTH_LONG)
                    snackBar.view.setBackgroundColor(resources.getColor(R.color.orange,null))
                    snackBar.show()
                }else{
                    CommLoginManager.login(userName, userPassword, (activity as ILoginObserver))
                }
            }
            R.id.fl_register ->
                (activity as ILoginObserver).goFragment(this,"RegisterFragment")
            R.id.fl_find_password ->
                (activity as ILoginObserver).goFragment(this,"FindPasswordFragment")
        }
    }
}
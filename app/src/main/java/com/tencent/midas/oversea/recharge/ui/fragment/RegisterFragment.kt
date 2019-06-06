package com.tencent.midas.oversea.recharge.ui.fragment

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import com.tencent.midas.oversea.recharge.R
import com.tencent.midas.oversea.recharge.comm.MLog
import com.tencent.midas.oversea.recharge.data.RegisterParams
import com.tencent.midas.oversea.recharge.login.CommLoginManager
import me.yokeyword.fragmentation.SupportFragment
import java.util.*



class RegisterFragment : SupportFragment(),View.OnClickListener {
    private val TAG = "RegisterFragment"
    private lateinit var goBackView:ImageView
    private lateinit var email:EditText;
    private lateinit var password:EditText;
    private lateinit var surePassword:EditText;
    private lateinit var firstName:EditText;
    private lateinit var lastName:EditText;
    private lateinit var birthData:EditText;
    companion object{
        fun newInstance():RegisterFragment{
            val args = Bundle()
            val fragment = RegisterFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.layout_fragment_register,container,false)

        goBackView = layout.findViewById(R.id.fr_close)
        goBackView.setColorFilter(Color.WHITE)

        email = layout.findViewById(R.id.fr_email);
        password = layout.findViewById(R.id.fr_password);
        surePassword = layout.findViewById(R.id.fr_password_again);
        firstName = layout.findViewById(R.id.fr_first_name);
        lastName = layout.findViewById(R.id.fr_last_name);

        birthData = layout.findViewById(R.id.fr_birthdaty);
        birthData.isFocusable = false

        goBackView.setOnClickListener(this)
        birthData.setOnClickListener(this)
        layout.findViewById<View>(R.id.fr_register).setOnClickListener(this)

        //密码校验，输入密码提示
        checkPassword()

        return layout
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.fr_close->
                goBack()
            R.id.fr_birthdaty->
                showDatePicker()
            R.id.fr_register->
                register()
        }
    }


    override fun onBackPressedSupport(): Boolean {
        goBack()
        return true
    }

    //回退
    private fun goBack(){
        (activity as ILoginObserver).goFragment(this,"LoginFragment")
    }

    //显示日期选择器
    private fun showDatePicker(){

        val calendar = Calendar.getInstance()

        val dataPickerDialog = DatePickerDialog(activity)
        dataPickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR,year)
            calendar.set(Calendar.MONTH,month)
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)

            val sb = StringBuilder()
            sb.append(year).append("-").append(month).append("-").append(dayOfMonth)

            MLog.d(TAG,"date: "+sb.toString())
            birthData.setText(sb.toString().toCharArray(),0,sb.toString().length)
        }
        dataPickerDialog.show()
    }

    //注册
    private fun register(){

        var isCheckSuccess = true;
        var errorMsg = "";

        val pEmail = email.text.toString()
        val pPassword = password.text.toString()
        val pPasswordSure = surePassword.text.toString()
        val pFirstName = firstName.text.toString()
        val pLastName = lastName.text.toString()
        val pDate = birthData.text.toString()

        //参数为空校验
        if(TextUtils.isEmpty(pEmail)
                || TextUtils.isEmpty(pPassword)
                || TextUtils.isEmpty(pFirstName)
                || TextUtils.isEmpty(pLastName)){
            isCheckSuccess = false
            errorMsg = "Some params are empty,please check them."
        }
        //密码不一致校验
        else if(pPassword != pPasswordSure){
            isCheckSuccess = false
            errorMsg = "Your passwords are not equals,please check them."
        }


        //注册
        if(!isCheckSuccess) {
            showErrorTips(errorMsg)
        }else{
            var registerParams = RegisterParams(pEmail, pPassword, pFirstName, pLastName, pDate)
            CommLoginManager.register(registerParams, activity as ILoginObserver)
        }
    }


    private fun checkPassword(){
        //check email
        email.setOnEditorActionListener{v, actionId, event ->
            when (actionId){
                EditorInfo.IME_ACTION_DONE->{
                    var strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$"
                    if(!v.text.toString().matches(Regex(strPattern))){
                        showErrorTips("Your must input an email.")
                    }
                }
            }

            true
        }

        password.setOnEditorActionListener { v, actionId, event ->
            when(actionId){
                EditorInfo.IME_ACTION_DONE-> {
                    val key = password.text.toString()
                    //长度校验
                    if(key.length < 8){
                        showErrorTips("Your password must more than eight length.")
                    }
                }
            }

            true
        }

        surePassword.setOnEditorActionListener { v, actionId, event ->
            when(actionId){
                EditorInfo.IME_ACTION_DONE->
                    if(v.text.toString() != password.text.toString()){
                        showErrorTips("Your passwords are not equals,please check them.")
                    }
            }

            true
        }
    }


    private fun showErrorTips(tips:String){
        val snackBar = Snackbar.make(birthData,tips,Snackbar.LENGTH_LONG)
        snackBar.view.setBackgroundColor(resources.getColor(R.color.orange,null))
        snackBar.show()
    }
}
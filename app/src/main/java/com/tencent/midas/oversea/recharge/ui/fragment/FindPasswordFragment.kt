package com.tencent.midas.oversea.recharge.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import com.tencent.midas.oversea.recharge.R
import me.yokeyword.fragmentation.SupportFragment

class FindPasswordFragment : SupportFragment(),View.OnClickListener {
    private val TAG = "RegisterFragment"
    private lateinit var goBackView: ImageView

    companion object{
        fun newInstance():FindPasswordFragment{
            val args = Bundle()
            val fragment = FindPasswordFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.layout_fragment_find_password,container,false)

        goBackView = layout.findViewById(R.id.fp_close)
        goBackView.setColorFilter(Color.WHITE)

        goBackView.setOnClickListener(this)

        return layout
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.fp_close->
                goBack()
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


}
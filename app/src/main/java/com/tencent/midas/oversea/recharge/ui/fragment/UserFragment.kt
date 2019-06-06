package com.tencent.midas.oversea.recharge.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tencent.midas.oversea.recharge.R
import com.tencent.midas.oversea.recharge.comm.MLog
import com.tencent.midas.oversea.recharge.ui.activity.MBaseActivity
import com.tencent.midas.oversea.recharge.ui.adapter.UserFragmentAdapter

import me.yokeyword.fragmentation.SupportFragment

class UserFragment : SupportFragment() {

    private var mRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        MLog.d(TAG, "onCreateView")

        val layout = inflater.inflate(R.layout.layout_fragment_user, container, false) as RecyclerView
        initView(layout)
        return layout
    }

    private fun initView(recyclerView: RecyclerView) {
        mRecyclerView = recyclerView
        mRecyclerView!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val adapter = UserFragmentAdapter(activity as MBaseActivity)
        mRecyclerView!!.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        MLog.d(TAG, "onActivityResult")
    }

    companion object {
        const val TAG = "UserFragment"

        fun newInstance(): UserFragment {
            val args = Bundle()
            val fragment = UserFragment()
            fragment.arguments = args
            return fragment
        }
    }
}

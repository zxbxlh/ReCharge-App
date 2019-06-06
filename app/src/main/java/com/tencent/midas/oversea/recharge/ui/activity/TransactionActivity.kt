package com.tencent.midas.oversea.recharge.ui.activity

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.tencent.midas.oversea.recharge.R
import com.tencent.midas.oversea.recharge.ui.adapter.TransactionAdapter
import java.util.ArrayList
import java.util.Random

class TransactionActivity : MBaseActivity() {

    private var mRefreshLayout: RefreshLayout? = null
    private var mRecyclerViewAdapter: TransactionAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_activity_transaction)

        initRefreshLayout()
        initRecyclerView()
    }

    private fun initRefreshLayout() {
        mRefreshLayout = findViewById<View>(R.id.at_refreshLayout) as RefreshLayout

        //设置 Footer 为 球脉冲
        mRefreshLayout!!.setRefreshFooter(BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale))
        mRefreshLayout!!.setOnRefreshListener { refreshData() }
        //刷新数据
        mRefreshLayout?.autoRefresh()
    }

    //ToDo 测试代码
    private fun initData(): ArrayList<String> {
        val res = ArrayList<String>()
        val random = Random()
        for (i in 0..29) {
            res.add("item " + random.nextInt())
        }
        return res
    }

    private fun refreshData() {
        Log.d(TAG, "refresh data...")

        //ToDo 测试代码，模拟耗时任务
        mRefreshLayout!!.layout.postDelayed({
            if (mRecyclerViewAdapter != null) {
                mRecyclerViewAdapter!!.refresh(initData())
            }

            //结束刷新
            mRefreshLayout!!.finishRefresh()
        }, 3000)

    }

    private fun initRecyclerView() {
        val recyclerView = findViewById<View>(R.id.at_recyclerView) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mRecyclerViewAdapter = TransactionAdapter(this)
        recyclerView.adapter = mRecyclerViewAdapter
    }

    companion object {
        const val TAG = "TransactionActivity"

        fun start(activity: Activity) {
            val intent = Intent(activity, TransactionActivity::class.java)
            activity.startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
        }
    }
}

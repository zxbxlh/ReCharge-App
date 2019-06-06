package com.tencent.midas.oversea.recharge.ui.fragment

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tencent.midas.oversea.recharge.R
import com.tencent.midas.oversea.recharge.comm.MLog
import com.tencent.midas.oversea.recharge.ui.adapter.HomePagerFragmentAdapter

import me.yokeyword.fragmentation.SupportFragment

class HomeFragment : SupportFragment() {

    //    private TabLayout mTab;
    //    private Toolbar mToolbar;
    private var mViewPager: ViewPager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        MLog.d(TAG, "onCreateView")
        val layout = inflater.inflate(R.layout.layout_fragment_home, container, false)
        initView(layout)
        return layout
    }

    private fun initView(layout: View) {
        //        mToolbar = (Toolbar)layout.findViewById(R.id.fh_toolbar);
        //        mTab = (TabLayout)layout.findViewById(R.id.fh_tab_layout);
        //        mToolbar.setTitle(R.string.home_title);
        //        mTab.addTab(mTab.newTab());

        mViewPager = layout.findViewById<View>(R.id.fh_viewPager) as ViewPager
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        mViewPager!!.adapter = HomePagerFragmentAdapter(childFragmentManager)
        //        mTab.setupWithViewPager(mViewPager);
    }

    companion object {
        const val TAG = "HomeFragment"

        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}

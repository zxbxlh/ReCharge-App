package com.tencent.midas.oversea.recharge.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tencent.midas.oversea.recharge.R
import com.tencent.midas.oversea.recharge.comm.MLog
import com.tencent.midas.oversea.recharge.ui.view.BottomBar
import com.tencent.midas.oversea.recharge.ui.view.BottomBarTab
import me.yokeyword.fragmentation.SupportFragment

class MainFragment : SupportFragment() {
    private val TAG = "MainFragment"
    private val FIRST = 0
    private val SECOND = 1

    private val mFragments = arrayOfNulls<SupportFragment>(2)
    private var mBottomBar: BottomBar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.layout_fragment_main, container, false)
        initView(layout)
        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val firstFragment = findChildFragment(HomeFragment::class.java)
        if (firstFragment == null) {
            MLog.d(TAG, "create new instance")
            mFragments[FIRST] = HomeFragment.newInstance()
            mFragments[SECOND] = UserFragment.newInstance()

            loadMultipleRootFragment(
                    R.id.fm_container,
                    FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND])
        } else {
            MLog.d(TAG, "reuse exist instance")
            mFragments[FIRST] = firstFragment
            mFragments[SECOND] = findChildFragment(UserFragment::class.java)
        }
    }

    private fun initView(layout: View) {
        mBottomBar = layout.findViewById<View>(R.id.fm_bottom_bar) as BottomBar
        mBottomBar!!
                .addItem(BottomBarTab(_mActivity, R.mipmap.icons8_home_page, getString(R.string.home_title)))
                .addItem(BottomBarTab(_mActivity, R.mipmap.icons8_user_menu_male, getString(R.string.user_title)))

        mBottomBar!!.setOnTabSelectedListener(object : BottomBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int, prePosition: Int) {
                MLog.d(TAG, "onTabSelected: position=$position,prePosition=$prePosition")
                showHideFragment(mFragments[position], mFragments[prePosition])
            }

            override fun onTabUnselected(position: Int) {
                MLog.d(TAG, "onTabUnselected: position=$position")
            }

            override fun onTabReselected(position: Int) {
                MLog.d(TAG, "onTabReselected: position=$position")
            }
        })
    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle) {
        MLog.d(TAG, "onFragmentResult")
        super.onFragmentResult(requestCode, resultCode, data)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        MLog.d(TAG, "onActivityResult")
        //pass to top child fragment
        val topChild = topChildFragment
        if (topChild != null && topChild is Fragment) {
            topChild.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        fun newInstance(): MainFragment {
            val args = Bundle()
            val fragment = MainFragment()
            fragment.arguments = args
            return fragment
        }
    }
}

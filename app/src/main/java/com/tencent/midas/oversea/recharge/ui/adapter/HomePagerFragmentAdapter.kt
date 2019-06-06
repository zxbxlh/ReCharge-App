package com.tencent.midas.oversea.recharge.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.tencent.midas.oversea.recharge.ui.fragment.RechargeFragment
import java.util.ArrayList

/**
 * @author zachzeng
 * 2019.2.27
 */
class HomePagerFragmentAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private val mFragments = ArrayList<Fragment>()
    private val mFragmentTitles = ArrayList<String>()

    init {
        addFragment(RechargeFragment.newInstance(), "ReChargeFragment")
    }

    fun addFragment(fragment: Fragment, fragmentTitle: String) {
        mFragments.add(fragment)
        mFragmentTitles.add(fragmentTitle)
    }

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitles[position]
    }

    companion object {
        const val TAG = "HomePagerFragmentAdapter"
    }
}

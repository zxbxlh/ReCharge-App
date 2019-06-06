package com.tencent.midas.oversea.recharge.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.tencent.midas.oversea.recharge.R

import java.util.ArrayList

class TransactionAdapter(private val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mData = ArrayList<String>()

    //外部更新数据
    fun refresh(data: ArrayList<String>) {
        if (!data.isEmpty()) {
            mData.clear()
            mData.addAll(data)
            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TransactionHolder) {
            holder.textView.text = mData[position]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = LayoutInflater.from(mContext).inflate(R.layout.layout_transaction_item, parent, false)
        return TransactionHolder(layout)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    //internal包级可见
    internal class TransactionHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textView: TextView = view as TextView
    }

    companion object {
        const val TAG = "TransactionAdapter"
    }
}

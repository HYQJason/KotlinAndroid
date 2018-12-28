package com.example.jajaying.kotlinproject.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.jajaying.kotlinproject.R
import com.example.jajaying.kotlinproject.bean.BluetoothDeviceBean

/**
 * Created by wangyao3 on 2018/9/3.
 */
class TestBluetoothAdapter(list: List<String>) :BaseQuickAdapter<String,BaseViewHolder>(R.layout.child_item_layout,list) {
    override fun convert(helper: BaseViewHolder?, item: String?) {
     helper!!.setText(R.id.child_text,item)
    }
}
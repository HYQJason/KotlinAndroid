package com.example.jajaying.kotlinproject.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.jajaying.kotlinproject.R
import com.example.jajaying.kotlinproject.bean.Info

/**
 * Created by jajaying on 2018/8/28.
 */
class TestRecycleAdapter( data: MutableList<Info>?) : BaseQuickAdapter<Info, BaseViewHolder>(R.layout.test_item_layout, data) {
    override fun convert(helper: BaseViewHolder?, item: Info?) {
        item ?:return
        helper?.setText(R.id.text_item,item.name)

    }
}
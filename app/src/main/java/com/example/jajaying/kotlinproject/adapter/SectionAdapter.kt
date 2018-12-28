package com.example.jajaying.kotlinproject.adapter

import android.util.Log
import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.jajaying.kotlinproject.R
import com.example.jajaying.kotlinproject.bean.SectionBean

/**
 * Created by wangyao3 on 2018/8/31.
 */
class SectionAdapter(list: List<SectionBean>) :BaseSectionQuickAdapter<SectionBean,BaseViewHolder>(R.layout.child_item_layout,R.layout.head_layout,list) {
    override fun convertHead(helper: BaseViewHolder?, item: SectionBean?) {
        helper !!.let {
            Log.d("TAG",it.toString())
            Log.d("TAG",item!!.header)
            it.setText(R.id.group_text,   item!!.header)
        }




    }

    override fun convert(helper: BaseViewHolder?, item: SectionBean?) {
        helper!!.let {
            it.setText(R.id.child_text,item!!.name)
        }

    }

}
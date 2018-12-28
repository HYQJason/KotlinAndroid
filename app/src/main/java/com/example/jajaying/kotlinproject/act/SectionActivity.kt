package com.example.jajaying.kotlinproject.act

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.entity.SectionEntity
import com.example.jajaying.kotlinproject.R
import com.example.jajaying.kotlinproject.adapter.SectionAdapter
import com.example.jajaying.kotlinproject.bean.SectionBean
import kotlinx.android.synthetic.main.activity_section.*

class SectionActivity : AppCompatActivity() {
    private val list = ArrayList<SectionBean>()


    private val sectionlayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private val sectionAdapter: SectionAdapter by lazy {
        SectionAdapter(list)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_section)
        sectionRecycleview.run {
            layoutManager = sectionlayoutManager
            adapter = sectionAdapter
        }
        sectionAdapter.run {
            bindToRecyclerView(sectionRecycleview)
        }
        var bean0 = SectionBean("", true, "请选择？")
        var bean1 = SectionBean("", true, "请选择？")
        var bean2 = SectionBean("", true, "请选择？")
        var bean3 = SectionBean("", true, "请选择？")
        var bean4 = SectionBean("", true, "请选择？")
        list.add(bean0)
        list.add(bean1)
        list.add(bean2)
        list.add(bean3)
        list.add(bean4)

        var beana = SectionBean("A", false, "no")
        var beanb = SectionBean("B", false, "no")
        var beanc = SectionBean("C", false, "no")
        var beand = SectionBean("D", false, "no")
        var beane = SectionBean("E", false, "no")
list.add(beana)
list.add(beanb)
list.add(beanc)
list.add(beand)
list.add(beane)

        sectionAdapter.notifyDataSetChanged()
    }

    public fun intentact(view: View){
        val intent = Intent(this, BluetoothActivity::class.java)
  startActivity(intent)
    }
}

package com.example.jajaying.kotlinproject.act

import android.annotation.TargetApi
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.jajaying.kotlinproject.R
import com.example.jajaying.kotlinproject.adapter.TestRecycleAdapter
import com.example.jajaying.kotlinproject.bean.Info
import com.example.jajaying.kotlinproject.runtimepermissions.PermissionsManager
import com.example.jajaying.kotlinproject.runtimepermissions.PermissionsResultAction
import com.githang.statusbar.StatusBarCompat
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName
    //lateinit 只用于变量 var，而 lazy 只用于常量 val
    //lateinit不能用在可空的属性上和java的基本类型上
    //  lateinit可以在任何位置初始化并且可以初始化多次。而lazy在第一次被调用时就被初始化，想要被改变只能重新定义
    //lazy()是接受一个 lambda 并返回一个 Lazy <T> 实例的函数
    private val list = mutableListOf<Info>()
    // private var recycleview: RecyclerView? = null
    private var isErr = true
    private val testadapter: TestRecycleAdapter by lazy {
        TestRecycleAdapter(list)
    }
    private val handler = WeakReferenceHandler(this)

    class WeakReferenceHandler(activity: MainActivity) : Handler() {
        private val reference: WeakReference<MainActivity>

        init {
            reference = WeakReference<MainActivity>(activity)
        }

        override fun handleMessage(msg: Message) {
            val mainActivity = reference.get() as MainActivity
            when (msg.what) {
                1 -> {
                    if (mainActivity != null) {
                        mainActivity.addData(mainActivity.testadapter.itemCount, mainActivity.testadapter.itemCount + 10)
                    }
                }
                else -> {
                    mainActivity.list.clear()
                    mainActivity.testadapter.notifyDataSetChanged()
                }
            }
        }
    }

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private val itemDecoration :DividerItemDecoration by lazy {
        DividerItemDecoration(this ,DividerItemDecoration.HORIZONTAL)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StatusBarCompat.setStatusBarColor(this,Color.parseColor("#FFAFB53F"))
        requestPermissions()
        hellotext.setText("添加item")
        hellotext.setOnClickListener(View.OnClickListener { handler.sendEmptyMessage(1) })
        btclean.setOnClickListener(View.OnClickListener { handler.sendEmptyMessage(2) })
        testRecycleview.run {
            testRecycleview?.layoutManager = linearLayoutManager
            adapter = testadapter
            testRecycleview.addItemDecoration(itemDecoration)
        }

        refreshlayout.setOnRefreshListener(OnRefreshListener {
            Log.d("TAG", "OnRefreshListener")
            refreshlayout.finishRefresh()
        })



        testadapter.run {
            bindToRecyclerView(testRecycleview)
            setEmptyView(R.layout.empty_layout)
            openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT)
            disableLoadMoreIfNotFullPage()
            setPreLoadNumber(8)
            setOnLoadMoreListener(BaseQuickAdapter.RequestLoadMoreListener {
                testRecycleview.postDelayed(Runnable {
                    if (testadapter.itemCount - 1 >= 200) {
                        testadapter.loadMoreEnd()
                    } else {
                        if (isErr) {
                            addData(testadapter.itemCount, testadapter.itemCount + 20)
                            testadapter.loadMoreComplete()
                        } else {
                            isErr = true
                            Toast.makeText(this@MainActivity, "", Toast.LENGTH_LONG).show()
                            testadapter.loadMoreComplete()
                        }

                    }
                }, 2000)
            }, testRecycleview)
            setOnItemClickListener(BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
                list.get(position).name = "00000000000000000"
                testadapter.notifyItemChanged(position)
            })

        }

        for (i: Int in 0..10) {
            list.add(Info("name========" + i, i))
            if (i == 10) {
                testadapter.notifyDataSetChanged()

            }
        }
    }

    private fun addData(and: Int, length: Int) {
        for (i: Int in and..length) {
            list.add(Info("name========" + i, i))

        }
        testadapter.notifyDataSetChanged()
    }

    fun startIntent(v:View) {
        val intent = Intent(this, SectionActivity::class.java)
        startActivity(intent)
    }

    @TargetApi(23)
    private fun requestPermissions() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, object : PermissionsResultAction() {
            override   fun onGranted() {
                Log.d(TAG,"授权")
            }

            override   fun onDenied(permission: String) {
                //Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"拒绝授权======"+permission)
            }
        })
    }

    override  fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                             grantResults: IntArray) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults)
    }
}

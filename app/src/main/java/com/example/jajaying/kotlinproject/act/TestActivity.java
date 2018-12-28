package com.example.jajaying.kotlinproject.act;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.jajaying.kotlinproject.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
/**
 * Created by wangyao3 on 2018/8/31.
 */

public class TestActivity extends Activity {
private   final String TAG=this.getClass().getSimpleName();

RecyclerView recyclerView;
    SmartRefreshLayout refreshLayout;
    TabLayout tabLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_test);

tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
});


    }

    BaseQuickAdapter.OnItemClickListener itemClickListener=new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        }
    };

    public void startIntent(){
        Intent intent=new Intent(this,TestActivity.class);
        startActivity(intent);
    }


}

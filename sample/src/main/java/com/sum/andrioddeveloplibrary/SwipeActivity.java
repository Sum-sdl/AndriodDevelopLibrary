package com.sum.andrioddeveloplibrary;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.scwang.smart.refresh.header.BezierRadarHeader;
import com.scwang.smart.refresh.header.FalsifyFooter;
import com.scwang.smart.refresh.header.FalsifyHeader;
import com.scwang.smart.refresh.header.TwoLevelHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.sum.adapter.RecyclerAdapter;
import com.sum.adapter.RecyclerDataHolder;
import com.sum.andrioddeveloplibrary.refreshview.CustomRefreshHeader;
import com.sum.andrioddeveloplibrary.testActivity.holder.DemoDataHolder;
import com.sum.library.utils.Logger;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SwipeActivity extends AppCompatActivity {
    RecyclerAdapter mAdapter;

    SmartRefreshLayout smartRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        RecyclerAdapter adapter = new RecyclerAdapter();
        mAdapter = adapter;
        SwipeMenuRecyclerView recyclerView = findViewById(R.id.sm_rv);
        smartRefreshLayout = findViewById(R.id.smart_refresh);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLongPressDragEnabled(true);
        recyclerView.setItemViewSwipeEnabled(true);
        recyclerView.setAdapter(adapter);

//        smartRefreshLayout.setRefreshHeader(new BezierRadarHeader(this));
//        smartRefreshLayout.setRefreshHeader(new TwoLevelHeader(this));
        smartRefreshLayout.setRefreshHeader(new CustomRefreshHeader(this));
        //不好用
//        smartRefreshLayout.setRefreshHeader(new FalsifyHeader(this));
//        smartRefreshLayout.setRefreshFooter(new FalsifyFooter(this));

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //开始刷新
                Logger.e("onRefresh finish1111");
                //获取结果
                top();
                //刷新结束
                refreshLayout.finishRefresh(4000, false, true);//传入
            }
        });
        smartRefreshLayout.setEnableFooterFollowWhenNoMoreData(true);
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                add();
                refreshLayout.finishLoadMore(2000);
                refreshLayout.setNoMoreData(true);
            }
        });

        smartRefreshLayout.autoRefresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.e("destory2222");
    }

    private void top() {
        List<RecyclerDataHolder> holders = new ArrayList<>();
        for (int i = 0; i <= 20; i++) {
            holders.add(new DemoDataHolder("index1111111->" + i + ""));
        }
        mAdapter.setDataHolders(holders);
    }

    private void add() {
        List<RecyclerDataHolder> holders = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            holders.add(new DemoDataHolder("add->index->" + i + ""));
        }
        mAdapter.addDataHolder(holders);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}

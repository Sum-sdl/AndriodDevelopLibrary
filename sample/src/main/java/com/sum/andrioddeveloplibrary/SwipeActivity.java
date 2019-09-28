package com.sum.andrioddeveloplibrary;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sum.adapter.RecyclerAdapter;
import com.sum.adapter.RecyclerDataHolder;
import com.sum.andrioddeveloplibrary.testActivity.holder.DemoDataHolder;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SwipeActivity extends AppCompatActivity {
    RecyclerAdapter<RecyclerDataHolder> mAdapter;

    SmartRefreshLayout smartRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        RecyclerAdapter<RecyclerDataHolder> adapter = new RecyclerAdapter<>();
        mAdapter = adapter;
        SwipeMenuRecyclerView recyclerView = findViewById(R.id.sm_rv);
        smartRefreshLayout = findViewById(R.id.smart_refresh);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLongPressDragEnabled(true);
        recyclerView.setItemViewSwipeEnabled(true);
        recyclerView.setAdapter(adapter);

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                top();
                refreshLayout.finishRefresh(1000);//传入
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                add();
                refreshLayout.finishLoadMore(2000);
            }
        });

        smartRefreshLayout.autoRefresh();
    }

    private void top() {
        List<RecyclerDataHolder> holders = new ArrayList<>();
        for (int i = 0; i <= 20; i++) {
            holders.add(new DemoDataHolder("index->" + i + ""));
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

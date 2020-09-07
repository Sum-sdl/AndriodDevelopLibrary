package com.sum.andrioddeveloplibrary;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.FalsifyFooter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.sum.adapter.RecyclerAdapter;
import com.sum.adapter.RecyclerDataHolder;
import com.sum.andrioddeveloplibrary.testActivity.holder.DemoDataHolder;
import com.sum.library.utils.Logger;
import com.sum.library.view.refresh.RefreshEmptyFooter;
import com.sum.library.view.refresh.RefreshEmptyHeader;
import com.sum.library.view.refresh.RefreshMaterialFooter;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SwipeActivity extends AppCompatActivity {
    RecyclerAdapter mAdapter;

    SmartRefreshLayout smartRefreshLayout;

    int index = 0;


    public int getColorByResId(int colorResId) {
        return ContextCompat.getColor(this, colorResId);
    }

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
//        smartRefreshLayout.setRefreshHeader(new CustomRefreshHeader(this));
        //滚动效果，支持回调
        smartRefreshLayout.setRefreshHeader(new RefreshEmptyHeader(this, true, "5555"));
//        smartRefreshLayout.setRefreshFooter(new RefreshEmptyFooter(this));

        RefreshMaterialFooter footer = new RefreshMaterialFooter(this);
        footer.getTipTextView().setText("666666");
        footer.getNoMoreTextView().setText("No More Data");
        footer.getTipTextView().setTextColor(getColorByResId(R.color.colorAccent));
        footer.setRefreshColor(getColorByResId(R.color.colorAccent));
        smartRefreshLayout.setRefreshFooter(footer);
        //用于空滚动回弹效果
//        smartRefreshLayout.setRefreshHeader(new FalsifyHeader(this));
//        smartRefreshLayout.setRefreshFooter(new FalsifyFooter(this));
//        smartRefreshLayout.setRefreshFooter(new ClassicsFooter(this));

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                index = 0;
                ToastUtils.showShort("refreshTop");
                //开始刷新
                Logger.e("onRefresh finish->>>");
                //获取结果
                top();
                //刷新结束
                refreshLayout.finishRefresh(2000);//传入
            }
        });

        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                index++;
                ToastUtils.showShort("loadMore");
                add();
                if (index == 3) {
                    refreshLayout.finishLoadMore(1500, true, true);
                }else {
                    refreshLayout.finishLoadMore(500, false,false);
                }
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

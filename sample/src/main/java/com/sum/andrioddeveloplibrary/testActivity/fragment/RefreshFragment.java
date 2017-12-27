package com.sum.andrioddeveloplibrary.testActivity.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sum.andrioddeveloplibrary.R;
import com.sum.andrioddeveloplibrary.testActivity.holder.DemoDataHolder;
import com.sum.library.app.BaseFragment;
import com.sum.library.view.SwipeRefresh.SwipeRefreshLayout;
import com.sum.library.view.recyclerview.RecyclerAdapter;
import com.sum.library.view.recyclerview.RecyclerDataHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 365 on 2017/3/2.
 */

public class RefreshFragment extends BaseFragment {

    private RecyclerView recyclerView;

    //RecyclerView 统一适配器
    private RecyclerAdapter adapter;

    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void initParams(View view) {

        recyclerView = _findViewById(R.id.recyclerView);
        //初始化刷新控件
        refreshLayout = _findViewById(R.id.swipeLayout);

        //recyclerView 设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //统一适配器
        adapter = new RecyclerAdapter(getContext());
        recyclerView.setAdapter(adapter);

        
    }


    public void onRefreshLoadData() {
        //这个方法是配合SwipeRefreshLayout刷新组件回掉使用的
        //每次上啦或者下拉都会返回到这边，通过getPageIndex获取下一页
        //注意RefreshViewImpl里面的实现流程

        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<String> data = createData(1);

                //RecyclerView 的每一项数据源
                List<RecyclerDataHolder> holders = new ArrayList<>();

                for (String s : data) {
                    DemoDataHolder holder = new DemoDataHolder(s);
                    holders.add(holder);
                }

                if (10> 1) {
                    //增加数据
                    adapter.addDataHolder(holders);
                } else {
                    //全部刷新
                    adapter.setDataHolders(holders);
                }
                refreshLayout.setRefreshing(false);
            }
        }, 800);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_refresh;
    }


    //正常通过网络请求获取数据
    private List<String> createData(int page) {
        List<String> data = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            data.add("数据项:" + i + " ->页数:" + page);
        }
        return data;
    }

}

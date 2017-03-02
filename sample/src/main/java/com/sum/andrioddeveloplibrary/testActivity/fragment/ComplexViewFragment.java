package com.sum.andrioddeveloplibrary.testActivity.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sum.andrioddeveloplibrary.R;
import com.sum.andrioddeveloplibrary.testActivity.holder.Demo2DataHolder;
import com.sum.andrioddeveloplibrary.testActivity.holder.Demo3DataHolder;
import com.sum.andrioddeveloplibrary.testActivity.holder.DemoDataHolder;
import com.sum.library.app.BaseFragment;
import com.sum.library.view.SwipeRefresh.SwipeRefreshLayout;
import com.sum.library.view.SwipeRefresh.SwipeRefreshLayoutDirection;
import com.sum.library.view.recyclerview.RecyclerAdapter;
import com.sum.library.view.recyclerview.RecyclerDataHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 365 on 2017/3/2.
 */

public class ComplexViewFragment extends BaseFragment {

    private RecyclerView recyclerView;

    //RecyclerView 统一适配器
    private RecyclerAdapter adapter;

    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void initParams(View view) {

        recyclerView = _findViewById(R.id.recyclerView);
        refreshLayout = _findViewById(R.id.swipeLayout);
        initRefresh(refreshLayout, SwipeRefreshLayoutDirection.BOTTOM);

        //recyclerView 设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //统一适配器
        adapter = new RecyclerAdapter(getContext());
        recyclerView.setAdapter(adapter);

        onRefreshLoadData();
    }

    @Override
    public int getPageIndex() {
        //返回分页参数
        return super.getPageIndex();
    }

    @Override
    public void onRefreshLoadData() {
        super.onRefreshLoadData();
        //这个方法是配合SwipeRefreshLayout刷新组件回掉使用的
        //每次上啦或者下拉都会返回到这边，通过getPageIndex获取下一页
        //注意RefreshViewImpl里面的实现流程

        //RecyclerView 的每一项数据源
        List<RecyclerDataHolder> holders = new ArrayList<>();

        //相当于3个不同的ViewHolder组合在一起（可重复）
        //可以随意修改加入的前后顺序
        holders.add(new Demo3DataHolder(null));
        holders.add(new DemoDataHolder("Type1++++++++++++++"));
        holders.add(new Demo3DataHolder("Type3+++++++++++++"));
        holders.add(new Demo2DataHolder(null));
        holders.add(new Demo2DataHolder(null));
        holders.add(new Demo2DataHolder(null));
        holders.add(new Demo2DataHolder(null));
        holders.add(new Demo2DataHolder(null));
        holders.add(new Demo2DataHolder(null));

        adapter.setDataHolders(holders);

        refreshLayout.setRefreshing(false);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_refresh;
    }

}

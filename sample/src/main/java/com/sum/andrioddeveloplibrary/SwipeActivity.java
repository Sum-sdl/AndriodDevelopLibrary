package com.sum.andrioddeveloplibrary;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.blankj.utilcode.util.ToastUtils;
import com.sum.andrioddeveloplibrary.testActivity.holder.DemoDataHolder;
import com.sum.lib.rvadapter.RecyclerAdapter;
import com.sum.lib.rvadapter.RecyclerDataHolder;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SwipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        RecyclerAdapter adapter = new RecyclerAdapter();
        SwipeMenuRecyclerView recyclerView = findViewById(R.id.sm_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setLongPressDragEnabled(true);
        recyclerView.setItemViewSwipeEnabled(true);
        recyclerView.useDefaultLoadMore();

        List<RecyclerDataHolder> holders = new ArrayList<>();
        for (int i = 0; i <= 50; i++) {
            holders.add(new DemoDataHolder(i + ""));
        }
        adapter.setDataHolders(holders);

        recyclerView.setAdapter(adapter);


        recyclerView.setLoadMoreListener(() ->

                {
                    ToastUtils.showLong("LoadMore");
                    recyclerView.loadMoreFinish(true, true);
                }

        );
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}

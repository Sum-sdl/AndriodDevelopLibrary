package com.sum.andrioddeveloplibrary;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.sum.adapter.RecyclerDataHolder;
import com.sum.adapter.RecyclerViewHolder;
import com.sum.adapter.sticky.StickRecyclerAdapter;
import com.sum.adapter.sticky.StickyHeadDecoration;
import com.sum.andrioddeveloplibrary.model.StickyItemModel;

import java.util.ArrayList;
import java.util.List;

public class StickyActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private SmartRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky);

        //数据源
        List<StickyItemModel> infos = getModel();

        //显示数据的Holder
        List<RecyclerDataHolder> holders = new ArrayList<>();
        for (StickyItemModel i : infos) {
            holders.add(new Holder(i));
        }
        Stick mAdapter = new Stick();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new StickyHeadDecoration(mAdapter));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setDataHolders(holders);

    }

    private List<StickyItemModel> getModel() {
        List<StickyItemModel> itemModels = new ArrayList<>();

        String[] array = getResources().getStringArray(R.array.animals);

        int i = 0;

        for (String s : array) {
            if (i <= 5) {
                itemModels.add(new StickyItemModel(s, 1));
            } else if (i < 8) {
                itemModels.add(new StickyItemModel(s, 2));
            } else if (i <= 10) {
                itemModels.add(new StickyItemModel(s, 4));
            } else if (i <= 40) {
                itemModels.add(new StickyItemModel(s, 5));
            } else if (i <= 50) {
                itemModels.add(new StickyItemModel(s, 6));
            } else {
                itemModels.add(new StickyItemModel(s, 3));
            }
            i++;
        }
        return itemModels;
    }

    private class Stick extends StickRecyclerAdapter<StickyItemModel> {
        @Override
        public int headLayoutId() {
            return R.layout.sticky_head;
        }

        @Override
        public long headId(int position, StickyItemModel data) {
            return data.headId;
        }

        @Override
        public void onBindHeaderViewHolder(View holder, int position, StickyItemModel data) {
            TextView name = (TextView) holder.findViewById(R.id.tv_title);
            StickyItemModel d = data;
            name.setText(d.name + " " + d.headId);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showLong("Name click->" + d.name);
                }
            });
        }
    }

    private class Holder extends RecyclerDataHolder<StickyItemModel> {
        public Holder(StickyItemModel data) {
            super(data);
        }

        @Override
        protected int getItemViewLayoutId() {
            return R.layout.sticky_item;
        }

        @Override
        protected RecyclerView.ViewHolder onCreateViewHolder(View itemView, int position) {
            return new RecyclerViewHolder(itemView);
        }

        @Override
        protected void onBindViewHolder(int position, RecyclerView.ViewHolder viewHolder, StickyItemModel data) {
            TextView name = (TextView) viewHolder.itemView.findViewById(R.id.tv);
            name.setText("" + position + "->" + data.name);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showShort(data.name);
                }
            });
        }
    }

}

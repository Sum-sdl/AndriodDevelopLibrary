package com.sum.andrioddeveloplibrary;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sum.andrioddeveloplibrary.model.StickyItemModel;
import com.sum.library.utils.Logger;
import com.sum.library.view.recyclerview.RecyclerDataHolder;
import com.sum.library.view.recyclerview.RecyclerViewHolder;
import com.sum.library.view.widget.sticky.StickRecyclerAdapter;
import com.sum.library.view.widget.sticky.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.List;

public class StickyActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private StickAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky);

        //数据源
        List<StickyItemModel> infos = getModel();

        //显示数据的Holder
        List<RecyclerDataHolder> holders = new ArrayList<>();
        for (StickyItemModel i : infos) {
            holders.add(new DataHolder(i));
        }
        mAdapter = new StickAdapter(this, holders);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(mAdapter));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private List<StickyItemModel> getModel() {
        List<StickyItemModel> itemModels = new ArrayList<>();

        String[] array = getResources().getStringArray(R.array.animals);

        int i = 0;

        for (String s : array) {
            i++;
            if (i <= 10) {
                itemModels.add(new StickyItemModel(s, 1));
            } else if (i > 30) {
                itemModels.add(new StickyItemModel(s, 2));
            } else {
                itemModels.add(new StickyItemModel(s, 3));
            }
        }


        return itemModels;
    }

    class StickAdapter extends StickRecyclerAdapter {

        public StickAdapter(Context context) {
            super(context);
        }

        public StickAdapter(Context context, List<RecyclerDataHolder> holders) {
            super(context, holders);
        }

        @Override
        public int layoutId() {
            return R.layout.sticky_head;
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position, Object o) {
            TextView name = (TextView) holder.itemView.findViewById(R.id.tv_title);
            StickyItemModel d = (StickyItemModel) o;
            name.setText(d.name + " " + d.headId);
            Logger.e("onBindHeaderViewHolder "+position);
        }

        @Override
        public long getHeaderId(int position) {
            RecyclerDataHolder holder = queryDataHolder(position);
            DataHolder d = (DataHolder) holder;
            return d.getData().headId;
        }
    }


    class DataHolder extends RecyclerDataHolder<StickyItemModel> {

        public DataHolder(StickyItemModel data) {
            super(data);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
//            View view = View.inflate(context, R.layout.sticky_item, null);
            View view =  LayoutInflater.from(parent.getContext()).inflate( R.layout.sticky_item, parent, false);
            return new RecyclerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, StickyItemModel data) {
            TextView name = (TextView) vHolder.itemView.findViewById(R.id.tv);
            name.setText(data.name);
        }
    }

}

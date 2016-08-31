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
import com.sum.library.utils.ToastUtil;
import com.sum.library.view.recyclerview.RecyclerDataHolder;
import com.sum.library.view.recyclerview.RecyclerViewHolder;
import com.sum.library.view.recyclerview.line.DividerItemDecoration;
import com.sum.library.view.recyclerview.sticky.StickRecyclerAdapter;
import com.sum.library.view.recyclerview.sticky.StickyHeadDecoration;

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
        List<RecyclerDataHolder<StickyItemModel>> holders = new ArrayList<>();
        for (StickyItemModel i : infos) {
            holders.add(new DataHolder(i));
        }
        mAdapter = new StickAdapter(this, holders);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new StickyHeadDecoration(mAdapter));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

    class StickAdapter extends StickRecyclerAdapter<StickyItemModel> {

        public StickAdapter(Context context) {
            super(context);
        }

        public StickAdapter(Context context, List<RecyclerDataHolder<StickyItemModel>> holders) {
            super(context, holders);
        }

        @Override
        public int layoutId() {
            return R.layout.sticky_head;
        }

        @Override
        public long headId(int position, StickyItemModel o) {
         /*   if (position == 0 || position == 1) {
                return -1;//负数，忽略不偏移
            }*/
            return o.headId;
        }

        @Override
        public void onBindHeaderViewHolder(View holder, int position, StickyItemModel o) {
            TextView name = (TextView) holder.findViewById(R.id.tv_title);
            StickyItemModel d = o;
            name.setText(d.name + " " + d.headId);
        }


        @Override
        public void firstHead(long headId, int position) {
        }
    }


    class DataHolder extends RecyclerDataHolder<StickyItemModel> {

        public DataHolder(StickyItemModel data) {
            super(data);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
//            View view = View.inflate(context, R.layout.sticky_item, null);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticky_item, parent, false);
            return new RecyclerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, final StickyItemModel data) {
            TextView name = (TextView) vHolder.itemView.findViewById(R.id.tv);
            name.setText("" + position + "->" + data.name);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.showToastShort(data.name);
                }
            });
        }
    }

}

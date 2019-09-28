package com.sum.andrioddeveloplibrary;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.sum.adapter.RecyclerDataHolder;
import com.sum.adapter.RecyclerViewHolder;
import com.sum.adapter.sticky.StickRecyclerAdapter;
import com.sum.adapter.sticky.StickyHeadDecoration;
import com.sum.andrioddeveloplibrary.model.StickyItemModel;

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
        mAdapter = new StickAdapter(holders);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new StickyHeadDecoration(mAdapter));
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

        public StickAdapter() {
            super();
        }

        public StickAdapter(List<RecyclerDataHolder<StickyItemModel>> holders) {
            super(holders);
        }

        @Override
        public int headLayoutId() {
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
        public int getItemViewLayoutId() {
            return R.layout.sticky_item;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(View contentView, int position) {
            return new RecyclerViewHolder(contentView);
        }

        @Override
        public void onBindViewHolder(int i, RecyclerView.ViewHolder viewHolder, StickyItemModel stickyItemModel) {
            TextView name = (TextView) viewHolder.itemView.findViewById(R.id.tv);
            name.setText("" + i + "->" + stickyItemModel.name);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showShort(stickyItemModel.name);
                }
            });
        }
    }

}

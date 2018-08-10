package com.sum.andrioddeveloplibrary.testActivity.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sum.adapter.RecyclerDataHolder;
import com.sum.adapter.RecyclerViewHolder;
import com.sum.andrioddeveloplibrary.R;

/**
 * Created by 365 on 2017/3/2.
 */

public class Demo2DataHolder extends RecyclerDataHolder<String> {

    public Demo2DataHolder(String data) {
        super(data);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.dataholder_item2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(View contentView, int position) {
        return new DemoViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(int i, RecyclerView.ViewHolder viewHolder, String s) {

    }

    //类似于ListView 里面的ViewHolder
    private class DemoViewHolder extends RecyclerViewHolder {


        public DemoViewHolder(View view) {
            super(view);
        }
    }
}

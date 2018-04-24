package com.sum.andrioddeveloplibrary.testActivity.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sum.andrioddeveloplibrary.R;
import com.sum.lib.rvadapter.RecyclerDataHolder;
import com.sum.lib.rvadapter.RecyclerViewHolder;

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

    //要实现RecyclerView中Item的View不一致
    //必须确保getType返回的值不一样
    //例如常见的首页不同楼层的排版不一致
    @Override
    public int getType() {
        return 2;
    }

    //类似于ListView 里面的ViewHolder
    private class DemoViewHolder extends RecyclerViewHolder {


        public DemoViewHolder(View view) {
            super(view);
        }
    }
}

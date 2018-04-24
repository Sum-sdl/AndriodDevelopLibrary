package com.sum.andrioddeveloplibrary.testActivity.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sum.andrioddeveloplibrary.R;
import com.sum.lib.rvadapter.RecyclerDataHolder;
import com.sum.lib.rvadapter.RecyclerViewHolder;

/**
 * Created by 365 on 2017/3/2.
 */

public class DemoDataHolder extends RecyclerDataHolder<String> {

    public DemoDataHolder(String data) {
        super(data);
    }

    @Override
    public int getItemViewLayoutId() {
       return R.layout.dataholder_item;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(View contentView, int position) {
        return new DemoViewHolder(contentView);
    }

    //onBindViewHolder 方法会每次Item可见的时候调用一次
    @Override
    public void onBindViewHolder(int i, RecyclerView.ViewHolder vHolder, String s) {
        DemoViewHolder holder = (DemoViewHolder) vHolder;
        holder.bind(s);
    }

    //要实现RecyclerView中Item的View不一致
    //必须确保getType返回的值不一样
    //例如常见的首页不同楼层的排版不一致
    @Override
    public int getType() {
        return super.getType();
    }

    //类似于ListView 里面的ViewHolder
    private class DemoViewHolder extends RecyclerViewHolder {

        private TextView textView;

        public DemoViewHolder(View view) {
            super(view);
            textView = findViewById(R.id.text);
        }

        public void bind(String text) {
            textView.setText(text);
        }
    }
}

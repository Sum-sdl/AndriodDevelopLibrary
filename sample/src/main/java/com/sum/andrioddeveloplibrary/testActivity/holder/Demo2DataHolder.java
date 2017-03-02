package com.sum.andrioddeveloplibrary.testActivity.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.sum.andrioddeveloplibrary.R;
import com.sum.library.view.recyclerview.RecyclerDataHolder;
import com.sum.library.view.recyclerview.RecyclerViewHolder;

/**
 * Created by 365 on 2017/3/2.
 */

public class Demo2DataHolder extends RecyclerDataHolder<String> {

    public Demo2DataHolder(String data) {
        super(data);
    }

    //要实现RecyclerView中Item的View不一致
    //必须确保getType返回的值不一样
    //例如常见的首页不同楼层的排版不一致
    @Override
    public int getType() {
        return 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int position) {
        return new DemoViewHolder(LayoutInflate(R.layout.dataholder_item2, context, parent));
    }

    //onBindViewHolder 方法会每次Item可见的时候调用一次
    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, String data) {
        DemoViewHolder holder = (DemoViewHolder) vHolder;
    }

    //类似于ListView 里面的ViewHolder
    private class DemoViewHolder extends RecyclerViewHolder {


        public DemoViewHolder(View view) {
            super(view);
        }
    }
}

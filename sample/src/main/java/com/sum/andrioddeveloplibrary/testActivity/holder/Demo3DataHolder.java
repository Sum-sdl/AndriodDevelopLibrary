package com.sum.andrioddeveloplibrary.testActivity.holder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sum.adapter.RecyclerDataHolder;
import com.sum.adapter.RecyclerViewHolder;
import com.sum.andrioddeveloplibrary.R;

/**
 * Created by 365 on 2017/3/2.
 */

public class Demo3DataHolder extends RecyclerDataHolder<String> {

    public Demo3DataHolder(String data) {
        super(data);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.dataholder_item3;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(View contentView, int position) {
        return  new DemoViewHolder(contentView);
    }

    //onBindViewHolder 方法会每次Item可见的时候调用一次
    @Override
    public void onBindViewHolder(int i, RecyclerView.ViewHolder viewHolder, String s) {
        DemoViewHolder holder = (DemoViewHolder) viewHolder;
        holder.bind(s);
    }

    //要实现RecyclerView中Item的View不一致
    //必须确保getType返回的值不一样
    //例如常见的首页不同楼层的排版不一致

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

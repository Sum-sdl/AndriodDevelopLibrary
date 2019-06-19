package com.sum.library_ui.utils;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sdl on 2017/12/19.
 * ViewPager PagerAdapter适配器
 */

public class ViewPagerImageViewAdapter<T extends ViewPagerImageViewAdapter.ImageData> extends PagerAdapter {

    public void setListener(onItemImageClick<T> mListener) {
        this.mListener = mListener;
    }

    //加载异常图片
    public void setNetErrorImageRes(int netErrorImageRes) {
        this.mNetErrorImageRes = netErrorImageRes;
    }

    public static abstract class ImageData {
        public abstract String getImageUrl();
    }

    public static class SimpleImageData extends ImageData {
        private String mUrl;

        public SimpleImageData(String imageUrl) {
            this.mUrl = imageUrl;
        }

        @Override
        public String getImageUrl() {
            return mUrl;
        }
    }

    public interface onItemImageClick<T> {
        void onItemClick(T item, int position);
    }

    private List<T> mData;
    private onItemImageClick<T> mListener;
    private int mNetErrorImageRes = -1;

    private ArrayList<ImageView> mCachedViews;

    public ViewPagerImageViewAdapter(List<T> mData) {
        this.mData = mData;
        mCachedViews = new ArrayList<>(1);
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @NonNull
    @Override
    public View instantiateItem(@NonNull ViewGroup container, final int position) {
        ImageView view;
        if (mCachedViews.size() > 0) {
            view = mCachedViews.get(0);
            mCachedViews.remove(0);
        } else {
            view = new ImageView(container.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        }

        if (mData != null) {
            String url = mData.get(position).getImageUrl();
            view.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onItemClick(mData.get(position), position);
                }
            });
            if (url != null) {
                RequestOptions error = RequestOptions.fitCenterTransform().error(mNetErrorImageRes);
                Glide.with(view.getContext()).asDrawable().load(url).apply(error).into(view);
            }
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ImageView content = (ImageView) object;
        container.removeView(content);
        if (!mCachedViews.contains(content) && mCachedViews.size() < 1) {//只能支持单个相同item缓存
            mCachedViews.add(content);
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}

package com.sum.library.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

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
            this.mUrl = mUrl;
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

    private ArrayList<SimpleDraweeView> mCachedViews;

    public ViewPagerImageViewAdapter(List<T> mData) {
        this.mData = mData;
        mCachedViews = new ArrayList<>(1);
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public View instantiateItem(@NonNull ViewGroup container, final int position) {
        SimpleDraweeView view;
        if (mCachedViews.size() > 0) {
            view = mCachedViews.get(0);
            mCachedViews.remove(0);
        } else {
            view = new SimpleDraweeView(container.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
            view.getHierarchy().setPlaceholderImage(mNetErrorImageRes);
            view.getHierarchy().setFailureImage(mNetErrorImageRes);
            view.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
        }

        if (mData != null) {
            String url = mData.get(position).getImageUrl();
            view.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onItemClick(mData.get(position), position);
                }
            });
            if (url != null) {
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).build();
                DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request).setAutoPlayAnimations(true).build();
                view.setController(draweeController);
            }
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        SimpleDraweeView content = (SimpleDraweeView) object;
        container.removeView(content);
        if (!mCachedViews.contains(content)) {//只能支持单个相同item缓存
            mCachedViews.add(content);
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}

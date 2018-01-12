package com.sum.library.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

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

    public interface onItemImageClick<T> {
        void onItemClick(T item, int position);
    }

    private List<T> mData;
    private onItemImageClick<T> mListener;
    private int mNetErrorImageRes = -1;

    public ViewPagerImageViewAdapter(List<T> mData) {
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }


    @Override
    public View instantiateItem(@NonNull ViewGroup container, final int position) {
        SimpleDraweeView view = new SimpleDraweeView(container.getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        if (mData != null) {
            String url = mData.get(position).getImageUrl();
            if (!TextUtils.isEmpty(url)) {
                view.setOnClickListener(v -> {
                    if (mListener != null) {
                        mListener.onItemClick(mData.get(position), position);
                    }
                });
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                        .build();
                DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(view.getController())
                        .setAutoPlayAnimations(true).build();
                if (mNetErrorImageRes != -1) {
                    view.getHierarchy().setFailureImage(mNetErrorImageRes);
                    view.getHierarchy().setPlaceholderImage(mNetErrorImageRes);
                }
                view.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
                view.setController(draweeController);
            }
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}

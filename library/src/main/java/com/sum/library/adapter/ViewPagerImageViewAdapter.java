package com.sum.library.adapter;

import android.net.Uri;
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

    public void setNetErrorImageRes(int mNetErrorImageRes) {
        this.mNetErrorImageRes = mNetErrorImageRes;
    }

    public static abstract class ImageData {
        public abstract String getImageUrl();
    }

    public interface onItemImageClick<T> {
        void onItemClick(T item);
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
    public View instantiateItem(ViewGroup container, final int position) {

        SimpleDraweeView view = new SimpleDraweeView(container.getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        if (mData != null) {
            String url = mData.get(position).getImageUrl();
            if (!TextUtils.isEmpty(url)) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onItemClick(mData.get(position));
                        }
                    }
                });
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
//                .setResizeOptions(new ResizeOptions(SizeUtils.dp2px(72), SizeUtils.dp2px(72)))
                        .build();
                DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request).setAutoPlayAnimations(true).build();
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

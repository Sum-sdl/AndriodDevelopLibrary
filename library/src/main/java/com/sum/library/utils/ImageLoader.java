package com.sum.library.utils;

import android.text.TextUtils;
import android.widget.ImageView;

import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.sum.library.R;

/**
 * Created by sdl on 2018/7/27.
 */
public class ImageLoader {

    public static int mPlaceResId = R.drawable.pub_imageload_bg;
    public static int mErrorResId = R.drawable.pub_imageload_bg;

    public static void loadImage(ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url)) {
            loadImage(imageView, url, false, mPlaceResId, mErrorResId);
        }
    }

    public static void loadImage(ImageView imageView, String url, boolean isCircle) {
        if (!TextUtils.isEmpty(url)) {
            loadImage(imageView, url, isCircle, mPlaceResId, mErrorResId);
        }
    }

    public static void loadImage(ImageView imageView, String url, boolean isCircle, int placeholderResId, int errorResId) {

        RequestOptions options;
        if (isCircle) {
            options = RequestOptions.circleCropTransform();
        } else {
            options = RequestOptions.centerCropTransform();
        }

        RequestOptions format = options
                .placeholder(placeholderResId)
                .error(errorResId)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .format(DecodeFormat.PREFER_RGB_565);

        Glide.with(imageView.getContext())
                .load(url)// A file path, or a uri or url handled by
                .apply(format)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
    }

    /**
     * 清除glide内存缓存，必须运行在主线程
     */
    public static void clearMemoryCache() {
        Glide.get(Utils.getApp()).clearMemory();
    }

    /**
     * 清除glide磁盘缓存，必须运行在子线程
     */
    public static void clearDiskCache() {
        Glide.get(Utils.getApp()).clearDiskCache();
    }
}

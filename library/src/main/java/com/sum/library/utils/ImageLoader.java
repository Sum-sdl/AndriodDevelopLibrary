package com.sum.library.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by sdl on 2018/7/27.
 */
public class ImageLoader {

    private ImageLoader() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static class Builder {
        private ImageView imageView;
        private String imageUrl;
        private int placeholderResId;
        private int errorResId;
        private boolean hasCrossFade = true;
        private int duration = 400;
        private boolean isCircle = false;
        private RequestListener<Drawable> listener;

        public Builder into(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        public Builder setUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder placeholder(int placeholderResId) {
            this.placeholderResId = placeholderResId;
            return this;
        }

        public Builder error(int errorResId) {
            this.errorResId = errorResId;
            return this;
        }

        public Builder hasCrossFade(boolean hasCrossFade) {
            this.hasCrossFade = hasCrossFade;
            return this;
        }

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder isCircle(boolean isCircle) {
            this.isCircle = isCircle;
            return this;
        }

        public Builder listener(RequestListener<Drawable> listener) {
            this.listener = listener;
            return this;
        }

        public void load() {
            if (imageView != null && imageUrl != null && !"".equals(imageUrl)) {
                RequestOptions ro = new RequestOptions();
                if (placeholderResId != 0) ro.placeholder(placeholderResId);
                if (errorResId != 0) ro.error(errorResId);
                if (isCircle) ro.circleCrop();
                else ro.centerCrop();
                ro.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                if (hasCrossFade) {
                    if (listener != null) {
                        Glide.with(imageView.getContext())
                                .load(imageUrl)
                                .apply(ro)
                                .transition(new DrawableTransitionOptions().crossFade(duration))
                                .listener(listener)
                                .into(imageView);
                    } else {
                        Glide.with(imageView.getContext())
                                .load(imageUrl)
                                .apply(ro)
                                .transition(new DrawableTransitionOptions().crossFade(duration))
                                .into(imageView);
                    }
                } else {
                    if (listener != null) {
                        Glide.with(imageView.getContext())
                                .load(imageUrl)
                                .apply(ro)
                                .transition(new DrawableTransitionOptions().dontTransition())
                                .listener(listener)
                                .into(imageView);
                    } else {
                        Glide.with(imageView.getContext())
                                .load(imageUrl)
                                .apply(ro)
                                .transition(new DrawableTransitionOptions().dontTransition())
                                .into(imageView);
                    }
                }
            }
        }
    }

    /**
     * 清除glide内存缓存，必须运行在主线程
     *
     * @param context 当前上下文
     */
    public static void clearMemoryCache(Context context) {
        Glide.get(context).clearMemory();
    }

    /**
     * 清除glide磁盘缓存，必须运行在子线程
     *
     * @param context 当前上下文
     */
    public static void clearDiskCache(Context context) {
        Glide.get(context).clearDiskCache();
    }
}

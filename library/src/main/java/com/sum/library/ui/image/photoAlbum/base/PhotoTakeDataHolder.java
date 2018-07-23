package com.sum.library.ui.image.photoAlbum.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ScreenUtils;
import com.sum.lib.rvadapter.RecyclerDataHolder;
import com.sum.lib.rvadapter.RecyclerViewHolder;
import com.sum.library.R;
import com.sum.library.ui.image.photoAlbum.AlbumInfo;
import com.sum.library.ui.image.photoAlbum.PhotoAlbumListener;

/**
 * Created by sdl on 2018/1/12.
 * 拍照
 */

public class PhotoTakeDataHolder extends RecyclerDataHolder<Photo> {

    private PhotoAlbumListener mListener;

    private AlbumInfo mAlbumInfo;

    public PhotoTakeDataHolder(AlbumInfo data, PhotoAlbumListener listener) {
        super(null);
        mListener = listener;
        mAlbumInfo = data;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.vh_image_take_item;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(View contentView, int position) {
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(int i, RecyclerView.ViewHolder viewHolder, Photo photo) {

    }

    class ViewHolder extends RecyclerViewHolder {

        private ViewHolder(View view) {
            super(view);

            View take = view.findViewById(R.id.iv_take_photo);
            take.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onTakePhotoClick();
                }
            });
            int span_count = mAlbumInfo.span_count;
            int screenWidth = ScreenUtils.getScreenWidth();
            int width = screenWidth / span_count;
            int height = width;
            ViewGroup.LayoutParams params = take.getLayoutParams();
            params.width = width;
            params.height = height;
            take.setLayoutParams(params);
        }

    }
}

package com.sum.library.ui.image.photoAlbum.base;

import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.sum.lib.rvadapter.RecyclerDataHolder;
import com.sum.lib.rvadapter.RecyclerViewHolder;
import com.sum.library.R;
import com.sum.library.ui.image.photoAlbum.AlbumInfo;
import com.sum.library.ui.image.photoAlbum.PhotoAlbumListener;

/**
 * Created by sdl on 2018/1/12.
 */

public class PhotoDataHolder extends RecyclerDataHolder<Photo> {

    private PhotoAlbumListener mListener;

    public AlbumInfo albumInfo;

    public PhotoDataHolder(Photo data, PhotoAlbumListener listener) {
        super(data);
        mListener = listener;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.vh_image_item;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(View contentView, int position) {
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(int i, RecyclerView.ViewHolder viewHolder, Photo photo) {
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.onBindData(photo);
    }

    class ViewHolder extends RecyclerViewHolder implements View.OnClickListener {
        ImageView mDView;
        ImageView mState;
        View mBgView;
        Photo mData;

        private ViewHolder(View view) {
            super(view);
            mBgView = view.findViewById(R.id.view_bg);
            mDView = view.findViewById(R.id.vh_album_image);
            mDView.setOnClickListener(this);
            //计算view大小
            int span_count = albumInfo.span_count;
            int screenWidth = ScreenUtils.getScreenWidth();
            int width = screenWidth / span_count;
            int height = width + 1;
            ViewGroup.LayoutParams params = mDView.getLayoutParams();
            params.width = width;
            params.height = height;
            mDView.setLayoutParams(params);
            mBgView.setLayoutParams(params);

            mState = view.findViewById(R.id.vh_album_selected);
            mState.setOnClickListener(this);
        }

        private void onBindData(Photo photo) {
            if (mData == photo) {
                updateUI();
                return;
            }
            mData = photo;

            RequestOptions ro = RequestOptions.centerCropTransform()
                    .diskCacheStrategy(DiskCacheStrategy.NONE);

            Glide.with(mContext)
                    .load(photo.getPath())
                    .apply(ro)
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(mDView);
            updateUI();
        }

        private void updateUI() {
            if (mData.getSelected()) {
                if (albumInfo.choose_tint_nor_res_id != -1) {
                    mState.setImageResource(albumInfo.choose_tint_nor_res_id);
                } else {
                    mState.setImageResource(R.mipmap.lib_ic_selected);
                }
                DrawableCompat.setTint(mState.getDrawable(), albumInfo.choose_tint_sel);
                mBgView.setVisibility(View.VISIBLE);
            } else {
                if (albumInfo.choose_tint_sel_res_id != -1) {
                    mState.setImageResource(albumInfo.choose_tint_sel_res_id);
                } else {
                    mState.setImageResource(R.mipmap.lib_ic_select);
                }
                DrawableCompat.setTint(mState.getDrawable(), albumInfo.choose_tint_nor);
                mBgView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.vh_album_image) {
                mListener.onImageClick(mData, getAdapterPosition());
            } else if (v.getId() == R.id.vh_album_selected) {
                mListener.onChooseClick(mData, getAdapterPosition());
            }
        }
    }
}

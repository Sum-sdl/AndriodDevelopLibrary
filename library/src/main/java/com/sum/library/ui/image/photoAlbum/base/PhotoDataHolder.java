package com.sum.library.ui.image.photoAlbum.base;

import android.net.Uri;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.ScreenUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
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
    public int getType() {
        return 1;
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
        SimpleDraweeView mDView;
        ImageView mState;
        View mBgView;
        Photo mData;

        private int mSize = 0;

        private ViewHolder(View view) {
            super(view);
            mBgView = view.findViewById(R.id.view_bg);
            mDView = view.findViewById(R.id.vh_album_image);
            mDView.setOnClickListener(this);
            //计算view大小
            int default_space = albumInfo.default_space;
            int span_count = albumInfo.span_count;
            int screenWidth = ScreenUtils.getScreenWidth();
            int width = screenWidth / span_count;
            int height = width;
            ViewGroup.LayoutParams params = mDView.getLayoutParams();
            params.width = width;
            params.height = height;
            mDView.setLayoutParams(params);
            mSize = height;
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
            String path = photo.getPath();
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse("file://" + path))
                    .setResizeOptions(new ResizeOptions(mSize, mSize))
                    .build();
            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(mDView.getController())
                    .setAutoPlayAnimations(true).build();
            mDView.setController(draweeController);

            updateUI();
        }

        private void updateUI() {
            if (mData.getSelected()) {
                if (albumInfo.choose_tint_nor_res_id != -1) {
                    mState.setImageResource(albumInfo.choose_tint_nor_res_id);
                } else {
                    mState.setImageResource(R.mipmap.ic_selected);
                }
                DrawableCompat.setTint(mState.getDrawable(), albumInfo.choose_tint_sel);
                mBgView.setVisibility(View.VISIBLE);
            } else {
                if (albumInfo.choose_tint_sel_res_id != -1) {
                    mState.setImageResource(albumInfo.choose_tint_sel_res_id);
                } else {
                    mState.setImageResource(R.mipmap.ic_select);
                }
                DrawableCompat.setTint(mState.getDrawable(), albumInfo.choose_tint_nor);
                mBgView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.vh_album_image) {
                mListener.onImageClick(mData, getAdapterPosition());
//                mListener.onChooseClick(mData, getAdapterPosition());
            } else if (v.getId() == R.id.vh_album_selected) {
                mListener.onChooseClick(mData, getAdapterPosition());
            }
        }
    }
}

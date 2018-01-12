package com.sum.library.ui.image.photoAlbum.base;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.SizeUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.sum.library.R;
import com.sum.library.ui.image.photoAlbum.PhotoAlbumListener;
import com.sum.library.view.recyclerview.RecyclerDataHolder;
import com.sum.library.view.recyclerview.RecyclerViewHolder;

/**
 * Created by sdl on 2018/1/12.
 */

public class PhotoDataHolder extends RecyclerDataHolder<Photo> {

    private PhotoAlbumListener mListener;

    public PhotoDataHolder(Photo data, PhotoAlbumListener listener) {
        super(data);
        mListener = listener;
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.vh_image_item;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(View contentView, int position) {
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(Context context, int position, RecyclerView.ViewHolder vHolder, Photo data) {
        ViewHolder holder = (ViewHolder) vHolder;
        holder.onBindData(data);
    }

    class ViewHolder extends RecyclerViewHolder implements View.OnClickListener {
        SimpleDraweeView mDView;
        ImageView mState;
        Photo mData;

        private ViewHolder(View view) {
            super(view);
            mDView = view.findViewById(R.id.vh_album_image);
            mDView.setOnClickListener(this);
            mState = view.findViewById(R.id.vh_album_selected);
            mState.setOnClickListener(this);
        }

        private void onBindData(Photo photo) {
            mData = photo;
            String path = photo.getPath();
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse("file://" + path))
                    .setResizeOptions(new ResizeOptions(SizeUtils.dp2px(115), SizeUtils.dp2px(115))).build();
            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(mDView.getController())
                    .setAutoPlayAnimations(true).build();
            mDView.setController(draweeController);

            if (photo.getSelected()) {
                mState.setImageResource(R.mipmap.ic_choice_select);
            } else {
                mState.setImageResource(R.mipmap.ic_choice_normal);
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

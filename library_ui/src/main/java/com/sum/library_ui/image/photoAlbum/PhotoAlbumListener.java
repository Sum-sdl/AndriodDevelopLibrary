package com.sum.library_ui.image.photoAlbum;

import com.sum.library_ui.image.photoAlbum.base.Photo;

/**
 * Created by sdl on 2018/1/12.
 */

public interface PhotoAlbumListener {

     void onTakePhotoClick();

     void onChooseClick(Photo photo, int position);

     void onImageClick(Photo photo, int position);

}

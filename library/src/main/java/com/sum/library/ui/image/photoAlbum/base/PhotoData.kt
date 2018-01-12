package com.sum.library.ui.image.photoAlbum.base

/**
 * Created by sdl on 2018/1/12.
 */
data class Photo(var id: Int?, var path: String?, var selected: Boolean)

data class PhotoDirectory(var id: String?, var name: String?, var coverPath: String?, var date: Long?) {

    private var photos: ArrayList<Photo>? = null

    fun addPhoto(id: Int, path: String) {
        if (photos == null) {
            photos = arrayListOf()
        }
        photos?.add(Photo(id, path, false))
    }

    fun getAllPhotos(): ArrayList<Photo> {
        if (photos == null) {
            photos = arrayListOf()
        }
        return photos as ArrayList<Photo>
    }
}
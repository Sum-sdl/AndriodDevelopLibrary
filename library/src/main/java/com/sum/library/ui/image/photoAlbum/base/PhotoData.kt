package com.sum.library.ui.image.photoAlbum.base

/**
 * Created by sdl on 2018/1/12.
 */
data class Photo(var id: Int?, var path: String?, var selected: Boolean)

data class PhotoDirectory(var id: String, var name: String, var coverPath: String?, var date: Long?) {

    private val photos: ArrayList<Photo> by lazy {
        arrayListOf<Photo>()
    }

    fun addPhoto(id: Int, path: String) {
        photos.add(Photo(id, path, false))
    }

    fun addAllPhoto(photo: ArrayList<Photo>) {
        photos.addAll(photo)
    }

    fun getAllPhotos(): ArrayList<Photo> {
        return photos
    }
}

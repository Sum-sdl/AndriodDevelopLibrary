package com.sum.library_ui.image.photoAlbum.base

import java.io.Serializable

/**
 * Created by sdl on 2018/1/12.
 */
data class Photo(var id: Int?, var path: String, var selected: Boolean) : Serializable

data class PhotoDirectory(var id: String, var name: String, var coverPath: String?, var date: Long?) : Serializable {

    private val photos: ArrayList<Photo> by lazy {
        arrayListOf<Photo>()
    }

    fun addPhoto(id: Int, path: String): Photo {
        val photo = Photo(id, path, false)
        photos.add(photo)
        return photo
    }

    fun addAllPhoto(photo: ArrayList<Photo>) {
        photos.addAll(photo)
    }

    fun getAllPhotos(): ArrayList<Photo> {
        return photos
    }
}

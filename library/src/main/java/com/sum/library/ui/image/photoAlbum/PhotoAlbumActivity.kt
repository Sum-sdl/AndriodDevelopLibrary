package com.sum.library.ui.image.photoAlbum

import android.database.Cursor
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.sum.library.R
import com.sum.library.app.BaseActivity
import com.sum.library.ui.image.photoAlbum.base.Photo
import com.sum.library.ui.image.photoAlbum.base.PhotoDataHolder
import com.sum.library.ui.image.photoAlbum.base.PhotoDirectory
import com.sum.library.ui.image.photoAlbum.base.PhotoDirectoryLoader
import com.sum.library.ui.image.preview.ImagePreviewActivity
import com.sum.library.view.recyclerview.RecyclerAdapter
import com.sum.library.view.recyclerview.RecyclerDataHolder
import com.sum.library.view.widget.PubTitleView
import kotlinx.android.synthetic.main.activity_album_photo.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by sdl on 2018/1/12.
 * 相册界面
 */
class PhotoAlbumActivity : BaseActivity(), PhotoAlbumListener {

    override fun getLayoutId(): Int = R.layout.activity_album_photo

    override fun statusBarColor(): Int = Color.WHITE

    private lateinit var mTitle: PubTitleView

    private var mAllFile: LinkedHashMap<String, PhotoDirectory>? = null

    private var mMaxNum = 9

    private lateinit var mChoosePhoto: ArrayList<Photo>

    override fun initParams() {
        mChoosePhoto = arrayListOf()
        mTitle = findViewById(R.id.pub_title_view)
        mAdapter = RecyclerAdapter<Any>(this)
        rv_images.adapter = mAdapter
        rv_images.setHasFixedSize(true)
        rv_images.layoutManager = GridLayoutManager(this, 3)
        rv_images.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect?.left = SizeUtils.dp2px(3f)
                outRect?.bottom = SizeUtils.dp2px(3f)
                if (parent?.getChildLayoutPosition(view)!! % 3 == 0) {
                    outRect?.left = 0
                }
            }
        })
    }

    override fun loadData() {
        loadDirectory()
    }

    private fun loadDirectory() {
        supportLoaderManager.initLoader(0, null, object : LoaderManager.LoaderCallbacks<Cursor> {
            override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
                if (data == null) return
                val hashMap = LinkedHashMap<String, PhotoDirectory>()
                val photoDirectoryAll = PhotoDirectory("all", "所有图片", null, null)
                while (data.moveToNext()) {
                    val imageId = data.getInt(data.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID))
                    val bucketId = data.getString(data.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_ID))
                    val name = data.getString(data.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME))
                    val path = data.getString(data.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA))
                    val file = File(path)
                    if (file.exists() && file.length() > 0) {
                    } else {
                        continue
                    }
//                    Log.d("PhotoPickerActivity", "$bucketId ,$name, $path")
                    if (!hashMap.containsKey(bucketId)) {
                        val photoDirectory = PhotoDirectory(bucketId, name, path, data.getLong(data.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_ADDED)))
                        photoDirectory.addPhoto(imageId, path)
                        hashMap.put(bucketId, photoDirectory)
                    } else {
                        hashMap[bucketId]?.addPhoto(imageId, path)
                    }
                    photoDirectoryAll.addPhoto(imageId, path)
                }
                if (photoDirectoryAll.getAllPhotos().size > 0) {
                    photoDirectoryAll.coverPath = photoDirectoryAll.getAllPhotos()[0].path
                }
                hashMap.put("all", photoDirectoryAll)
                mAllFile = hashMap
                showImages()
            }

            override fun onLoaderReset(loader: Loader<Cursor>?) {

            }

            override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
                return PhotoDirectoryLoader(this@PhotoAlbumActivity, false)
            }
        })
    }

    private fun showImages() {
        if (mAllFile == null) return

        showDirectoryKey("all")
    }

    private fun showDirectoryKey(key: String) {
        val directory = mAllFile?.get(key) ?: return
        tv_album_file.text = directory.name
        val holders = ArrayList<RecyclerDataHolder<*>>()
        directory.getAllPhotos().forEach {
            holders.add(PhotoDataHolder(it, this))
        }
        mAdapter.setDataHolders(holders)
    }

    override fun onChooseClick(photo: Photo, position: Int) {
        if (!photo.selected) {//添加
            if (mChoosePhoto.size < mMaxNum) {
                photo.selected = true
                mChoosePhoto.add(photo)
                mAdapter.notifyItemChanged(position)
            } else {
                ToastUtils.showShort("最多可选 $mMaxNum 个")
            }
        } else {
            mChoosePhoto.remove(photo)
            photo.selected = false
            mAdapter.notifyItemChanged(position)
        }
    }

    override fun onImageClick(photo: Photo?, position: Int) {
        ImagePreviewActivity.open(this, photo?.path)
    }

    override fun onTakePhotoClick() {
        ToastUtils.showShort("拍照")
    }
}
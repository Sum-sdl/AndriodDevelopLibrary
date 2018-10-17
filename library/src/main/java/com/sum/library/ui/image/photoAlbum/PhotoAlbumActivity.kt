package com.sum.library.ui.image.photoAlbum

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.ListPopupWindow
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.sum.adapter.RecyclerAdapter
import com.sum.adapter.RecyclerDataHolder
import com.sum.library.R
import com.sum.library.app.BaseActivity
import com.sum.library.ui.image.AppImageUtils
import com.sum.library.ui.image.photoAlbum.base.*
import com.sum.library.ui.image.preview.ImagePreviewActivity
import com.sum.library.view.widget.PubTitleView
import kotlinx.android.synthetic.main.ui_activity_album_photo.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by sdl on 2018/1/12.
 * 相册界面
 */
class PhotoAlbumActivity : BaseActivity(), PhotoAlbumListener {

    companion object {
        fun open(activity: Activity, info: AlbumInfo) {
            val intent = Intent(activity, PhotoAlbumActivity::class.java)
            intent.putExtra("data", info)
            activity.startActivityForResult(intent, info.request_code)
        }
    }


    override fun getLayoutId(): Int = R.layout.ui_activity_album_photo

    override fun statusBarColor(): Int = Color.WHITE

    private lateinit var mTitle: PubTitleView

    private var mAllFile: HashMap<String, PhotoDirectory>? = null

    private var mMaxNum = 9

    private var mTakePhotoOpen = false
    private var mTakePhotoFile: File? = null

    private lateinit var mChoosePhoto: ArrayList<Photo>

    private lateinit var mRightView: TextView

    private lateinit var mPopupWindow: ListPopupWindow
    private val mDictData = arrayListOf<PhotoDirectory>()

    private lateinit var mAlbumInfo: AlbumInfo

    //当前勾选的文件夹
    private var mCurDict: String = ""

    private var TAG_ALL: String = "所有图片"

    private lateinit var mAdapter: RecyclerAdapter<RecyclerDataHolder<*>>

    override fun onCreate(savedInstanceState: Bundle?) {
        mHasLoadFinish = false
        if (savedInstanceState != null) {
            try {
                mHasLoadFinish = savedInstanceState.getBoolean("load_finish", false)
            } catch (e: Exception) {
            }
        }
        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean("load_finish", mHasLoadFinish)
    }


    override fun initParams() {
        mChoosePhoto = arrayListOf()
        mTitle = findViewById(R.id.pub_title_view)
        mRightView = mTitle.addRightTextButton("完成", View.OnClickListener {
            rightBtnClick()
        })

        var data = intent.getSerializableExtra("data")
        if (data != null && data is AlbumInfo) {
            mAlbumInfo = data
        } else {
            data = AlbumInfo()
            mAlbumInfo = data
        }
        mMaxNum = data.max_count
        mTakePhotoOpen = data.take_photo_open

        mAdapter = RecyclerAdapter()
        rv_images.adapter = mAdapter
        rv_images.setHasFixedSize(true)
        rv_images.layoutManager = GridLayoutManager(this, data.span_count)
        rv_images.addItemDecoration(object : RecyclerView.ItemDecoration() {

            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.left = data.default_space
                outRect.bottom = data.default_space
                val loc = parent.getChildLayoutPosition(view) % data.span_count
                if (loc == 0) {
                    outRect.left = 0
                }
            }
        })

        mPopupWindow = ListPopupWindow(this)
        mPopupWindow.width = ListPopupWindow.MATCH_PARENT
        mPopupWindow.anchorView = tv_album_file
        mPopupWindow.isModal = true
        mPopupWindow.setDropDownGravity(Gravity.TOP)
        mPopupWindow.setOnDismissListener {
            val params = window.attributes
            params.alpha = 1f
            window.attributes = params

        }
        mPopupWindow.setOnItemClickListener { _, _, position, _ ->
            val type = mDictData[position].name
            tv_album_file.text = type
            mPopupWindow.dismiss()
            showDirectoryKey(type)
        }

        tv_album_file.setOnClickListener {
            if (mPopupWindow.isShowing) {
                mPopupWindow.dismiss()
            } else if (!isFinishing) {
                val params = window.attributes
                params.alpha = 0.5f
                window.attributes = params

                mPopupWindow.show()
            }
        }
        tv_album_preview.setOnClickListener {
            if (mChoosePhoto.size > 0) {
                val list = arrayListOf<String>()
                mChoosePhoto.forEach {
                    list.add(it.path)
                }
                ImagePreviewActivity.open(this, list)
            }
        }

        updateBtn()
    }

    private var mHasLoadFinish = false

    override fun loadData() {
        loadDirectory()
    }

    private fun loadDirectory() {
        supportLoaderManager.initLoader(0, null, object : LoaderManager.LoaderCallbacks<Cursor> {
            override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
                if (data == null)
                    return
                if (mHasLoadFinish) {
                    return
                }
                mHasLoadFinish = true
                val hashMap = LinkedHashMap<String, PhotoDirectory>()
                val photoAll = PhotoDirectory(TAG_ALL, TAG_ALL, null, null)
                hashMap[photoAll.id] = photoAll
                mAllFile = hashMap

                //add
                while (data.moveToNext()) {
                    val imageId = data.getInt(data.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID))
//                    val bucketId = data.getString(data.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_ID))
                    val name = data.getString(data.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME))
                    val bucketId = name
                    val path = data.getString(data.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA))
                    val file = File(path)
                    if (!file.exists() || file.length() <= 0 || TextUtils.isEmpty(name)) {
                        continue
                    }
                    if (!hashMap.containsKey(bucketId)) {
                        val photoDirectory = PhotoDirectory(bucketId, name, path, data.getLong(data.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_ADDED)))
                        photoDirectory.addPhoto(imageId, path)
                        hashMap.put(bucketId, photoDirectory)
                    } else {
                        hashMap[bucketId]?.addPhoto(imageId, path)
                    }
                }
                //add total
                hashMap.entries.forEach {
                    if (photoAll != it.value) {
                        photoAll.addAllPhoto(it.value.getAllPhotos())
                    }
                }
                if (photoAll.getAllPhotos().size > 0) {
                    photoAll.coverPath = photoAll.getAllPhotos()[0].path
                }


                //文件夹数据
                mDictData.clear()
                hashMap.entries.forEach {
                    mDictData.add(it.value)
                }
                mPopupWindow.setAdapter(DictAdapter(this@PhotoAlbumActivity, mDictData))
                if (mDictData.size > 0) {
                    val count = if (mDictData.size > 4) 4 else mDictData.size
                    mPopupWindow.height = count * SizeUtils.dp2px(90f)
                }

                //回复上次选中的文件状态
                if (mChoosePhoto.size > 0) {
                    val selected = arrayListOf<Photo>()
                    val allPhotos = photoAll.getAllPhotos()
                    for (photo in allPhotos) {
                        mChoosePhoto.forEach {
                            if (photo.path == it.path) {
                                photo.selected = true
                                selected.add(photo)
                            }
                        }
                    }
                    mChoosePhoto.clear()
                    mChoosePhoto.addAll(selected)
                }

                showImages()
            }

            override fun onLoaderReset(loader: Loader<Cursor>) {

            }

            override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
                return PhotoDirectoryLoader(this@PhotoAlbumActivity, false)
            }

        })
    }

    private fun showImages() {
        showDirectoryKey(TAG_ALL)
    }

    private fun showDirectoryKey(key: String) {
        val directory = mAllFile?.get(key) ?: return
        if (mCurDict == key) {
            return
        }
        mCurDict = key
        tv_album_file.text = directory.name
        val holders = ArrayList<RecyclerDataHolder<*>>()

        if (mTakePhotoOpen && key == TAG_ALL) {
            holders.add(PhotoTakeDataHolder(mAlbumInfo, this))
        }
        directory.getAllPhotos().forEach {
            val item = PhotoDataHolder(it, this)
            item.albumInfo = mAlbumInfo
            holders.add(item)
        }
        mAdapter.dataHolders = holders
    }

    //完成按钮点击
    private fun rightBtnClick() {
        if (mChoosePhoto.size > 0) {
            val data = Intent()
            val list = arrayListOf<String>()
            mChoosePhoto.forEach {
                list.add(it.path)
            }
            data.putExtra("images", list)
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }


    private fun updateBtn() {
        val size = mChoosePhoto.size
        if (size == 0) {
            mRightView.setTextColor(getColorByResId(R.color.pub_title_text_right_color))
            if (mAlbumInfo.max_count == 1) {
                mRightView.text = ""
                tv_album_preview.text = ""
            } else {
                mRightView.text = "完成"
                tv_album_preview.text = "预览"
            }
        } else {
            mRightView.setTextColor(mAlbumInfo.choose_tint_sel)
            if (mAlbumInfo.max_count == 1) {
                mRightView.text = "确定"
                tv_album_preview.text = "点击预览"
            } else {
                mRightView.text = getString(R.string.photo_finish, size, mMaxNum)
                tv_album_preview.text = getString(R.string.photo_preview, size)
            }
        }
    }

    override fun onChooseClick(photo: Photo, position: Int) {
        if (!photo.selected) {//添加
            if (mChoosePhoto.size < mMaxNum) {
                photo.selected = true
                mChoosePhoto.add(photo)
                mAdapter.notifyItemChanged(position)
            } else {
                ToastUtils.showShort("最多可选 $mMaxNum 张图片")
            }
        } else {
            mChoosePhoto.remove(photo)
            photo.selected = false
            mAdapter.notifyItemChanged(position)
        }
        updateBtn()
    }

    override fun onImageClick(photo: Photo, position: Int) {
        if (mAlbumInfo.need_item_fast_preview) {
            AppImageUtils.appImagePreview(this, photo.path)
        } else {
            onChooseClick(photo, position)
        }
    }

    override fun onTakePhotoClick() {//系统拍照
        mTakePhotoFile = AppImageUtils.systemTakePhoto(this, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == 101) {//系统拍照
            AppImageUtils.appRefreshAlbum(mTakePhotoFile?.path)
            if (mTakePhotoFile != null && mTakePhotoFile!!.exists()) {
                addTakePic()
            }
//            else {
//                loadData()
//            }
        }
    }

    private fun addTakePic() {
        if (mTakePhotoOpen) {//在第一个位置添加图片
            val directory = mAllFile?.get(TAG_ALL)
            val photo = directory?.addPhoto(1, mTakePhotoFile!!.path)
            val holder = PhotoDataHolder(photo, this)
            holder.albumInfo = mAlbumInfo
            mAdapter.addDataHolder(1, holder)
        }
    }
}
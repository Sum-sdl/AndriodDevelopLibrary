package com.sum.andrioddeveloplibrary

import android.annotation.TargetApi
import android.app.Activity
import android.app.SharedElementCallback
import android.content.Intent
import android.graphics.Matrix
import android.graphics.RectF
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.transition.AutoTransition
import android.transition.Transition
import android.view.View
import androidx.core.view.ViewCompat
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ToastUtils
import com.sum.andrioddeveloplibrary.App.BaseAppActivity
import com.sum.library.storage.AppFileStorage
import com.sum.library.utils.LiveDataEventBus
import com.sum.library.utils.Logger
import com.sum.library_ui.camera.CameraActivity
import com.sum.library_ui.image.AppImageUtils
import com.sum.library_ui.image.photoAlbum.AlbumInfo
import com.sum.library_ui.utils.ImageLoader
import kotlinx.android.synthetic.main.activity_ui.*
import top.zibin.luban.OnCompressListener
import java.io.File


class LibUIActivity : BaseAppActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImageLoader.loadImage(
            iv_2,
            "http://img31.house365.com/M02/01/72/rBEBYFTTb52AKGnpAAGRhUbP6bI584.jpg"
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.sharedElementReenterTransition = AutoTransition()
            postponeEnterTransition()
            ViewCompat.setTransitionName(iv_2, "IMG_TRANSITION")
            startPostponedEnterTransition()
        }
        //
//        LiveDataEventBus.with("preview_image_drag_start").observeForever {
//            Logger.e("preview_image_drag_start:" + it)
//            iv_2.alpha = 1f
//        }
        LiveDataEventBus.with("preview_page_selected", Int::class.java).observeForever {
            Logger.e("preview_page_selected:" + it)
        }
    }

    override fun finishAfterTransition() {
        val data = Intent()
        data.putExtra("pos", 2)
        data.putExtra("tag", "IMG_TRANSITION")
        setResult(Activity.RESULT_OK, data)
        super.finishAfterTransition()
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun addTransitionListener() {
        val transition = window.sharedElementEnterTransition
        transition?.addListener(object : Transition.TransitionListener {
            override fun onTransitionResume(p0: Transition?) {
            }

            override fun onTransitionPause(p0: Transition?) {
            }

            override fun onTransitionCancel(p0: Transition?) {
            }

            override fun onTransitionStart(p0: Transition?) {

            }

            override fun onTransitionEnd(p0: Transition?) {
                transition.removeListener(this)
            }

        })
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun dealShareFame() {
        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>?,
                sharedElements: MutableMap<String, View>?
            ) {
                super.onMapSharedElements(names, sharedElements)
                //处理转场后返回后，需要对接的view
                //可以处理图片切换后，当前的图片位置
                Logger.e("onMapSharedElements back" + names.toString())

//                sharedElements?.remove("share_photo")
            }

            override fun onCaptureSharedElementSnapshot(
                sharedElement: View,
                viewToGlobalMatrix: Matrix?,
                screenBounds: RectF?
            ): Parcelable? {
                //sharedElement 本页面指定共享元素动画的view
                Logger.e("onCaptureSharedElementSnapshot")
                //以下代码已经没必要设置，因为demo中的动画效果已经全部设置在了rv_item_fake_iv上
                //解决执行共享元素动画的时候，一开始显示空白的问题
                sharedElement.alpha = 1f
                return super.onCaptureSharedElementSnapshot(
                    sharedElement,
                    viewToGlobalMatrix,
                    screenBounds
                )
            }

            override fun onSharedElementStart(
                sharedElementNames: MutableList<String>?,
                sharedElements: MutableList<View>?,
                sharedElementSnapshots: MutableList<View>?
            ) {
                super.onSharedElementStart(
                    sharedElementNames,
                    sharedElements,
                    sharedElementSnapshots
                )
                Logger.e("onSharedElementStart")
            }

            override fun onSharedElementEnd(
                sharedElementNames: MutableList<String>?,
                sharedElements: MutableList<View>?,
                sharedElementSnapshots: MutableList<View>?
            ) {
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots)
                Logger.e("onSharedElementEnd")
            }

        })
    }


    override fun initParams() {
        dealShareFame()
        btn_1.setOnClickListener {
            val list = arrayListOf<String>()
            list.add(mTake)
            list.addAll(mData)
            list.addAll(getAll())
            //通过一个同样大小的共享空白view，解决白屏问题
            AppImageUtils.appImagePreview(this, list, 3, iv_2_fake)
        }

        //系统相册
        btn_2.setOnClickListener {
            AppImageUtils.systemChooseImage(this, 1)
        }

        //系统拍照
        btn_3.setOnClickListener {
            mPhoto = AppImageUtils.systemTakePhoto(this, 2)
        }

        //自定义相册
        btn_4.setOnClickListener {
            AppImageUtils.appImageAlbum(this, 1)
        }

        //自定义相册
        btn_7.setOnClickListener {
            val info = AlbumInfo()
            info.max_count = 10
            info.take_photo_open = true
            info.customer_camera = true
            info.span_count = 4
            AppImageUtils.appImageAlbum(this, info)
        }

        //压缩  多张图片，onSuccess 会调用多次
        btn_5.setOnClickListener {
            if (mData.size > 0) {
                AppImageUtils.LuImageCompress(this, mData, object : OnCompressListener {
                    override fun onSuccess(file: File?) {
                        mUiActive.loadingView.hideLoading()
                        val dirSize = FileUtils.getSize(file)
                        tv_img_size.append(",新图大小->$dirSize")
                        tv_img_size.append("\n新图位置->${file?.path}")
                        ToastUtils.showLong("压缩成功")
                    }

                    override fun onError(e: Throwable?) {
                        mUiActive.loadingView.hideLoading()
                        e?.printStackTrace()
                    }

                    override fun onStart() {
                        mUiActive.loadingView.showLoading("开始压缩")
                    }
                })
            }
        }

        btn_6.setOnClickListener {
            //            WebActivity.open(this, "https://aznapi.house365.com/Home/Information/lists")
            val target = AppFileStorage.getStorageImagesDir().path + "/test.jpg"
            CameraActivity.open(this, 1001, "")
        }
    }

    private fun getAll(): ArrayList<String> {
        val list = arrayListOf<String>()
        list.add("http://img31.house365.com/M02/01/72/rBEBYFTTb52AKGnpAAGRhUbP6bI584.jpg")
        list.add("http://img31.house365.com/M02/01/71/rBEBYFTTb5qAGnRYAAda-f68kug942.jpg")
        list.add("http://img31.house365.com/M02/01/71/rBEBYFTTb5iAetrnAAGSIF0wiv0935.jpg")
        list.add("http://img31.house365.com/M02/01/71/rBEBYFTTb5iAUIriAAJYXBzYogs778.jpg")
        list.add("http://img31.house365.com/M02/01/71/rBEBYFTTb5mAJDA5AAMhtOOBMJA321.jpg")
        list.add("http://img31.house365.com/M02/01/71/rBEBYFTTb5mAZ7LcAAM8wtR8X-E576.jpg")
        list.add("http://img31.house365.com/M02/01/71/rBEBYFTTb5mAcaZdAAMWXiLK-Nk224.jpg")
        list.add("http://img31.house365.com/M02/01/71/rBEBYFTTb5mADsEOAALPYONdMdk748.jpg")
        return list
    }

    override fun getLayoutId(): Int = R.layout.activity_ui

    private val mData: ArrayList<String> = arrayListOf()

    private var mPhoto: File? = null

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        mPhoto = savedInstanceState?.getSerializable("file") as File?
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("file", mPhoto)
    }

    private var mTake = ""
    private fun updateImageShow(file: String?) {
        if (file != null) {
            iv_2.post {
                ImageLoader.loadImage(iv_2, file)
                mTake = file
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK)
            return
        if (requestCode == 1) {
            val uri = AppImageUtils.systemChooseImageIntentImagePath(mContext, data)
            if (!TextUtils.isEmpty(uri)) {
                AppImageUtils.appImageCrop(this, uri, 11)
            }
            updateImageShow(uri)
        } else if (requestCode == 2) {
            updateImageShow(mPhoto?.path)
        } else if (AlbumInfo.isAlbumResult(requestCode)) {
            val extra = AlbumInfo.getAlbumSelectedImages(data)
            mData.clear()
            mData.addAll(extra!!)

            val text = StringBuilder()
            extra.forEach {
                text.append("$it\n")
            }
            tv_xc_2.text = text

            //update size
            if (mData.isNotEmpty()) {
                val dirSize = FileUtils.getSize(mData[0])
                tv_img_size.text = "原图大小->$dirSize"
            }

        } else if (requestCode == 11) {
            tv_xc_2.append("\n")
            tv_xc_2.append("剪裁图片地址：")
            tv_xc_2.append(AppImageUtils.appImageCropIntentPath(data))
            updateImageShow(AppImageUtils.appImageCropIntentPath(data))
        } else if (requestCode == 1001) {
            val file = data?.getStringExtra("path")
            Logger.e(file)
            updateImageShow(file)

            val dirSize = FileUtils.getSize(file)
            tv_img_size.text = "拍照原图大小->$dirSize"

            //compress
            AppImageUtils.LuImageCompress(this, file, object : OnCompressListener {
                override fun onError(e: Throwable?) {
                }

                override fun onStart() {
                    mUiActive.loadingView.showLoading("开始压缩")

                }

                override fun onSuccess(file: File?) {
                    mUiActive.loadingView.hideLoading()
                    val dirSize = FileUtils.getSize(file)
                    tv_img_size.append(",新图大小->$dirSize")
                    tv_img_size.append("\n新图位置->${file?.path}")
                    ToastUtils.showLong("压缩成功")
                }
            })
        }
    }
}

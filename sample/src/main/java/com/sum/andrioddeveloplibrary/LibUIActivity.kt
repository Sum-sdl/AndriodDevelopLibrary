package com.sum.andrioddeveloplibrary

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.view.ViewCompat
import android.text.TextUtils
import android.transition.AutoTransition
import android.transition.Transition
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ToastUtils
import com.sum.andrioddeveloplibrary.App.BaseAppActivity
import com.sum.library.AppFileConfig
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
        ImageLoader.loadImage(iv_2, "http://img31.house365.com/M02/01/72/rBEBYFTTb52AKGnpAAGRhUbP6bI584.jpg")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.sharedElementReenterTransition = AutoTransition()
            postponeEnterTransition()
            ViewCompat.setTransitionName(iv_2, "IMG_TRANSITION")
            startPostponedEnterTransition()
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


    override fun initParams() {

        btn_1.setOnClickListener {
            val list = arrayListOf<String>()
            list.add(mTake)
            list.addAll(mData)
            list.addAll(getAll())
            AppImageUtils.appImagePreview(this, list)
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
            info.max_count = 12
            info.take_photo_open = true
            info.customer_camera = true
            info.span_count = 3
            AppImageUtils.appImageAlbum(this, info)
        }

        //压缩  多张图片，onSuccess 会调用多次
        btn_5.setOnClickListener {
            if (mData.size > 0) {
                AppImageUtils.LuImageCompress(this, mData, object : OnCompressListener {
                    override fun onSuccess(file: File?) {
                        mUiActive.loadingView.hideLoading()
                        val dirSize = FileUtils.getFileSize(file)
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
            val target = AppFileConfig.getAppStoreDirectory().path + "/test.jpg"
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
        } else if (requestCode == 10) {
            val extra = data?.getStringArrayListExtra("images")
            mData.clear()
            mData.addAll(extra!!)

            val text = StringBuilder()
            extra.forEach {
                text.append("$it\n")
            }
            tv_xc_2.text = text

            //update size
            if (mData.isNotEmpty()) {
                val dirSize = FileUtils.getFileSize(mData[0])
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

            val dirSize = FileUtils.getFileSize(file)
            tv_img_size.text = "拍照原图大小->$dirSize"

            //compress
            AppImageUtils.LuImageCompress(this,file,object :OnCompressListener{
                override fun onError(e: Throwable?) {
                }

                override fun onStart() {
                    mUiActive.loadingView.showLoading("开始压缩")

                }

                override fun onSuccess(file: File?) {
                    mUiActive.loadingView.hideLoading()
                    val dirSize = FileUtils.getFileSize(file)
                    tv_img_size.append(",新图大小->$dirSize")
                    tv_img_size.append("\n新图位置->${file?.path}")
                    ToastUtils.showLong("压缩成功")
                }
            })
        }
    }
}

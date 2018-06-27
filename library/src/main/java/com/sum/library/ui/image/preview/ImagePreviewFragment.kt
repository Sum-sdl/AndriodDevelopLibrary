package com.sum.library.ui.image.preview

import android.graphics.drawable.Animatable
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.sum.library.R
import com.sum.library.app.BaseFragment
import kotlinx.android.synthetic.main.ui_fragment_image_preview.*
import me.relex.photodraweeview.OnPhotoTapListener

/**
 * Created by sdl on 2018/1/3.
 */
class ImagePreviewFragment : BaseFragment() {

    companion object {
        fun instance(url: String, clickClose: Boolean): ImagePreviewFragment {
            return ImagePreviewFragment().apply {
                arguments = Bundle().apply {
                    putString("url", url)
                    putBoolean("close", clickClose)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        PRINT_LIFE = true
        super.onCreate(savedInstanceState)
    }

    private var mImageInfo: ImageInfo? = null

    override fun initParams(view: View?) {
        val url = arguments?.getString("url")
        val controller = Fresco.newDraweeControllerBuilder()
        val request = ImageRequestBuilder.newBuilderWithSource(
                Uri.parse(if (url?.indexOf("http") != -1) url else "file://$url"))
                .setResizeOptions(ResizeOptions(SizeUtils.dp2px(180f), SizeUtils.dp2px(320f))).build()
        controller.imageRequest = request//图片加载信息
        controller.oldController = photo_view.controller
        controller.controllerListener = object : BaseControllerListener<ImageInfo>() {
            override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?, animatable: Animatable?) {
                super.onFinalImageSet(id, imageInfo, animatable)
                if (imageInfo == null) {
                    return
                }
                mImageInfo = imageInfo
                photo_view.update(imageInfo.width, imageInfo.height)
            }

            override fun onFailure(id: String?, throwable: Throwable?) {
            }
        }
        photo_view.controller = controller.build()
        photo_view.onPhotoTapListener = OnPhotoTapListener { _, _, _ ->
            if (arguments!!.getBoolean("close", false)) {
                activity?.finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (mImageInfo != null) {
            photo_view.update(mImageInfo!!.width, mImageInfo!!.height)
        }
    }

    override fun getLayoutId(): Int = R.layout.ui_fragment_image_preview
}
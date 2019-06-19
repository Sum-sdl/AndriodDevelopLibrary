package com.sum.library.ui.image.preview

import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.sum.library.R
import com.sum.library.app.BaseFragment
import kotlinx.android.synthetic.main.ui_fragment_image_preview.*

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

    override fun initParams(view: View?) {
        val url = arguments?.getString("url")


        if (TextUtils.isEmpty(url)) {
            return
        }
        val source = Uri.parse(if (url?.indexOf("http") != -1) url else "file://$url")

        val options = RequestOptions.fitCenterTransform()
        Glide.with(this).asDrawable().load(source).apply(options)
                .transition(DrawableTransitionOptions().crossFade())
                /* .listener(object : RequestListener<Drawable> {
                     override fun onLoadFailed(e: GlideException?, model: Any?,
                                               target: Target<Drawable>?, isFirstResource:
                                               Boolean): Boolean {
                         ToastUtils.showShort("加载失败")
                     }

                     override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                     }
                 })*/
                .into(photo_view)

    }


    override fun getLayoutId(): Int = R.layout.ui_fragment_image_preview
}
package com.sum.library.ui.image.preview

import android.net.Uri
import android.os.Bundle
import android.view.View
import com.sum.library.R
import com.sum.library.app.BaseFragment

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

        val uri = Uri.parse(if (url?.indexOf("http") != -1) url else "file://$url")

    }


    override fun getLayoutId(): Int = R.layout.ui_fragment_image_preview
}
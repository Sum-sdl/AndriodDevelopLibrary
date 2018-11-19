package com.sum.andrioddeveloplibrary.aa_surface_test

import com.sum.andrioddeveloplibrary.R
import com.sum.library.app.BaseActivity
import kotlinx.android.synthetic.main.activity_surface.*

/**
 * Created by sdl on 2018/11/19.
 */

class SurfaceActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_surface

    override fun initParams() {

        btn_start.setOnClickListener {
            surface_view.start()
        }

        btn_stop.setOnClickListener {
            surface_view.stop()
        }

        btn_print.setOnClickListener {
            surface_view.print()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        surface_view.stop()
    }
}
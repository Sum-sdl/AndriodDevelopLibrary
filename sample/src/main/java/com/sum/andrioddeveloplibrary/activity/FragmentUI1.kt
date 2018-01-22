package com.sum.andrioddeveloplibrary.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import com.sum.andrioddeveloplibrary.R
import com.sum.library.app.BaseFragment
import kotlinx.android.synthetic.main.fragment_ui1.*

/**
 * Created by sdl on 2018/1/19.
 */
class FragmentUI1 : BaseFragment() {

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        log("onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log("onCreate " + savedInstanceState?.toString())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        log("onSaveInstanceState," + outState.toString())
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        log("onViewCreated," + savedInstanceState.toString())

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        log("onViewStateRestored," + savedInstanceState?.toString())
    }

    override fun onDetach() {
        super.onDetach()
        log("onDetach")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        log("onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }

    override fun initParams(view: View?) {
        tv_1.append(",managerSize:" + fragmentManager?.fragments?.size)
    }

    private fun log(text: String) {
//        Logger.e("FragmentUI1:" + text)
    }

    override fun getLayoutId(): Int = R.layout.fragment_ui1
}
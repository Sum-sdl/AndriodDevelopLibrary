package com.sum.andrioddeveloplibrary.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sum.andrioddeveloplibrary.R
import com.sum.library.app.BaseFragment
import com.sum.library.utils.Logger
import kotlinx.android.synthetic.main.fragment_ui1.*

/**
 * Created by sdl on 2018/1/19.
 */
class FragmentUI1 : BaseFragment() {

    var mLife: String? = ""


    override fun onAttach(context: Context?) {
        PRINT_LIFE = true
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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        log("onCreateView," + savedInstanceState.toString())
        return super.onCreateView(inflater, container, savedInstanceState)
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

    override fun onResume() {
        super.onResume()
        log("onResume")
        tv_1.text = mLife
    }

    private fun log(text: String) {
        Logger.e("FragmentUI1:" + text)
        mLife += text + " -> "
    }

    override fun getLayoutId(): Int = R.layout.fragment_ui1
}
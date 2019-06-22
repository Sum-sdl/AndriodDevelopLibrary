package com.sum.andrioddeveloplibrary.activity

import android.os.Bundle
import android.view.View
import com.sum.andrioddeveloplibrary.R
import com.sum.library.app.BaseActivity
import com.sum.library.framework.FragmentCacheManager
import com.sum.library.utils.Logger
import kotlinx.android.synthetic.main.activity_fragment_manager.*

class FragmentManagerActivity : BaseActivity() {


    override fun getLayoutId(): Int = R.layout.activity_fragment_manager

    private lateinit var mManager: FragmentCacheManager

    private var mCurIndex = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.e("onCreate:" + ",b:" + savedInstanceState?.toString())

        if (savedInstanceState != null) {
            mCurIndex = savedInstanceState.getInt("index", R.id.rb1)
        }

        mManager.setCurrentFragment(mCurIndex)
        mrg.check(mCurIndex)
    }

    override fun initParams() {
        mManager = FragmentCacheManager.instance()
        mManager.setUp(this, R.id.fl_content)
        mManager.addFragmentToCache(R.id.rb1, FragmentUI1::class.java)
        mManager.addFragmentToCache(R.id.rb2, FragmentUI2::class.java)

        rb1.setOnClickListener(listener)
        rb2.setOnClickListener(listener)

        mCurIndex = R.id.rb1
    }

    private val listener = View.OnClickListener {
        mCurIndex = it.id
        mManager.setCurrentFragment(mCurIndex)
        mrg.check(mCurIndex)
    }

    override fun onResume() {
        super.onResume()
        Logger.e("onResume")
    }

    override fun onPause() {
        super.onPause()
        Logger.e("onStop")
    }

    override fun onStop() {
        super.onStop()
        Logger.e("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.e("onDestroy")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Logger.e("onSaveInstanceState:" + ",b:" + outState?.toString())
        outState?.putInt("index", mCurIndex)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Logger.e("onRestoreInstanceState:" + ",b:" + savedInstanceState?.toString())
    }

}

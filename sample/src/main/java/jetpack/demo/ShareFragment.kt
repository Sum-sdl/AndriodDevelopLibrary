package jetpack.demo


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.sum.andrioddeveloplibrary.R
import com.sum.library.utils.Logger
import jetpack.demo.base.ViewModelOwner
import jetpack.demo.livedata.NameViewModel
import kotlinx.android.synthetic.main.fragment_share.*

class ShareFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //通过统一的一个ViewModelStore来创建管理数据源
        val provider = ViewModelProvider(ViewModelOwner.instance(),
                ViewModelOwner.instance().factory())

        val mViewModel = provider.get(NameViewModel::class.java)
        //这里的Observer会在onViewCreated执行后自动触发一次
        mViewModel.currentName.observe(this, (Observer {
            Logger.e("ShareFragment Observer start")
            it?.let {
                tv_data.text = it
            }
        }))

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        Logger.e("ShareFragment onCreateView")
        return inflater.inflate(R.layout.fragment_share, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.e("ShareFragment onViewCreated")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Logger.e("ShareFragment onDestroyView")
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Logger.e("ShareFragment onAttach")
    }

    override fun onDetach() {
        super.onDetach()
        Logger.e("ShareFragment onDetach")
    }
}

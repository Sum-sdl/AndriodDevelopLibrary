package jetpack.demo

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sum.andrioddeveloplibrary.R
import com.sum.library.utils.LiveDataEventBus
import com.sum.library.utils.Logger
import jetpack.demo.base.ViewModelOwner
import jetpack.demo.livedata.NameViewModel
import kotlinx.android.synthetic.main.activity_live_data_change.*

class LiveDataChangeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.e("LiveDataChangeActivity onCreate")
        setContentView(R.layout.activity_live_data_change)


        //通过统一的一个ViewModelStore来创建管理数据源
        val provider = ViewModelProvider(ViewModelOwner.instance(),
                ViewModelOwner.instance().factory())

        val mViewModel = provider.get(NameViewModel::class.java)
        //这里的Observer会在onViewCreated执行后自动触发一次
        mViewModel.currentName.observe(this, (Observer {
            Logger.e("LiveDataChangeActivity Observer start")
            it?.let {
                bt1.text = it
            }
        }))
        var index = 1
        bt1.setOnClickListener {
            mViewModel.currentName.value = "LiveDataChangeActivity click->" + (++index)
        }

        bt2.setOnClickListener {
            LiveDataEventBus.with("change").value = ("second activity index->" + (++index))
        }


        LiveDataEventBus.with("change", String::class.java)
                .observe(this, Observer {
                    Logger.e("second activity change->$it")
                    bt2.text = it
                })


        bt3.setOnClickListener {
            bt3.text = ("second activity change_Forever" + (++index))
            LiveDataEventBus.with("change_forever").value = bt3.text.toString()
        }
    }

    override fun onStart() {
        super.onStart()
        Logger.e("LiveDataChangeActivity onStart")
    }

    override fun onResume() {
        super.onResume()
        Logger.e("LiveDataChangeActivity onResume")
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        Logger.e("LiveDataChangeActivity onResumeFragments")
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.e("LiveDataChangeActivity onDestroy")
    }

}

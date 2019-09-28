package jetpack.demo

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import com.sum.andrioddeveloplibrary.R
import com.sum.library.utils.LiveDataEventBus
import com.sum.library.utils.Logger
import jetpack.demo.base.ViewModelOwner
import jetpack.demo.databind.DataBindTestActivity
import jetpack.demo.framework.HouseInfoTestActivity
import jetpack.demo.lifecycle.MyObserver
import jetpack.demo.livedata.NameViewModel
import jetpack.demo.page.PageActivity
import kotlinx.android.synthetic.main.activity_new_start.*
import java.util.*

/**
 * Created by sdl on 2018/7/9.
 * 最新框架测试
 */
class NewStartActivity : AppCompatActivity() {

    private lateinit var mObserver: MyObserver

    private lateinit var mViewModel: NameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_start)

        //27support库已经内部集成了Lifecycle框架
        //LifecycleObserver
//        mObserver = MyObserver()
        //创建自己的observer后，通过注解，就直接回调到LifecycleObserver对应的注解方法中
//        lifecycle.addObserver(mObserver)


        val provider = ViewModelProvider(ViewModelOwner.instance(), ViewModelOwner.instance().factory())
        //创建其他监听者去处理对应的业务
        mViewModel = provider.get(NameViewModel::class.java)
        lifecycle.addObserver(mViewModel)

        //通过方法来处理一个业务逻辑
        mViewModel.currentName.observe(this, (Observer<String> {
            Logger.e("NewStartActivity Observer start")
            it?.let {
                b1.text = it
            }
        }))

        var index = 1

        b2.setOnClickListener {
            mViewModel.currentName.value = "Hello Click->" + (++index)
        }

        b3.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fl_content, ShareFragment()).commit()
        }

        b4.setOnClickListener {
            startActivity(Intent(this, LiveDataChangeActivity::class.java))
        }

        b5.setOnClickListener {
            startActivity(Intent(this, PageActivity::class.java))
        }
        b6.setOnClickListener {
            startActivity(Intent(this, DataBindTestActivity::class.java))
        }
        b7.setOnClickListener {
            startActivity(Intent(this, HouseInfoTestActivity::class.java))
        }

        var hello_index = 0
        b8.setOnClickListener {
            hello_index++
            LiveDataEventBus.with("change").value = ("change->$hello_index")
        }

        //注册2个，2个都会触发
        LiveDataEventBus.with("change", String::class.java)
                .observe(this, Observer {
                    Logger.e("observe change 1->$it")
                    tv_nor_live_data.text = it
                })

        //同一个change改注册无效
        LiveDataEventBus.with("change", String::class.java)
                .observe(this, Observer {
                    Logger.e("observe change 2->$it")
                    tv_nor_live_data.append("->第二个 Observer append-$it")
                })


        //创建一个observeForever
        b9.setOnClickListener { _ ->
            LiveDataEventBus.with("change_forever", String::class.java)
                    .observeForever {
                        tv_forever_live_data.text = it.toString()
                        Logger.e("change_forever change->$it")
                        ToastUtils.showShort("change_forever->$it")
                    }
        }

        b10.setOnClickListener {
            LiveDataEventBus.with("change_forever").value = "hello Random:" + Random().nextInt(666)
        }
    }

    override fun onStart() {
        super.onStart()
        Logger.e("NewStartActivity onStart")
    }

    override fun onRestart() {
        super.onRestart()
        Logger.e("NewStartActivity onRestart")
    }

    override fun onResume() {
        super.onResume()
        Logger.e("NewStartActivity onResume")
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.e("NewStartActivity onDestroy")
//        lifecycle.removeObserver(mObserver)
        lifecycle.removeObserver(mViewModel)
    }

}
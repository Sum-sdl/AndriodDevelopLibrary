package jetpack.demo

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sum.andrioddeveloplibrary.R
import com.sum.library.utils.Logger
import jetpack.demo.base.ViewModelOwner
import jetpack.demo.lifecycle.MyObserver
import jetpack.demo.livedata.NameViewModel
import kotlinx.android.synthetic.main.activity_new_start.*

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
        mObserver = MyObserver()

        //创建自己的observer后，通过注解，就直接回调到LifecycleObserver对应的注解方法中
        lifecycle.addObserver(mObserver)


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
        lifecycle.removeObserver(mObserver)
        lifecycle.removeObserver(mViewModel)
    }

}
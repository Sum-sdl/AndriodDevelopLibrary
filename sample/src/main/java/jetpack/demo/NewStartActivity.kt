package jetpack.demo

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sum.andrioddeveloplibrary.R
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


        //创建其他监听者去处理对应的业务
        mViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(NameViewModel::class.java)

        //通过方法来处理一个业务逻辑
        mViewModel.currentName.observe(this, (Observer<String> {
            it?.let { b1.text = it }
        }))


        b2.setOnClickListener {
            mViewModel.currentName.value = "Hello Click"
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(mObserver)
    }

}
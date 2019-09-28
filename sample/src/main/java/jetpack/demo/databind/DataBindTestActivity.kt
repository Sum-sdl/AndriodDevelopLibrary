package jetpack.demo.databind

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ToastUtils
import com.sum.adapter.RecyclerAdapter
import com.sum.adapter.RecyclerDataHolder
import com.sum.andrioddeveloplibrary.R
import com.sum.andrioddeveloplibrary.databinding.ActivityDataBindTestBinding
import com.sum.library.domain.ActionState
import com.sum.library.utils.Logger
import jetpack.demo.databindAdapter.InfoDataHolder
import jetpack.demo.databindAdapter.InfoModel
import kotlinx.android.synthetic.main.activity_data_bind_test.*
import java.util.*

class DataBindTestActivity : AppCompatActivity() {

    private lateinit var mAdapter: RecyclerAdapter<RecyclerDataHolder<Any>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        val viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)).get(UserViewModel::class.java)

        val binding = DataBindingUtil.setContentView<ActivityDataBindTestBinding>(this, R.layout.activity_data_bind_test)
//        val binding = DataBindingUtil.setContentView<ActivityDb2Binding>(this, R.layout.activity_db_2)

        binding.viewModel = viewModel


        var index = 100

        btn_1.setOnClickListener {
            index += 100
            viewModel.mInfoLiveData.value = index.toString()
        }

        btn_2.setOnClickListener {
            viewModel.sendActionState(Random().nextInt())
        }

        btn_4.setOnClickListener {

            //            val state = ActionState(Random().nextInt())
//            viewModel.sendActionState(state)
            mAdapter.notifyDataSetChanged()
        }
        btn_5.setOnClickListener {

            mAdapter.dataHolders.forEachIndexed { index, item ->
                val info = InfoModel()
                info.name = "index->$index"
//                item.updateData(info)
            }
            mAdapter.notifyDataSetChanged()

        }

        btn_3.setOnClickListener {
            val list = arrayListOf<RecyclerDataHolder<Any>>()

            for (i in 1..60) {
                val info = InfoModel()
                info.name = "name->$i"
                info.age = (i + 1).toString()
                list.add(InfoDataHolder(info))
            }
            mAdapter.setDataHolders(list)
        }


        mAdapter = RecyclerAdapter()
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.adapter = mAdapter



        viewModel.mRespotryData.observe(this, Observer {
            it?.let {
                ToastUtils.showLong("remoteData->" + it.data)
            }
        })

        //base
        viewModel.registerActionState(this, Observer {
            it?.let {
                Logger.e(it.toString())
                ToastUtils.showLong("state->" + it.state)
                ActionState.release(it)
            }
        })
    }
}

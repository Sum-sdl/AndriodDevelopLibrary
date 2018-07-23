package jetpack.demo.databind

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.sum.andrioddeveloplibrary.R
import com.sum.andrioddeveloplibrary.databinding.ActivityDataBindTestBinding
import com.sum.lib.rvadapter.RecyclerAdapter
import com.sum.lib.rvadapter.RecyclerDataHolder
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

        val viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

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

            mAdapter.dataHoders.forEachIndexed { index, item ->
                val info = InfoModel()
                info.name = "index->$index"
                item.updateData(info)
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
        recyclerView.layoutManager = LinearLayoutManager(this)
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

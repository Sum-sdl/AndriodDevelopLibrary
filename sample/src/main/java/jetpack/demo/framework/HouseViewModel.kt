package jetpack.demo.framework

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.sum.library.domain.BaseViewModel
import java.util.*

/**
 * Created by sdl on 2018/7/16.
 */
class HouseViewModel : BaseViewModel<HouseRepository>() {
    override fun getRepositoryClass(): Class<*> = HouseRepository::class.java

    private val mInfoLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    private val mUploadLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    //注册观察数据源
    val mRespotryData: LiveData<HouseRepository.Info> =
            Transformations.switchMap(mInfoLiveData) { mRepository.getData(it) }

    //注册观察数据源
    val mRespotryUpload: LiveData<HouseRepository.Info> =
            Transformations.switchMap(mUploadLiveData) { mRepository.uploadFile(it) }


    fun loadData() {
        mInfoLiveData.value = "100"
    }


    fun click() {
        mInfoLiveData.value = "random->" + Random().nextInt(100)
    }

    fun upload() {
        mUploadLiveData.value = "random upload ->" + Random().nextInt(100)
    }

}
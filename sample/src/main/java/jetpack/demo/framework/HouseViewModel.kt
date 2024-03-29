package jetpack.demo.framework

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.sum.library.domain.BaseViewModel
import java.util.*

/**
 * Created by sdl on 2018/7/16.
 */
class HouseViewModel : BaseViewModel<HouseRepository>() {
    override fun buildRepository(): HouseRepository {
        return HouseRepository();
    }

    private val mInfoLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    private val mUploadLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    //注册观察数据源
    val mRespotryData: LiveData<HouseRepository.Info> =
            Transformations.switchMap(mInfoLiveData) { repository.getData(it) }

    //注册观察数据源
    val mRespotryUpload: LiveData<HouseRepository.Info> =
            Transformations.switchMap(mUploadLiveData) { repository.uploadFile(it) }


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
package jetpack.demo.databind

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.blankj.utilcode.util.ToastUtils
import com.sum.library.domain.BaseViewModel

/**
 * Created by sdl on 2018/7/13.
 */
class UserViewModel : BaseViewModel<UserRepository>() {
    override fun getRepositoryClass(): Class<*> = UserRepository::class.java

    var name = ObservableField("Hello world")

    var show = ObservableBoolean(false)

    val mInfoLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val mRespotryData: LiveData<UserRepository.Info> =
            Transformations.switchMap(mInfoLiveData) { mRepository.getData(it) }


    fun textClickData(content: String) {
        show.set(!show.get())
        ToastUtils.showLong("Click->$content->$show")
    }
}

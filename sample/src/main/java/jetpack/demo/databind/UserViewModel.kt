package jetpack.demo.databind

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.blankj.utilcode.util.ToastUtils
import com.sum.library.domain.BaseRepository
import com.sum.library.domain.BaseViewModel

/**
 * Created by sdl on 2018/7/13.
 */
class UserViewModel : BaseViewModel<UserRepository>() {

    override fun buildRepository(): UserRepository {
        return UserRepository()
    }

    var name = ObservableField("Hello world")

    var show = ObservableBoolean(false)

    val mInfoLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val mRespotryData: LiveData<UserRepository.Info> =
        Transformations.switchMap(mInfoLiveData) { repository.getData(it) }


    fun textClickData(content: String) {
        show.set(!show.get())
        ToastUtils.showLong("Click->$content->$show")
    }
}

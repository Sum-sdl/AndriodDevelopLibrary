package jetpack.demo.databing

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.view.View
import com.blankj.utilcode.util.ToastUtils

/**
 * Created by sdl on 2018/7/13.
 */
class UserViewModel : ViewModel() {

    var name = ObservableField("Hello world")

    var show = ObservableBoolean(false)

    val mRespotry = Respotry()

    val mInfoLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val mRespotryData: LiveData<Respotry.Info> =
            Transformations.switchMap(mInfoLiveData) { mRespotry.getData(it) }

    fun textClick(view: View) {
        ToastUtils.showLong("Click")
        name.set(name.get() + "->")
    }

    fun textClickData(content: String) {
        show.set(!show.get())
        ToastUtils.showLong("Click->$content->$show")
    }
}

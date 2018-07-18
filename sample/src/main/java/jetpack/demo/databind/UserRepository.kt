package jetpack.demo.databind

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.sum.library.domain.ActionState
import com.sum.library.domain.BaseRepository
import com.sum.library.utils.Logger
import com.sum.library.utils.ioThread

/**
 * Created by sdl on 2018/7/13.
 */
class UserRepository : BaseRepository() {

    private val mInfoLiveData = MutableLiveData<Info>()

    class Info {
        var index = 0
        var data = ""
    }

    fun getData(input: String): LiveData<Info> {

        Logger.e("UserRepository getData ->$input")

        ioThread {
            Logger.e("Thread load data start")
            sendActionState(ActionState.NET_START)
            Thread.sleep(2000)
            mInfoLiveData.postValue(Info().apply { data = input })
            Logger.e("Thread load data finish")
            Thread.sleep(2000)
            sendActionState(ActionState.NET_FINISH)

        }

        return mInfoLiveData
    }

}
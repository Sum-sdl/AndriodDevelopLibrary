package jetpack.demo.databing

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.sum.library.utils.Logger
import com.sum.library.utils.ioThread

/**
 * Created by sdl on 2018/7/13.
 */
class Respotry {

    val mInfoLiveData = MutableLiveData<Info>()

    class Info {
        var index = 0
        var data = ""
    }

    fun getData(input: String): LiveData<Info> {

        Logger.e("Respotry getData ->$input")

        ioThread {
            Logger.e("Thread load data start")
            Thread.sleep(2000)
            mInfoLiveData.postValue(Info().apply { data = input })
            Logger.e("Thread load data finish")
        }

        return mInfoLiveData
    }

}
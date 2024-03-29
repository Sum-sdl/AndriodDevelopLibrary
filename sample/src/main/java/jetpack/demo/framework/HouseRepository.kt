package jetpack.demo.framework

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sum.library.domain.ActionState
import com.sum.library.domain.BaseRepository
import com.sum.library.utils.Logger
import com.sum.library.utils.TaskExecutor.ioThread

/**
 * Created by sdl on 2018/7/16.
 */
class HouseRepository : BaseRepository() {

    private val mInfoLiveData = MutableLiveData<Info>()

    private val mUploadLiveData = MutableLiveData<Info>()

    class Info {
        var data = ""
    }

    fun getData(input: String): LiveData<Info> {
        Logger.e("UserRepository getData ->$input")

        ioThread {
            Logger.e("Thread load data start")
            val state = ActionState.obtain(ActionState.DIALOG_LOADING_SHOW)
            state.msg = "加载中..."

//            sendActionState(state)
            Thread.sleep(1000)
            mInfoLiveData.postValue(Info().apply { data = input })
            Logger.e("Thread load data finish")
            Thread.sleep(1500)
//            sendActionState(ActionState.DIALOG_HIDE)
        }

        return mInfoLiveData
    }


    fun uploadFile(input: String): LiveData<Info> {
        Logger.e("UserRepository getData ->$input")

        ioThread {
            Logger.e("Thread uploadFile data start")
            val state = ActionState.obtain(ActionState.DIALOG_PROGRESS_SHOW)
            state.msg = "上传文件中..."
//            sendActionState(state)

            Thread.sleep(1500)
            mUploadLiveData.postValue(Info().apply { data = input })
            Logger.e("Thread uploadFile data finish")

            Thread.sleep(500)
//            sendActionState(ActionState.DIALOG_HIDE)
        }

        return mUploadLiveData
    }
}
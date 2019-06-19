package jetpack.demo.page

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sum.andrioddeveloplibrary.R
import com.sum.library.utils.Logger
import com.sum.library.utils.TaskExecutor.ioThread
import com.sum.library.utils.TaskExecutor.mainThread

class PageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page)

        ioThread {
            Logger.e("PageActivity start io")
        }

        mainThread {
            Logger.e("PageActivity start main")
        }

    }
}

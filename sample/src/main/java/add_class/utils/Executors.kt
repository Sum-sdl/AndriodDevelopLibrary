/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package add_class.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

private val IO_EXECUTOR = Executors.newSingleThreadExecutor()

private val MAIN_EXECUTOR: Executor = MainThreadExecutor()

private class MainThreadExecutor : Executor {

    private val mMainHandler by lazy {
        Handler(Looper.getMainLooper())
    }

    override fun execute(command: Runnable) {
        mMainHandler.post(command)
    }
}

/**
 * Utility method to run blocks on a dedicated background thread, used for io/database work.
 */
fun ioThread(f: () -> Unit) {
    IO_EXECUTOR.execute(f)
}

/**
 * Utility method to run blocks on a dedicated main thread
 */
fun mainThread(f: () -> Unit) {
    MAIN_EXECUTOR.execute(f)
}
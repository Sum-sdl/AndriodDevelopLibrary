package com.sum.andrioddeveloplibrary;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;

import com.blankj.utilcode.util.Utils;
import com.google.gson.Gson;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshInitializer;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.squareup.leakcanary.LeakCanary;
import com.sum.andrioddeveloplibrary.net.TestToken;
import com.sum.library.AppFileConfig;
import com.sum.library.utils.ACache;
import com.sum.library.utils.Logger;
import com.sum.library_network.Retrofit2Helper;

import me.jessyan.autosize.AutoSizeConfig;

/**
 * Created by sdl on 2017/12/27.
 */

public class SumApp extends Application {

    static {
        //设置全局默认配置（优先级最低，会被其他设置覆盖）
        SmartRefreshLayout.setDefaultRefreshInitializer(new DefaultRefreshInitializer() {
            @Override
            public void initialize(@NonNull Context context, @NonNull RefreshLayout layout) {
                //开始设置全局的基本参数（可以被下面的DefaultRefreshHeaderCreator覆盖）
                layout.setEnableOverScrollDrag(false);//越界拖动
                layout.setPrimaryColorsId(R.color.colorAccent);
            }
        });

        //全局设置默认的 Header
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setHeaderMaxDragRate(3.5f);
                layout.setEnableHeaderTranslationContent(false);
                return new MaterialHeader(context).setColorSchemeResources(R.color.colorAccent);
            }
        });

        //全局设置默认的 Footer
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicsFooter(context);
            }
        });
    }

    //TODO 以/结尾
    public static String BASE_URL = "http://apps.meitoutiao.net/";

    public static long mOpenStartTime = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        mOpenStartTime = System.currentTimeMillis();
        //内存检测
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
        //ui
//        BlockCanary.install(this, new BlockCanaryContext()).start();
//        if (BuildConfig.DEBUG) {
//            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                    .detectAll()
//                    .penaltyLog()
//                    .build());
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                    .detectAll()
//                    .penaltyLog()
//                    .build());
//        }
        //ANRWatchDog ANR监测线程

        Logger.e("SumApp onCreate");
        Retrofit2Helper.getInstance().initRetrofit(BASE_URL, new Gson(), new TestToken());

        ACache.get(this).put("time", mOpenStartTime);

        AppFileConfig.init(this, "AA_Sum",true);

        //自适配
        //多进程适配
//        AutoSize.initCompatMultiProcess(this);
        AutoSizeConfig.getInstance().setCustomFragment(false).setLog(true);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Logger.e("SumApp onTrimMemory "+level);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Logger.e("SumApp onTerminate");
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}

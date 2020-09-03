package com.sum.andrioddeveloplibrary;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;

import com.blankj.utilcode.util.Utils;

import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshInitializer;
import com.sum.andrioddeveloplibrary.net.TestToken;
import com.sum.library.storage.AppFileStorage;
import com.sum.library.storage.StorageConfig;
import com.sum.library.utils.ACache;
import com.sum.library.utils.Logger;
import com.sum.library_network.PubReqLogInterceptor;
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

    public static long mOpenStartTime = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        mOpenStartTime = System.currentTimeMillis();
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

        //网络请求基础类
        String BASE_URL = "http://apps.meitoutiao.net/";

        //自定义插值器
        Retrofit2Helper.getInstance().setBaseUrl(BASE_URL).addInterceptor(new TestToken()).addInterceptor(new PubReqLogInterceptor()).init();

        ACache.get(this).put("time", mOpenStartTime);

        //默认的文件初始化
        AppFileStorage.init(new StorageConfig.Builder(this).build());

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

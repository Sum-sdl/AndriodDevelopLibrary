package com.sum.andrioddeveloplibrary.App;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshInitializer;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.sum.andrioddeveloplibrary.R;
import com.sum.andrioddeveloplibrary.net.TestToken;
import com.sum.library.AppFileConfig;
import com.sum.library.app.BaseApplication;
import com.sum.library.net.Retrofit2Helper;
import com.sum.library.utils.Logger;

import okhttp3.OkHttpClient;

/**
 * Created by sdl on 2017/12/27.
 */

public class SumApp extends BaseApplication {

    static {
        AppFileConfig.App_External_Directory_Name = "AA_Sum";

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

    @Override
    public void onCreate() {
        super.onCreate();
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);
        Logger.e("SumApp onCreate");
        Retrofit2Helper instance = Retrofit2Helper.getInstance();
        OkHttpClient client = instance.buildDefaultOkHttpClient().addInterceptor(new TestToken()).build();
        instance.initRetrofit("https://www.baidu.com", client);
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}

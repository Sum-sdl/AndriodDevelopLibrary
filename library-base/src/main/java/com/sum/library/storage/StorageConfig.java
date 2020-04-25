package com.sum.library.storage;

import android.content.Context;
import android.text.TextUtils;

/**
 * Created by sdl on 2020/4/13
 */
public class StorageConfig {

    //Context
    private Context application;

    //根路径文件名称,必须设置一个
    private String baseDirName;

    //true:App的SD卡文件存储路径是Android/data/xxx_package/xxx
    //false:App内部文件存储路径
    private boolean isAppDataDir;

    //自定义FileProvider文件存储路径
    private String customFileProvider;

    private StorageConfig() {
    }

    public Context getApplication() {
        return application;
    }

    public String getBaseDirName() {
        return baseDirName;
    }

    public boolean isAppDataDir() {
        return isAppDataDir;
    }

    public String getCustomFileProvider() {
        return customFileProvider;
    }


    public static class Builder {
        private Context context;
        //文件夹根路径名称
        private String baseDirName;
        //默认外部存储
        private boolean isAppDataDir = true;
        //自定义file provider
        private String customFileProvider;

        public Builder(Context context) {
            if (context == null) {
                throw new RuntimeException("Context is null");
            }
            this.context = context.getApplicationContext();
            //默认参数,在base_sdk_file_path.xml里面配置
            baseDirName = "app";
            //com.xxx.xxx.fileProvider -> manifest里面配置的,默认是包名+".sdkFileProvider"
            //该权限可用影响root.dir目录下的文件共享
            customFileProvider = context.getPackageName() + ".sdkFileProvider";
        }

        //不设置，采用默认
        public Builder setBaseDirName(String baseDirName) {
            this.baseDirName = baseDirName;
            return this;
        }

        //不设置，默认和文件配套使用
        public Builder setCustomFileProvider(String customFileProvider) {
            if (TextUtils.isEmpty(customFileProvider)) {
                throw new RuntimeException("provider is null");
            }
            this.customFileProvider = customFileProvider;
            return this;
        }

        //外部存储还是内部存储
        public Builder setAppDataDir(boolean appDataDir) {
            isAppDataDir = appDataDir;
            return this;
        }

        public StorageConfig build() {
            StorageConfig config = new StorageConfig();
            config.application = context;
            config.baseDirName = baseDirName;
            config.customFileProvider = customFileProvider;
            config.isAppDataDir = isAppDataDir;
            return config;
        }
    }
}

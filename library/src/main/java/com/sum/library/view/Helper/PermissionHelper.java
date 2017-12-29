package com.sum.library.view.Helper;

import android.Manifest;

/**
 * Created by Sum on 16/3/19.
 */
public class PermissionHelper {

    //读写文件权限
    public static String[] FilePermission() {
        return new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE};
    }

    //定位权限
    public static String[] LocationPermission() {
        return new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
        };
    }

}

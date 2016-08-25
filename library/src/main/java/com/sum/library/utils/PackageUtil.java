package com.sum.library.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.xutils.common.util.LogUtil;

public class PackageUtil
{
    private static final String tag = "PackageUtil";

    public static String getPackageName(Context context)
    {
        String packageName = null;
        try
        {
            packageName = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).packageName;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return packageName;
    }

    public static String getVersionName(Context context)
    {
        try
        {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return info.versionName;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }

    public static int getVersionCode(Context context)
    {
        try
        {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return info.versionCode;
        }
        catch (Exception e)
        {
            LogUtil.e(tag, e);
        }
        return -1;
    }

    public static String getSourceChannel(Context context)
    {
        String sourceChannel = "";
        Object value = null;

        try
        {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (info != null && info.metaData != null)
            {
                value = info.metaData.get("SOURCE_CHANNEL");
                if (value != null)
                {
                    sourceChannel = value.toString();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return sourceChannel;
    }
}

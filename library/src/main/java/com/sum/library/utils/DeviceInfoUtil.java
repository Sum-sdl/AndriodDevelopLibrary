package com.sum.library.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import org.xutils.x;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

public class DeviceInfoUtil
{
    public static String getNetType(Context context)
    {
        ConnectivityManager conn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conn != null)
        {
            NetworkInfo info = conn.getActiveNetworkInfo();
            if (info != null)
            {
                String type = info.getTypeName().toLowerCase(
                        Locale.getDefault());
                if (type.equals("wifi"))
                {
                    return type;
                }
                else if (type.equals("mobile"))
                {
                    String apn = info.getExtraInfo().toLowerCase(
                            Locale.getDefault());
                    if (apn != null
                            && (apn.equals("cmwap") || apn.equals("3gwap")
                                    || apn.equals("uniwap") || apn
                                        .equals("ctwap")))
                    {
                        return "wap";
                    }
                    else
                    {
                        return apn;
                    }
                }
            }
        }
        return null;
    }

    public static boolean isWifiInternet(){
        String netType = getNetType(x.app());
        return netType.equalsIgnoreCase("wifi");
    }

    public static boolean isHaveInternet(final Context context)
    {
        try
        {
            ConnectivityManager manger = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo info = manger.getActiveNetworkInfo();

            return (info != null && info.isConnected());
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public static String getDeviceId(Context context)
    {
        return Secure
                .getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

    public static String getSysVersion(Context context)
    {
        String release = android.os.Build.VERSION.RELEASE;
        return release;
    }

    public static String getDeviceName(Context context)
    {
        String model = android.os.Build.MODEL;
        return model;
    }

    public static String getCpuName()
    {
        FileReader fr = null;
        BufferedReader br = null;
        try
        {
            fr = new FileReader("/proc/cpuinfo");
            br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++)
            {
            }
            return array[1];
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (fr != null)
                try
                {
                    fr.close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            if (br != null)
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
        return null;
    }

    public static boolean isSimExist(Context context)
    {
        final TelephonyManager mTelephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        int simState = mTelephonyManager.getSimState();

        return !(simState == TelephonyManager.SIM_STATE_ABSENT
                || simState == TelephonyManager.SIM_STATE_UNKNOWN);

    }

//    public static void startCall(Context context, String uri)
//    {
//        Intent intent = new Intent(Intent.ACTION_CALL);
//        intent.setData(Uri.parse("tel:" + uri));
//        context.startActivity(intent);
//    }

}

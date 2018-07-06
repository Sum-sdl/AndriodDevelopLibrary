package com.sum.andrioddeveloplibrary.test;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by sdl on 2018/7/6.
 */
public class ConnectivityUtil {
        private static final String TAG = "ConnectivityUtil";
        public ConnectivityManager mConnectivityManager;
        public NetworkInfo mNetworkInfo;

        public ConnectivityUtil(Context context) {
            mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        }

        public boolean isNetworkConnected() {
            if (null != mNetworkInfo) {
                return mNetworkInfo.isConnected();
            }

            return false;
        }

        public boolean isMobileConnected() {
            if ((null != mNetworkInfo) && mNetworkInfo.isConnected()) {
                return ConnectivityManager.TYPE_MOBILE == mNetworkInfo.getType();
            }

            return false;
        }

        public void setMobileDataEnabled(boolean isMobileDataEnabled) {
            try {
                Method method = mConnectivityManager.getClass().getDeclaredMethod("setMobileDataEnabled", boolean.class);

                if (null != method) {
                    method.setAccessible(true);
                    method.invoke(mConnectivityManager, isMobileDataEnabled);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public boolean getMobileDataEnabled() {
            try {
                Method method = mConnectivityManager.getClass().getDeclaredMethod("getMobileDataEnabled");

                if (null != method) {
                    method.setAccessible(true);
                    boolean isMobileDataEnabled = (Boolean) method.invoke(mConnectivityManager);

                    return isMobileDataEnabled;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
}

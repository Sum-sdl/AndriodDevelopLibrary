package com.sum.library.utils;

import android.util.Log;

public class Logger {

    public static String tag = "com.logger";

    private static boolean mDebug = true;

    private static Logger instance = new Logger();

    private String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();

        if (sts == null) {
            return null;
        }
        for (StackTraceElement st : sts)
            if (!st.isNativeMethod()) {
                if (!st.getClassName().equals(Thread.class.getName())) {
                    if (!st.getClassName().equals(getClass().getName())) {
                        return "[" + Thread.currentThread().getName() + "(" + Thread.currentThread().getId() +
                                "): " + st.getFileName() + ":" + st.getLineNumber() + "]";
                    }
                }
            }
        return null;
    }

    private String createMessage(String msg) {
        String functionName = getFunctionName();
        return functionName + " - " + msg;
    }

    private void info(String msg) {
        if (mDebug) {
            String message = createMessage(msg);
            Log.i(tag, message);
        }
    }

    public static void i(String msg) {
        instance.info(msg);
    }

    public static void i(Exception e) {
        instance.info(e != null ? e.toString() : "null");
    }

    private void verbose(String msg) {
        if (mDebug) {
            String message = createMessage(msg);
            Log.v(tag, message);
        }
    }

    public static void v(String msg) {
        instance.verbose(msg);
    }

    public static void v(Exception e) {
        instance.verbose(e != null ? e.toString() : "null");
    }

    public void debug(String msg) {
        if (mDebug) {
            String message = createMessage(msg);
            Log.d(tag, message);
        }
    }

    public static void d(String msg) {
        instance.debug(msg);
    }

    public static void d(Exception e) {
        instance.debug(e != null ? e.toString() : "null");
    }

    public void error(String msg) {
        if (mDebug) {
            String message = createMessage(msg);
            Log.e(tag, message);
        }
    }

    public static void e(String msg) {
        instance.error(msg);
    }

    public void error(Exception e) {
        if (mDebug) {
            StringBuilder sb = new StringBuilder();
            String name = getFunctionName();
            StackTraceElement[] sts = e.getStackTrace();

            if (name != null)
                sb.append(name).append(" - ").append(e).append("\r\n");
            else {
                sb.append(e).append("\r\n");
            }
            if ((sts != null) && (sts.length > 0)) {
                for (StackTraceElement st : sts) {
                    if (st != null) {
                        sb.append("[ ").append(st.getFileName()).append(":").append(st.getLineNumber()).append(" ]\r\n");
                    }
                }
            }
            Log.e(tag, sb.toString());
        }
    }

    public static void e(Exception e) {
        instance.error(e);
    }


    private void warn(String msg) {
        if (mDebug) {
            String message = createMessage(msg);
            Log.w(tag, message);
        }
    }

    public static void w(String msg) {
        instance.warn(msg);
    }

    public static void w(Exception e) {
        instance.warn(e != null ? e.toString() : "null");
    }

    public static void setDebug(boolean isDebug) {
        mDebug = isDebug;
    }
}

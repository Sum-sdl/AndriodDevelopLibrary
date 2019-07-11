package com.sum.library.utils;

import android.util.Log;

public class Logger {

    public static String tag = "com.logger";

    private static boolean mDebug = true;

    private static Logger instance = new Logger();

    public static int defaultLogStackOffset = 0;

    private String getFunctionName(int offset) {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        int index = 0;
        int size = sts.length;
        for (StackTraceElement st : sts) {
            if (!st.isNativeMethod()) {
                if (!st.getClassName().equals(Thread.class.getName())) {
                    if (!st.getClassName().equals(getClass().getName())) {
                        int indexOffset = index + offset;
                        if (indexOffset < size) {
                            return getStackInfo(sts[index + offset]);
                        } else {
                            return getStackInfo(st);
                        }
                    }
                }
            }
            index++;
        }
        return null;
    }

    private String getStackInfo(StackTraceElement st) {
        return "[" + Thread.currentThread().getName() + "(" + Thread.currentThread().getId() +
                "): " + st.getFileName() + ":" + st.getLineNumber() + "]";
    }

    private String createMessage(String msg, int offset) {
        String functionName = getFunctionName(offset);
        return functionName + " - " + msg;
    }

    private void info(String msg, int offset) {
        if (mDebug) {
            String message = createMessage(msg, offset);
            Log.i(tag, message);
        }
    }

    public static void i(String msg) {
        instance.info(msg, defaultLogStackOffset);
    }

    public static void i(String msg, int offset) {
        instance.info(msg, offset);
    }

    private void debug(String msg, int offset) {
        if (mDebug) {
            String message = createMessage(msg, offset);
            Log.d(tag, message);
        }
    }

    public static void d(String msg) {
        instance.debug(msg, defaultLogStackOffset);
    }

    public static void d(String msg, int offset) {
        instance.debug(msg, offset);
    }

    public void error(String msg, int offset) {
        if (mDebug) {
            String message = createMessage(msg, offset);
            Log.e(tag, message);
        }
    }

    public static void e(String msg) {
        instance.error(msg, defaultLogStackOffset);
    }

    public static void e(String msg, int offset) {
        instance.error(msg, offset);
    }

    public void error(Exception e) {
        if (mDebug) {
            StringBuilder sb = new StringBuilder();
            String name = getFunctionName(0);
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


    private void warn(String msg, int offset) {
        if (mDebug) {
            String message = createMessage(msg, offset);
            Log.w(tag, message);
        }
    }

    public static void w(String msg) {
        instance.warn(msg, defaultLogStackOffset);
    }

    public static void w(String msg, int offset) {
        instance.warn(msg, offset);
    }

    public static void w(Exception e) {
        instance.warn(e != null ? e.toString() : "null", defaultLogStackOffset);
    }

    public static void setDebug(boolean isDebug) {
        mDebug = isDebug;
    }
}

package com.sum.library.utils;

import android.util.Log;

import com.sum.library.LibConfig;

public class Logger {

    private String tag = "com.logger";

    private static boolean debug = LibConfig.Logger;

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
        String message = functionName + " - " + msg;
        return message;
    }

    public void info(String msg) {
        if (debug) {
            String message = createMessage(msg);
            Log.i(this.tag, message);
        }
    }

    public static void i(String msg) {
        instance.info(msg);
    }

    public static void i(Exception e) {
        instance.info(e != null ? e.toString() : "null");
    }

    public void verbose(String msg) {
        if (debug) {
            String message = createMessage(msg);
            Log.v(this.tag, message);
        }
    }

    public static void v(String msg) {
        instance.verbose(msg);
    }

    public static void v(Exception e) {
        instance.verbose(e != null ? e.toString() : "null");
    }

    public void debug(String msg) {
        if (debug) {
            String message = createMessage(msg);
            Log.d(this.tag, message);
        }
    }

    public static void d(String msg) {
        instance.debug(msg);
    }

    public static void d(Exception e) {
        instance.debug(e != null ? e.toString() : "null");
    }

    public void error(String msg) {
        if (debug) {
            String message = createMessage(msg);
            Log.e(this.tag, message);
        }
    }

    public static void e(String msg) {
        instance.error(msg);
    }

    public void error(Exception e) {
        if (debug) {
            StringBuffer sb = new StringBuffer();
            String name = getFunctionName();
            StackTraceElement[] sts = e.getStackTrace();

            if (name != null)
                sb.append(name + " - " + e + "\r\n");
            else {
                sb.append(e + "\r\n");
            }
            if ((sts != null) && (sts.length > 0)) {
                for (StackTraceElement st : sts) {
                    if (st != null) {
                        sb.append("[ " + st.getFileName() + ":" + st.getLineNumber() + " ]\r\n");
                    }
                }
            }
            Log.e(this.tag, sb.toString());
        }
    }

    public static void e(Exception e) {
        instance.error(e);
    }


    public void warn(String msg) {
        if (debug) {
            String message = createMessage(msg);
            Log.w(this.tag, message);
        }
    }

    public static void w(String msg) {
        instance.warn(msg);
    }

    public static void w(Exception e) {
        instance.warn(e != null ? e.toString() : "null");
    }

}

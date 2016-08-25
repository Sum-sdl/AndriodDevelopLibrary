package com.sum.library.utils;

import org.xutils.common.util.LogUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 常用数字转换工具类
 */
public class NumberUtil {

    public static int parseIntByString(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            LogUtil.e("NumberFormatException", e);
            return 0;
        }
    }

    public static float parseFloatByString(String string) {
        try {
            return Float.parseFloat(string);
        } catch (NumberFormatException e) {
            LogUtil.e("NumberFormatException", e);
            return 0.00f;
        }
    }

    public static double parseDoubleByString(String string) {
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException e) {
            LogUtil.e("NumberFormatException", e);
            return 0.00;
        } catch (Exception e) {
            LogUtil.e("Exception", e);
            return 0.00;
        }
    }


    public static String getDouble(double data) {
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(data);
    }

    public static String getDouble(String data) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(parseDoubleByString(data));
    }

    public static String getDoubleNoDecimal(double data) {
        DecimalFormat df = new DecimalFormat("0");
        return df.format(data);
    }

    public static String fixNumber(int number) {
        if (number < 10) {
            return "0" + number;
        } else {
            return String.valueOf(number);
        }
    }

    // 12345 -> 12,345
    public static String formatPoolNum(String pools) {
        int length = pools.length();
        StringBuilder sb = new StringBuilder();
        int start;
        int num;

        if (length <= 3) {
            return pools;
        }

        start = length % 3;
        if (start == 0) {
            num = length / 3;
            start = 3;
        } else {
            num = length / 3 + 1;
        }

        sb.append(pools.substring(0, start));
        start = sb.length();

        for (int i = 1; i < num; i++) {
            sb.append(",");
            sb.append(pools.substring(start, start + 3));
            start = start + 3;
        }

        return sb.toString();
    }

    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }
}

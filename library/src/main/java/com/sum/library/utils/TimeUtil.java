package com.sum.library.utils;

import android.text.TextUtils;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtil {

    public static final String[] WEEK = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    public static String dateToWeek(Date date) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
        cal.setTime(date);
        int dayIndex = cal.get(Calendar.DAY_OF_WEEK);
        if (dayIndex < 1 || dayIndex > 7) {
            return null;
        }
        return WEEK[dayIndex - 1];
    }

    public static Date nextDate(Date baseDate, int dayInteval) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(baseDate);
        cal.add(Calendar.DAY_OF_YEAR, dayInteval);
        return cal.getTime();
    }

    public static String getStringTime(Date date, String format) {
        return DateFormat.format(format, date).toString();
    }

    public static String getStringTime(Date date) {
        return DateFormat.format("yyyy-MM-dd HH:mm:ss", date).toString();
    }

    public static Date getDateFromString(String date) {
        Date realDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            realDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return realDate;
    }

    public static Date getDateFromString(String date, String format) {
        Date realDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
        try {
            realDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return realDate;
    }

    public static long getDataDuration(String beginDate, String endDate) {
        Date fromDate = null;
        Date toDate = null;

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            fromDate = dateFormat.parse(beginDate);
            toDate = dateFormat.parse(endDate);
        } catch (ParseException e) {
        }

        long milliSecond = toDate.getTime() - fromDate.getTime();

        return milliSecond / 1000;
    }

    public static Date getEndDate(String time) {
        Date endDate = null;
        if (!TextUtils.isEmpty(time)) {
            final SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            try {
                endDate = dateFormat.parse(time);
            } catch (ParseException e) {
            }
        }
        return endDate;
    }

    public static String timeToNow(String dateTime) {
        Date date = getEndDate(dateTime);
        return getTimeAgoString(date);
    }

    public static String timeToNow(long secondTime) {
        long time = secondTime * 1000;
        Date datetime = new Date(time);
        if (datetime == null) {
            return "未知";
        }

        return getTimeAgoString(datetime);
    }

    private static String getTimeAgoString(Date datetime) {
        String ftime = "";
        Calendar cal = Calendar.getInstance();
        long lt = datetime.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - datetime.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math
                        .max((cal.getTimeInMillis() - datetime.getTime()) / 60000,
                                1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天";
        } else if (days > 2 && days <= 5) {
            ftime = days + "天前";
        } else if (days > 5) {
            ftime = getStringTime(datetime);
        }
        return ftime;
    }
}

package com.sum.library.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Summer on 2016/8/25.
 * <p/>
 * 检测功能
 */
public class CheckUtil {

    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static boolean isFastDoubleClick(int millisecond) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < millisecond) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static boolean isMobilePhoneNumb(String number)
    {
        Pattern pMobile = Pattern.compile("[1][0-9]{10}$");
        Matcher matcher = pMobile.matcher(number);
        return matcher.matches();
    }

    public static boolean matcherMail(String mail)
    {
        Pattern pMobile = Pattern
                .compile("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$");
        Matcher matcher = pMobile.matcher(mail);
        return matcher.matches();
    }

    public static boolean matcherPassword(String password)
    {
        if (password.length() < 6 || password.length() > 20)
        {
            return false;
        }
        int num = 0;
        num = Pattern.compile("\\d").matcher(password).find() ? num + 1 : num;
        num = Pattern.compile("[a-zA-Z]").matcher(password).find() ? num + 1
                : num;
        num = Pattern.compile("[-.!@#$%^&*()+?]").matcher(password).find() ? num + 1
                : num;
        return num >= 2;
    }

}

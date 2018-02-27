package com.xk.xkds.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.SimpleDateFormat;

/**
 * Created  on 2017/2/9.
 */

public class DateUtils {
    /**
     * 显示时间格式为 hh:mm
     *
     * @param context
     * @param when
     * @return String
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatTimeShort(Context context, long when) {
        String formatStr = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        String temp = sdf.format(when);
        if (temp != null && temp.length() == 5 && temp.substring(0, 1).equals("0")) {
            temp = temp.substring(1);
        }
        return temp;
    }
}

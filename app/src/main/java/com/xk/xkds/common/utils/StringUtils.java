package com.xk.xkds.common.utils;

import java.text.DecimalFormat;

/**
 * Created  on 2017/2/20.
 */

public class StringUtils {
    /**
     * 返回缓冲字符串
     *
     * @param size
     * @return
     */
    public static String getNetSpeed(int size) {
        if (size < 0) {
            size = 0;
        }
        DecimalFormat formater = new DecimalFormat("####.00");
        if (size < 1024) {
            return size + "KB/S";
        } else if (size < 1024 * 1024) {
            float kbsize = size / 1024f;
            return formater.format(kbsize) + "MB/S";
        } else if (size < 1024 * 1024 * 1024) {
            float mbsize = size / 1024f / 1024f;
            return formater.format(mbsize) + "GB/S";
        } else {
            return "error";
        }
    }

}

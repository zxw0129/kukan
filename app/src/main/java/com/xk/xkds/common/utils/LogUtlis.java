package com.xk.xkds.common.utils;

import android.util.Log;

import com.xk.xkds.common.base.Global;


/**
 * Created  on 2017/2/8.
 */

public class LogUtlis {
    private LogUtlis() {
    }

    private static LogUtlis utlis;

    public static LogUtlis getInstance() {
        if (utlis == null) {
            utlis = new LogUtlis();
        }
        return utlis;
    }
    public void showLogE(String str){
        if(Global.isDebug){
            Log.e("showLog",str);
        }

    }

}

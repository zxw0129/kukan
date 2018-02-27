package com.xk.xkds.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.xk.xkds.component.base.BaseApplication;

/**
 * Created  on 2017/2/28.
 */

public class SpUtils {
    private static final String SP_POSITION = "last_position";

    private static final String SP_POSITION_KEy = "mLastpositon";
    private static final String SP_HARDWARE = "hardware";
    private static final String SP_UPTONEXT = "upToNext";

    private static  final  String SP_KEY_NAME_LOCATION="location_key";
    private static  final  String SP_KEY_VALUE_LOCATION="location_value";
    private  Context mContext= BaseApplication.getInstance();
    private SharedPreferences sp;


    private SpUtils() {
    }

    private static SpUtils spUtils;

    public static SpUtils getInstance() {
        if (spUtils == null) {
            spUtils = new SpUtils();
        }
        return spUtils;
    }

    /***
     * 保存省份
     * @param Province
     * @return
     */
    public boolean saveProvince(String Province) {
        if (sp == null) {
            sp = mContext.getSharedPreferences(SP_KEY_NAME_LOCATION, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(SP_KEY_VALUE_LOCATION, Province);
        return edit.commit();
    }
    /***
     * 获取省份
     * @return
     */
    public String getProvince() {
        if (sp == null) {
            sp = mContext.getSharedPreferences(SP_KEY_NAME_LOCATION, Context.MODE_PRIVATE);
        }
        return sp.getString(SP_KEY_VALUE_LOCATION,"北京市");
    }

    /***
     * 保存当前播放的position
     *
     * @param position
     * @return
     */
    public boolean savePosition(int position) {
        if (sp == null) {
            sp = mContext.getSharedPreferences(SP_POSITION, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(SP_POSITION_KEy, position);
        return edit.commit();
    }

    /***
     * @return 当前播放台号
     */
    public int getPosition() {
        if (sp == null) {
            sp = mContext.getSharedPreferences(SP_POSITION, Context.MODE_PRIVATE);
        }
        return sp.getInt(SP_POSITION_KEy, 1);
    }

    /***
     * @return 解码方式
     */
    public boolean getHardware() {
        if (sp == null) {
            sp = mContext.getSharedPreferences(SP_POSITION, Context.MODE_PRIVATE);
        }
        return sp.getBoolean(SP_HARDWARE, false);
    }
    /***
     * 保存当前播放的position
     * @param hardware
     * @return
     */
    public boolean saveHardware(boolean hardware) {
        if (sp == null) {
            sp = mContext.getSharedPreferences(SP_POSITION, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(SP_HARDWARE, hardware);
        return edit.commit();
    }

    /**
     * 是不是按是上去下一个台(默认false  按上返回上一个台)
     * @return
     */
    public boolean getUpToNext() {
        if (sp == null) {
            sp = mContext.getSharedPreferences(SP_POSITION, Context.MODE_PRIVATE);
        }
        return sp.getBoolean(SP_UPTONEXT, false);
    }
    public boolean saveUpToNext(boolean upToNext) {
        if (sp == null) {
            sp = mContext.getSharedPreferences(SP_POSITION, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(SP_UPTONEXT, upToNext);
        return edit.commit();
    }
}

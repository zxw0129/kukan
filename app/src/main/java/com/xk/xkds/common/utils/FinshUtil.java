package com.xk.xkds.common.utils;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created  on 2017/1/5.
 */
public class FinshUtil {
    private static FinshUtil mUpnpFinshUtil;
    private ArrayList<Activity> mList = new ArrayList<>();

    private FinshUtil() {
    }

    public static FinshUtil getInstance() {
        if (mUpnpFinshUtil == null) {
            mUpnpFinshUtil = new FinshUtil();
        }
        return mUpnpFinshUtil;
    }

    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void clearActivity() {
        mList.clear();
    }

    public void finishAllActivity() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            clearActivity();
        }

    }
}

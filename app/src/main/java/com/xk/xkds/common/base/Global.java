package com.xk.xkds.common.base;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.umeng.analytics.AnalyticsConfig;

/**
 * Created by on 2017/2/8.
 */

public class Global {
    private static Toast mToast;
    private static Handler mHandler = new Handler();
    public static Context mContext;
    /** 屏幕宽度 */
    public static float mScreenWidth;
    /** 屏幕高度 */
    public static float mScreenHeight;
    /** 屏幕密度 */
    public static float mDensity;
    public static boolean isDebug = false;//发布出去的时候必须改成false
    public static String channel  ="guan fang" ;
    public static Handler getHandler() {
        return mHandler;
    }
    public static void init(Context context) {
        mContext = context;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mScreenHeight = dm.heightPixels;
        mScreenWidth = dm.widthPixels;
        mDensity = dm.density;
        channel = AnalyticsConfig.getChannel(mContext);
    }
    /**
     * dp to px
     */
    public static int dp2px(int dp) {
        return (int) (dp * mDensity + 0.5f);
    }
    /**
     * px to dp
     */
    public static int px2dp(int px) {
        return (int) (px / mDensity + 0.5f);
    }
    /**
     * 判断当前的不是再主线程运行
     */
    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
    /**
     * 运行在主线程
     */
    public static void runOnMainThread(Runnable run) {
        if (isMainThread()) {
            run.run();
        } else {
            mHandler.post(run);
        }
    }
    /**
     * Toast显示再子线程可以调用
     */
    public static void showToast(final String text) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
                }
                mToast.setText(text);
                mToast.show();
            }
        });
    }
    public static View inflate(int resId, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(resId, parent,false);
        return view;
    }


}

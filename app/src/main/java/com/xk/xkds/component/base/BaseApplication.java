package com.xk.xkds.component.base;

import android.app.Activity;
import android.app.Application;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.dangbei.euthenia.manager.DangbeiAdManager;
import com.umeng.analytics.MobclickAgent;
import com.xk.xkds.common.base.ExceptionHandler;
import com.xk.xkds.common.base.Global;
import com.xk.xkds.common.receiver.LocationListener;
import com.xk.xkds.common.utils.ChannelUtils;
import com.xk.xkds.common.utils.FinshUtil;

/**
 * Created by  on 2017/2/8.
 */

public class BaseApplication extends Application {
    private Activity mActivity;
    private static BaseApplication application;
    public LocationClient mLocationClient = null;
    private LocationListener myListener = new LocationListener();
    public static BaseApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        Global.init(this);
        ExceptionHandler.getInstance().init(this);
        //友盟统计
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        //当贝广告
//        DangbeiAdManager.init(this, "hm7Woq62QMpZpbfieV2Zn9ihlUVWuhzJJvNbbuHVvWNzAqav",
//                "6lzUs2IAIgECGLnf", ChannelUtils.getChannelName(this));
        /*
        百度定位功能先注销掉不使用
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        */
    }

    // 设置当前活动
    public void setCurrentActivity(Activity activity) {
        FinshUtil.getInstance().addActivity(activity);
        this.mActivity = activity;
    }

    // 获取当前活动
    public Activity getActivity() {
        return mActivity;
    }
}


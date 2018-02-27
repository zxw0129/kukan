package com.xk.xkds.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.xk.xkds.common.base.Global;
import com.xk.xkds.common.utils.LogUtlis;
import com.xk.xkds.common.utils.NetworkUtil;


/**
 * 监听wifi连接状态改变的广播
 * 断开会执行2次
 */
public class WifiStateReceiver extends BroadcastReceiver {

    /**
     * wifi发生改变
     **/
    public static final String ACTION_CHANGE_WIFIINFO = "action_change_wifiinfo";
    /**
     * 广播意图wifi名称
     **/
    public static final String INTENT_WIFI_NAME = "intent_wifi_name";
    /**
     * 广播意图ip地址
     **/
    public static final String INTENT_WIFI_IPADDRESS = "intent_wifi_ipaddress";
    /*Handler mhandler = new Handler(TvServiceApp.mContext.getMainLooper());*/

    @Override
    public void onReceive(Context context, Intent intent) {
        String LOG_TAG = context.getClass().getSimpleName();
        if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            LogUtlis.getInstance().showLogE("=======================");
            if (wifiState == WifiManager.WIFI_STATE_DISABLING) {
                LogUtlis.getInstance().showLogE("wifi关闭");
            } else if (wifiState == WifiManager.WIFI_STATE_ENABLED) {
                LogUtlis.getInstance().showLogE("wifi开启");
            }
        }
        if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {//wifi连接上与否
            LogUtlis.getInstance().showLogE("---------------------");
            LogUtlis.getInstance().showLogE("网络状态改变");
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
                LogUtlis.getInstance().showLogE("wifi网络连接断开");
                Global.showToast("wifi网络连接断开");

            } else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
                LogUtlis.getInstance().showLogE("wifi重新连接");
                Global.showToast("wifi重新连接");
            }
        }
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            if (NetworkUtil.isConnected(context)) {
                //获取当前wifi名称
                String connectWifiSSID = NetworkUtil.getConnectWifiSSID(context);
                String ipAddress = NetworkUtil.getLocalIpAddress(context);
                LogUtlis.getInstance().showLogE("wifi网络已连接" + connectWifiSSID + " / " + ipAddress);
                Intent intent_changeWifi = new Intent();
                intent.setAction(ACTION_CHANGE_WIFIINFO);
                context.sendBroadcast(intent_changeWifi);
            }
        }
    }

}

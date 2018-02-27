package com.xk.xkds.component.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dangbei.euthenia.manager.DangbeiAdManager;
import com.dangbei.euthenia.manager.OnAdDisplayListener;
import com.dangbei.euthenia.ui.IAdContainer;
import com.xk.xkds.R;
import com.xk.xkds.common.base.Global;
import com.xk.xkds.common.manager.DataCleanManager;
import com.xk.xkds.common.manager.HttpManger;
import com.xk.xkds.common.manager.IHttpResponse;
import com.xk.xkds.common.utils.AppUtil;
import com.xk.xkds.common.utils.ChannelResourceUtils;
import com.xk.xkds.common.utils.GsonUtil;
import com.xk.xkds.common.utils.LogUtlis;
import com.xk.xkds.common.utils.NetworkUtil;
import com.xk.xkds.common.utils.SpUtils;
import com.xk.xkds.component.base.BaseActivity;
import com.xk.xkds.entity.AddressEntity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

import static com.xk.xkds.common.base.Global.mContext;


/**
 * Created on 2017/2/8.
 * 广告初始化页面之内的
 */

public class SplashActivity extends BaseActivity implements IHttpResponse {
    private final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 2;
    private final int PERMISSIONS_REQUEST_RW_SDCARD = 3;
    private boolean connected;
    private ImageView mImageview;
    private int delayTime = 4000;
    private int versionCode;
    private int installed;
    private Handler handler;
    private int timeCount;
    private String localIpAddress;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_splash;
    }

    @Override
    public void initIntent(Intent intent) {
        //加载数据,先获取ip
//        if (NetworkUtil.isWifi(this)) {
//            localIpAddress = NetworkUtil.getLocalIpAddress(this);
//        } else if (NetworkUtil.isEthernet(this)) {
//            localIpAddress = NetworkUtil.getIpAddressString();
//        } else {
//            localIpAddress = "00.00.00.00";
//        }
        String url = "http://ip.taobao.com/service/getIpInfo2.php?ip=myip";
        LogUtlis.getInstance().showLogE(url);
        HttpManger.getInstace().doGet(url, this);
    }

    @Override
    public void initView() {
        mImageview = (ImageView) findViewById(R.id.iv_explain);
        mImageview.setVisibility(View.VISIBLE);
        TextView tvVerSion = findView(R.id.tv_version);
        tvVerSion.setText("版本号: " + AppUtil.getVersion());
        handler = new Handler();
        String url = "http://ip.taobao.com/service/getIpInfo2.php?ip=myip";
        LogUtlis.getInstance().showLogE(url);
        HttpManger.getInstace().doGet(url, this);
        checkChannel();
        //广告
//        IAdContainer adContainer = DangbeiAdManager.getInstance().createSplashAdContainer(this);
//        adContainer.open();
//        adContainer.setOnAdDisplayListener(new OnAdDisplayListener() {
//            @Override
//            public void onDisplaying() {
//                LogUtlis.getInstance().showLogE("open ad success");
//            }
//
//            @Override
//            public void onFailed(Throwable throwable) {
//                mImageview.setVisibility(View.VISIBLE);
//                LogUtlis.getInstance().showLogE("open ad filed" + throwable.getMessage());
//                throwable.printStackTrace();
//                delayTime = 4000;
//                checkChannel();
//                throwable.printStackTrace();
//            }
//
//            @Override
//            public void onFinished() {
//                delayTime = 100;
//                checkChannel();
//            }
//
//            @Override
//            public void onClosed() {
//                delayTime = 100;
//                checkChannel();
//            }
//
//            @Override
//            public void onTerminated() {
//                delayTime = 100;
//                checkChannel();
//            }
//
//            @Override
//            public void onSkipped() {
//                delayTime = 100;
//                checkChannel();
//            }
//
//            @Override
//            public void onTriggered() {
//
//            }
//        });
    }

    private void checkChannel() {
        timeCount = 0;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean status = ChannelResourceUtils.getInstace().getStatus();
                LogUtlis.getInstance().showLogE("status = " + status + " timeCount = " + timeCount);
                if (status) {
                    gotoMainActivity();
//                    updataAPP();
                } else {
                    timeCount = timeCount + 2000;
                    if (timeCount >= 20 * 1000) {
                        ChannelResourceUtils.getInstace().parseResource();
                        timeCount = 0;
                    }
                    handler.postDelayed(this, 2000);
                }
            }
        }, delayTime);
        Toast.makeText(SplashActivity.this, "正在加载播放列表,请稍候", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void initListener() {
    }


    @Override
    public void initDatas() {
        //检查网络
        connected = NetworkUtil.isConnected(this);
        /**
         * 检查应用权限
         */
        checkPhoneStatePermission();
    }

    @Override
    protected void initNewIntent(Intent intent) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //检查更新
        if (!connected) {
            Global.showToast("加载最新节目源失败,请检查网络");
        }
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(SplashActivity.this, XkdsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view, int id) {

    }

    private void checkPhoneStatePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) ==
                PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }

        if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission
                .WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(SplashActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_RW_SDCARD);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[]
            grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_RW_SDCARD: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager
                        .PERMISSION_GRANTED) {
                    DataCleanManager.cleanInternalCache(mContext);
                    Global.showToast("成功清理应用缓存");
                } else {
                    Global.showToast("读写SD卡失败，请在权限设置中打开");
                }
                break;
            }
            case PERMISSIONS_REQUEST_READ_PHONE_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager
                        .PERMISSION_GRANTED) {
                } else {
                    Global.showToast("获取盒子信息失败,请在权限设置中打开");
                }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onFailure(Call call, IOException e) {
        ChannelResourceUtils.getInstace().parseResource();
    }

    @Override
    public void onSuccess(Call call, Response response) throws IOException {
        if (response.body() != null) {
            String string = response.body().string();
            if (string != null) {
                LogUtlis.getInstance().showLogE(string);
                if (string.contains("ip")) {
                    AddressEntity parse = GsonUtil.parse(string, AddressEntity.class);
                    //空判断，数据异常
                    if( null != parse && null != parse.getData() )
                    {
                        SpUtils.getInstance().saveProvince(parse.getData().getRegion());
                    }
                    ChannelResourceUtils.getInstace().parseResource();
                    return;
                }
            }
        }
        ChannelResourceUtils.getInstace().parseResource();
    }
}

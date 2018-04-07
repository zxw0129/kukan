package com.xk.xkds.component.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dangbei.euthenia.manager.DangbeiAdManager;
import com.dangbei.euthenia.manager.OnAdDisplayListener;
import com.dangbei.euthenia.ui.IAdContainer;
import com.socks.library.KLog;
import com.xk.xkds.R;
import com.xk.xkds.common.base.Global;
import com.xk.xkds.common.manager.DataCleanManager;
import com.xk.xkds.common.manager.HttpManger;
import com.xk.xkds.common.manager.IHttpResponse;
import com.xk.xkds.common.utils.AppUtil;
import com.xk.xkds.common.utils.ChannelResourceUtils;
import com.xk.xkds.common.utils.GetRealClientIp;
import com.xk.xkds.common.utils.GsonUtil;
import com.xk.xkds.common.utils.LogUtlis;
import com.xk.xkds.common.utils.NetworkUtil;
import com.xk.xkds.common.utils.SpUtils;
import com.xk.xkds.component.dialog.MyDialog;
import com.xk.xkds.entity.AddressEntity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

import static com.xk.xkds.common.base.Global.init;
import static com.xk.xkds.common.base.Global.mContext;

public class SplashActivity1 extends Activity implements GetRealClientIp.IpInfoListener
{
    private String TAG = "SplashActivity1";
    private final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 2;
    private final int PERMISSIONS_REQUEST_RW_SDCARD = 3;
    private boolean connected;
    //背景图片
    private ImageView mIvExplain;
    private int delayTime = 2000;
    private int timeCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initViews();
        /**
         * 检查应用权限
         */
        boolean checkPermission = checkPhoneStatePermission();
        if( checkPermission )
        {
            dangbeiAd();
            // initData();
        }
        else
        {
            LogUtlis.getInstance().showLogE("oncreate 111");
        }
    }

    private void initViews()
    {
        mIvExplain = (ImageView) findViewById(R.id.iv_explain);
        mIvExplain.setVisibility(View.VISIBLE);
        TextView tvVerSion = (TextView) findViewById(R.id.tv_version);
        tvVerSion.setText("版本号: " + AppUtil.getVersion());
        return;
    }

    private void showErrorExitDialog(String message)
    {
        MyDialog mNoNetworkDialog = new MyDialog(this);
        mNoNetworkDialog.setContent(message);
        mNoNetworkDialog.setTitleIcon(getString(R.string.fa_warning));
        mNoNetworkDialog.setOkText(getString(R.string.action_ok));
        mNoNetworkDialog.setOnOkClickListener(new MyDialog.OkOnClickListener()
        {
            @Override
            public void onClick()
            {
                finish();
            }

            @Override
            public void onCancel()
            {

            }
        });
        mNoNetworkDialog.showMyDialog();
        return;
    }

    private boolean checkPhoneStatePermission()
    {
        boolean isPermissionsAllowed = true;
        //        if( ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED )
        //        {
        //            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSIONS_REQUEST_READ_PHONE_STATE);
        //            isPermissionsAllowed = false;
        //        }

        if( ContextCompat.checkSelfPermission(SplashActivity1.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(SplashActivity1.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_RW_SDCARD);
            isPermissionsAllowed = false;
        }
        return isPermissionsAllowed;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        boolean allowed = true;
        switch( requestCode )
        {
            case PERMISSIONS_REQUEST_RW_SDCARD:
            {
                if( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED )
                {
                    DataCleanManager.cleanInternalCache(mContext);
                    Global.showToast("成功清理应用缓存");
                }
                else
                {
                    allowed = false;
                    showErrorExitDialog("读写SD卡失败，请在权限设置中打开");
                }
                break;
            }
            //            case PERMISSIONS_REQUEST_READ_PHONE_STATE:
            //            {
            //                if( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED )
            //                {
            //                }
            //                else
            //                {
            //                    allowed = false;
            //                    showErrorExitDialog("获取盒子信息失败,请在权限设置中打开");
            //                }
            //                break;
            //            }
        }
        if( allowed )
        {
            initData();
        }
    }

    private void initData()
    {
        connected = NetworkUtil.isConnected(this);
        if( connected )
        {
            //检测当前IP对应的地址
            //            checkIpLocation();
            GetRealClientIp getIpBean = new GetRealClientIp();
            getIpBean.setListener(this);
            new Thread(getIpBean).start();
        }
        else
        {
            showErrorExitDialog(getString(R.string.no_network_tip));
        }
        return;
    }

    private void checkChannel()
    {
        timeCount = 0;
        mHandler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                boolean status = ChannelResourceUtils.getInstace().getStatus();
                if( status )
                {
                    gotoMainActivity();
                    //                    updataAPP();
                }
                else
                {
                    timeCount = timeCount + 1000;
                    if( timeCount >= 20 * 1000 )
                    {
                        ChannelResourceUtils.getInstace().parseResource();
                        timeCount = 0;
                    }
                    mHandler.postDelayed(this, 1000);
                }
            }
        }, delayTime);
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(SplashActivity1.this, "正在加载播放列表,请稍候", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void gotoMainActivity()
    {
        Intent intent = new Intent(this, XkdsActivity.class);
        startActivity(intent);
        finish();
    }

    Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
        }
    };

    @Override
    public void retData(String ret)
    {
        if( !TextUtils.isEmpty(ret) )
        {
            try
            {
                AddressEntity parse = GsonUtil.parse(ret, AddressEntity.class);
                //空判断，数据异常
                if( null != parse && null != parse.getData() )
                {
                    SpUtils.getInstance().saveProvince(parse.getData().getRegion());
                }
            }catch( Exception e )
            {
            }
        }
        ChannelResourceUtils.getInstace().parseResource();
        checkChannel();
        return;
    }

    private void dangbeiAd()
    {
        IAdContainer adContainer = DangbeiAdManager.getInstance().createSplashAdContainer(this);
        adContainer.setOnAdDisplayListener(new OnAdDisplayListener()
        {
            @Override
            public void onDisplaying()
            {
                LogUtlis.getInstance().showLogE("open ad success");
            }

            @Override
            public void onFailed(Throwable throwable)
            {
                LogUtlis.getInstance().showLogE("ad onFailed");
                throwable.printStackTrace();
                initData();
            }

            @Override
            public void onFinished()
            {
                initData();
                LogUtlis.getInstance().showLogE("ad onFinished");
            }

            @Override
            public void onClosed()
            {
                initData();
            }

            @Override
            public void onTerminated()
            {
            }

            @Override
            public void onSkipped()
            {
                initData();
            }

            @Override
            public void onTriggered()
            {
                initData();
            }
        });
        adContainer.open();
    }
}

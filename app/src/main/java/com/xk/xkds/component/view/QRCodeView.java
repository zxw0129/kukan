package com.xk.xkds.component.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xk.xkds.R;
import com.xk.xkds.common.base.Global;
import com.xk.xkds.common.base.NetMessage;
import com.xk.xkds.common.utils.LogUtlis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created  on 2017/4/17.
 */

public class QRCodeView extends LinearLayout {
        private int twentySecond = 6000;
//    private int twentySecond = 600000;
    private int showTime = 6000;
    //    private  int showTime=6000;
    private LinearLayout mQRCode;
    private TextView mTVQRCode_content;
    private ImageView mIvQRCode;
    private Handler handler;
    private Runnable runnable;
    private HttpURLConnection conn;
    private String content = "";
    private String eqUrl = "";
    private View inflate;
    private Runnable missRun;

    public QRCodeView(Context context) {
        this(context, null);
    }

    public QRCodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public QRCodeView(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate = LayoutInflater.from(context).inflate(R.layout.view_qr_code, this);
        mQRCode = (LinearLayout) findViewById(R.id.llt_item_qrcode);
        mTVQRCode_content = (TextView) findViewById(R.id.tv_qrcode_content);
        mIvQRCode = (ImageView) findViewById(R.id.iv_qr_code);
        handler = new Handler();
        missRun = new Runnable() {
            @Override
            public void run() {
                mQRCode.setVisibility(GONE);
            }
        };
        runnable = new Runnable() {
            @Override
            public void run() {
                if (eqUrl != null && content != null && !eqUrl.equals("")) {
                    Glide.with(Global.mContext).load(eqUrl).into(mIvQRCode);
                    //介绍不显示了
//                    mTVQRCode_content.setText(content);
                    mQRCode.setVisibility(VISIBLE);
                    Global.getHandler().postDelayed(missRun, showTime);
                } else {
                    loaddates();
                }
                handler.postDelayed(this, twentySecond);
            }
        };
        startRun();

    }

    public void startRun() {
        handler.postDelayed(runnable, 20000);
    }

    public void stopRun() {
        handler.removeCallbacks(runnable);
    }

    private void loaddates() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = null;
                    URL url = new URL(NetMessage.ERCODEURL);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.connect();
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        //从连接对象中获取字节输入流，
                        is = conn.getInputStream();
                        BufferedReader bf = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                        String line = "";
                        while ((line = bf.readLine()) != null) {
                            String string = new String(line.getBytes(), "UTF-8");
                            String[] split = string.split(",");
                            if (split.length < 2) continue;
                            eqUrl = split[0];
//                            content = split[1];
                            try {
                                showTime = Integer.parseInt(split[1]);
                                twentySecond = Integer.parseInt(split[2]);
                                LogUtlis.getInstance().showLogE("showTime = " + showTime + " " +
                                        "twentySecond = " + twentySecond);
                            } catch (Exception e) {

                            }
                            LogUtlis.getInstance().showLogE("has load ad");
                        }
                        bf.close();
                        is.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }
}

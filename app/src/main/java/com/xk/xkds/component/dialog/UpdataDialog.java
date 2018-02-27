package com.xk.xkds.component.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xk.xkds.R;
import com.xk.xkds.common.base.Global;
import com.xk.xkds.common.base.NetMessage;
import com.xk.xkds.common.manager.FileManager;
import com.xk.xkds.common.utils.AppUtil;
import com.xk.xkds.common.utils.UpdataUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created  on 2017/3/3.
 */

public class UpdataDialog extends DialogFragment implements View.OnKeyListener {
    private View parent;
    private TextView mTvBack;
    private TextView mTvCancel;
    private TextView mTvTitle;
    private ProgressBar mProgressBar;
    private String path;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(R.color.Dialog);
        parent = inflater.inflate(R.layout.dialog_updata, container, false);
        mTvBack = (TextView) parent.findViewById(R.id.tv_dialog_updata_yes);
        mTvCancel = (TextView) parent.findViewById(R.id.tv_dialog_updata_no);
        mTvTitle = (TextView) parent.findViewById(R.id.tv_dialog_updata_title);
        mProgressBar = (ProgressBar) parent.findViewById(R.id.pb_dialog_updata);
        mProgressBar.setVisibility(View.GONE);
        mTvBack.requestFocus();
        mTvBack.setOnKeyListener(this);
        mTvCancel.setOnKeyListener(this);
//        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+NetMessage.getAPkName();
        path= FileManager.getApksDownloadPath(Global.mContext)+NetMessage.getAPkName();
        return parent;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            if (v.getId() == R.id.tv_dialog_updata_yes) {
                mTvTitle.setText("更新中");
                mTvBack.setVisibility(View.INVISIBLE);
                mTvCancel.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                //更新
                updataServer(path);
//                installApk();
            } else if (v.getId() == R.id.tv_dialog_updata_no) {
                //不更新
                gotoMainActivity();
            }
        }
        if (keyCode ==KeyEvent.KEYCODE_BACK){
            gotoMainActivity();
        }
        return false;
    }

    private void installApk(String path) {
        File file = new File(path);
        if (AppUtil.isFullApk(Global.mContext, file)) {
            AppUtil.install(Global.mContext, file);
        } else {
            Global.showToast("更新失败,请稍后重试");
            gotoMainActivity();
        }
    }

    /***
     * 更新 apk
     * @param path
     */
    private void updataServer(final String path) {
        if (checkFileExists(path)) {
            installApk(path);
        } else { //下载
            new Thread(new Runnable() {
                public int downFileSize;
                public int fileSize = 0;

                @Override
                public void run() {
                    try {
                        InputStream is = null;
//                        URL url = new URL(NetMessage.getUrl(Global.channel));
                        URL url = new URL(UpdataUtils.updaUrl);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setConnectTimeout(5000);
                        conn.connect();
                        int code = conn.getResponseCode();
                        if (code == 200) {
                            fileSize = conn.getContentLength();
                            is = conn.getInputStream();
                            File file = new File(path);
                            FileOutputStream fos = new FileOutputStream(file);
                            byte[] byteStr = new byte[1024];
                            int len = 0;
                            while ((len = is.read(byteStr)) > 0) {
                                fos.write(byteStr, 0, len);
                                if (downFileSize != fileSize) {
                                    downFileSize += len;
                                    int progress = (int) (downFileSize * 100.0 / fileSize);
                                    mProgressBar.setProgress(progress);
                                }
                            }
                            is.close();
                            fos.flush();
                            fos.close();
                        }
                        installApk(path);
                    } catch (IOException e) { //更新出错,跳转到首页
                        e.printStackTrace();
                        gotoMainActivity();
                    }
                }
            }).start();
        }
    }

    private boolean checkFileExists(String path) {
        File file = new File(path);
        if (file.isFile() && file.exists()) {
            return true;
        }
        return false;
    }

    private void gotoMainActivity() {
        dismiss();
   /*     Intent intent = new Intent(Global.mContext, XkdsActivity.class);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        Global.mContext.startActivity(intent);*/
    }

}

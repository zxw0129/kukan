package com.xk.xkds.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.xk.xkds.common.base.Global;
import com.xk.xkds.common.base.NetMessage;
import com.xk.xkds.common.manager.HttpManger;
import com.xk.xkds.common.manager.IHttpResponse;
import com.xk.xkds.entity.ADBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created  on 2017/3/6.
 */

public class UpdataUtils {
    private static HttpURLConnection conn;
    public static String updaUrl = "";
    public static int channelCount = 0;
    public static String visionNameNew = "";
    private static boolean isUpdataSuccess = true;
    private static int downFileSize = 0;
    private static int fileSize = -1;
    private static ADBean adBean = null;

    /***
     * 检查广告
     * @return
     */
    public static ADBean getAdBean() {
        if (null == adBean) {
            adBean = new ADBean();
            checkADBean();
            return adBean;
        }
        return adBean;
    }

    public static void resetADBean() {
        if (adBean != null) {
            adBean = null;
        }
    }

    private static void checkADBean() {
        HttpManger.getInstace().doGet(NetMessage.ADURL, new IHttpResponse() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onSuccess(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                if ( null == body )
                {
                    LogUtlis.getInstance().showLogE("checkADBean--->body is null");
                    return;
                }
                String string = body.string();
                if ( TextUtils.isEmpty(string) )
                {
                    LogUtlis.getInstance().showLogE("checkADBean--->string is null");
                    return;
                }
                LogUtlis.getInstance().showLogE("checkADBean--->AD infO:"+string);
                adBean = GsonUtil.parse(string, ADBean.class);
                LogUtlis.getInstance().showLogE(string);
            }
        });
    }

    /**
     * 下载 广告apk
     *
     * @param path 本地文件路径,updaUrl 下载路径
     */
    public static void updataServer(final String path, final String updaUrl) {
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
                        URL url = new URL(updaUrl);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setConnectTimeout(5000);
                        conn.connect();
                        int code = conn.getResponseCode();
                        if (code == 200) {
                            fileSize = conn.getContentLength();
                            is = conn.getInputStream();
                            File file = new File(path);
                            file.delete();
                            file.createNewFile();
                            FileOutputStream fos = new FileOutputStream(file);
                            byte[] byteStr = new byte[1024];
                            int len = 0;
                            while ((len = is.read(byteStr)) > 0) {
                                fos.write(byteStr, 0, len);
                            }
                            is.close();
                            fos.flush();
                            fos.close();
                        }
                        installApk(path);
                    } catch (IOException e) { //更新出错,跳转到首页
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static boolean checkFileExists(String path) {
        File file = new File(path);
        if (file.isFile() && file.exists()) {
            if (isFullApk(Global.mContext, file)) {
                return true;
            }
        }
        return false;
    }

    public static void installApk(String path) {
        File file = new File(path);
        if (AppUtil.isFullApk(Global.mContext, file)) {
            AppUtil.install(Global.mContext, file);
        }
    }

    public static boolean isFullApk(Context context, File apkFile) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info;
        try {
            info = pm.getPackageArchiveInfo(apkFile.getAbsolutePath(), PackageManager
                    .GET_ACTIVITIES);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return info != null;
    }
}

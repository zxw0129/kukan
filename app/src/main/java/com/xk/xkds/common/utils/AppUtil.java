package com.xk.xkds.common.utils;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.xk.xkds.common.base.Global;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZWF on 2015/12/30.
 * app工具类
 */
public class AppUtil {

    private static final String LOG_TAG = "AppUtil";

    public static final int NO_APP = 0;
    public static final int SAME_APP = 1;
    public static final int HIGHER_VERSION = 2;
    public static final int LOWER_VERSION = 3;

    /**
     * 检查是否安装对应应用
     *
     * @param context     上下文
     * @param packageName 包名
     * @param versionCode 版本号
     * @return (0表示应用未安装, 1表示相同版本, 2表示单前播放器版本大于传过来的版本号, 3表示单前播放器版本小于等于传过来的版本号)
     */
    public static int isInstalled(Context context, String packageName, int versionCode) {
        int contrastCode = 0;
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            if (TextUtils.equals(packageInfo.packageName, packageName)) {
                LogUtlis.getInstance().showLogE("当前版本" + packageInfo.versionCode);
                if (packageInfo.versionCode == versionCode) {
                    contrastCode = SAME_APP;
                } else {
                    // 当前版本大于等于传过来的版本号
                    if (packageInfo.versionCode > versionCode) {
                        contrastCode = HIGHER_VERSION;
                    } else {// 当前版本小于等于传过来的版本号
                        contrastCode = LOWER_VERSION;
                    }
                    break;
                }
            }
        }
        return contrastCode;
    }

    /**
     *判断应用是否安装
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isInstalled(Context context, String packageName) {
        PackageInfo packageInfo =null;
        try {
             packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (Exception e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if(packageInfo ==null){
         return  false;
        }else{
         return true;
        }
    }

    /**
     * 根据包名获取版本号
     *
     * @param context
     * @param packageName
     * @return
     */
    public static int versionCodeByPkgName(Context context, String packageName) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            if (TextUtils.equals(packageInfo.packageName, packageName)) {
                return packageInfo.versionCode;
            }
        }
        return -1;
    }

    /**
     * 安装apk文件
     *
     * @param context
     * @param apkFile 文件
     */
    public static void install(Context context, File apkFile) {
        String chmodCmd = "chmod 666 " + apkFile.getAbsolutePath();
        try {
            Runtime.getRuntime().exec(chmodCmd);
        } catch (Exception e) {
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }


    /**
     * 卸载指定App
     *
     * @param context     上下
     * @param packageName 包名
     */
    public static void uninstall(Context context, String packageName) {
        Uri packageURI = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, packageURI);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 验证apk文件是否损坏
     *
     * @param context
     * @param apkFile
     * @return
     */
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

    /**
     * 获取本应用信息
     *
     * @param context
     * @return
     */
    public static PackageInfo getAppVersionInfo(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packageInfo;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取app应用信息
     *
     * @param context
     * @return
     */
    public static PackageInfo getPackageInfo(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
            return packageInfo;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 2  * 获取版本号
     * 3  * @return 当前应用的版本号
     * 4
     */
    public static String getVersion() {
        try {
            PackageManager manager = Global.mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(Global.mContext.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 通过包名打开App应用
     *
     * @param context     上下文
     * @param packageName 要打开的App包名
     */
    public static void openApp(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        context.startActivity(intent);
    }

    public static void drawableTofile(Drawable drawable, String path) {
        //Log.i(TAG, "drawableToFile:"+path);
        File file = new File(path);

        if (file.exists()) {
            return;
        }

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
            fos.write(bitmapdata);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断app是否为系统应用
     **/
    public static boolean isSystemApp(ApplicationInfo appInfo) {
        return (appInfo.flags & appInfo.FLAG_SYSTEM) > 0;
    }

    /**
     * 判断app是否为系统应用
     * uid是应用程序安装时由系统分配(1000 ～ 9999为系统应用程序保留)
     **/
    private static boolean isSystemApp(int uid) {
        return uid > 1000;
    }

    /**
     * 获取系统中的全部包信息
     *
     * @param context 应用上下文
     */
    public static void getPackageInfos(Context context) {

        PackageManager pm = context.getPackageManager();

        List<PackageInfo> packageInfos = pm.getInstalledPackages(0);

        for (PackageInfo packageInfo : packageInfos) {
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            String packageName = packageInfo.packageName;
            String appName = applicationInfo.loadLabel(pm).toString();

            if (isSystemApp(applicationInfo)) {
                Log.d(LOG_TAG, "系统应用:" + appName + "  包名:" + packageName);
            } else {
                Log.d(LOG_TAG, "安装应用:" + appName + "  包名:" + packageName);
            }
        }
    }

    /**
     * 获取系统中的全部包信息
     *
     * @param context 应用上下文
     */
    public static void getThirdAppInfo(Context context) {

        PackageManager pm = context.getPackageManager();

        List<PackageInfo> packageInfos = pm.getInstalledPackages(0);

        for (PackageInfo packageInfo : packageInfos) {
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            String packageName = packageInfo.packageName;
            String appName = applicationInfo.loadLabel(pm).toString();

            if (!isSystemApp(applicationInfo)) {

            }
        }
    }

    /**
     * 通过包名获取应用名称
     *
     * @param context     上下文
     * @param packageName 包名
     * @return 应用名称
     */
    public static String getAppName(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, PackageManager
                    .GET_META_DATA);
            return applicationInfo.loadLabel(pm).toString();
//            return pm.getApplicationLabel(pm.getApplicationInfo(packageName, PackageManager
// .GET_META_DATA)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "获取应用名失败";
    }

    public static Drawable getAppIcon(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, PackageManager
                    .GET_META_DATA);
            return applicationInfo.loadIcon(pm);
//            return pm.getApplicationLabel(pm.getApplicationInfo(packageName, PackageManager
// .GET_META_DATA)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getAppVersionCode(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            return pm.getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA)
                    .versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getFileNameForDownloadApk(String packageName, int versionCode) {
        return packageName + "_" + versionCode + ".apk";
    }

    /**
     * 获取Mac地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        //在wifi未开启状态下，仍然可以获取MAC地址，但是IP地址必须在已连接状态下否则为0
        String macAddress = null, ip = null;
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
        if (null != info) {
            macAddress = info.getMacAddress().replace(":", "").toUpperCase();
        }
        return macAddress;
    }

    /**
     * 获取服务器自定义的设备id
     */
    public static String getServerDeviceId(Context context) {
        String mac = getMac();
        if (mac == null) {
            WifiManager wifiMng = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiMng.getConnectionInfo();
            if (wifiInfo != null) {
                String macAddress = wifiInfo.getMacAddress();
                if (macAddress != null) {
                    mac = macAddress.replace(":", "").toUpperCase();
                }
            }
        }
        return mac;
    }

    /**
     * 获取当前设备
     *
     * @return MAC
     */
    public static String getMac() {
        String macSerial = null;
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim().replace(":", "").toUpperCase();
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return macSerial;
    }

    public static String getCPU() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return Build.SUPPORTED_ABIS[0] + Build.SUPPORTED_ABIS[1];
        } else {
            return Build.CPU_ABI;
        }

    }

    public static String GetUsedCPUPercent() {
        String Result;
        StringBuffer tv = new StringBuffer();
        try {
            Process p = Runtime.getRuntime().exec("top -n 1");

            BufferedReader br = new BufferedReader(new InputStreamReader
                    (p.getInputStream()));
            while ((Result = br.readLine()) != null) {
                if (Result.trim().length() < 1) {
                    continue;
                } else {
                    String[] CPUusr = Result.split("%");
                    //tv.append("USER:" + CPUusr[0] + "\n");
                    String[] CPUusage = CPUusr[0].split("User");
                    String[] SYSusage = CPUusr[1].split("System");
                    //tv.append("CPU:" + CPUusage[1].trim() + " length:" + CPUusage[1].trim()
                    // .length() + "\n");
                    //tv.append("SYS:" + SYSusage[1].trim() + " length:" + SYSusage[1].trim()
                    // .length() + "\n");
                    tv.append(Result + "\n");
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tv.toString();
    }

    /**
     * 获取android系统版本号
     *
     * @return
     */
    public static String getVersionRelease() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取正在运行的应用信息
     *
     * @param m_context
     * @return
     */
    public static ArrayList<String> GetCurrentRunningPrograms(Context m_context) {
        ActivityManager activityManager = (ActivityManager) m_context.getSystemService(Context
                .ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processList = activityManager
                .getRunningAppProcesses();
        ArrayList<String> runningProcessNameList = new ArrayList();

        if (processList != null) {
            for (ActivityManager.RunningAppProcessInfo process : processList) {
                if (process.processName.equals("system") || process.processName.equals("com" +
                        ".android.phone")) {
                    continue;
                }
                ;
                runningProcessNameList.add(process.processName);
            }
        }
        return runningProcessNameList;

    }

    /**
     * 检测该包名所对应的应用是否存在
     *
     * @param packageName
     * @return
     */
    public static boolean checkPackage(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName))
            return false;
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager
                    .PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = null;
            FileReader fstream = null;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            if (fstream != null) {
                try {
                    in = new BufferedReader(fstream, 1024);
                    mac = in.readLine();
                } catch (IOException e) {
                } finally {
                    if (fstream != null) {
                        try {
                            fstream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

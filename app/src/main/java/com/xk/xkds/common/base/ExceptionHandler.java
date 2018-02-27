package com.xk.xkds.common.base;

import android.content.Context;
import android.os.Environment;

import com.xk.xkds.common.utils.LogUtlis;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created  on 2017/2/16.
 */

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final String ERROR_PATH = "/XKDS/error/";
    //格式化日期,作为错误日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private static ExceptionHandler exceptionHandler;
    private Context context;
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private ExceptionHandler() {
    }

    public static ExceptionHandler getInstance() {
        if (exceptionHandler == null) {
            exceptionHandler = new ExceptionHandler();
        }
        return exceptionHandler;
    }

    public void init(Context context) {
        this.context = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        handleException(ex);
        try {
            Thread.sleep(2000);

        } catch (InterruptedException e) {
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }
        /**收集设备信息*/
//        collectDeviceInfo();
//		putErrer(ex);
        /**保存错误日志到文件*/
        saveErrorInfotoFile(ex);
        return true;
    }

    private String saveErrorInfotoFile(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            String time = formatter.format(new Date());
            String fileName = "excption_" + time + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory() + ERROR_PATH;
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            LogUtlis.getInstance().showLogE("an error occured while writing file...");
        }
        return null;
    }

}

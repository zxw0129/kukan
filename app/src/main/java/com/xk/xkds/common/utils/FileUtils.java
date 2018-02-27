package com.xk.xkds.common.utils;

import android.os.Environment;

import com.xk.xkds.entity.ChannelBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created  on 2017/3/3.
 * 读取文件
 */

public class FileUtils {
    private static final String FILE_NAME = "/channel.txt";
    private static final String FILE_PATH = "/XKDS/channel";
    private static ArrayList<ChannelBean> mList;
    public static boolean newCopy =false;

    /***
     * 获取U盘里面的 channel.text 文件,并且存入 本地
     *
     * @return
     */
    public static boolean copyFile(String sdCardPath) {
        try {
            //检查文件是不是存在
            String SdFilePath = sdCardPath + FILE_NAME;
            File sdFile = new File(SdFilePath);
            if (sdFile.exists()) {//存在就继续
                String targetFile = Environment.getExternalStorageDirectory() + FILE_PATH;
                //目标目录
                File targetDir = new File(targetFile);
                //创建目录
                if (!targetDir.exists()) {
                    targetDir.mkdirs();
                }
                LogUtlis.getInstance().showLogE(" copyFile targetFilePath " + targetFile +
                        FILE_NAME);
                LogUtlis.getInstance().showLogE(" copyFile SdFilePath" + SdFilePath);
                if (CopySdcardFile(SdFilePath, targetFile + FILE_NAME) == 0) { //写入成功,开始添加到map集合
//                    //读取 本地,并添加到集合
//                    new FileInputStream(targetFile + FILE_NAME);
                    return true;
                } else {
//                Global.showToast("写入本地错误");
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void readTextFromSDcard(FileInputStream is) throws IOException {
        mList = new ArrayList<>();
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            ChannelBean bean = new ChannelBean();
            String string = new String(str.getBytes(), "UTF-8");
            String[] split = string.split(",");
            if (split.length < 2) {
                continue;
            }
            int type = 9; //台 类型
            bean.setChannelType(type);
            bean.setChannelName(split[0]);
            bean.setChannelUrl(split[1]);
            mList.add(bean);
//         LogUtlis.getInstance().showLogE(string);
        }
        bufferedReader.close();
        reader.close();
        is.close();

    }

    /**
     * 获取自定义频道集合
     */
    public static ArrayList<ChannelBean> getSelfList() {
        if (mList == null) {
            try {
                readTextFromSDcard(new FileInputStream(Environment.getExternalStorageDirectory()
                        + FILE_PATH + FILE_NAME));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mList;
    }
    public static boolean SelfFileExsit(){
        File file = new File(Environment.getExternalStorageDirectory() + FILE_PATH + FILE_NAME);
        if (file.exists()){
            return true;
        }else {
            return false;
        }
    }
    public static void cleanList(){
        if (mList!=null){
            mList.clear();
            mList=null;
        }
    }

    /***
     * 删除本地文件
     *
     * @return
     */
    public static boolean deletailFile() {
        File file = new File(Environment.getExternalStorageDirectory() + FILE_PATH + FILE_NAME);
        if (file.isFile() && file.exists()) {
//            Global.showToast("已经为您清空自定义频道");
            return file.delete();
        }
        return false;
    }

    /***
     * 从U盘存入本地
     *
     * @param fromFile
     * @param toFile
     * @return
     */
    public static int CopySdcardFile(String fromFile, String toFile) {
        try {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return 0;

        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }
}

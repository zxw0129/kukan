package com.xk.xkds.common.utils;

import android.os.AsyncTask;
import android.os.Environment;

import com.xk.xkds.R;
import com.xk.xkds.common.base.Global;
import com.xk.xkds.entity.ChannelBean;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created  on 2017/3/2.
 */

public class HttpUtils {
    private static HttpURLConnection conn;
    public static Boolean updata = true;
    private static final String URL = "http://118.184.38.13/updatainfo.txt";

    public static void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = null;
                    URL url = new URL(URL);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.connect();
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        //从连接对象中获取字节输入流，is中是图片
                        is = conn.getInputStream();
                        readTextFromSDcard(is);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }).start();

    }

    private static void readTextFromSDcard(InputStream is) throws IOException {
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String str;

        while ((str = bufferedReader.readLine()) != null) {
            if (str.contains("0")) {
                updata = false;
            } else {
                updata = true;
            }
        }
//        bufferedReader.close();
//        reader.close();
//        is.close();
    }
}

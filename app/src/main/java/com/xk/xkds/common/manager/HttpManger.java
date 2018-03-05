package com.xk.xkds.common.manager;

import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.webkit.WebChromeClient;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created  LJP on 2017/6/26.
 */

public class HttpManger
{
    private OkHttpClient mOkHttpClient;
    private Request.Builder requestBuilder;
    private Request request;
    private Call mcall;
    private HttpURLConnection conn;

    private HttpManger()
    {
    }

    private static HttpManger instance;

    public static HttpManger getInstace()
    {
        if( instance == null )
        {
            instance = new HttpManger();
        }
        return instance;
    }

    public void doGet(String url, final IHttpResponse iHttpResponse)
    {
        if( mOkHttpClient == null )
        {
            mOkHttpClient = new OkHttpClient();
        }
        requestBuilder = new Request.Builder().url(url);
        requestBuilder.method("GET", null);
        request = requestBuilder.build();
//        requestBuilder.removeHeader("User-Agent").addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");
        mcall = mOkHttpClient.newCall(request);
        mcall.enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                if( iHttpResponse != null )
                {
                    iHttpResponse.onFailure(call, e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                if( iHttpResponse != null )
                {
                    iHttpResponse.onSuccess(call, response);
                }
            }
        });
    }

    public static String GetNetIp()
    {
        String IP = "";
        try
        {
            String address = "http://ip.taobao.com/service/getIpInfo2.php?ip=myip";
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            if( connection.getResponseCode() == HttpURLConnection.HTTP_OK )
            {
                InputStream in = connection.getInputStream();      // 将流转化为字符串
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String tmpString = "";
                StringBuilder retJSON = new StringBuilder();
                while( (tmpString = reader.readLine()) != null )
                {
                    retJSON.append(tmpString + "\n");
                }
                JSONObject jsonObject = new JSONObject(retJSON.toString());
                String code = jsonObject.getString("code");
                if( code.equals("0") )
                {
                    JSONObject data = jsonObject.getJSONObject("data");
                    IP = data.getString("ip") + "(" + data.getString("country") + data.getString("area") + "区" + data.getString("region") + data.getString("city") + data.getString("isp") + ")";
                    Log.e("提示", "您的IP地址是：" + IP);
                }
                else
                {
                    IP = "";
                    Log.e("提示", "IP接口异常，无法获取IP地址！");
                }
            }
            else
            {
                IP = "";
                Log.e("提示", "网络连接异常，无法获取IP地址！");
            }
        }catch( Exception e )
        {
            IP = "";
            Log.e("提示", "获取IP地址时出现异常，异常信息是：" + e.toString());
        }
        return IP;
    }

}

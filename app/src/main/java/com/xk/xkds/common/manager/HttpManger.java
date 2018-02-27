package com.xk.xkds.common.manager;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created  LJP on 2017/6/26.
 */

public class HttpManger {
    private OkHttpClient mOkHttpClient;
    private Request.Builder requestBuilder;
    private Request request;
    private Call mcall;
    private HttpURLConnection conn;

    private HttpManger() {
    }

    private static HttpManger instance;

    public static HttpManger getInstace() {
        if (instance == null) {
            instance = new HttpManger();
        }
        return instance;
    }

    public void doGet(String url, final IHttpResponse iHttpResponse) {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        }
        requestBuilder = new Request.Builder().url(url);
        requestBuilder.method("GET",null);
        request = requestBuilder.build();
        mcall = mOkHttpClient.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (iHttpResponse != null) {
                    iHttpResponse.onFailure(call, e);
                }
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (iHttpResponse != null) {
                    iHttpResponse.onSuccess(call, response);
                }
            }
        });
    }

}

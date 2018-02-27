package com.xk.xkds.common.manager;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Kevin on 2015/12/31.
 * Http请求回调
 */
public interface IHttpResponse {
    void onFailure(Call call, IOException e);
    void onSuccess(Call call, Response response) throws IOException;
}

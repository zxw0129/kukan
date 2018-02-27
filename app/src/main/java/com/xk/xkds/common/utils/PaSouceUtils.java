package com.xk.xkds.common.utils;

import com.xk.xkds.common.manager.HttpManger;
import com.xk.xkds.common.manager.IHttpResponse;

/**
 * Created  LJP on 2017/6/26.
 */

public class PaSouceUtils {

    private PaSouceUtils() {
    }

    private static PaSouceUtils instance;

    public static PaSouceUtils getInstace() {
        if (instance == null) {
            instance = new PaSouceUtils();
        }
        return instance;
    }

    public void parsePaSouce( String url ,IHttpResponse iHttpResponse) {
        HttpManger.getInstace().doGet(url, iHttpResponse);
    }


}

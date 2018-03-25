package com.xk.xkds.common.utils;

import android.util.Log;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class GetRealClientIp implements Runnable
{
    private final String TAG = getClass().getSimpleName();
    private final String TB_HOST = "ip.taobao.com";
    private final String TB_REQUET_BODY = "GET /service/getIpInfo.php?ip=myip HTTP/1.1\r\nHost: ip.taobao.com\r\nUser-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36\r\nConnection: Close\r\n\r\n";

    private IpInfoListener mListener;
    public interface IpInfoListener
    {
        void retData(String ret);
    }

    public void setListener(IpInfoListener listener)
    {
        mListener = listener;
        return;
    }

    private void socketCreate()
    {
        try
        {
            Socket socket = new Socket(TB_HOST,80);
            OutputStream out = socket.getOutputStream();
            out.write(TB_REQUET_BODY.getBytes());
            out.flush();

            InputStream is = socket.getInputStream();
            byte[] readBuf = new byte[1024];
            int ret = is.read(readBuf);
            try
            {
                String retBody = new String(readBuf,"utf-8");
                Log.e(TAG,"ret:"+ret+"    "+retBody);
                int jsonStartIndex = retBody.indexOf("{");
                int jsonEndIndex = retBody.lastIndexOf("}");
                String realJson = retBody.substring(jsonStartIndex,jsonEndIndex+1);
                Log.e(TAG,realJson);
                if( null != mListener )
                {
                    mListener.retData(realJson);
                }
            }catch( Exception e )
            {
                if( null != mListener )
                {
                    mListener.retData("");
                }
                e.printStackTrace();
            }
            is.close();
            out.close();
            socket.close(); // 关闭Socket
        }catch( Exception e )
        {
            if( null != mListener )
            {
                mListener.retData("");
            }
            Log.e(TAG,"can not listen to:" + e);// 出错，打印出错信息
        }
    }

    @Override
    public void run()
    {
        socketCreate();
    }
}

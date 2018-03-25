package com.xk.xkds.common.utils;

import android.text.TextUtils;
import android.util.Log;

import com.socks.library.KLog;
import com.xk.xkds.R;
import com.xk.xkds.common.base.NetMessage;
import com.xk.xkds.common.manager.HttpManger;
import com.xk.xkds.common.manager.IHttpResponse;
import com.xk.xkds.component.activity.XkdsActivity;
import com.xk.xkds.component.base.BaseApplication;
import com.xk.xkds.component.dialog.UpdataDialog;
import com.xk.xkds.entity.ChannelBean;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * Created  on 2017/2/20.
 * 直播频道资源解析
 */

public class ChannelResourceUtils
{
    private static final String URL_CHANNEL = NetMessage.URL + "channelinfo.php";
    /**
     * 总map
     */
    private Map<Integer, ChannelBean> mChannelMap;
    private ArrayList<ChannelBean> mListDatas;
    private HttpURLConnection conn;
    private boolean status;
    private String localIpAddress;

    private ChannelResourceUtils()
    {
    }

    private static ChannelResourceUtils instacce;

    public static ChannelResourceUtils getInstace()
    {
        if( instacce == null )
        {
            instacce = new ChannelResourceUtils();
        }
        return instacce;
    }

    /***
     * 解析数据
     */
    public void parseResource()
    {
        status = false;
        mChannelMap = new TreeMap<>();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    long current = System.currentTimeMillis() / 1000;
                    KLog.e("current time:" + current);
                    String sign = MD5Util.MD5("kukan" + current);
                    String strUrl = URL_CHANNEL + "?sign=" + sign + "&time=" + current;
                    HttpManger.getInstace().doGet(strUrl, new IHttpResponse()
                    {
                        @Override
                        public void onFailure(Call call, IOException e)
                        {
                            try
                            {
                                InputStream is = BaseApplication.getInstance().getResources().openRawResource(R.raw.channelinfo1);
                                LogUtlis.getInstance().showLogE("get channel from local");
                                readTextFromSDcard(is);
                            }catch( Exception e1 )
                            {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }

                        @Override
                        public void onSuccess(Call call, Response response) throws IOException
                        {
                            ResponseBody body = response.body();
                            if( body == null )
                            {
                                LogUtlis.getInstance().showLogE("channel return body is nulll");
                                return;
                            }
                            String encryptStr = body.string();
                            if( TextUtils.isEmpty(encryptStr) )
                            {
                                LogUtlis.getInstance().showLogE("channel string is null");
                                return;
                            }
                            try
                            {
                                byte[] bytes = CommApi.decode(encryptStr,"kukan123");
                                String decryptStr = new String(bytes);
                                LogUtlis.getInstance().showLogE("channel list:"+decryptStr);
                                ByteArrayInputStream insReader = new ByteArrayInputStream(decryptStr.getBytes(Charset.forName("utf8")));
                                readTextFromSDcard(insReader);
                            }catch( Exception e )
                            {
                                e.printStackTrace();
                            }
                        }
                    });
                }catch( Exception e )
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    /**
     * 按行读取txt
     *
     * @param is
     * @return
     * @throws Exception
     */
    public void readTextFromSDcard(InputStream is) throws Exception
    {
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String str;
        int counts = 0;
        String province = SpUtils.getInstance().getProvince();
        while( (str = bufferedReader.readLine()) != null )
        {
            ChannelBean bean = new ChannelBean();
            String string = new String(str.getBytes(), "UTF-8");
            String[] split = string.split(",");
            if( split.length < 4 )
            {
                continue;
            }
            int type = 8;
            // 区分频道
            String trim = split[0].trim();
            if( trim.contains("央视") )
            {
                type = 1;
            }
            else if( trim.contains("卫视") )
            {
                type = 2;
            }
            else if( trim.contains("购物") )
            {
                type = 3;
            }
            else if( trim.contains("地方") )
            {
                type = 4;
            }
            else if( trim.contains("体育") )
            {
                type = 5;
            }
            else if( trim.contains("少儿") )
            {
                type = 6;
            }
            else if( trim.contains("影视") )
            {
                type = 7;
            }
            else if( trim.contains("综艺") )
            {
                type = 8;
            }
            else if( trim.contains("测试") )
            {
                type = 9;
            }
            bean.setChannelType(type);
            //请求台号 id 用于获取当前播放
            bean.setChannelId(split[1]);
            //台名
            bean.setChannelName(split[2]);
            //确定是否是购物台;
            if( split[3].equals("0") )
            {
                bean.setShop(false);
            }
            else
            {
                bean.setShop(true);
                try
                {
                    int i = Integer.parseInt(split[3]);
                    bean.setShopNum(i);
                }catch( Exception e )
                {
                }
            }
            //要是台名为空 不处理
            if( split[2].equals("") )
                continue;
            ArrayList<String> list = new ArrayList<>();
            if( type == 4 )
            { //省内频道
                if( split[4].contains(province) )
                {
                    for( int i = 5; i < split.length; i++ )
                    {
                        list.add(split[i]);
                    }
                }
                else
                {
                    bean.setChannelType(10);//地方频道
                    for( int i = 5; i < split.length; i++ )
                    {
                        list.add(split[i]);
                    }
                }
            }
            else
            {
                for( int i = 4; i < split.length; i++ )
                {
                    list.add(split[i]);
                }
            }
            if( list.size() == 0 )
                continue;
            bean.setResourceList(list);
            counts++;
            //台号
            bean.setChannelNum(counts);
            mChannelMap.put(counts, bean);
        }
        bufferedReader.close();
        reader.close();
        is.close();
        counts = 0;
        status = true;
        LogUtlis.getInstance().showLogE("mChannelMap length = " + mChannelMap.size());
    }

    public boolean getStatus()
    {
        if( mChannelMap == null )
        {
            status = false;
        }
        else
        {
            status = true;
        }
        return status;
    }

    /***
     * 获取新数据map
     *
     * @return
     */
    public Map<Integer, ChannelBean> getChannerMap()
    {
        if( null != mChannelMap && mChannelMap.size() == 0 )
        {
            LogUtlis.getInstance().showLogE(" xxxload channel datas form raw");
            InputStream is = BaseApplication.getInstance().getResources().openRawResource(R.raw.channelinfo1);
            try
            {
                readTextFromSDcard(is);
            }catch( Exception e )
            {
                e.printStackTrace();
            }
        }
        return mChannelMap;
    }

    public ArrayList<ChannelBean> getAllList()
    {
        if( mListDatas == null )
        {
            mListDatas = new ArrayList<>();
        }
        else
        {
            mListDatas.clear();
        }
        if( mChannelMap == null || mChannelMap.size() == 0 )
        {
            getChannerMap();
        }
        for( Map.Entry<Integer, ChannelBean> entry : mChannelMap.entrySet() )
        {
            mListDatas.add(entry.getValue());
        }
        return mListDatas;
    }

    public void cleanMap()
    {
        if( mChannelMap != null )
        {
            mChannelMap.clear();
            mChannelMap = null;
        }
    }
}

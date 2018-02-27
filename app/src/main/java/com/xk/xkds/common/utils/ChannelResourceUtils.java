package com.xk.xkds.common.utils;

import com.xk.xkds.R;
import com.xk.xkds.common.base.NetMessage;
import com.xk.xkds.component.base.BaseApplication;
import com.xk.xkds.entity.ChannelBean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;


/**
 * Created  on 2017/2/20.
 * 直播频道资源解析
 */

public class ChannelResourceUtils {
    private static final String URL_CHANNEL = NetMessage.URL + "channelinfo1.txt";
    /**
     * 总map
     */
    private Map<Integer, ChannelBean> mChannelMap;
    private ArrayList<ChannelBean> mListDatas;
    private HttpURLConnection conn;
    private boolean status;
    private String localIpAddress;

    private ChannelResourceUtils() {
    }

    private static ChannelResourceUtils instacce;

    public static ChannelResourceUtils getInstace() {
        if (instacce == null) {
            instacce = new ChannelResourceUtils();
        }
        return instacce;
    }

    /***
     * 解析数据
     */
    public void parseResource() {
        status = false;
        mChannelMap = new TreeMap<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = null;
                    //自带方式
//             is = Global.mContext.getResources().openRawResource(R.raw.channelinfo);
////                    //网络
                    URL url = new URL(URL_CHANNEL);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setUseCaches(false);
                    conn.connect();
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        LogUtlis.getInstance().showLogE(" xxx is loadData from net");
                        //从连接对象中获取字节输入流，
                        is = conn.getInputStream();
                        //注释掉 只能读网络的方式,获取源
                    } else {
//                    读文件方式
                        LogUtlis.getInstance().showLogE("xxx is loadData from locate");
                        is = BaseApplication.getInstance().getResources().openRawResource(R.raw.channelinfo1);
                    }
                    readTextFromSDcard(is);
                } catch (Exception e) {
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
    public void readTextFromSDcard(InputStream is) throws Exception {
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String str;
        int counts = 0;
        String province = SpUtils.getInstance().getProvince();
        while ((str = bufferedReader.readLine()) != null) {
            ChannelBean bean = new ChannelBean();
            String string = new String(str.getBytes(), "UTF-8");
            String[] split = string.split(",");
            if (split.length < 4) {
                continue;
            }
            int type = 8;
            // 区分频道
            String trim = split[0].trim();
            if (trim.contains("央视")) {
                type = 1;
            } else if (trim.contains("卫视")) {
                type = 2;
            } else if (trim.contains("购物")) {
                type = 3;
            } else if (trim.contains("地方")) {
                type = 4;
            } else if (trim.contains("体育")) {
                type = 5;
            } else if (trim.contains("少儿")) {
                type = 6;
            } else if (trim.contains("影视")) {
                type = 7;
            } else if (trim.contains("综艺")) {
                type = 8;
            } else if (trim.contains("测试")) {
                type = 9;
            }
            bean.setChannelType(type);
            //请求台号 id 用于获取当前播放
            bean.setChannelId(split[1]);
            //台名
            bean.setChannelName(split[2]);
            //确定是否是购物台;
            if (split[3].equals("0")) {
                bean.setShop(false);
            } else {
                bean.setShop(true);
                try {
                    int i = Integer.parseInt(split[3]);
                    bean.setShopNum(i);
                } catch (Exception e) {
                }
            }
            //要是台名为空 不处理
            if (split[2].equals("")) continue;
            ArrayList<String> list = new ArrayList<>();
            if (type == 4) { //省内频道
                if (split[4].contains(province)) {
                    for (int i = 5; i < split.length; i++) {
                        list.add(split[i]);
                    }
                }else {
                    bean.setChannelType(10);//地方频道
                    for (int i = 5; i < split.length; i++) {
                        list.add(split[i]);
                    }
                }
            } else {
                for (int i = 4; i < split.length; i++) {
                    list.add(split[i]);
                }
            }
            if (list.size() == 0) continue;
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

    public boolean getStatus() {
        if (mChannelMap == null) {
            status = false;
        } else {
            status = true;
        }
        return status;
    }

    /***
     * 获取新数据map
     *
     * @return
     */
    public Map<Integer, ChannelBean> getChannerMap() {
        if (null != mChannelMap && mChannelMap.size() == 0) {
            LogUtlis.getInstance().showLogE(" xxxload channel datas form raw");
            InputStream is = BaseApplication.getInstance().getResources().openRawResource(R.raw.channelinfo1);
            try {
                readTextFromSDcard(is);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mChannelMap;
    }

    public ArrayList<ChannelBean> getAllList() {
        if (mListDatas == null) {
            mListDatas = new ArrayList<>();
        } else {
            mListDatas.clear();
        }
        if (mChannelMap == null || mChannelMap.size() == 0) {
            getChannerMap();
        }
        for (Map.Entry<Integer, ChannelBean> entry : mChannelMap.entrySet()) {
            mListDatas.add(entry.getValue());
        }
        return mListDatas;
    }

    public void cleanMap() {
        if (mChannelMap != null) {
            mChannelMap.clear();
            mChannelMap = null;
        }
    }
}

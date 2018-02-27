package com.xk.xkds.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created  on 2017/2/20.
 */
public class ChannelBean implements Serializable {
    /**
     * 台类型
     */
    private int ChannelType = 1;
    /**
     * 台号
     */
    private int ChannelNum = 0;
    private String channelName = "";
    private String channelUrl = "";
    /**
     * 请求id
     */
    private String channelId = "";
    public boolean selected = false;
    private boolean isShop = false;
    /**
     * 广告图
     */
    private int shopNum ;

    public int getShopNum() {
        return shopNum;
    }

    public void setShopNum(int shopNum) {
        this.shopNum = shopNum;
    }

    private ArrayList<String> resourceList = new ArrayList<>();

    public ArrayList<String> getResourceList() {
        return resourceList;
    }

    public void setResourceList(ArrayList<String> resourceList) {
        this.resourceList = resourceList;
    }

    public boolean isShop() {
        return isShop;
    }

    public void setShop(boolean shop) {
        isShop = shop;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public int getChannelType() {
        return ChannelType;
    }

    public void setChannelType(int channelType) {
        ChannelType = channelType;
    }

    public int getChannelNum() {
        return ChannelNum;
    }

    public void setChannelNum(int channelNum) {
        ChannelNum = channelNum;
    }


    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelUrl() {
        return channelUrl;
    }

    public void setChannelUrl(String channelUrl) {
        this.channelUrl = channelUrl;
    }
}


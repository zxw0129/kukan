package com.xk.xkds.entity;

import java.io.Serializable;

/**
 * Created  LJP on 2017/10/30.
 */

public class QRDialogBean implements Serializable
{
    private String iconUrl="";
    private String appName="";
    private String tvExplain="";
    private String packageName="";
    private String appUrl="";

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTvExplain() {
        return tvExplain;
    }

    public void setTvExplain(String tvExplain) {
        this.tvExplain = tvExplain;
    }
}

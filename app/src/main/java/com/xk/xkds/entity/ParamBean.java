package com.xk.xkds.entity;

/**
 * Created by Administrator on 2017/10/22.
 */

public class ParamBean {

    private liveBean liveBean;

    public liveBean getliveBean() {
        return liveBean;
    }
    public void setliveBean(liveBean liveBean) {
        this.liveBean = liveBean;
    }

    public static class liveBean {
        private String isLive;
        private String channelName;

        public String getIsLive() {
            return isLive;
        }

        public void setIsLive(String isLive) {
            this.isLive = isLive;
        }

        public String getChannelName() {
            return channelName;
        }

        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }

    }
}

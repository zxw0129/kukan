package com.xk.xkds.entity;

import java.util.List;

/**
 * Created  on 2017/4/13.
 */

public class ADBean {
    private List<ListBean> list;
    public List<ListBean> getList() {
        return list;
    }
    public void setList(List<ListBean> list) {
        this.list = list;
    }
    public static class ListBean {
        /**
         * APKName : 想看电视
         * APKUrl : http://www.google.com
         * imageBg : http://www.google.com
         */

        private String APKName;
        private String APKUrl;
        private String imageBg;
        private String packageName;

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getAPKName() {
            return APKName;
        }

        public void setAPKName(String APKName) {
            this.APKName = APKName;
        }

        public String getAPKUrl() {
            return APKUrl;
        }

        public void setAPKUrl(String APKUrl) {
            this.APKUrl = APKUrl;
        }

        public String getImageBg() {
            return imageBg;
        }

        public void setImageBg(String imageBg) {
            this.imageBg = imageBg;
        }
    }
}

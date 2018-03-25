package com.xk.xkds.common.base;


/**
 * Created  on 2017/3/6.
 */

public class NetMessage {
//    public final static String URL = "http://139.219.57.123:8882/";
    public final static String URL = "http://91mytv.com/";
    public final static String UPDATACHECK = URL + "updateinfo.txt";
    //广告
    public final static String ADURL = URL + "tcgg.txt";
    //图 购物栏目
    public final static String SHOP_IMAGE_URL_1 = URL + "1.png";
    public final static String SHOP_IMAGE_URL_2 = URL + "2.png";
    public final static String SHOP_IMAGE_URL_3 = URL + "3.png";
    public final static String SHOP_IMAGE_URL_4 = URL + "4.png";
    /**二维码*/
    public final static String ERCODEURL = URL + "akdserguanggao.txt";
    public final static String SHAFA = "sha fa";
    public final static String DANGBEI = "dang bei";
    public final static String QIBO = "qibo";
    public final static String MIFENG = "mifeng";
    public final static String GUANGFA = "guan fang";
    public final static String HUANSHI = "huan shi";
    public final static String JIUI = "9i";
    public final static String DUOLE = "duo le";
    public final static String KK = "kk";
    //官方
    public final static String UPDATAURL_GUANGFANG = URL + "xkds_guanfang.apk";
    //当贝
    public final static String UPDATAURL_DANGBEI = URL + "xkds_dangbei.apk";
    //沙发
    public final static String UPDATAURL_SHAFA = URL + "xkds_shafa.apk";
    //齐博
    public final static String UPDATAURL_QIBO = URL + "xkds_qibo.apk";
    //蜜蜂
    public final static String UPDATAURL_MIFENG = URL + "xkds_mifeng.apk";
    //欢视
    public final static String UPDATAURL_HUANSHI = URL + "xkds_huanshi.apk";
    //9i看点
    public final static String UPDATAURL_JIUI = URL + "xkds_9i.apk";
    //多乐
    public final static String UPDATAURL_DUOLE = URL + "xkds_duole.apk";
    //kk
    public final static String UPDATAURL_kk = URL + "xkds_kk.apk";

    public static String getUrl(String channel) {
        if (channel.equals(DANGBEI)) { //当贝
            return UPDATAURL_DANGBEI;
        } else if (channel.equals(GUANGFA)) {//官方
            return UPDATAURL_GUANGFANG;
        } else if (channel.equals(MIFENG)) {//蜜蜂
            return UPDATAURL_MIFENG;
        } else if (channel.equals(QIBO)) {//齐博
            return UPDATAURL_QIBO;
        } else if (channel.equals(SHAFA)) {//沙发
            return UPDATAURL_SHAFA;
        } else if (channel.equals(HUANSHI)) {//欢视
            return UPDATAURL_HUANSHI;
        } else if (channel.equals(JIUI)) {//9i
            return UPDATAURL_JIUI;
        } else if (channel.equals(DUOLE)) {//多乐
            return UPDATAURL_DUOLE;
        } else if (channel.equals(KK)) {//kk
            return UPDATAURL_kk;
        }
        return UPDATAURL_GUANGFANG;
    }

    public static String getAPkName() {
        if (Global.channel.equals(DANGBEI)) { //当贝
            return "xkds_dangbei.apk";
        } else if (Global.channel.equals(GUANGFA)) {//官方
            return "xkds_guanfang.apk";
        } else if (Global.channel.equals(MIFENG)) {//蜜蜂
            return "xkds_mifeng.apk";
        } else if (Global.channel.equals(QIBO)) {//齐博
            return "xkds_qibo.apk";
        } else if (Global.channel.equals(SHAFA)) {//沙发
            return "xkds_shafa.apk";
        } else if (Global.channel.equals(HUANSHI)) {//欢视
            return "xkds_huanshi.apk";
        } else if (Global.channel.equals(JIUI)) {//想看助手
            return "xkds_9i.apk";
        } else if (Global.channel.equals(DUOLE)) {//多乐
            return "xkds_duole.apk";
        } else if (Global.channel.equals(KK)) {//kk
            return "xkds_kk.apk";
        }
        return "xkds_guanfang.apk";
    }
}

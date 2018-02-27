package com.xk.xkds.common.receiver;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.xk.xkds.common.utils.LogUtlis;
import com.xk.xkds.common.utils.SpUtils;

/**
 * Created  LJP on 2018/1/23.
 */

public class LocationListener extends BDAbstractLocationListener {
    @Override
    public void onReceiveLocation(BDLocation location){
        //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
        //以下只列举部分获取地址相关的结果信息
        //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

        String addr = location.getAddrStr();    //获取详细地址信息
        String country = location.getCountry();    //获取国家
        String province = location.getProvince();    //获取省份
        String city = location.getCity();    //获取城市
        String district = location.getDistrict();    //获取区县
        String street = location.getStreet();    //获取街道信息
        SpUtils.getInstance().saveProvince(province);
        LogUtlis.getInstance().showLogE(addr);
        LogUtlis.getInstance().showLogE("获取省份"+province);
    }
}

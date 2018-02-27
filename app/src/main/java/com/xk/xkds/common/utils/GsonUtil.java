package com.xk.xkds.common.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by xq on 2016/7/4.
 */
public class GsonUtil {

    /**
     * 解析Json数据
     * @param result
     * @param clazz
     * @return
     * @throws Exception
     */
        public static <T extends Object> T parse(String result, Class<T> clazz) {
        T newObject = null;
        try{
            Gson gson = new Gson();
            newObject = gson.fromJson(result, clazz);
        } catch (JsonSyntaxException e){
            e.printStackTrace();
        }
        return newObject;
    }

    public static String createJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

//    public static <T extends Object> List<T> getListPerson(String jsonString) {
//        List<T> list = new ArrayList<T>();
//        Gson gson = new Gson();
//        list = gson.fromJson(jsonString, new TypeToken<List<T>>() {}.getType());
//        return list;
//    }
}

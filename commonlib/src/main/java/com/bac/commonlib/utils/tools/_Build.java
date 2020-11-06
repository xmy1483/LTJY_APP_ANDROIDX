package com.bac.commonlib.utils.tools;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Field;
import java.util.HashMap;


/**
 * Created by Wjz on 2017/6/2.
 */

public class _Build {
    public static String getPhoneParameter() {
        String str = "";
        HashMap<String, Object> phoneParameter = null;
        try {
            phoneParameter = phoneParameter();
        } catch (Exception e) {
        }
        if (phoneParameter != null) {
            str = JSON.toJSONString(phoneParameter);
        }
        return str;
    }

    private static HashMap<String, Object> phoneParameter() throws ClassNotFoundException, IllegalAccessException {

        // 反射获取 android.os.Build 文件
        HashMap<String, Object> hashMap = new HashMap<>();

        Class<?> _class = Class.forName("android.os.Build");

        getFiled(hashMap, _class);

        // 得到内部类
        Class<?>[] innerClass = _class.getDeclaredClasses();
        for (int i = 0; i < innerClass.length; i++) {
            getFiled(hashMap, innerClass[i]);
        }

        return hashMap;
    }

    private static void getFiled(HashMap<String, Object> hashMap, Class<?> _class) throws IllegalAccessException {
        // 得到成员变量
        Field[] fields = _class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            f.setAccessible(true);

            String name = f.getName();
            hashMap.put(name, f.get(name));
        }
    }

}

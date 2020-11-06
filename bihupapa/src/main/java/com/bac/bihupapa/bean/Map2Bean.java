package com.bac.bihupapa.bean;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Map2Bean {

    static Map<Class<?>, Map<String, Method>> methodMap=new ConcurrentHashMap<Class<?>, Map<String,Method>>();

    private static Map<String, Method> getMethodMap(Class<?> clazz){
        Map<String, Method> map=methodMap.get(clazz);
        if(map==null) {
            map=new HashMap<String, Method>();
            for(Method method:clazz.getMethods()) {
                map.put(method.getName().replaceFirst("set", ""), method);
            }
            methodMap.put(clazz, map);
        }
        return map;
    }
    /**
     * @param <T>
     * @param clazz
     *            所要封装的javaBean
     * @param rs
     *            记录集
     * @return ArrayList 数组里边装有 多个javaBean
     * @throws IllegalAccessException
     * @throws Exception
     * @throws Exception
     * @说明：利用反射机制从ResultSet自动绑定到JavaBean；根据记录集自动调用javaBean里边的对应方法。
     */
    public static <T> T  getBean(Class<T> clazz, Map<String, Object> map) throws Exception{

        Map<String, Method> methodMap= getMethodMap(clazz);


        T t=null;

            t = clazz.newInstance();

        for(Map.Entry<String, Object> entry:map.entrySet()) {
            Method method=methodMap.get(entry.getKey());
            if(method==null)
                continue;

            // 根据 rs 列名 ，组装javaBean里边的其中一个set方法，object 就是数据库第一行第一列的数据了
            Object value = entry.getValue();

            if (value != null) {
                    method.invoke(t, value);

            }
        }
        return t;

    }

}

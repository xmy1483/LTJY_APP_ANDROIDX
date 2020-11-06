package com.bac.commonlib.utils.fun;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import rx.functions.Func1;

/**
 * Created by Wjz on 2017/5/10.
 */

public class JsonFunc1<T extends String, R> implements Func1<T, R> {
    @Override
    public R call(T t) {

       /* Class<? extends JsonFunc1> aClass = JsonFunc1.this.getClass();
        Type[] genericInterfaces = aClass.getGenericInterfaces();
        ParameterizedType genericSuperclass   = (ParameterizedType) genericInterfaces[0];
        Type[]            actualTypeArguments = genericSuperclass.getActualTypeArguments();
        Type              type                = actualTypeArguments[1];*/
        return JSON.parseObject(t, new TypeReference<R>(){}.getType());
    }

}

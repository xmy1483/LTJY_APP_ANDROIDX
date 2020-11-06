package com.bac.bacplatform.utils.logger;

import android.util.Log;

import com.bac.bacplatform.conf.Constants;

/**
 * 项目名称：Bacplatform
 * 包名：com.bac.bacplatform.utils.logger
 * 创建人：Wjz
 * 创建时间：2017/4/19
 * 类描述：
 */

public class LogUtil {
    public static void sf(Object o, String str) {
        if (Constants.APP.DEBUG) {
            Log.i(o.getClass().getSimpleName(), str);
        }
    }
}

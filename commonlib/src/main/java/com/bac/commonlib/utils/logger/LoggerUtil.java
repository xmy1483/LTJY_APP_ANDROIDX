package com.bac.commonlib.utils.logger;

import android.util.Log;

import com.bac.commonlib.param.CommonParam;

/**
 * Created by wujiazhen on 2017/8/15.
 */

public class LoggerUtil {
    public static void loggerUtil(String tag, String log) {
        if (CommonParam.getInstance().isDebug()) {
            Log.e(tag, log);
        }
    }
}

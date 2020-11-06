package com.bac.bacinnermanager.utils;

import android.content.Context;

import com.bac.bacinnermanager.BuildConfig;
import com.bac.bacinnermanager.R;


/**
 * Created by wujiazhen on 2017/8/14.
 */

public class SeedUtil {

    public static String getSeed(Context context){
        return "abcd".concat(StringUtil.localDeCode(context.getResources().getString(R.string.sec_prefix)
                        + BuildConfig.appKeyMid + Constants.SECODELAST,
                (byte) Integer.parseInt(context.getResources().getString(R.string.seed_num) + Constants.XX + BuildConfig.appKeySeed)));
    }
}

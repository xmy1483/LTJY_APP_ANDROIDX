package com.bac.bacinnermanager.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * Created by wujiazhen on 2017/8/14.
 */

public class StringUtil {
    /**
     * 本地加密
     *
     * @param str
     * @param seed
     * @return
     */
    public static String localDeCode(String str, byte seed) {
        String code = null;
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] ^= seed;
        }
        code = new String(bytes, StandardCharsets.UTF_8);
        return code;
    }
}

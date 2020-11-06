package com.bac.commonlib.utils.str;

import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Base64;

import com.bac.commonlib.seed.AES;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wujiazhen on 2017/7/7.
 */

public class StringUtil {

    /**
     * Base64 + Aes
     *
     * @param context
     * @return
     */
    public static String encode(Context context, Object obj,String s) {

        String str = "";
        try {
            // base64
            byte[] encode = String.valueOf(obj).getBytes();
            // aes
            AES aes = new AES((s).getBytes());

            byte[] decrypt = aes.encrypt(encode);
            str = Base64.encodeToString(decrypt, 2);

        } catch (Exception e) {
        }
        return str;
    }

    /**
     * Base64 + Aes
     *
     * @param context
     * @return
     */
    public static String decode(Context context, String key,String s) {

        String str = "";
        try {
            String string = PreferenceManager.getDefaultSharedPreferences(context).getString(key, "");

            // aes
            AES aes = new AES((s).getBytes());
            byte[] decrypt = aes.decrypt(Base64.decode(string.getBytes(), 2));

            str = new String(decrypt, StandardCharsets.UTF_8);

        } catch (Exception e) {
        }

        return str;
    }

    public static String replaceBlank(String str) {
        String dest = "";

        if (!TextUtils.isEmpty(str)) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 判断 11位的手机号
     *
     * @param str
     * @return false 匹配失败
     * true  匹配成功
     */
    public static boolean isPhone(String str) {

        if (TextUtils.isEmpty(str)) {
            return false;
        } else {
            String regExp = "^[1][0-9]{10}$";

            Pattern p = Pattern.compile(regExp);

            Matcher m = p.matcher(str);
            return m.find();
        }

    }

    /**
     * 判断 江苏车牌号
     * 苏D M8888
     * @param str
     * @return false 匹配失败
     * true  匹配成功
     */
    public static boolean isCarCode(String str) {

        if (TextUtils.isEmpty(str)) {
            return false;
        } else {
            String regExp = "^[a-zA-Z]{1}[a-zA-Z0-9]{5}$";

            Pattern p = Pattern.compile(regExp);

            Matcher m = p.matcher(str);
            return m.find();
        }

    }

}

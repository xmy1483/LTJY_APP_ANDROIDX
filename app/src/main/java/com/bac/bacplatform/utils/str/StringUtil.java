package com.bac.bacplatform.utils.str;

import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Base64;

import com.bac.bacplatform.BuildConfig;
import com.bac.bacplatform.R;
import com.bac.bacplatform.conf.Constants;
import com.bac.commonlib.seed.AES;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 项目名称：Bacplatform
 * 包名：com.bac.bacplatform.repository
 * 创建人：Wjz
 * 创建时间：2017/4/10
 * 类描述：
 */

public class StringUtil {


    /**
     * Base64 + Aes
     *
     * @param context
     * @return
     */
    public static String encode(Context context, Object obj) {

        String str = "";
        try {
            // base64
            byte[] encode = String.valueOf(obj).getBytes();
            // aes
            AES aes = new AES((context.getString(R.string.seed_num) + Constants.XX + BuildConfig.appKeySeed + BuildConfig.appKeySeed2).getBytes());

            byte[] decrypt = aes.encrypt(encode);
            str = Base64.encodeToString(decrypt, 2);

        } catch (Exception e) {
        }
        return str;
    }

    /**
     * Base64 + ^
     *
     * @param context
     * @return
     */
    public static String encode2(Context context, Object obj) {
        return StringUtil.localDeCode(Base64.encodeToString(String.valueOf(obj).getBytes(), 2), (byte) Integer.parseInt(context.getString(R.string.seed_num) + Constants.XX + BuildConfig.appKeySeed));
    }

    /**
     * Base64 + Aes
     *
     * @param context
     * @return
     */
    public static String decode(Context context, String key) {

        String str = "";
        try {
            String string = PreferenceManager.getDefaultSharedPreferences(context).getString(key, "");

            // aes
            AES aes = new AES((context.getString(R.string.seed_num) + Constants.XX + BuildConfig.appKeySeed + BuildConfig.appKeySeed2).getBytes());
            byte[] decrypt = aes.decrypt(Base64.decode(string.getBytes(), 2));

            str = new String(decrypt, "UTF-8");

        } catch (Exception e) {
        }

        return str;
    }

    /**
     * Base64 + ^
     *
     * @param context
     * @return
     */
    public static String decode2(Context context, String key) {
        String s = "";
        try {
            s = new String(Base64.decode(StringUtil.localDeCode(PreferenceManager.getDefaultSharedPreferences(context).getString(key, ""), (byte) Integer.parseInt(context.getString(R.string.seed_num) + Constants.XX + BuildConfig.appKeySeed)), 2), "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return s;
    }

    public static boolean isLicNo(String str) {

        if (TextUtils.isEmpty(str)) {
            return false;
        } else {
            String regExp = "[\\u4e00-\\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}";

            // str 不能为空
            Pattern p = Pattern.compile(regExp);

            Matcher m = p.matcher(str);
            return m.find();
        }
    }

    public static boolean isJiangSu(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        } else {
            // 江苏中石化
            return Pattern.compile("^10001132\\d{11}").matcher(str).find();
        }
    }

    /**
     * 中石油
     *
     * @param str
     * @return
     */
    public static boolean isZhongShiYou(String str) {

        if (TextUtils.isEmpty(str)) {
            return false;
        } else {
            String regExp = "^9\\d{15}";

            // str 不能为空
            Pattern p = Pattern.compile(regExp);

            Matcher m = p.matcher(str);
            return m.find();
        }
    }

    /**
     * 中石化
     *
     * @param str
     * @return
     */
    public static boolean isZhongShiHua(String str) {

        if (TextUtils.isEmpty(str)) {
            return false;
        } else {
            String regExp = "^100011\\d{13}";

            // str 不能为空
            Pattern p = Pattern.compile(regExp);

            Matcher m = p.matcher(str);
            return m.find();
        }
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
     * 本地加密
     *
     * @param str
     * @param seed
     * @return
     */
    public static String localDeCode(String str, byte seed) {
        String code = null;
        try {
            byte[] bytes = str.getBytes("UTF-8");
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] ^= seed;
            }
            code = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return code;
    }

    public static String isNullOrEmpty(Object msg) {
        String msgStr = "空";
        if (msg != null) {
            msgStr = msg + "";
        }
        return msgStr;
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

    //静态方法，便于作为工具类
    public static String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes("UTF-8"));
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (Exception e) {
            //log.error("加密字符串 " + str + " 出现异常", e);
            return "";
        }
    }
}

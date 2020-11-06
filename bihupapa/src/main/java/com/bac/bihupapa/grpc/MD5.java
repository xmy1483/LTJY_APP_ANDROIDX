package com.bac.bihupapa.grpc;

/**
 * Created by dxf on 2017/12/27.
 */

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;


public class MD5 {

    public static byte[] getKeyedDigest(byte[] buffer, byte[] key) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(buffer);
        return md5.digest(key);
    }


    public static String getKeyedDigest(String strSrc, String key) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(strSrc.getBytes(StandardCharsets.UTF_8));
        byte[] temp;
        temp=md5.digest(key.getBytes(StandardCharsets.UTF_8));
        return Tools.toBytesString(temp);
    }

    public static String getKeyedDigest(String strSrc) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(strSrc.getBytes(StandardCharsets.UTF_8));
        byte[] temp;
        temp=md5.digest();
        return Tools.toBytesString(temp);
    }

    /**
     * @param args
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // TODO Auto-generated method stub
        String mi;
        String s = "hf1000";

        //第二个参数请填空字符串
        mi=MD5.getKeyedDigest(s,"12345");

        System.out.println("mi:"+mi);
        mi=MD5.getKeyedDigest(s,"54321");

        System.out.println("mi:"+mi);

//		mi:5fbcd3f8f62aee6b4e629a12a39489a0
//		mi:5baf5d7421032d8da6f0a52a57bba383
    }

}
package com.bac.commonlib.seed;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class KeyedDigestMD5_HEX {
    
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

}
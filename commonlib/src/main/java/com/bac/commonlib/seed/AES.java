package com.bac.commonlib.seed;


import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    private byte[] byteKey;
    private Key    key;
    private Cipher enCipher;
    private Cipher deCipher;

    public static final String KEY_ALGORITHM = "AES";

    public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS7Padding";


    private void init(byte[] byteKey) throws Exception {
        this.byteKey = byteKey;
        key = toKey(byteKey);
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        enCipher = Cipher.getInstance(CIPHER_ALGORITHM, "BC");
        //初始化，设置为加密模式
        enCipher.init(Cipher.ENCRYPT_MODE, key);

        deCipher = Cipher.getInstance(CIPHER_ALGORITHM, "BC");
        //初始化，设置为解密模式
        deCipher.init(Cipher.DECRYPT_MODE, key);
    }

    public AES(byte[] byteKey) throws Exception {
        init(byteKey);
    }

    public static Key toKey(byte[] key) throws Exception {
        //实例化DES密钥
        //生成密钥
        SecretKey secretKey = new SecretKeySpec(key, KEY_ALGORITHM);
        return secretKey;
    }


    public byte[] encrypt(byte[] data) throws Exception {
        return enCipher.doFinal(data);
    }

    public byte[] decrypt(byte[] data) throws Exception {

        return deCipher.doFinal(data);
    }


}

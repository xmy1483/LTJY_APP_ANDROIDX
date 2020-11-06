package com.bac.bihupapa.grpc;

/**
 * Created by dxf on 2017/12/27.
 */

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class AES {
    private byte[] byteKey;
    private Key key;
    private Cipher enCipher;
    private Cipher deCipher;
    /**
     * 密钥算法
     * java6支持56位密钥，bouncycastle支持64位
     */
    public static final String KEY_ALGORITHM = "AES";

    /**
     * 加密/解密算法/工作模式/填充方式
     * <p>
     * JAVA6 支持PKCS5PADDING填充方式
     * Bouncy castle支持PKCS7Padding填充方式
     */
    //public static final String CIPHER_ALGORITHM="AES/ECB/PKCS7Padding";
    public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    private void init(byte[] byteKey) throws Exception {
        this.byteKey = byteKey;
        key = toKey(byteKey);
        //Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        enCipher = Cipher.getInstance(CIPHER_ALGORITHM);
        //初始化，设置为加密模式
        enCipher.init(Cipher.ENCRYPT_MODE, key);

        deCipher = Cipher.getInstance(CIPHER_ALGORITHM);
        //初始化，设置为解密模式
        deCipher.init(Cipher.DECRYPT_MODE, key);
    }

    public AES(byte[] byteKey) throws Exception {
        init(byteKey);
    }


    public AES() throws Exception {
        this.byteKey = initkey();
        init(byteKey);
    }

    /**
     * 生成密钥，java6只支持56位密钥，bouncycastle支持64位密钥
     *
     * @return byte[] 二进制密钥
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     */
    public static byte[] initkey() throws NoSuchAlgorithmException, NoSuchProviderException {

        //实例化密钥生成器
        //Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM, "BC");
        //初始化密钥生成器，AES要求密钥长度为128位、192位、256位
        kg.init(256);
        //kg.init(128);
        //生成密钥
        SecretKey secretKey = kg.generateKey();
        //获取二进制密钥编码形式
        return secretKey.getEncoded();
        //为了便于测试，这里我把key写死了，如果大家需要自动生成，可用上面注释掉的代码
//            return new byte[] { 0x08, 0x08, 0x04, 0x0b, 0x02, 0x0f, 0x0b, 0x0c,
//                                 0x09, 0x07, 0x0c, 0x03, 0x07, 0x0a, 0x04, 0x0f,
//                    };
    }

    /**
     * 转换密钥
     *
     * @param key 二进制密钥
     * @return Key 密钥
     */
    public static Key toKey(byte[] key) throws Exception {
        //实例化DES密钥
        //生成密钥
        SecretKey secretKey = new SecretKeySpec(key, KEY_ALGORITHM);
        return secretKey;
    }

    /**
     * 加密数据
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return byte[] 加密后的数据
     */
    public byte[] encrypt(byte[] data) throws Exception {
        return enCipher.doFinal(data);
    }

    /**
     * 解密数据
     *
     * @param data 待解密数据
     * @return byte[] 解密后的数据
     */
    public byte[] decrypt(byte[] data) throws Exception {
        /**
         * 实例化
         * 使用 PKCS7PADDING 填充方式，按如下方式实现,就是调用bouncycastle组件实现
         * Cipher.getInstance(CIPHER_ALGORITHM,"BC")
         */
        return deCipher.doFinal(data);
    }


    public byte[] getKey() {
        return byteKey;
    }


}

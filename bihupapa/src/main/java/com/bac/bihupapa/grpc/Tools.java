package com.bac.bihupapa.grpc;

/**
 * Created by dxf on 2017/12/27.
 */








/**

 * 常用工具类

 *

 * @author Administrator

 *

 */

public class Tools {



    /**

     * 字节数组转换十六机制字符串

     *

     * @param 字节数组

     *

     * @param 开始字节

     *

     * @param  长度

     *

     * @return

     */

    public static String toBytesString(byte[] buff, int begin, int size) {

        if (size > buff.length)

            size = buff.length;

        char[] c = new char[size << 1];// size<<1=size*2

        byte b;

        int hexIndex, j, k = 0;



        for (int i = begin; i < size + begin; i++) {

            b = buff[i];

            j = k << 1;// k<<1=k*2

            hexIndex = b & 0x0F;

            c[j + 1] = DigitHex[hexIndex];

            hexIndex = (b >> 4) & 0x0F;

            c[j] = DigitHex[hexIndex];

            k++;

        }

        return new String(c, 0, c.length);

    }



    /**

     * 字节数组转换十六机制字符串(默认从0开始)

     *

     * @param 字节数组

     *

     * @param 长度

     *

     * @return

     */

    public static String toBytesString(byte[] buff, int size) {

        return toBytesString(buff, 0, size);

    }



    /**

     * 字节数组转换十六机制字符串(默认从0开始,长度为数组长度)

     *

     * @param buff

     * @return

     */

    public static String toBytesString(byte[] buff) {

        return toBytesString(buff, 0, buff.length);

    }



    public static byte[] toBytes(String s) {

        byte[] bb = new byte[s.length() / 2];

        char[] cc = s.toUpperCase().toCharArray();

        for (int i = 0; i < bb.length; i++) {

            byte b = charToByte(cc[i * 2]);

            b = (byte) (b << 4);

            b = (byte) (b + charToByte(cc[i * 2 + 1]));

            bb[i] = b;

        }

        return bb;

    }



    private static byte charToByte(char c) {

        for (int i = 0; i < DigitHex.length; i++) {

            if (c == DigitHex[i])

                return (byte) i;

        }

        return 0;

    }



    /**

     * 字节转换十六进制字符串

     *

     * @param 字节

     *

     * @return

     */

    public static String toHexString(byte b) {

        int tmp;

        char[] c = new char[2];

        tmp = b & 0x0F;

        c[1] = DigitHex[tmp];

        tmp = (b >> 4) & 0x0F;

        c[0] = DigitHex[tmp];



        return new String(c, 0, 2);

    }



    /**

     * short类型转换十六进制字符串

     *

     * @param short 类型数据

     *

     * @return

     */

    public static String toHexString(short s) {

        int tmp, ii;

        char[] c = new char[4];

        tmp = s & 0x0F;

        c[3] = DigitHex[tmp];



        ii = s >> 4;

        tmp = ii & 0x0F;

        c[2] = DigitHex[tmp];



        ii = ii >> 4;

        tmp = ii & 0x0F;

        c[1] = DigitHex[tmp];



        ii = ii >> 4;

        tmp = ii & 0x0F;

        c[0] = DigitHex[tmp];



        return new String(c, 0, 4);

    }



    /**

     * int类型转换十六进制字符串

     *

     * @param int类型数据

     *

     * @return

     */

    public static String toHexString(int i) {

        int tmp, ii;

        char[] c = new char[8];



        ii = i;

        tmp = ii & 0x0F;

        c[7] = DigitHex[tmp];



        ii = ii >> 4;

        tmp = ii & 0x0F;

        c[6] = DigitHex[tmp];



        ii = ii >> 4;

        tmp = ii & 0x0F;

        c[5] = DigitHex[tmp];



        ii = ii >> 4;

        tmp = ii & 0x0F;

        c[4] = DigitHex[tmp];



        ii = ii >> 4;

        tmp = ii & 0x0F;

        c[3] = DigitHex[tmp];



        ii = ii >> 4;

        tmp = ii & 0x0F;

        c[2] = DigitHex[tmp];



        ii = ii >> 4;

        tmp = ii & 0x0F;

        c[1] = DigitHex[tmp];



        ii = ii >> 4;

        tmp = ii & 0x0F;

        c[0] = DigitHex[tmp];



        return new String(c, 0, 8);

    }



    /**

     * 字节数组转换整型

     *

     * @param 字节数组

     *

     * @return

     */

    public static int bytesToShort(byte[] data) {

        return bytesToShort(data, 0);

    }



    /**

     * 两字节转成int

     *

     * @param 字节数组

     *

     * @param 开始字节

     *

     * @return

     */

    public static int bytesToShort(byte[] data, int begin) {

        int re = byteToUbyte(data[begin]);

        re = (re << 8);

        re = re + byteToUbyte(data[begin + 1]);

        return re;

    }



    /**

     * 四字节转成int以long存储

     *

     * @param 字节数组

     *

     * @param 开始字节

     *

     * @return

     */

    public static long bytesToInt(byte[] data, int begin) {



        long re = byteToUbyte(data[begin]);

        re = (re << 8);

        re = re + byteToUbyte(data[begin + 1]);

        re = (re << 8);

        re = re + byteToUbyte(data[begin + 2]);

        re = (re << 8);

        re = re + byteToUbyte(data[begin + 3]);

        return re;

    }



    /**

     * 有符号的byte转为short<br/> 例:(byte)-24转为(short)232 *

     *

     * @param value

     * @return

     */

    public static short byteToUbyte(byte value) {

        return (short) (value < 0 ? value + 256 : value);

    }



    public static byte uByteToByte(short value) {

        return (byte) (value > 0x80 ? value - 256 : value);

    }



    /**

     * 有符号的short转换无符号int

     *

     * @param value

     * @return

     */

    public static int shortToUShort(short value) {

        return value < 0 ? value + 0x10000 : value;

    }



    public static short uShortToShort(int value) {

        return (short) (value > 0x8000 ? value - 0x10000 : value);

    }



    /**

     * 有符号的int转换无符号long

     *

     * @param value

     * @return

     */

    public static long intToUInt(int value) {

        return value < 0 ? value + 0x100000000L : value;

    }



    /**

     * 无符号的int转换有符号int

     *

     * @param value

     * @return

     */

    public static int uIntToInt(long value) {

        return (int) (value > 0x80000000L ? value - 0x100000000L : value);

    }



    /**

     * 把有符号的Short转成无符号的byte<br/>例:(short)232转为(byte)-24

     *

     * @param value

     * @return

     */

    public static byte shortToUbyte(short value) {

        // int byteValue;

        // int temp = value % 256;

        // if (value < 0) {

        // byteValue = temp < -128 ? 256 + temp : temp;

        // } else {

        // byteValue = temp > 127 ? temp - 256 : temp;

        // }

        return (byte) (value & 0xFF);

    }



    /**

     * BCD码的字节转成16进制的字节

     *

     * @param b

     * @return

     */

    public static byte BCDByteToByte(byte BCDByte) {



		/*

		 * (BCDByte>>4) 把BCD码右移4. (BCDByte>>4)&0x0F) 去掉符号位的数据,JAVA全是有符号移位

		 * ((BCDByte>>4)&0x0F)*10 把数据转10进制数据

		 */

        byte re = (byte) (((BCDByte >> 4) & 0x0F) * 10);

        re += BCDByte & 0x0F;

        return re;

    }



    public static byte ByteToBCDByte(byte _10Byte) {

        byte re = (byte) ((_10Byte / 10) << 4);

        re += _10Byte % 10;

        return re;

    }



    // public static void main(String[] args) {

    // byte b = (byte) 0x96;

    // System.out.println(BCDByteToByte(b));

    // System.out.println(toHexString(ByteToBCDByte(BCDByteToByte(b))));

    // System.out.println("toHexString(b) ->" + toHexString(b));

    //

    // }





    public static void main(String[] args) {

//		int i=20;

//		short s=(short)i;

//		System.out.println(s);

//		byte[] aa1 = new byte[]{0x01,(byte) 0xB0,0x01};

//		ByteBuffer buffer = ByteBuffer.wrap(aa1);

//		 byte t1 = buffer.get();

//		 System.out.println("pack t1->"+Tools.toHexString(t1));

//		 int t=t1;

//		 t=t<<8;

//		 byte t2 = buffer.get();

//		 System.out.println("pack t2->"+Tools.toHexString(t2));

//		 t=t+(t2&0x00FF);

//		 System.out.println("ttt->"+t);

//		 t=t<<8;

//		 byte t3 = buffer.get();

//		 System.out.println("pack t3->"+Tools.toHexString(t3));

//		 t=t+(t3&0x00FF);

//		 int packCount=t&0x0FFF;

//		 System.out.println("base packCount->"+packCount);

//		 t=t>>12;

//		 int packNum=t&0x0FFF;

//		 System.out.println("base  packNum->"+packNum);



        int t= 0x01;

        System.out.println("t1->"+t);

        t=t<<8;

        t=t+0xB0;

        System.out.println("ttt->"+t);

        t=t<<8;

        t=t+0x01;

        int  packCount=t&0x0FFF;

        t=t>>12;

        int packNum=t&0x0FFF;

        System.out.println("packCount->"+packCount);

        System.out.println("packNum->"+packNum);

    }

//

    final static char[] DigitHex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B',

            'C', 'D', 'E', 'F' };



}


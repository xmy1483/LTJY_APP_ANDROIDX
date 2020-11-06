package com.bac.commonlib.seed;

public class Tools {


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



	public static String toBytesString(byte[] buff, int size) {

		return toBytesString(buff, 0, size);

	}


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


	public static String toHexString(byte b) {

		int tmp;

		char[] c = new char[2];

		tmp = b & 0x0F;

		c[1] = DigitHex[tmp];

		tmp = (b >> 4) & 0x0F;

		c[0] = DigitHex[tmp];



		return new String(c, 0, 2);

	}


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


	public static int bytesToShort(byte[] data) {

		return bytesToShort(data, 0);

	}


	public static int bytesToShort(byte[] data, int begin) {

		int re = byteToUbyte(data[begin]);

		re = (re << 8);

		re = re + byteToUbyte(data[begin + 1]);

		return re;

	}

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


	public static short byteToUbyte(byte value) {

		return (short) (value < 0 ? value + 256 : value);

	}



	public static byte uByteToByte(short value) {

		return (byte) (value > 0x80 ? value - 256 : value);

	}


	public static int shortToUShort(short value) {

		return value < 0 ? value + 0x10000 : value;

	}



	public static short uShortToShort(int value) {

		return (short) (value > 0x8000 ? value - 0x10000 : value);

	}


	public static long intToUInt(int value) {

		return value < 0 ? value + 0x100000000L : value;

	}


	public static int uIntToInt(long value) {

		return (int) (value > 0x80000000L ? value - 0x100000000L : value);

	}

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

	public static byte BCDByteToByte(byte BCDByte) {


		byte re = (byte) (((BCDByte >> 4) & 0x0F) * 10);

		re += BCDByte & 0x0F;

		return re;

	}



	public static byte ByteToBCDByte(byte _10Byte) {

		byte re = (byte) ((_10Byte / 10) << 4);

		re += _10Byte % 10;

		return re;

	}

	final static char[] DigitHex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B',

			'C', 'D', 'E', 'F' };

}


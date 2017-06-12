package com.meta.util.security;

import java.security.MessageDigest;

/**
 * Description: MD5 算法
 * Created by zhihu.kang
 * Time: 2017/6/13 0:27
 * Email:kangzhihu@163.com
 */

public class MD5Util {

    /**
	 * MD5
	 * @param origin
	 * @return
	 */
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	} 

	public static String md5Encode(String origin) {
		String resultString = null;

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(origin
					.getBytes()));
		} catch (Exception ex) {

		}
		return resultString;
	}

}

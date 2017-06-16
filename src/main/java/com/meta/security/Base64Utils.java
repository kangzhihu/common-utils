package com.meta.security;

import java.io.IOException;

/**
 * Description: <br/>
 * &nbsp;&nbsp;Base64 加密、解密工具<br/>
 * Created by zhihu.kang<br/>
 * Time: 2017/6/13 0:23<br/>
 * Email:kangzhihu@163.com<br/>
 */

public class Base64Utils {
    /**
	 * 编码
	 * 
	 * @param bstr
	 * @return String
	 */
	@SuppressWarnings("restriction")
	public static String encode(byte[] bstr) {
		return new sun.misc.BASE64Encoder().encode(bstr);
	}

	/**
	 * 解码
	 * 
	 * @param str
	 * @return string
	 */
	@SuppressWarnings("restriction")
	public static byte[] decode(String str) {
		byte[] bt = null;
		try {
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			bt = decoder.decodeBuffer(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bt;
	}
	
}

package com.meta.security;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Description: <br/>
 * AES高级加密标准<br/>
 * Created by zhihu.kang<br/>
 * Time: 2017/6/13 0:25<br/>
 * Email:kangzhihu@163.com<br/>
 */

public class AESUtil {
    private static final String KEY = "@Qsf/%3IUHl.df^sf;8&#$";
	
	private static SecretKeySpec skeySpec;                  
    private static Cipher cipher;      
    static{       
        try {  
        	 skeySpec = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), "AES");
             cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
        } catch (NoSuchPaddingException e) {  
            e.printStackTrace();  
        } catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}  
    }  //加密算法  
	/** 
     * 加密，使用指定数据源生成密钥，使用用户数据作为算法参数进行AES加密 
     * @param msg 加密的数据 
     * @return 
     */  
    public static String encrypt(String msg) {  
        String str = "";  
        try {             
            //用密钥和一组算法参数初始化此 cipher  
        	 cipher.init(Cipher.ENCRYPT_MODE, skeySpec);   
            //加密并转换成16进制字符串  
        	 str = asHex(cipher.doFinal(msg.getBytes(StandardCharsets.UTF_8)));
        } catch (BadPaddingException e) {  
            e.printStackTrace();  
        } catch (InvalidKeyException e) {  
            e.printStackTrace();  
        } catch (IllegalBlockSizeException e) {  
            e.printStackTrace();  
        }
		return str;
    }  
    /** 
     * 将字节数组转换成16进制字符串 
     * @param buf 
     * @return 
     */  
    private static String asHex(byte buf[]) {  
        StringBuffer strbuf = new StringBuffer(buf.length * 2);  
        int i;  
        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10)//小于十前面补零  
                strbuf.append("0");  
            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));  
        }  
        return strbuf.toString();  
    }  
	/**  
	 * 加密  
	 *   
	 * @param content 需要加密的内容  
	 * @param password  加密密码  
	 * @return  
	 */  
	public static String encrypt(String content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
	        secureRandom.setSeed(password.getBytes(StandardCharsets.UTF_8));
	        kgen.init(128, secureRandom);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(byteContent);
			return parseByte2HexStr(result); // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 解密
	 * 
	 * @param content
	 *            待解密内容
	 * @param password
	 *            解密密钥
	 * @return
	 */  
	public static String decrypt(String content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
	        secureRandom.setSeed(password.getBytes(StandardCharsets.UTF_8));
	        kgen.init(128, secureRandom);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(parseHexStr2Byte(content));
			return new String(result); // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**将二进制转换成16进制  
	 * @param buf  
	 * @return  
	 */  
	public static String parseByte2HexStr(byte buf[]) {   
	        StringBuffer sb = new StringBuffer();   
	        for (int i = 0; i < buf.length; i++) {   
	                String hex = Integer.toHexString(buf[i] & 0xFF);   
	                if (hex.length() == 1) {   
	                        hex = '0' + hex;   
	                }   
	                sb.append(hex.toUpperCase());   
	        }   
	        return sb.toString();   
	}  


	/**将16进制转换为二进制  
	 * @param hexStr  
	 * @return  
	 */  
	public static byte[] parseHexStr2Byte(String hexStr) {   
	        if (hexStr.length() < 1)   
	                return null;   
	        byte[] result = new byte[hexStr.length()/2];   
	        for (int i = 0;i< hexStr.length()/2; i++) {   
	                int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);   
	                int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);   
	                result[i] = (byte) (high * 16 + low);   
	        }   
	        return result;   
	}  

	public static void main(String[] args){
		String content = "<aa vlue=\"哈哈\">你好</aa>";   
		String password = "12345678";   
		//加密   
		String code = encrypt(content, password);
		System.out.println(code);
		System.out.println(decrypt(code, password));

	}
}

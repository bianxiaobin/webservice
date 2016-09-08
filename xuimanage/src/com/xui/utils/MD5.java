package com.xui.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	public static String  getMD5(String  sth){
		String  result="";
		try {
			MessageDigest md=MessageDigest.getInstance("md5");
			md.update(sth.getBytes());
			byte[] digest = md.digest();
			StringBuilder  builder=new StringBuilder();
			for(byte b:digest){
				/**toHexString默认在转换过程中将8进制的000000011换算成16进制的B故需要在修改为0B*/
				String str=Integer.toHexString(b&0xFF);
				if (str.length()==1) {
					builder.append("0");
				}
				builder.append(str);
			}
			result=builder.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}
}

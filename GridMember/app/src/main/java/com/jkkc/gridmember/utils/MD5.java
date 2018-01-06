package com.jkkc.gridmember.utils;

import java.security.MessageDigest;

public class MD5 {
	public MD5() {
	}

	private final static String[] HEXDATA = {
			"0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c",
			"d", "e", "f"};

	public static String byteArrayToString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	/**
	 * 将字符转换为16进制的字符串
	 * @param 要转换的字符
	 * @return 转换好的字符串
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n += 256;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return HEXDATA[d1] + HEXDATA[d2];
	}

	/**
	 * 将字符串使用MD5生成Hash摘要
	 * @param 要生成Hash摘要的字符串
	 * @return 32位的Hash摘要,String类型
	 */
	public static String md5Encode(String origin) {
		String resultString = null;

		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToString(
					md.digest(resultString.getBytes()));
		}
		catch (Exception ex) {

		}
		return resultString;
	}
	
	public static void main(String[] args){
		System.out.println(MD5.md5Encode("4e4380187bf5451a8dff9fb829d61375"));
		
	}
 

}

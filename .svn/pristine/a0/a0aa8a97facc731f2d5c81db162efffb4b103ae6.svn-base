package com.aiait.eflow.util;

import java.util.Random;

import org.apache.poi.ss.usermodel.DateUtil;

public class RandomUtil {
	
	public static final String ISUPPERCASE="isUpperCase";
	public static final String ISLOWERCASE="isLowCase";
	public static final String ISNUMBER="isNumber";
	public static final String OTHER="other";

	public static String createRandomStr(int length,String type){
		StringBuffer sb = null;
		if(length > 0){
			StringBuffer sbuffer = new StringBuffer();
			if(ISUPPERCASE.equals(type)){
				sbuffer.append("ABCDEFGHIGKLMNOPQRSTUVWXYZ");
			} else if(ISLOWERCASE.equals(type)){
				sbuffer.append("abcdefghigklmnopqrstuvwxyz");
			} else if(ISNUMBER.equals(type)){
				sbuffer.append("0123456789");
			} else {
				String words = "abcdefghigklmnopqrstuvwxyz0123456789ABCDEFGHIGKLMNOPQRSTUVWXYZ";
				sbuffer.append(words);
			}
			sb = new StringBuffer(length);
			for (int i = 0; i < length; i++) {
				sb.append(sbuffer.charAt(new Random().nextInt(sbuffer.length())));
			}
		}
		return sb.toString();
	}
}

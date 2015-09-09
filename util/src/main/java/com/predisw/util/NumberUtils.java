package com.predisw.util;

public class NumberUtils {
	/**
	 * 如果s 为null,则抛出NullPointerException.如果字符串是整数,或则浮点格式,则返回true
	 * @param s 字符串
	 * @return
	 */
	public static boolean isNumber(String s){
		try{
			Double.valueOf(s);
		}catch(NumberFormatException e){
			return false;
		}
		return true;

	}
}

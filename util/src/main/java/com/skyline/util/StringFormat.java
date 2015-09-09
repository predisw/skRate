package com.skyline.util;

import 	java.util.regex.*;

public class StringFormat {
	
	String reg = "\\d+[.,]\\d+[-~]+\\d+[.,]\\d+|\\d+[.,]\\d*|\\d+[-~]+\\d+|\\d+"; //匹配的顺序是 小数范围（0.12-0.23），小数，整数范围（2-4)，整数 ,是或的关系
	//如果前面的匹配了，那么后面的条件就不会再匹配，所以要将范围匹配的放到前面
	
	//提取字符串中的数字/浮点数部分，并把，替换成.，如4,5 换成4.5；还有就是 2-3，或者2~3 或者 3.4-4.4 这种只会取范围的第一个数字2，或者3.4
	public String  StringToNumF(String str){
	/*	String reg = "\\d+[.,]\\d*|\\d+"; //小数或者整数，不能把匹配整数部分放到第一部分来匹配，这样小数只会匹配完整数部分，就跳过了，因为|
		// 第二部分的 \\d+ 如果换成\\d*,就不能匹配 w2.8 之类的的，就是数字之前有字符的类型  不知为啥？？
*/		
		Pattern p= Pattern.compile(reg);
		Matcher m = p.matcher(str);
		if(m.find()){
			return m.group().replace(",", ".").replaceAll("[-~]+\\d+[.,]\\d+", "").replaceAll("[-~]+\\d+", "");  //replaceAll 的作用是把2-3 或者2~3中的-3或者~3  换成空字符串“”。
			//是为了将范围的前半段的数字提取出来，支持浮点或整数
		}else 
			return null;
	}
	
	//提取字符串中的数字/浮点数部分，并把，替换成.，如4,5 换成4.5；还有就是 2-3，或者2~3 或者浮点 3.2-4.3 这种只会取范围的后面这个值如3，或者 4.3
		public String StringToNumL(String str){
		/*	String reg = "\\d+[.,]\\d+[-~]+\\d+[.,]\\d+|\\d+[.,]\\d*|\\d+[-~]+\\d+|\\d+"; //匹配的顺序是 小数范围（0.12-0.23），小数，整数范围（2-4)，整数 ,是或的关系
			//如果前面的匹配了，那么后面的条件就不会再匹配，所以要将范围匹配的放到前面
			*/
			Pattern p= Pattern.compile(reg);
			Matcher m = p.matcher(str);
			if(m.find()){
				return m.group().replace(",", ".").replaceAll("\\d+[.,]\\d+[-~]+", "").replaceAll("\\d+[-~]+", "");  //replaceAll 的作用是把2-3 或者2~3中的2- 或者2～  换成空字符串“”。
			}else 
		return null;
	}
}

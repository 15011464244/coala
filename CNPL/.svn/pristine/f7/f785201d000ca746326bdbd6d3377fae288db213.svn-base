package com.cn.net.cnpl.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

public class RegexPattern {
	/**
	 * 正则表达式验证
	 * @param context android控制层的上下文
	 * @param regx 正则表达式字符串
	 * @param editText android文本控件
	 * @param msg 正则表达式验证不符的提示信息
	 * @return 验证符合返回true
	 */
	public static boolean matchs(Context context,String regx,EditText editText,String msg){
		Pattern pattern=Pattern.compile(regx);
		String text=editText.getText().toString();
		boolean b=pattern.matcher(text).matches();
		if(!b){
			Toast.makeText(context, msg,Toast.LENGTH_LONG).show();
			return false;			
		}
		return true;
	}
	
	/**
	 * 正则表达式验证
	 * @param context context android控制层的上下文
	 * @param regx 正则表达式字符串
	 * @param text 要验证的字符串
	 * @param msg 正则表达式验证不符的提示信息
	 * @return 验证符合返回true
	 */
	public static boolean matchs(Context context,String regx,String text,String msg){
		Pattern pattern=Pattern.compile(regx);
		boolean b=pattern.matcher(text).matches();
		if(!b){
			Toast.makeText(context, msg,Toast.LENGTH_LONG).show();
			return false;			
		}
		return true;
	}
	
	/**
	 * 正则表达式验证
	 * @param regx 正则表达式字符串
	 * @param text 要验证的字符串
	 * @return 验证符合返回true
	 */
	public static boolean matchs(String regx,String text){
		Pattern pattern=Pattern.compile(regx);
		return pattern.matcher(text).matches();
	}
	
	
	public static   boolean validateEmail(Context context,String email,String msg) {
//		if(email==null||"".equals(email)){
//			Toast.makeText(context, msg,Toast.LENGTH_LONG).show();
//			return false;
//		}
		String pattern1 = "^([a-z0-9A-Z_]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern pattern = Pattern.compile(pattern1);
		boolean b=pattern.matcher(email).matches();
		if(!b){
			Toast.makeText(context, msg,Toast.LENGTH_LONG).show();
			return false;			
		}
		return true;
	}
	
	
	
	public static   boolean validateIsMobile(Context context,String phone,String msg) {
//		if(phone==null||"".equals(phone)){
//			Toast.makeText(context, msg,Toast.LENGTH_LONG).show();
//			return false;
//		} 
		Pattern p = Pattern.compile("^((1[0-9][0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(phone);
		if(!m.matches()){
			Toast.makeText(context, msg,Toast.LENGTH_LONG).show();
			return false;			
		}
		return true;
	}
	
	
}

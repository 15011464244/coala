package com.arvin.koalapush.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SpfsUtil {

	private static SharedPreferences sharedPreferences;

	public static void init(Context c) {
		sharedPreferences = c.getSharedPreferences("koalaPush", 0);
	}

	public static void setWifiMacAddress(String wifiMacAddress){
		sharedPreferences.edit().putString("wifiMac", wifiMacAddress).commit();
	}
	public static String getWifiMacAddress(){
		return sharedPreferences.getString("wifiMac", null);
	}
	//是否打印错误日志,默认不打印
	public static void setShowErrorLog(Boolean show){
		 sharedPreferences.edit().putBoolean("errorLog", show).commit();
	}
	public static boolean isShowErrorLog(){
		return sharedPreferences.getBoolean("errorLog", false);
	}
	//重连次数，默认为0
	public static void setTimes(int times){
		sharedPreferences.edit().putInt("times", times).commit();
	}
	public static int getTimes(){
		return sharedPreferences.getInt("times", 0);
	}
	//超时时间,默认30秒
	public static void setTimeOut(int timeOut){
		sharedPreferences.edit().putInt("timeOut", timeOut).commit();
	}
	public static int getTimeOut(){
		return sharedPreferences.getInt("timeOut",30);
	}
	
	/* mina服务器地址  app_key */
	public static void setAppKey(String appKey){
		sharedPreferences.edit().putString("appKey", appKey).commit();
	}
	public static String getAppKey(){
		return sharedPreferences.getString("appKey",null);
	}
	//ClientId
	public static void setClientId(String clientId){
		sharedPreferences.edit().putString("clientId", clientId).commit();
	}
	public static String getClientId(){
		return sharedPreferences.getString("clientId",null);
	}
}

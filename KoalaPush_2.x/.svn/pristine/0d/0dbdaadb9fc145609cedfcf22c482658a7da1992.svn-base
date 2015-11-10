package com.arvin.koalapush.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SpfsUtil {

	private static SharedPreferences sharedPreferences;

	public static void init(Context c) {
		sharedPreferences = c.getSharedPreferences("koalaPush", 0);
	}

	public static void setWifiMacAddress(String wifiMacAddress){
		if(sharedPreferences!=null){
			sharedPreferences.edit().putString("wifiMac", wifiMacAddress).commit();
		}
	}
	public static String getWifiMacAddress(){
		if(sharedPreferences == null){
			return null;
		}
		return sharedPreferences.getString("wifiMac", null);
	}
	public static void setClientId(String clientId){
		if(sharedPreferences!=null){
			sharedPreferences.edit().putString("clientId", clientId).commit();
		}
	}
	public static String getClientId(){
		if(sharedPreferences==null){
			return null;
		}
		return sharedPreferences.getString("clientId", null);
	}
}

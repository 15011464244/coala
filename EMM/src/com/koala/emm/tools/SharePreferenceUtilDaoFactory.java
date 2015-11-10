package com.koala.emm.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

@SuppressLint("CommitPrefEdits")
public class SharePreferenceUtilDaoFactory {
	private static SharedPreferences sp;
	private static SharePreferenceUtilDaoFactory spDao;
	private static SharedPreferences.Editor editor;
	private static String file = "saveUser";

	public synchronized static SharePreferenceUtilDaoFactory getInstance(
			Context context) {// 单线程单例
		if (spDao == null) {
			spDao = new SharePreferenceUtilDaoFactory();
		}
		sp = context.getSharedPreferences(file, context.MODE_PRIVATE);
		editor = sp.edit();
		return spDao;
	}

	// 是否是第一次定位成功
	public void setIsFirstLocation(boolean IsFirstLocation) {
		editor.putBoolean("IsFirstLocation", IsFirstLocation);
		editor.commit();
	}

	public boolean getIsFirstLocation() {
		return sp.getBoolean("IsFirstLocation", true);
	}

	// 是否是第一次安装 默认是第一次安装
	public void setIsFirst(boolean IsFirst) {
		editor.putBoolean("IsFirst", IsFirst);
		editor.commit();
	}

	public boolean getIsFirst() {
		return sp.getBoolean("IsFirst", true);
	}
	// 存储流量
	public void setTraffic(String Traffic) {
		editor.putString("Traffic", Traffic);
		editor.commit();
	}
	// 获取流量
	public String getTraffic() {
		return sp.getString("Traffic", "");
	}
}

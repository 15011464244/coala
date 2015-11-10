package com.ems.express.util;

import java.util.ArrayList;

import com.ems.express.ui.AddCommonActivity;

import android.content.Context;
import android.content.SharedPreferences;

public class SpfsUtil {

	private static SharedPreferences sharedPreferences;

	public static void init(Context c) {
		sharedPreferences = c.getSharedPreferences("unlock", 0);
	}

	public static void saveId(String sid) {
		sharedPreferences.edit().putString("sid", sid).commit();
	}

	public static String loadId() {
		return sharedPreferences.getString("sid", "");
	}

	public static void savePhone(String phone) {
		sharedPreferences.edit().putString("phone", phone).commit();
	}

	public static String loadPhone() {
		return sharedPreferences.getString("phone", "");
	}
	
	public static void saveTelephone(String phone) {
		sharedPreferences.edit().putString("telephone", phone).commit();
	}
	
	public static String loadTelephone() {
		return sharedPreferences.getString("telephone", "");
	}

	public static void saveHeader(String header) {
		sharedPreferences.edit().putString("header", header).commit();
	}

	public static String loadHeader() {
		return sharedPreferences.getString("header", "");
	}

	public static void saveAddress(String address) {
		sharedPreferences.edit().putString("address", address).commit();
	}

	public static String loadAddress() {
		return sharedPreferences.getString("address", "");
	}

	public static void saveName(String name) {
		sharedPreferences.edit().putString("name", name).commit();
	}

	public static String loadName() {
		return sharedPreferences.getString("name", "");
	}

	public static void saveHeadImageUrl(String url){
		sharedPreferences.edit().putString("headImageUrl", url).commit();
	}
	public static String loadHeadImageUrl(){
		return sharedPreferences.getString("headImageUrl", "");
	}
	
	public static ArrayList<String> loadCommonWords() {
		String commonWords = sharedPreferences.getString(AddCommonActivity.DEFAULT_COMMON_WORDS, "__");
		String[] wordsArray = commonWords.split("__");
		ArrayList<String> wordsList = null;
		if (wordsArray.length != 0) {
			wordsList = new ArrayList<String>();
			for (String string : wordsArray) {
				wordsList.add(string);
			}
		}
		return wordsList;
	}

	public static void saveCommonWords(ArrayList<String> wordsList) {
		if (wordsList.isEmpty()) {
			return;
		}
		String commonWords = "";
		for (String string : wordsList) {
			commonWords += string + "__";
		}
		commonWords.substring(0, commonWords.lastIndexOf("__") - 1);
		sharedPreferences.edit().putString(AddCommonActivity.DEFAULT_COMMON_WORDS, commonWords).commit();
	}

	public static boolean getBoolean(String key) {
		return sharedPreferences.getBoolean(key, false);
	}

	public static void putBoolean(String key, boolean value) {
		sharedPreferences.edit().putBoolean(key, value).commit();
	}
	
	public static String getString(String key){
		return sharedPreferences.getString(key, "");
	}
	
	public static void saveString(String key, String value){
		sharedPreferences.edit().putString(key, value).commit();
	}
	//设置获取震动开关的值
	public static void saveShake(Boolean isShake) {
		sharedPreferences.edit().putBoolean("isShake", isShake).commit();
	}

	public static Boolean loadShake() {
		return sharedPreferences.getBoolean("isShake", true);
	}
	//设置获取提示音开关的值
	public static void saveTone(Boolean isTone) {
		sharedPreferences.edit().putBoolean("isTone", isTone).commit();
	}

	public static Boolean loadTone() {
		return sharedPreferences.getBoolean("isTone", true);
	}
	
}

package com.ems.express.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.ems.express.bean.PeopleInfo;
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
	
	public static void isLoadingCity(Boolean isLoading){
		sharedPreferences.edit().putBoolean("isLoading", isLoading).commit();
	}
	public static Boolean getIsLoadingCity(){
		return sharedPreferences.getBoolean("isLoading", true);
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
		String result = sharedPreferences.getString("name", "");
		if(null == result || "null".equals(result)){
			return "";
		}
		return result;
//		return sharedPreferences.getString("name", "");
	}

	public static void saveHeadImageUrl(String url){
		sharedPreferences.edit().putString("headImageUrl", url).commit();
	}
	public static String loadHeadImageUrl(){
		return sharedPreferences.getString("headImageUrl", "");
	}
	
	public static void saveMailNums(String mail) {
		sharedPreferences.edit().putString("mails",mail).commit();
	}

	public static String loadMailNums() {
		return  sharedPreferences.getString("mails", null);
	}
	
	
	
	
	
	
	public static ArrayList<String> loadCommonWords() {
		String commonWords = sharedPreferences.getString(AddCommonActivity.DEFAULT_COMMON_WORDS, "__");
//		String commonWords = "";
		String[] wordsArray = commonWords.split("__");
		ArrayList<String> wordsList = new ArrayList<String>();
		if (wordsArray.length != 0 ) {
//			wordsList = new ArrayList<String>();
			for (String string : wordsArray) {
				wordsList.add(string);
			}
		}
		return wordsList;
	}

	public static void saveCommonWords(ArrayList<String> wordsList) {
//		if (wordsList.isEmpty()) {
//			return;
//		}
		String commonWords = null;
		if(wordsList.size() > 0){
			commonWords = "";
		}
		for (String string : wordsList) {
			commonWords += string + "__";
		}
		if(null != commonWords && commonWords.contains("__")){
			commonWords.substring(0, commonWords.lastIndexOf("__") - 1);
		}
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
	//是否正在更新
	public static void saveDowning(Boolean downing) {
		sharedPreferences.edit().putBoolean("downing", downing).commit();
	}

	public static Boolean loadDowning() {
		return sharedPreferences.getBoolean("downing", false);
	}
	/**
	 * 是否签到
	 */
	public static void setIsSign(Boolean isSign){
		sharedPreferences.edit().putBoolean("isSign", isSign).commit();
	}
	public static Boolean isSign(){
		return sharedPreferences.getBoolean("isSign", false);
	}
	
	/**
	 * 在搜索的时候将搜素的结果添加到sharedPreference中
	 */
	public static void saveHistory(String history){
		sharedPreferences.edit().putString("history", history).commit();
	}
	public static String getHistory(){
		return sharedPreferences.getString("history", "");
	}
	
	/**
	 * 设置默认的寄件人
	 * @param peopleInfo
	 */
	public static void saveDefaultSender(PeopleInfo peopleInfo){
		sharedPreferences.edit().putString("sname", peopleInfo.getName())
								.putString("sphone", peopleInfo.getPhone())
								.putString("send_prov", peopleInfo.getProv())
								.putString("send_city", peopleInfo.getCity())
								.putString("send_county", peopleInfo.getCounty())
								.putString("send_prov_code", peopleInfo.getProvCode())
								.putString("send_city_code", peopleInfo.getCityCode())
								.putString("send_county_code", peopleInfo.getCountyCode())
								.putString("slocation", peopleInfo.getLocation())
								.commit();
	}
	public static PeopleInfo getDefaultSender(){
		PeopleInfo peopleInfo = new PeopleInfo();
		peopleInfo.setName(sharedPreferences.getString("sname", ""));
		peopleInfo.setPhone(sharedPreferences.getString("sphone", ""));
		peopleInfo.setProv(sharedPreferences.getString("send_prov", ""));
		peopleInfo.setCity(sharedPreferences.getString("send_city", ""));
		peopleInfo.setCounty(sharedPreferences.getString("send_county", ""));
		peopleInfo.setProvCode(sharedPreferences.getString("send_prov_code", ""));
		peopleInfo.setCityCode(sharedPreferences.getString("send_city_code", ""));
		peopleInfo.setCountyCode(sharedPreferences.getString("send_county_code", ""));
		peopleInfo.setLocation(sharedPreferences.getString("slocation", ""));
		return peopleInfo;
	}
}

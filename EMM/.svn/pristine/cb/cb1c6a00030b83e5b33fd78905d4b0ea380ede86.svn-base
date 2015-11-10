package com.koala.emm.util;

import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import android.content.Context;
import android.content.SharedPreferences;

public class SpfsUtil {

	private static SharedPreferences sharedPreferences;

	public static void init(Context c) {
		sharedPreferences = c.getSharedPreferences("unlock", 0);
	}
	
	/**
	 * 存储电量报警设置
	 * @param obj
	 */
	public static void setElectricity(String obj){
		sharedPreferences.edit().putString("electricity", obj).commit();
	}
	/**
	 * 获取电量报警设置
	 * @return
	 */
	public static String getElectricity(){
		return sharedPreferences.getString("electricity", null);
	}
	/**
	 * 存储流量报警设置
	 * @param obj
	 */
	public static void setFlow(String obj){
		sharedPreferences.edit().putString("flow", obj).commit();
	}
	/**
	 * 获取流量报警设置
	 * @return
	 */
	public static String getFlow(){
		return sharedPreferences.getString("flow", null);
	}
	/**
	 * 存储内存报警设置
	 * @param obj
	 */
	public static void setMemory(String obj){
		sharedPreferences.edit().putString("memory", obj).commit();
	}
	/**
	 * 获取内存报警设置
	 * @return
	 */
	public static String getMemory(){
		return sharedPreferences.getString("memory", null);
	}
	/**
	 * 推送ID
	 * @param obj
	 */
	public static void setClientId(String obj){
		sharedPreferences.edit().putString("clientId", obj).commit();
	}
	public static String getClientId(){
		return sharedPreferences.getString("clientId", null);
	}
	/**
	 * 加密的状态
	 * @param obj
	 */
	public static void setEncryption(String obj){
		sharedPreferences.edit().putString("encryption", obj).commit();
	}
	public static String getEncryption(){
		return sharedPreferences.getString("encryption", "0");
	}
	/**
	 * 存入强制更新应用版本
	 */
	public static void setUpdateVersion(String updateVersion){
		sharedPreferences.edit().putString("updateVersion", updateVersion).commit();
	}
	/**
	 * 获取强制更新应用版本
	 * @return
	 */
	public static String getUpdateVersion(){
		return sharedPreferences.getString("updateVersion", null);
	}
	/**
	 * 存入强制更新下载地址
	 */
	public static void setUpdateUrl(String updateUrl){
		sharedPreferences.edit().putString("updateUrl", updateUrl).commit();
	}
	/**
	 * 获取强制更新下载地址
	 * @return
	 */
	public static String getUpdateUrl(){
		return sharedPreferences.getString("updateUrl", null);
	}
	/**
	 * 存入强制更新标识
	 */
	public static void setUpdateForce(Boolean isUpadateForce){
		sharedPreferences.edit().putBoolean("isUpadateForce", isUpadateForce).commit();
	}
	/**
	 * 获取强制更新标识
	 * @return
	 */
	public static Boolean getUpdateForce(){
		return sharedPreferences.getBoolean("isUpadateForce", false);
	}
	/**
	 * 强制卸载标识
	 * @param uninstall
	 */
	public static void setUninstall(Boolean uninstall){
		sharedPreferences.edit().putBoolean("uninstall", uninstall).commit();
	}
	/**
	 * 获取强制卸载标识
	 * @return
	 */
	public static Boolean getUninstall(){
		return sharedPreferences.getBoolean("uninstall", false);
	}
	/**
	 * 设置check标识
	 * @param state
	 */
	public static void setCheckState(Boolean state){
		sharedPreferences.edit().putBoolean("checkState", state).commit();
	}
	/**
	 * 获取check标识
	 * @return
	 */
	public static Boolean getCheckState(){
		return sharedPreferences.getBoolean("checkState", true);
	}
	/**
	 * 存用户名
	 * @param userInfo
	 */
	public static void setUserName(String userName){
		sharedPreferences.edit().putString("userName", userName).commit();
	}
	/**
	 * 获取用户名
	 */
	public static String getUserName(){
		return sharedPreferences.getString("userName", "");
	}
	
	/**
	 * 存机构号
	 * @param userInfo
	 */
	public static void setOrgId(String orgId){
		sharedPreferences.edit().putString("orgId", orgId).commit();
	}
	/**
	 * 获取机构号
	 */
	public static String getOrgId(){
		return sharedPreferences.getString("orgId", "");
	}
	
	/**
	 * 存用户id
	 * @param userInfo
	 */
	public static void setUserId(String userId){
		sharedPreferences.edit().putString("userId", userId).commit();
	}
	/**
	 * 获取用户id
	 */
	public static String getUserId(){
		return sharedPreferences.getString("userId", "");
	}
	/**
	 * 设置服务的启动时间
	 */
	public static void setStartTime(String times){
		sharedPreferences.edit().putString("startTime", times).commit();
	}
	/**
	 * 获取服务的启动时间
	 */
	public static String getStartTime(){
		return  sharedPreferences.getString("startTime", null);
	}
	
	/**
	 * 设置服务的启动时间
	 */
	public static void setEndtTime(String timeSet){
		sharedPreferences.edit().putString("endTime", timeSet).commit();
	}
	
	/**
	 * 获取服务的启动时间
	 */
	public static String getEndTime(){
		return  sharedPreferences.getString("endTime", null);
	}
	
	/**
	 * 设置密码
	 */
	public static void setPsw(String psw){
		sharedPreferences.edit().putString("password", psw).commit();
	}
	
	/**
	 * 获取密码
	 */
	public static String getPsw(){
		return  sharedPreferences.getString("password", null);
	}
	
	
}

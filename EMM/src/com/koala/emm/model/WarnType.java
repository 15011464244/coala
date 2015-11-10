/**
 * 
 */
package com.koala.emm.model;

/**
 * @author wanjun  2015年6月5日,下午2:11:44
 * 
 */
public abstract class WarnType {

	/**
	 * 数据加密
	 */
	public static String DATA_ENCRYPTION = "1";
	/**
	 * 数据强制更新
	 */
	public static String DATA_FORCED_UPDATE = "2";
	/**
	 * 非强制数据更新
	 */
	public static String NOT_DATA_FORCED_UPDATE = "3";
	/**
	 * 应用强制更新
	 */
	public static String APPLICATION_UPDATE_FORCED = "4";
	/**
	 * 应用删除
	 */
	public static String APPLICATION_DELETE = "5";
	/**
	 * 内存清理
	 */
	public static String MEMORY_CLEANUP = "6";
	/**
	 * 策略更新
	 */
	public static String POLICY_UPDATE = "7";
	/**
	 * 数据解密
	 */
	public static String DATA_DECRYPTION = "8";
	/**
	 * 接受消息
	 */
	public static String RECEIVE_MESSAGE = "9";
	/**
	 * 应用非强制更新
	 */
	public static String APPLICATION_NOT_UPDATE_FORCED = "10";
	/**
	 * 锁屏
	 */
	public static String DEVICE_LOCK = "11";
	/**
	 * 恢复出厂设置
	 */
	public static String DEVICE_RECOVERY = "12";
	
	
}

package com.koala.emm.tools;

import android.content.Context;

import com.koala.emm.update.CurrentVersion;

/**
 * 手机信息类
 * 
 * @author hanrong
 * 
 */
public class PhoneMessageUtils {

	private String mSystemVersion;// 系统版本
	private String mEquipmentmanufacturers;// 生产厂家
	private String mENS; // ESN(序列号)
	private Context mContext;
	private String mAppVersion;// 应用版本

	public PhoneMessageUtils(Context mContext) {
		super();
		this.mContext = mContext;
		mSystemVersion = android.os.Build.VERSION.RELEASE;
		mEquipmentmanufacturers = android.os.Build.MANUFACTURER;
	}

	/**
	 * 获取系统版本号
	 * 
	 * @return
	 */
	public String getmSystemVersion() {
		mSystemVersion = android.os.Build.VERSION.RELEASE;
		return mSystemVersion;
	}

	/**
	 * 获取生产厂家
	 * 
	 * @return
	 */
	public String getmEquipmentmanufacturers() {
		mEquipmentmanufacturers = android.os.Build.MANUFACTURER;
		return mEquipmentmanufacturers;
	}
	/**
	 * 获取设备型号
	 * 
	 * @return
	 */
	public String getmAnlagentyp() {
		return android.os.Build.MODEL;
	}

	/**
	 * 获得序列号
	 * @return
	 */
	public String getmENS() {
		mENS = android.os.Build.SERIAL;
		return mENS;
	}

	/**
	 * 获取应用版本号
	 * @return
	 */
	public String getmAppVersion() {
		if (mContext != null) {
			mAppVersion =CurrentVersion.getVersinName(mContext);
		}
		return mAppVersion;
	}
}

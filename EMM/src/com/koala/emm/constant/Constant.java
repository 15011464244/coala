package com.koala.emm.constant;

public class Constant {
	//集成版
//	public static final String ROOT = "http://218.245.3.10:8080/MDMIntegrate";
//	public static final String ROOT = "http://192.168.253.8:8080/MDMIntegrate";
	//完整版
	public static final String ROOT = "http://218.245.3.10:8080/ProjectEMM";
//	public static final String ROOT = "http://172.29.38.3:8080/ProjectEMM";

	// 激活
	public static final String auth = ROOT + "/application/activated.html";
	// 设备信息采集
	public static final String deviceCollection = ROOT
			+ "/deviceInfo/insertInfo.html";
	// 策略更新接口
	public static final String strategyStorage = ROOT + "/application/selectStrategy.html";
	// 报警信息采集
	public static final String warnInfoCollection = ROOT + "/warnList/insertWarnInfo.html";
	// 签到接口
	public static final String sign = ROOT + "/application/onSign.html";
	
	//获取签到策略接口
	public static final String signPolicy = ROOT + "/application/onSignStrategy.html";
	/*
	 * 查看app是否需要升级或者更新
	 */
	public static final String isUpdate = ROOT + "/application/getHistoryMsg.html";
	

}

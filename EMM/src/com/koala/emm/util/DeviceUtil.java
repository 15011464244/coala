package com.koala.emm.util;

import com.koala.emm.app.EmmApplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class DeviceUtil {
	private static DeviceUtil deviceUtil = new DeviceUtil();
	static TelephonyManager mTelephonyMgr = (TelephonyManager) EmmApplication.getInstance().
			getSystemService(Context.TELEPHONY_SERVICE);
	static ConnectivityManager connectManager = (ConnectivityManager) EmmApplication.getInstance()   
            .getSystemService(Context.CONNECTIVITY_SERVICE);
	private DeviceUtil(){}
	
	public static DeviceUtil getInstance(){
		return deviceUtil;
	}
	//获取手机的IMEI
	public static String getDeviceNo(){
		return mTelephonyMgr.getDeviceId();
	}
	//检测手机网络
	public static boolean isNetwork() {        	
            NetworkInfo[] info = connectManager.getAllNetworkInfo();   
            if (info != null) {   
                for (int i = 0; i < info.length; i++) {   
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {   
                        return true;   
                    } 
                }
            }
        return false;
	}
	//检测wifi网络
	public static boolean isWifiNetwork() {        	
        NetworkInfo[] info = connectManager.getAllNetworkInfo(); 
        NetworkInfo wifi = connectManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifi.isAvailable();
	}
	//检测手机移动网络
	public static boolean isMobileNetwork() {        	
        NetworkInfo[] info = connectManager.getAllNetworkInfo(); 
        NetworkInfo Mobile = connectManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return Mobile.isAvailable();
	}

}

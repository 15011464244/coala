package com.cn.net.cnpl.tools;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

//GPS地址获取类
public class BaiduGps {
	
	private static BaiduGps instance = null;
	// 百度
	public LocationClient mLocationClient = null;

	private String addressStr = "";
	
	private  BDLocation bdLocation;
	
	public static BaiduGps getInstance(Context context) {
		if (instance == null) {
			instance = new BaiduGps(context);
	    }
		return instance;
	}

	private  BaiduGps(Context context) {
		mLocationClient = new LocationClient(context); // 声明LocationClient类
		baidusetOption();
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
//		mLocationClient.start();
	}
	


	public BDLocationListener myListener = new BDLocationListener() {

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location != null) {
				bdLocation=location;
				addressStr = location.getAddrStr();
			}
			if (mLocationClient != null && mLocationClient.isStarted()) {
				mLocationClient.stop();
			}
		}
	};

	public String getAddress() {
		if(addressStr != null){
			return addressStr;
		}else{
			return "";
		}
		
	}

	public BDLocation getBdLocation() {
		return bdLocation;
	}

	public void getLocation() {
		if (! mLocationClient.isStarted()) {
			mLocationClient.start();
		}
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.requestLocation();
		}

	}

	
	private void baidusetOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(10000);// 设置发起定位请求的间隔时间为10miao
		option.disableCache(true);// 禁止启用缓存定位
		option.setPoiNumber(0);// 最多返回POI个数
		option.setPoiDistance(0);// poi查询距离
		option.setPoiExtraInfo(false);// 是否需要POI的电话和地址等详细信息
		option.setPriority(LocationClientOption.GpsFirst);//优先使用GPS
		mLocationClient.setLocOption(option);
	}

}

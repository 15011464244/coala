package com.koala.emm.gps;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.koala.emm.tools.BaiduGpsContants;
import com.koala.emm.tools.SharePreferenceUtilDaoFactory;
import com.lidroid.xutils.util.LogUtils;

/**
 * 定位服务
 * 
 * @author hanrong
 * 
 */
public class LocationService extends Service {
	private LocationClient mLocationClient = null;
	private String locAddr, locLat, locLng, pro, city, district, street,
			streetNumber;
	
	private LocationClientOption option ;
	private int user_distance;// 用户自定义范围
	// private String oldAddr;// 旧地址
	private int LOCTIME = 15 * 1000;// 定位间隔时间
	private Timer timer;// 定时器重启定位
	public MyLocationListener myListener;// 定位监听器
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				Log.e("tag", "开始");
				baiduLocation();
			}
		};
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public ComponentName startService(Intent service) {
		return super.startService(service);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		 Log.e("tag", "启动");
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				handler.sendEmptyMessage(1);
			}
		}, 10, 1000 * 30 * 1);
		return START_STICKY;
	}

	private void baiduLocation() {
		if(null == mLocationClient){
			mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
			if (myListener == null) {
				myListener = new MyLocationListener();
			}
			if(null == option){
				option = new LocationClientOption();
				
				option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
				option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
				option.setScanSpan(LOCTIME);// 设置发起定位请求的间隔时间为15000ms
				option.setIsNeedAddress(true);// 是否需要地址信息
			}
			mLocationClient.setLocOption(option);
			mLocationClient.registerLocationListener(myListener);
			mLocationClient.start();
			if (mLocationClient.isStarted() && mLocationClient != null) {
				mLocationClient.requestLocation();
			}
		}
		
	}

	class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(final BDLocation location) {
			if (null != location) {
				locAddr = location.getAddrStr();
				locLat = String.valueOf(location.getLatitude());
				locLng = String.valueOf(location.getLongitude());
				pro = String.valueOf(location.getProvince());
				city = String.valueOf(location.getCity());
				district = String.valueOf(location.getDistrict());
				street = String.valueOf(location.getStreet());
				streetNumber = String.valueOf(location.getStreetNumber());
				BaiduGpsContants.getInstance().setAddressStr(locAddr);
				BaiduGpsContants.getInstance().setLat(locLat);
				BaiduGpsContants.getInstance().setLng(locLng);
				BaiduGpsContants.getInstance().setPro(pro);
				BaiduGpsContants.getInstance().setCity(city);
				BaiduGpsContants.getInstance().setDistrict(district);
				BaiduGpsContants.getInstance().setStreet(street);
				BaiduGpsContants.getInstance().setStreetNumber(streetNumber);
				BaiduGpsContants.getInstance().setTime(
						System.currentTimeMillis());
			} else {
				mLocationClient.requestLocation();
				// baiduLocation();
			}
			// Log.e("tag", "地址信息" + locAddr + locLat + locLng + pro + city
			// + district + street + streetNumber);
			// LogUtils.e("locLat:  " + locLat + "  locLng:  " + locLng);
			// 只发送一次地理位置广播
			if (SharePreferenceUtilDaoFactory.getInstance(
					getApplicationContext()).getIsFirstLocation()) {
				// 发送广播 以便上传基本信息
				Intent mIntent = new Intent("location.ok");
				mIntent.putExtra("locLat", locLat);
				mIntent.putExtra("locLng", locLng);
				sendBroadcast(mIntent);
				SharePreferenceUtilDaoFactory.getInstance(
						getApplicationContext()).setIsFirstLocation(false);
			}
		}
	};

	/**
	 * 优化收件地址，对于非空地址，添加定位省市信息，提高距离计算精准度
	 * 
	 * @return 如：小店区 --->山西省太原市小店区
	 */
	public String optAddress(String addr) {
		if (addr != null && !"".equals(addr)) {
			if (city != null && !"null".equals(city)) {
				addr = city + addr;
			}
			if (pro != null && !"null".equals(pro)) {
				addr = pro + addr;
			}
		}
		return addr;
	}

	/**
	 * 关闭定位的一切活动
	 */
	public void stopLocationListener() {
		timer.cancel();
		mLocationClient.unRegisterLocationListener(myListener);
		mLocationClient.stop();
		mLocationClient = null;
		myListener = null;
		stopSelf();
	}

	@Override
	public void onDestroy() {
		try {
			stopLocationListener();
		} catch (Exception e) {
		}
		super.onDestroy();
	}

}

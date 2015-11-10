package com.koala.emm.supervision;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.koala.emm.tools.NetHelper;
import com.koala.emm.tools.PhoneMessageUtils;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Service;
import android.app.ActivityManager.MemoryInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

/**
 * 警告服务
 * 
 * @author hanrong
 * 
 */
public class AlarmService extends Service {
	private int LOCTIME = 15 * 1000;// 定位间隔时间
	private Timer timer;// 定时器重启定位
	private BatteryMessageReceiver mBatteryMessageReceiver = null;
	public static String ALARM_URL = "http://172.22.56.14:8080/MDMProject/warnList/insertWarnInfo.html";
	private static List<NameValuePair> lists;
	private static PhoneMessageUtils utils;
	private JSONObject obj;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				// Log.e("tag", "AlarmService开始");
				alram();
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
	public void onCreate() {
		super.onCreate();
		utils = new PhoneMessageUtils(getApplicationContext());
		mBatteryMessageReceiver = new BatteryMessageReceiver();
		// 注册一个系统 BroadcastReceiver，作为访问电池计量之用这个不能直接在AndroidManifest.xml中注册
		registerReceiver(mBatteryMessageReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// Log.e("tag", "AlarmService启动");
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				handler.sendEmptyMessage(1);
			}
		}, 10, 1000 * 60 * 5);
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 警报执行
	 */
	private void alram() {
		// Log.e("tag", "电量" + BatteryN);
		if (BatteryN < 20) {
			new Thread(new Runnable() {
				@Override
				public void run() {

					String result = NetHelper.doPostJson(ALARM_URL,
							alramsPrams("1", "电量不足"), "json");
					Message msg = new Message();
					msg.what = 1;
					msg.obj = result;
					mAlramHandler.sendMessage(msg);
				}
			}).start();
		}

		// Log.e("tag",
		// "剩余内存"
		// + getAvailMemory()
		// + "-或者-"
		// + Formatter.formatFileSize(getBaseContext(),
		// getAvailMemory()));

		if (getAvailMemory() < 100000000) {// 100MB
			new Thread(new Runnable() {
				@Override
				public void run() {
					String result = NetHelper.doPostJson(ALARM_URL,
							alramsPrams("2", "内存不足"), "json");
					Message msg = new Message();
					msg.what = 2;
					msg.obj = result;
					mAlramHandler.sendMessage(msg);
				}
			});
		}

		// Log.e("tag", "流量" + "200M");
		if (getAvailMemory() < 10000000) {// 10M
			new Thread(new Runnable() {
				@Override
				public void run() {
					String result = NetHelper.doPostJson(ALARM_URL,
							alramsPrams("3", "流量不足"), "json");
					Message msg = new Message();
					msg.what = 3;
					msg.obj = result;
					mAlramHandler.sendMessage(msg);
				}
			});
		}
	}

	/**
	 * 警报参数
	 */
	private String alramsPrams(String waring_type, String warning_info) {
		obj = new JSONObject();
		try {
			obj.put("client_id", utils.getmENS());
			obj.put("organization_id", "88888888");
			obj.put("user_id", "1005");
			obj.put("user_name", "耀阳");
			obj.put("app_edition", utils.getmAppVersion());
			obj.put("warning_type", waring_type);
			obj.put("warning_time", new Date() + "");
			obj.put("warning_info", warning_info);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Log.e("tag", "警告参数" + obj.toString());
		return obj.toString();
	}

	/**
	 * 关闭定位的一切活动
	 */
	public void stopAlramListener() {
		timer.cancel();
		stopSelf();
	}

	@Override
	public void onDestroy() {
		try {
			stopAlramListener();
		} catch (Exception e) {
		}
		if (mBatteryMessageReceiver != null) {
			try {
				unregisterReceiver(mBatteryMessageReceiver);
			} catch (Exception e) {
				Log.e("tag", "unregisterReceiver mBatInfoReceiver failure :"
						+ e.getCause());
			}
		}
		super.onDestroy();
	}

	private static int BatteryN; // 目前电量

	class BatteryMessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			String action = intent.getAction();
			if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
				BatteryN = intent.getIntExtra("level", 0); // 目前电量
			}
		}

	}

	/**
	 * 获取android当前可用内存大小
	 */
	private long getAvailMemory() {// 获取android当前可用内存大小

		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		// mi.availMem; 当前系统的可用内存
		// Log.e("tag", "neicun" + mi.availMem);
		return mi.availMem;// 将获取的内存大小规格化
	}

	private Handler mAlramHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// if (dialog != null) {
			// dialog.toDimiss();
			// }
			switch (msg.what) {
			case 1:
				String result_DL = (String) msg.obj;
				// Log.e("tag", "=resultAlarm" + result_DL);
				if (result_DL == null || "请求服务器失败".equals(result_DL)) {
					Toast.makeText(getApplicationContext(), "请求服务器失败,请检查网络",
							Toast.LENGTH_SHORT).show();
				} else if ("false".equals(resState(result_DL))) {
					Toast.makeText(getApplicationContext(), "电量警告失败，服务器挂掉",
							Toast.LENGTH_SHORT).show();
				} else if ("true".equals(resState(result_DL))) {
					Toast.makeText(getApplicationContext(), "电量警告提交成功",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case 2:
				String result_NC = (String) msg.obj;
				// Log.e("tag", "=resultAlarm" + result_NC);
				if (result_NC == null || "请求服务器失败".equals(result_NC)) {
					Toast.makeText(getApplicationContext(), "请求服务器失败,请检查网络",
							Toast.LENGTH_SHORT).show();
				} else if ("false".equals(resState(result_NC))) {
					Toast.makeText(getApplicationContext(), "内存警告失败，服务器挂掉",
							Toast.LENGTH_SHORT).show();
				} else if ("true".equals(resState(result_NC))) {
					Toast.makeText(getApplicationContext(), "内存警告提交成功",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case 3:
				String result_LL = (String) msg.obj;
				// Log.e("tag", "=resultAlarm" + result_LL);
				if (result_LL == null || "请求服务器失败".equals(result_LL)) {
					Toast.makeText(getApplicationContext(), "请求服务器失败,请检查网络",
							Toast.LENGTH_SHORT).show();
				} else if ("false".equals(resState(result_LL))) {
					Toast.makeText(getApplicationContext(), "流量警告失败，服务器挂掉",
							Toast.LENGTH_SHORT).show();
				} else if ("true".equals(resState(result_LL))) {
					Toast.makeText(getApplicationContext(), "流量警告提交成功",
							Toast.LENGTH_SHORT).show();
				}
				break;

			default:
				break;
			}
		}

	};

	public static String resState(String s) {
		String str = "";
		try {
			JSONObject json = new JSONObject(s);
			str = json.get("result").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return str;
	}
}

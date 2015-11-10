package com.koala.emm.basic;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Message;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.koala.emm.R;
import com.koala.emm.basicdata.BasicDataService;
import com.koala.emm.clean.DataCleanManager;
import com.koala.emm.gps.LocationService;
import com.koala.emm.supervision.AlarmService;
import com.koala.emm.supervision.NetworkStateService;
import com.koala.emm.supervision.NetworkUtils;
import com.koala.emm.tools.NetHelper;
import com.koala.emm.tools.PhoneMessageUtils;
import com.koala.emm.tools.SharePreferenceUtilDaoFactory;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class Basic_deviceActivity extends BaseActivity {

	// TODO 基本信息采集加入E速递中时要注意加在登录成功之后 并且要定位成功之后才上传 否则获取不到经纬度
	// 使用了Utils的注解

	@ViewInject(R.id.xitongleixing)
	TextView tv_xitongleixing;// 系统类型
	@ViewInject(R.id.shebeichangjia)
	static TextView tv_shebeichangjia;// 设备厂商
	@ViewInject(R.id.shebeixinghao)
	static TextView tv_shebeixinghao;// 设备型号
	@ViewInject(R.id.xitongbanben)
	static TextView tv_xitongbanben;// 系统版本
	@ViewInject(R.id.esn)
	static TextView tv_esn;// ESN(序列号)
	@ViewInject(R.id.wangluo)
	static TextView tv_wangluo;// 网络状态
	@ViewInject(R.id.dianchirongliang)
	static TextView tv_dianchirongliang;// 电池信息
	@ViewInject(R.id.zongliuliang)
	static TextView tv_zongliuliang;// 总流量
	@ViewInject(R.id.shengyuliuliang)
	static TextView tv_shengyuliuliang;// 剩余流量
	@ViewInject(R.id.shoujineicun)
	static TextView tv_shoujineicun;// 手机内存
	@ViewInject(R.id.shengyuneicun)
	static TextView tv_shengyuneicun;// 剩余内存
	@ViewInject(R.id.appversion)
	static TextView tv_appversion;// 当前应用版本
	@ViewInject(R.id.cleanrom)
	private Button tv_cleanrom;// 清理内存
	@ViewInject(R.id.btn_submit)
	private Button tv_btn_submit;// 提交设备信息
	@ViewInject(R.id.btn_alarm)
	private Button tv_btn_alarm;// 提交警报请求
	@ViewInject(R.id.btn_uninistall)
	private Button tv_btn_uninistall;// 卸载自己
	@ViewInject(R.id.btn_clean)
	private Button tv_btn_clean;// 清除数据
	private BatteryMessageReceiver mBatteryMessageReceiver = null;
	private static int BatteryN; // 目前电量
	private int BatteryV; // 电池电压
	private double BatteryT; // 电池温度
	private String BatteryStatus; // 电池状态
	private String BatteryTemp; // 电池使用情况
//	private Intent mLocationService;
	private Intent mAlarmService;
	private static String locLat;
	private static String locLng;
	static SetDataAsyncTask setDataTask = null;
	private static PhoneMessageUtils utils;
	private JSONObject obj;// 设备信息参数
	public static String url = "http://172.22.56.14:8080/MDMProject/deviceInfo/insertInfo.html";// 设备信息url

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basic_device);
		ViewUtils.inject(this); // 注入view和事件
		SharePreferenceUtilDaoFactory.getInstance(getApplicationContext())
				.setIsFirstLocation(true);
		registerBoradcastReceiver();// 注册广播
		mBatteryMessageReceiver = new BatteryMessageReceiver();

		initData();
		addListener();
		startServer();
	}

	/**
	 * 启动相关服务
	 */
	private void startServer() {
		/**
		 * 网络状态
		 */
		Intent i = new Intent(getApplicationContext(),
				NetworkStateService.class);
		startService(i);
		LogUtils.e("获取网络类型: "
				+ NetworkUtils.getCurrentNetworkType(Basic_deviceActivity.this));
		LogUtils.e("获取运营商: "
				+ NetworkUtils.getProvider(Basic_deviceActivity.this));
		/**
		 * 定位信息
		 */
//		mLocationService = new Intent(getApplicationContext(),
//				LocationService.class);
//		startService(mLocationService);
		Intent BasicService = new Intent(this, BasicDataService.class);
		this.startService(BasicService);
		// /**
		// * 警报服务
		// */
		// mAlarmService = new Intent(getApplicationContext(),
		// AlarmService.class);
		// startService(mAlarmService);
	}

	@SuppressLint("NewApi")
	private void initData() {
		utils = new PhoneMessageUtils(getApplicationContext());
		tv_xitongleixing.setText("android");
		tv_xitongbanben.setText(utils.getmSystemVersion());
		tv_shebeichangjia.setText(utils.getmEquipmentmanufacturers());
		tv_shebeixinghao.setText(utils.getmAnlagentyp());
		tv_esn.setText(utils.getmENS());
		tv_appversion.setText(utils.getmAppVersion());
		tv_wangluo
				.setText(""
						+ NetworkUtils
								.getCurrentNetworkType(Basic_deviceActivity.this));
		tv_shoujineicun.setText(getTotalMemory());
		tv_shengyuneicun.setText(getAvailMemory());

	}

	private void addListener() {
		tv_cleanrom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				clear(Basic_deviceActivity.this);
				tv_shoujineicun.setText(getTotalMemory());
				tv_shengyuneicun.setText(getAvailMemory());
			}
		});
		tv_btn_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setdataToNet();
			}
		});
		tv_btn_alarm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/**
				 * 警报服务
				 */
				// mAlarmService = new Intent(getApplicationContext(),
				// AlarmService.class);
				// startService(mAlarmService);
				Intent intent = new Intent();
				intent.setAction("MDM");
				intent.putExtra("name", "张三");
				sendBroadcast(intent);
			}
		});
		tv_btn_uninistall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// uninstall();
				new Thread() {
					@Override
					public void run() {
						RootCommand("pm uninstall "
								+ Basic_deviceActivity.this.getPackageName());
					}
				}.start();
			}
		});
		tv_btn_clean.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DataCleanManager.cleanInternalCache(getApplicationContext());
				DataCleanManager.cleanDatabases(getApplicationContext());
				DataCleanManager.cleanExternalCache(getApplicationContext());
			}
		});
	}

	// TODO 设备提交参数未全获取完成 流量的
	protected void setdataToNet() {

		obj = new JSONObject();
		try {
			obj.put("vendor", utils.getmEquipmentmanufacturers());
			obj.put("device_model", utils.getmAnlagentyp());
			obj.put("system_model", utils.getmSystemVersion());
			obj.put("user_id", "1005");
			obj.put("user_name", "耀阳");
			obj.put("client_id", utils.getmENS());
			obj.put("organization_id", "88888888");
			obj.put("internet_state", tv_wangluo.getText().toString());
			obj.put("longitude", locLng);
			obj.put("latitudes", locLat);
			obj.put("device_total", "200MB");
			obj.put("device_rest", "");
			obj.put("device_memory_total", getTotalMemory());
			obj.put("device_memory_rest", getAvailMemory());
			obj.put("app_edition", utils.getmAppVersion());
			float totalBaty = 100;
			obj.put("batery_per", BatteryN / totalBaty + "");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setDataTask = new SetDataAsyncTask();
		setDataTask.execute(url);
	}

	class SetDataAsyncTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// Log.e("tag", "params[0]" + params[0]);
			// Log.e("tag", "lists" + obj.toString());

			String result = NetHelper.doPostJson(params[0], obj.toString(),
					"json");
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Log.e("tag", "result" + result);
			if (result == null || "请求服务器失败".equals(result)) {
				Toast.makeText(Basic_deviceActivity.this, "请求服务器失败，请检查网络",
						Toast.LENGTH_SHORT).show();
			} else if ("false".equals(resState(result))) {
				Toast.makeText(Basic_deviceActivity.this, "设备信息提交失败，服务器挂掉",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(Basic_deviceActivity.this, "设备信息提交成功",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	// 测试点击事件
	@OnClick(R.id.xitongleixing)
	public void testButtonClick(View v) { // 方法签名必须和接口中的要求一致
		LogUtils.e("点击事件");
	}

	@Override
	protected void onPause() {
		if (mBatteryMessageReceiver != null) {
			try {
				unregisterReceiver(mBatteryMessageReceiver);
			} catch (Exception e) {
				Log.e("tag", "unregisterReceiver mBatInfoReceiver failure :"
						+ e.getCause());
			}
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		// 注册一个系统 BroadcastReceiver，作为访问电池计量之用这个不能直接在AndroidManifest.xml中注册
		registerReceiver(mBatteryMessageReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
		super.onResume();
	}

	/**
	 * 获取android当前可用内存大小
	 */
	private String getAvailMemory() {// 获取android当前可用内存大小

		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		// mi.availMem; 当前系统的可用内存

		return Formatter.formatFileSize(getBaseContext(), mi.availMem);// 将获取的内存大小规格化
	}

	/**
	 * 获取android系统总内存大小
	 */
	private String getTotalMemory() {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;

		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

			arrayOfString = str2.split("\\s+");
			for (String num : arrayOfString) {
				Log.i(str2, num + "\t");
			}

			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
			localBufferedReader.close();

		} catch (IOException e) {
		}
		return Formatter.formatFileSize(getBaseContext(), initial_memory);// Byte转换为KB或者MB，内存大小规格化
	}

	/**
	 * 清理Android系统后台没有用到的内存：
	 * 
	 * @param context
	 */
	private void clear(Context context) {
		ActivityManager activityManger = (ActivityManager) context
				.getSystemService(ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> list = activityManger
				.getRunningAppProcesses();
		if (list != null)
			for (int i = 0; i < list.size(); i++) {
				ActivityManager.RunningAppProcessInfo apinfo = list.get(i);

				System.out.println("pid---->>>>>>>" + apinfo.pid);
				System.out.println("processName->> " + apinfo.processName);
				System.out.println("importance-->>" + apinfo.importance);
				String[] pkgList = apinfo.pkgList;

				if (apinfo.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE) {
					// Process.killProcess(apinfo.pid);
					for (int j = 0; j < pkgList.length; j++) {
						// 2.2以上是过时的,请用killBackgroundProcesses代替
						/** 清理不可用的内容空间 **/
						// activityManger.restartPackage(pkgList[j]);
						activityManger.killBackgroundProcesses(pkgList[j]);
					}
				}
			}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mBroadcastReceiver);
	}

	private void registerBoradcastReceiver() {
		IntentFilter newLocationIntentFilter = new IntentFilter();
		newLocationIntentFilter.addAction("location.ok");
		registerReceiver(mBroadcastReceiver, newLocationIntentFilter);
	}

	class BatteryMessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			String action = intent.getAction();
			if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
				BatteryN = intent.getIntExtra("level", 0); // 目前电量
				BatteryV = intent.getIntExtra("voltage", 0); // 电池电压
				BatteryT = intent.getIntExtra("temperature", 0); // 电池温度
				Message msg = new Message();
				switch (intent.getIntExtra("status",
						BatteryManager.BATTERY_STATUS_UNKNOWN)) {

				case BatteryManager.BATTERY_STATUS_CHARGING:
					BatteryStatus = "正在充电";
					break;
				case BatteryManager.BATTERY_STATUS_DISCHARGING:
					BatteryStatus = "放电状态";
					break;
				case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
					BatteryStatus = "未充电";
					break;
				case BatteryManager.BATTERY_STATUS_FULL:
					BatteryStatus = "充满电";
					break;
				case BatteryManager.BATTERY_STATUS_UNKNOWN:
					BatteryStatus = "未知道状态";
					break;
				}

				switch (intent.getIntExtra("health",
						BatteryManager.BATTERY_HEALTH_UNKNOWN)) {
				case BatteryManager.BATTERY_HEALTH_UNKNOWN:
					BatteryTemp = "未知错误";
					break;
				case BatteryManager.BATTERY_HEALTH_GOOD:
					BatteryTemp = "状态良好";
					break;
				case BatteryManager.BATTERY_HEALTH_DEAD:
					BatteryTemp = "电池没有电";
					break;
				case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
					BatteryTemp = "电池电压过高";
					break;
				case BatteryManager.BATTERY_HEALTH_OVERHEAT:
					BatteryTemp = "电池过热";
					break;
				}
				tv_dianchirongliang.setText("电量：" + BatteryN + "% - "
						+ BatteryStatus + "\n" + "电压：" + BatteryV + "mV - "
						+ BatteryTemp + "\n" + "温度：" + (BatteryT * 0.1) + "℃");
			}
		}

	}

	public static BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("location.ok")) {
				LogUtils.e("收到广播，展示数据吧");
				Bundle bundle = intent.getExtras();
				locLat = bundle.getString("locLat");
				locLng = bundle.getString("locLng");
				LogUtils.e("locLat:  " + locLat + "  locLng:  " + locLng);
			}
		}
	};

	/**
	 * 调用系统的卸载应用
	 */
	private void uninstall() {
		// TODO Auto-generated method stub

		Uri packageUri = Uri.parse("package:"
				+ Basic_deviceActivity.this.getPackageName());

		Intent intent = new Intent(Intent.ACTION_DELETE, packageUri);
		startActivity(intent);

	}

	/**
	 * 卸载app 需root权限
	 * 
	 * @param command
	 * @return
	 */
	public static boolean RootCommand(String command) {
		Process process = null;
		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (Exception e) {
			Log.d("*** DEBUG ***", "ROOT REE" + e.getMessage());
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				process.destroy();
			} catch (Exception e) {
			}
		}
		Log.d("*** DEBUG ***", "Root SUC ");
		return true;
	}

}

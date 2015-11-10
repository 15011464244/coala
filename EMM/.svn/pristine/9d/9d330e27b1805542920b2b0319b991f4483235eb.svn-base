package com.koala.emm.activity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

import com.koala.emm.R;
import com.koala.emm.activity.MainActivity.MyAsyncTask;
import com.koala.emm.basic.BaseActivity;
import com.koala.emm.basicdata.BasicDataService;
import com.koala.emm.model.WarnPushMessage;
import com.koala.emm.tools.PhoneMessageUtils;
import com.koala.emm.util.UpdateUtil;
import com.koala.emm.util.SpfsUtil;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

public class ShowDevice2Activity extends BaseActivity {
	
	private TextView mTotalMemory,mRemainMemory,mRemainBattery,mProductor,
		    mDeviceVersion,mSysVersion,mClientId;
	
	private String  BatteryN;
	
	private BatteryMessageReceiver mBatteryMessageReceiver = null;
	
	private static PhoneMessageUtils utils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_show_device2);
		utils = new PhoneMessageUtils(getApplicationContext());
		mBatteryMessageReceiver = new BatteryMessageReceiver();
		// 注册一个系统 BroadcastReceiver，作为访问电池计量之用这个不能直接在AndroidManifest.xml中注册
		registerReceiver(mBatteryMessageReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
		
		
		//测试要加密某个app的数据
		Intent intent1 = new Intent();
		intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		WarnPushMessage wpm = new WarnPushMessage();
		wpm.setType("test");
		intent1.putExtra("bean", wpm);
		intent1.setAction("com.example.mdm_sdk");  
		sendBroadcast(intent1);
		
		//后台进程   处理开启服务，强制更新卸载持久化
//		MyAsyncTask myAsyncTask = new MyAsyncTask();
//		myAsyncTask.execute("");
		
		initView();
		loadView();
		
		//强制更新
//		forcedUpdateVersion();
//		uninstall();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mBatteryMessageReceiver);
	}
	
	private void initView(){
		setHeadTitle("设备详情");
		mTotalMemory = (TextView) this.findViewById(R.id.tv_total_memory);
		mRemainMemory = (TextView) findViewById(R.id.tv_remain_memory);
		mRemainBattery = (TextView) findViewById(R.id.tv_remain_battery);
		mProductor = (TextView) findViewById(R.id.tv_productor);
		mDeviceVersion = (TextView) findViewById(R.id.tv_device_version);
		mSysVersion = (TextView) findViewById(R.id.tv_sys_version);
		mClientId = (TextView) findViewById(R.id.tv_client_id);
	}
	
	
	private void loadView(){
		mProductor.setText(utils.getmEquipmentmanufacturers());
		mDeviceVersion.setText(utils.getmAnlagentyp());
		mSysVersion.setText(utils.getmSystemVersion());
		mTotalMemory.setText(getTotalMemory()+" G");
		mRemainMemory.setText(getAvailMemory()+" G");
		mRemainBattery.setText(BatteryN);
		if(SpfsUtil.getClientId() != null){
			mClientId.setText(SpfsUtil.getClientId());
		}
		
	}
	
	
	/**
	 * 获取android系统总内存大小
	 */
	private double getTotalMemory() {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		double initial_memory = 0;

		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

			arrayOfString = str2.split("\\s+");
			for (String num : arrayOfString) {
				Log.i(str2, num + "\t");
			}

			initial_memory = initial_memory = Integer.valueOf(arrayOfString[1]).intValue();//kb单位
			localBufferedReader.close();

		} catch (IOException e) {
		}
		return Double.valueOf(new DecimalFormat("0.00").format(initial_memory/1024/1024)) ;
		
	}
	
	/**
	 * 获取android当前可用内存大小
	 */
	private double getAvailMemory() {// 获取android当前可用内存大小
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		return Double.valueOf(new DecimalFormat("0.00").format((double)mi.availMem/1024/1024/1024)) ;
	}
	
	class BatteryMessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub 
						//判断它是否是为电量变化的Broadcast Action 
						if(Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())){ 
						//获取当前电量 
						int level = intent.getIntExtra("level", 0); 
						//电量的总刻度 
						int scale = intent.getIntExtra("scale", 100); 
						//把它转成百分比 
//						tv.setText("电池电量为"+((level*100)/scale)+"%"); 
						BatteryN = level+"%";
						
						mRemainBattery.setText(BatteryN);
			} 
		}
	}
	
	/**
	 * 判断是否要强制卸载
	 */
	private void uninstall(){
		Boolean flag = SpfsUtil.getUninstall();
		if(flag){
			Uri packageUri = Uri.parse("package:"+getPackageName());
	        Intent intent = new Intent(Intent.ACTION_DELETE,packageUri);
	        startActivity(intent);
	        System.exit(0);
		}
		
		
	}
	
	/**
	 * 强制版本更新
	 */
	private void forcedUpdateVersion() {
		// TODO Auto-generated method stub
		String updateVersion = SpfsUtil.getUpdateVersion();
		if(updateVersion != null){
			//获取当前版本
			PhoneMessageUtils utils = new PhoneMessageUtils(this);
			String currentVersion = utils.getmAppVersion();
			if(currentVersion != null && Integer.valueOf(currentVersion.replace(".", "").substring(1)) < Integer.valueOf(updateVersion.replace(".", 

"").substring(1))){
				//更新
				String updateUrl = SpfsUtil.getUpdateUrl();
				if(updateUrl != null){
					UpdateUtil util = new UpdateUtil(this);
					util.updateConfirm(updateUrl, UpdateUtil.APP_UPDATE_FORCED);
				}
			}
		}
	}
	
	/**
	 * 开启服务
	 */
	private void startService() {
		Intent basicDataService = new Intent(this,BasicDataService.class);
		this.startService(basicDataService);
	}
	
	//异步线程
		class MyAsyncTask extends AsyncTask<String, Integer, String> { 
	        @Override
	        protected String doInBackground(String... parameter) {
	        	//启动服务
	        	startService();
//		        //强制卸载
//		    	uninstall();
	        	return null;
	        }
	        public MyAsyncTask() {
				super();
			}
			@Override
	        protected void onProgressUpdate(Integer... progress) { 
	        }
			@Override
	        protected void onPostExecute(String result) { 
	        } 
		}
	
}

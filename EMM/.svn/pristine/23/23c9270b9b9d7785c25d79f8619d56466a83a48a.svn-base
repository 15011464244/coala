package com.koala.emm.activity;

import com.arvin.koalapush.util.LogUtil;
import com.koala.emm.R;
import com.koala.emm.basic.BaseActivity;
import com.koala.emm.basicdata.BasicDataService;
import com.koala.emm.business.SafeUtil;
import com.koala.emm.model.WarnType;
import com.koala.emm.util.CheckUtil;
import com.koala.emm.util.SpfsUtil;
import com.lidroid.xutils.util.LogUtils;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends BaseActivity implements OnClickListener{
	LinearLayout mMyDeviceInfo,mApps,mMessages,mFiles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		//后台进程   处理开启服务，强制更新卸载持久化
		MyAsyncTask myAsyncTask = new MyAsyncTask();
		myAsyncTask.execute("");
		initView();
		
		//激活app
		SafeUtil safe = new SafeUtil(this);
		safe.getAdminActive();
		
	}
	
	private void initView(){
		mMyDeviceInfo = (LinearLayout) findViewById(R.id.tv_my_dervice);
		mApps = (LinearLayout) findViewById(R.id.tv_my_app);
		mMessages = (LinearLayout) findViewById(R.id.tv_my_message);
		mFiles = (LinearLayout) findViewById(R.id.tv_my_file);
		
		mMyDeviceInfo.setOnClickListener(this);
		mApps.setOnClickListener(this);
		mMessages.setOnClickListener(this);
		mFiles.setOnClickListener(this);
	}
	
	private void loadView(){
		
	}
	
	/**
	 * 开启服务
	 */
	private void startService() {
		LogUtil.e("----------------------------"+CheckUtil.checkServiceRunning(this, "com.koala.emm.basicdata.BasicDataService"));
//		if(!CheckUtil.checkServiceRunning(this, "com.koala.emm.basicdata.BasicDataService")){
			Intent basicDataService = new Intent(this,BasicDataService.class);
			this.startService(basicDataService);
//		}
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_my_dervice:
			Intent toDervice = new Intent(this, ShowDevice2Activity.class);
			startActivity(toDervice);
			break;
		case R.id.tv_my_app:
			Intent toAppList = new Intent(this, AppListActivity.class);
			startActivity(toAppList);
			break;
		case R.id.tv_my_message:
//			Intent intent = new Intent(this, MessageTypeListActivity.class);
//			startActivity(intent);
			Intent intent = new Intent(this, MessageListActivity.class);
			intent.putExtra("messageTypes", new String[]{WarnType.RECEIVE_MESSAGE});
			startActivity(intent);
			break;
		case R.id.tv_my_file:
			Intent toFileManager = new Intent(this,FileManagerActivity.class);
			startActivity(toFileManager);
			break;

		default:
			break;
		}
	}
	
	//异步线程
	class MyAsyncTask extends AsyncTask<String, Integer, String> { 
        @Override
        protected String doInBackground(String... parameter) {
        	//启动服务
        	startService();
//	        //强制卸载
//	    	uninstall();
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
}

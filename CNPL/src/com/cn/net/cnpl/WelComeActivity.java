package com.cn.net.cnpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.model.Head;
import com.cn.net.cnpl.model.User;
import com.cn.net.cnpl.tools.BaiduGps;
import com.cn.net.cnpl.tools.NetHelper;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class WelComeActivity extends Activity {

	private Dialog myDialog;
	private boolean updateflag = false;
	public final static int TASK_LOOP_COMPLETE = 0;
	private String excflag = null;
	private User user = null;
	public ProgressDialog myDialog2 = null;
	
	private boolean runFlag=false;
	private String  updateMsg= "";

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaoFactory.getInstance().setGlobalContext(this.getApplicationContext());
		Integer sdkVersion;
		try {
			sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			sdkVersion = 0;
		}
		if (sdkVersion != null && sdkVersion >= 3) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
					.build());
		}
		setContentView(R.layout.welcome);
//		autoLogin();
		DaoFactory.getInstance().init();
		BaiduGps.getInstance(this.getApplicationContext());
		
//		//注册广播
//		MyAlarmReceive  alarmReceive = new MyAlarmReceive();  
//		IntentFilter filter = new  IntentFilter();
//		filter.addAction(Intent.ACTION_BATTERY_CHANGED);
//		filter.addAction(Intent.ACTION_BOOT_COMPLETED);
//		filter.addAction(Intent.ACTION_BATTERY_OKAY);
//		filter.addAction(Intent.ACTION_BATTERY_LOW);
//		this.getApplicationContext().registerReceiver(alarmReceive,  filter) ;
	}
	
	
	@Override
	protected void onStart() {
		super.onStart();
		if(runFlag){
			Intent mainIntent = new Intent(WelComeActivity.this,
					MainActivity.class);
			WelComeActivity.this.startActivity(mainIntent);
			Global.activities.add(WelComeActivity.this);
		}else{
			runFlag=true;
			AsyncUpdate();
		}
		
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
			intent.setClass(WelComeActivity.this,
					MainActivity.class);
			WelComeActivity.this.startActivity(intent);
	
	}

	
	/**
	 * 自动登录
	 */
	private void autoLogin() {
		user = User.FindUser(this);
		if (user != null) {
			if (user.getIsAutoLogin() != null
					&& "0".equals(user.getIsAutoLogin())) {// 普通用户、内部员工且设置自动登录的

				myDialog2 = ProgressDialog.show(WelComeActivity.this,  Global.DIALOG_NAME,
						"正在连接服务器...", true, true);

				new Thread() {
					public void run() {
						try {
							LoginVerification(user);
						} catch (Exception e) {
						} finally {
							toastHandler.sendEmptyMessage(5);
						}
					}
				}.start();
			}
		}
	}

	private Handler toastHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(WelComeActivity.this, "网络连接失败，请检查!",
						Toast.LENGTH_LONG).show();
				break;
			case 1:
				Toast.makeText(WelComeActivity.this, "登录服务器失败!",
						Toast.LENGTH_LONG).show();
				break;
			case 3:
				Toast.makeText(WelComeActivity.this, msg.obj.toString(),
						Toast.LENGTH_LONG).show();
				break;
			case 4:
				Toast.makeText(WelComeActivity.this, msg.obj.toString(),
						Toast.LENGTH_LONG).show();
				break;
			case 5:
				if(myDialog2.isShowing())
				myDialog2.dismiss();
				break;
			}
		}
	};

	private void LoginVerification(User user) {
		try {
			

			TelephonyManager telephonemanage = (TelephonyManager) getWindow()
	                .getContext().getSystemService(Context.TELEPHONY_SERVICE);
				NetHelper client = new NetHelper();
				client.Create(Global.BASE_URL + Global.URL_LOGIN);
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("id", "0");
				jsonObject.put("deviceNumber", telephonemanage.getDeviceId());
				jsonObject.put("orgCode", "");
				jsonObject.put("userCode", user.getLoginName());
				jsonObject.put("role", "8");
				jsonObject.put("password", user.getPassword());
				jsonObject.put("sim", "");
				jsonObject.put("smisNo", "");
				JSONObject resultJsonObject = client.executeCnpl(jsonObject);

			if (resultJsonObject == null) {
				myDialog.dismiss();
				Message message = new Message();
				message.what = 1;
				toastHandler.sendMessage(message);
			}else if(!resultJsonObject.getBoolean("success")){
				myDialog.dismiss();
				Message message = new Message();
				message.what = 4;
				message.obj =resultJsonObject.getString("msg");
				toastHandler.sendMessage(message);
			}else{
				Global.setLogin_name(user.getLoginName());
			}
			
			user.setKey(resultJsonObject.getString("key"));
			User.SaveUser(this, user);
		} catch (Exception ex) {

		}
	}


	public void AsyncUpdate() {
		new AsyncTask<Object, Integer, Head>() {

			private AlertDialog downloadDialog;
			private ProgressBar progressView;
			private TextView textView;

			private static final String savePath = "/mnt/sdcard/updatedemo/";
			private static final String saveFileName = savePath
					+ "EmsClient.apk";

			private boolean interceptFlag = false;

			private String apkUrl = "";

			private void init() {
				boolean bAvailable = NetHelper
						.isNetworkAvailable(WelComeActivity.this);
				if (!bAvailable) {
					networkListener.sendEmptyMessage(1);
				} else {
					try {
						if (checkVersion()) {
							messageListener.sendEmptyMessage(1);
						} else {
							excflag = "1";
						}

					} catch (Exception e) {
						Toast.makeText(WelComeActivity.this, "程序启动失败！",
								Toast.LENGTH_LONG).show();
					}
				}
			}

			protected boolean checkVersion() {
				try {
					TelephonyManager telephonemanage = (TelephonyManager) getWindow()
			                .getContext().getSystemService(Context.TELEPHONY_SERVICE);
					NetHelper client = new NetHelper();
					String url = Global.BASE_URL
							+ Global.URL_ANDROIDVERSION;
					client.Create(url);
					JSONObject jsonObject=new JSONObject();
					jsonObject.put("versionFlag", "vs01");
					JSONObject resultJsonObject = client.executeCnpl(jsonObject);
					if (resultJsonObject == null || !resultJsonObject.getBoolean("success") ) {
						updateflag = false;
						return false;
					}
//					Integer versionCode = resultJsonObject.getInt("versionCode");
					String versionName = resultJsonObject.getString("versionName");
					PackageManager packageManager = WelComeActivity.this
							.getPackageManager();
//					Integer versionCodeMy = packageManager.getPackageInfo(
//							WelComeActivity.this.getPackageName(), 0).versionCode;
					String versionNameMy = packageManager.getPackageInfo(
							WelComeActivity.this.getPackageName(), 0).versionName;
//					if (versionCode != versionCodeMy
//							|| !versionNameMy.equals(versionName)) {
						if (!versionNameMy.equals(versionName)) {
						apkUrl = resultJsonObject.getString("versionUrl");
						updateMsg=resultJsonObject.getString("updateMsg");
						return true;
					} else {
						return false;
					}
				} catch (Exception ex) {
					return false;
				}
			}

			@Override
			protected Head doInBackground(Object... params) {

				Head head = new Head();
				try {
					init();
					while (excflag == null) {
					}
					if (updateflag) {
						publishProgress(-10);

						boolean b = downloadApk(apkUrl);
						if (b && !interceptFlag) {
							installApk();
						} else {
							publishProgress(-1);
							// head.setRet("9");
							head.setRet("-1");
							head.setErrorMsg("安装程序下载失败！");
							return head;
						}
					} else {
						head.setRet("-1");
					}
				} catch (Exception ex) {
					head.setRet("9");
					head.setErrorMsg("程序启动失败！");
				}
				return head;
			}

			@Override
			protected void onPostExecute(Head head) {

				if (downloadDialog != null) {
					downloadDialog.dismiss();
				}
				if (!"-1".equals(head.getRet())) {
					if (!"0".equals(head.getRet())) {
						Toast.makeText(WelComeActivity.this,
								head.getErrorMsg(), Toast.LENGTH_LONG).show();
						WelComeActivity.this.finish();
					}
				} else {
					
					Intent mainIntent = new Intent(WelComeActivity.this,
							MainActivity.class);
					WelComeActivity.this.startActivity(mainIntent);
					Global.activities.add(WelComeActivity.this);
					WelComeActivity.this.finish();
//			        }
				}
				super.onPostExecute(head);
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected void onProgressUpdate(Integer... values) {

				int count = values[0];
				if (count == -10) {
					showDownloadDialog();
				} else {
					progressView.setProgress(count);
					textView.setText("下载进度：" + count + "%");
				}

				super.onProgressUpdate(values);
			}

			private void showDownloadDialog() {
				AlertDialog.Builder builder = new Builder(WelComeActivity.this);
				builder.setTitle("软件版本更新");

				final LayoutInflater inflater = LayoutInflater
						.from(WelComeActivity.this);
				View v = inflater.inflate(R.layout.progress, null);
				progressView = (ProgressBar) v.findViewById(R.id.progress);
				textView = (TextView) v.findViewById(R.id.progresstext);

				builder.setView(v);
				builder.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						interceptFlag = true;
					}
				});
				downloadDialog = builder.create();
				downloadDialog.show();
			}

			private boolean downloadApk(String downUrl) {
				try {
					URL url = new URL(downUrl);

					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					int length = conn.getContentLength();
					InputStream is = conn.getInputStream();

					File file = new File(savePath);
					if (!file.exists()) {
						file.mkdir();
					}
					String apkFile = saveFileName;
					File ApkFile = new File(apkFile);
					FileOutputStream fos = new FileOutputStream(ApkFile);

					int count = 0;
					byte buf[] = new byte[1024];

					do {
						int numread = is.read(buf);
						if (numread <= 0) {
							break;
						}
						count += numread;
						int progress = (int) (((float) count / length) * 100);
						if (progress > 0) {
							publishProgress(progress);
						}
						fos.write(buf, 0, numread);
					} while (!interceptFlag);

					fos.close();
					is.close();
				} catch (MalformedURLException e) {
					Log.e( Global.DIALOG_NAME, "下载失败");
					return false;
				} catch (IOException e) {
					Log.e( Global.DIALOG_NAME, "下载失败");
					return false;
				} catch (Exception e) {
					Log.e( Global.DIALOG_NAME, "下载失败");
					return false;
				}
				return true;
			}

			private void installApk() {
				try {
					File apkfile = new File(saveFileName);
					if (!apkfile.exists()) {
						return;
					}
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
							"application/vnd.android.package-archive");
					WelComeActivity.this.startActivity(i);
				} catch (Exception e) {
					Log.e( Global.DIALOG_NAME, "程序安装失败！");
				}
			}
		}.execute();
	}

	private Handler networkListener = new Handler() {
		public void handleMessage(Message msg) {
			AlertDialog.Builder builder = new Builder(WelComeActivity.this);
			builder.setTitle("网络设置");
			builder.setMessage("未检测到可用的网络，请先进行网络设置！");
			builder.setPositiveButton("设置", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
					WelComeActivity.this.finish();
				}
			});
			builder.setNegativeButton("取消", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					WelComeActivity.this.finish();
				}
			});
			builder.create().show();
		}
	};

	private Handler messageListenerImage = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TASK_LOOP_COMPLETE:
				if(myDialog != null)
				myDialog.dismiss();
				break;
			}
		}
	};

	private Handler messageListener = new Handler() {
		public void handleMessage(Message msg) {
			AlertDialog.Builder builder = new Builder(WelComeActivity.this);
			builder.setMessage(updateMsg+"\n是否更新新版本?");
			builder.setTitle("更新提示");
			builder.setPositiveButton("确认", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					excflag = "1";
					updateflag = true;
				}
			});
			builder.setNegativeButton("取消", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					excflag = "1";
					updateflag = false;
				}
			});
			builder.create().show();
		}
	}; 



	
	
	
	// 返回登录
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN
					&& event.getRepeatCount() == 0) {
				android.os.Process.killProcess(android.os.Process.myPid());
				}
		}
		return super.dispatchKeyEvent(event);
	}
	

	
}

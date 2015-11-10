package com.cn.net.cnpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cn.net.cnpl.model.User;
import com.cn.net.cnpl.tools.BaseActivity;
import com.cn.net.cnpl.tools.NetHelper;

public class MainActivity extends BaseActivity {

	public ProgressDialog myDialog = null;
	private EditText eloginname;
	private EditText etPassword;
	private CheckBox tbAuto;
	private CheckBox tbRemPwd,tbtong;
	private EditText checkNum;
	private Button sendCheckNumBtn;
	private LinearLayout checkNumArea;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bg);
		
		eloginname = (EditText) findViewById(R.id.loginname);
		etPassword = (EditText) findViewById(R.id.password);
		tbAuto = (CheckBox) findViewById(R.id.tbAuto);
		tbtong = (CheckBox) findViewById(R.id.tbtong);
		tbAuto.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				tbRemPwd.setChecked(true);
			}
		});
		
		tbRemPwd = (CheckBox) findViewById(R.id.tbRemPwd);
		
		tbRemPwd.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(!isChecked)
					tbAuto.setChecked(false);
				
			}
		});

			
		
		this.onInit();
		this.onLogin();
	}


	/**
	 * 按钮先变灰不可点击，10秒后再恢复
	 * @param oldDate
	 */
	private void tenMiaoDo(final Date oldDate){
		new Thread() {
			public void run() {
				try {
					while(true){
						if((System.currentTimeMillis()-oldDate.getTime()) > 10*1000){
							sendCheckNumBtn.setEnabled(true);
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}


	private boolean onInit() {
		// 此处取
		User user = User.FindUser(this);
		if (user != null) {
			
			
			eloginname.setText(user.getLoginName());
			if (user.getIsPwd() != null && "0".equals(user.getIsPwd())) {
				etPassword.setText(user.getPassword());
				tbRemPwd.setChecked(true);
			} else {
				etPassword.setText("");
				tbRemPwd.setChecked(false);
			}
			if (user.getIsAutoLogin() != null
					&& "0".equals(user.getIsAutoLogin())) {
				tbAuto.setChecked(true);
				
				myDialog = ProgressDialog.show(MainActivity.this,
						 Global.DIALOG_NAME, "正在连接服务器...", true, true);
				
				new Thread() {
					public void run() {
						try {
							if (LoginVerification()) {
								String strtong = "";
								if(tbtong.isChecked())
									strtong = "1";
								else
									strtong = "0";
								Intent mainIntent = new Intent(MainActivity.this,
										MailDlvActivity.class);
								mainIntent.putExtra("tbtong", strtong);
								MainActivity.this.startActivity(mainIntent);
								MainActivity.this.finish();
							}
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							messageListener.sendEmptyMessage(0);
						}
					}
				}.start();
			} else {
				tbAuto.setChecked(false);
			}
		}
		return true;
	}

	
	
	private void onLogin() {

		Button loginButton = (Button) findViewById(R.id.btLogin);
		loginButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if (checkLogin()) {
					myDialog = ProgressDialog.show(MainActivity.this,
							 Global.DIALOG_NAME, "正在连接服务器...", true, true);
					new Thread() {
						public void run() {
							try {
								if (LoginVerification()) {
//								if (true) {
									String strtong = "";
									if(tbtong.isChecked())
										strtong = "1";
									else
										strtong = "0";
									Intent mainIntent = new Intent(MainActivity.this,
											MailDlvActivity.class);
//									Intent mainIntent = new Intent(MainActivity.this,
//											IndexActivity.class);
									mainIntent.putExtra("tbtong", strtong);
									MainActivity.this.startActivity(mainIntent);
									MainActivity.this.finish();
								}
							} catch (Exception e) {
//								e.printStackTrace();
							} finally {
								messageListener.sendEmptyMessage(0);
							}
						}
					}.start();
				}

			}
		});
	}

	private boolean checkLogin() {
		try {

			boolean bAvailable = NetHelper
					.isNetworkAvailable(MainActivity.this);
			if (!bAvailable) {
				Toast.makeText(MainActivity.this, "没有可用的网络,请设置网络!",
						Toast.LENGTH_LONG).show();
				return false;
			}
			
			if (eloginname.getText().toString() == null
					|| eloginname.getText().toString().trim().length() <= 0) {
				Toast.makeText(MainActivity.this, "请输入您的用户名!",
						Toast.LENGTH_LONG).show();
				eloginname.setFocusable(true);
				return false;
			}

			if (etPassword.getText().toString() == null
					|| etPassword.getText().toString().trim().length() <= 0) {
				Toast.makeText(MainActivity.this, "请输入您的用户密码!",
						Toast.LENGTH_LONG).show();
				etPassword.setFocusable(true);
				return false;
			}
			
		} catch (Exception e) {

			Toast.makeText(MainActivity.this, "连接服务器失败!", Toast.LENGTH_LONG)
					.show();
			return false;
		}
		return true;
	}

	private boolean LoginVerification() {

		try {
			TelephonyManager telephonemanage = (TelephonyManager) getWindow()
	                .getContext().getSystemService(Context.TELEPHONY_SERVICE);
				NetHelper client = new NetHelper();
				client.Create(Global.BASE_URL + Global.URL_LOGIN);
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("id", "0");
				jsonObject.put("deviceNumber", telephonemanage.getDeviceId());
				jsonObject.put("orgCode", "");
				jsonObject.put("userCode", eloginname.getText().toString());
				jsonObject.put("role", "8");
				jsonObject.put("password", etPassword.getText().toString());
				jsonObject.put("sim", "");
				jsonObject.put("smisNo", "");
				JSONObject resultJsonObject = client.executeCnpl(jsonObject);

			if (resultJsonObject == null) {
				myDialog.dismiss();
				Message message = new Message();
				message.what = 1;
				toastHandler.sendMessage(message);
				return false;
			}else if(!resultJsonObject.getBoolean("success")){
				myDialog.dismiss();
				Message message = new Message();
				message.what = 4;
				message.obj =resultJsonObject.getString("msg");
				toastHandler.sendMessage(message);
				return false;
			}
			
			Global.setLogin_name(eloginname.getText().toString());

			User user =User.FindUser(this.getApplicationContext());
			if(user ==  null) user= new User();
			if (tbRemPwd.isChecked()) {
				user.setIsPwd("0");
				user.setPassword(etPassword.getText().toString());
			} else {
				user.setPassword("");
				user.setIsPwd("1");
			}
			if (tbAuto.isChecked()) {
				user.setIsAutoLogin("0");
			} else {
				user.setIsAutoLogin("1");
			}
			
			user.setTelephone(telephonemanage.getDeviceId());
			user.setLoginName(eloginname.getText().toString());
			user.setUserName(resultJsonObject.getString("userName"));
			user.setMobile(resultJsonObject.getString("mobile"));
			user.setTransitName(resultJsonObject.getString("transitName"));
			user.setTransitCode(resultJsonObject.getString("transitCod"));
			user.setOrgCode(resultJsonObject.getString("orgCode"));
			user.setOrgName(resultJsonObject.getString("orgName"));
			user.setKey(resultJsonObject.getString("key"));
			// 此处存
			User.SaveUser(this, user);
			
			
		} catch (Exception e) {
			if(myDialog.isShowing()){
				myDialog.dismiss();
			}
			toastHandler.sendEmptyMessage(0);
			return false;
		}
		return true;
	}


	private Handler messageListener = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if(myDialog.isShowing())
				myDialog.dismiss();
				break;
			}
		}
	};

	private Handler toastHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					Toast.makeText(MainActivity.this, "网络连接失败，请检查!", Toast.LENGTH_LONG)
							.show();
					break;
				case 1:
					Toast.makeText(MainActivity.this, "登录服务器失败!", Toast.LENGTH_LONG)
							.show();
					break;
				case 3:
					Toast.makeText(MainActivity.this, msg.obj.toString(), Toast.LENGTH_LONG)
							.show();
					break;
				case 4:
					Toast.makeText(MainActivity.this, msg.obj.toString(), Toast.LENGTH_LONG)
							.show();
					break;
				}
			}
	};

}

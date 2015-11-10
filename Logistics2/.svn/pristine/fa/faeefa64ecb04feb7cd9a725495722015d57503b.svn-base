package com.ems.express.ui;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.HashMap;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.arvin.koalapush.potter.Kpush;
import com.ems.express.App;
import com.ems.express.PriceActivity;
import com.ems.express.R;
import com.ems.express.constant.Constant;
import com.ems.express.net.MyRequest;
import com.ems.express.potter.MinaClientHanlder;
import com.ems.express.ui.send.SendActivity;
import com.ems.express.util.DialogUtils;
import com.ems.express.util.FileUtil;
import com.ems.express.util.LogUtil;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;

/**
 * 登陆
 */
public class LoginActivity extends Activity {

	@InjectView(R.id.tv_title)
	TextView mTitle;
	
	@InjectView(R.id.tab_login)
	TextView mTabLogin;

	@InjectView(R.id.tab_register)
	TextView mTabRegister;

	@InjectView(R.id.view_login)
	LinearLayout mLoginContainer;
	@InjectView(R.id.view_regist)
	LinearLayout mRegistContainer;

	@InjectView(R.id.et_login_phone)
	EditText mLoginPhone;
	@InjectView(R.id.et_login_pw)
	EditText mLoginPW;

	@InjectView(R.id.et_regist_phone)
	EditText mRegistPhone;
	@InjectView(R.id.et_regist_pw)
	EditText mRegistPW;
	@InjectView(R.id.et_regist_pw_again)
	EditText mRegistPWAgain;
	@InjectView(R.id.tv_pw_tip)
	TextView mRegistTip;

	private ImageView imgProgress;
	private RelativeLayout rllayout;
	private AnimationDrawable mAnimation;

	public static void startAction(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, LoginActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		imgProgress =(ImageView)findViewById(R.id.progress_chrysanthemum);
		rllayout =(RelativeLayout)findViewById(R.id.progress_layout);
		ButterKnife.inject(this);
		mTabLogin.setSelected(true);
		mLoginContainer.setVisibility(View.VISIBLE);
		mTitle.setText(getString(R.string.C_LOGIN));
		
	}

	private void setDiaolog() {
		imgProgress.setBackgroundResource(R.drawable.progress_chrysanthemum);  
	     mAnimation = (AnimationDrawable) imgProgress.getBackground();  
	     rllayout.setVisibility(View.VISIBLE);
	     imgProgress.post(new Runnable() {
			@Override
			public void run() {
					mAnimation.start();
			}
		});
	}
	
	private void setRegistDialog() {
		imgProgress.setBackgroundResource(R.drawable.progress_chrysanthemum_wordless);  
	     mAnimation = (AnimationDrawable) imgProgress.getBackground();  
	     rllayout.setVisibility(View.VISIBLE);
	     imgProgress.post(new Runnable() {
			@Override
			public void run() {
					mAnimation.start();
			}
		});
	}


	@OnClick(R.id.btn_back)
	void toBack() {
		finish();
	}

	/**
	 * TAB登陆
	 * */
	@OnClick(R.id.tab_login)
	void toTabLogin() {
		mTabLogin.setSelected(true);
		mTabRegister.setSelected(false);
		mLoginContainer.setVisibility(View.VISIBLE);
		mRegistContainer.setVisibility(View.GONE);
		mTitle.setText(getString(R.string.C_LOGIN));
	}

	/**
	 * TAB注册
	 * */
	@OnClick(R.id.tab_register)
	void toTabRegist() {
		mTabRegister.setSelected(true);
		mTabLogin.setSelected(false);
		mRegistContainer.setVisibility(View.VISIBLE);
		mLoginContainer.setVisibility(View.GONE);
		mTitle.setText(getString(R.string.C_REGIST));
	}

	/**
	 * 登陆
	 * */
	@OnClick(R.id.btn_login)
	void toLogin() {
		App.loginId = "001";
		String phone = mLoginPhone.getText().toString().trim();
		String pw = mLoginPW.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			ToastUtil.show(this, "手机号为空");
			return;
		}
		if (TextUtils.isEmpty(pw)) {
			ToastUtil.show(this, "密码为空");
			return;
		}
		//隐藏软键盘
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0); //强制隐藏键盘  
		String clientId = Kpush.getInstence().getClientId();
		setDiaolog();
		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("phone_num", phone);
		json.put("password", pw);
		json.put("channel_id", Kpush.getInstence().getClientId());
		String params = ParamsUtil.getUrlParamsByMap(json);
		System.out.println("json-pre:" + params);
		MyRequest<Object> req = new MyRequest<Object>(Request.Method.POST, null, Constant.Login,
				new Response.Listener<Object>() {

					@Override
					public void onResponse(Object arg0) {
						Log.e("msg", arg0.toString());
						try {
							String result = (String) arg0;
							JSONObject object = new JSONObject(result.toString());
							if (object.getInt("result") == 1) {
								JSONArray resultJson = object.getJSONObject("body").getJSONArray("success");
								SpfsUtil.saveId(resultJson.getJSONObject(0).getString("sid"));
								SpfsUtil.savePhone(resultJson.getJSONObject(0).getString("phone_num"));
								SpfsUtil.saveTelephone(resultJson.getJSONObject(0).getString("telephone"));
								SpfsUtil.saveAddress(resultJson.getJSONObject(0).getString("address"));
								SpfsUtil.saveName(resultJson.getJSONObject(0).getString("name"));
								if (resultJson.getJSONObject(0).getString("image") != null
										&& !resultJson.getJSONObject(0).getString("image").isEmpty()) {
									String path = FileUtil.getTempFileName();
									SpfsUtil.saveHeader(path);
									SpfsUtil.saveHeadImageUrl(path);
									LogUtil.print("登录头像临时地址："+path);
									String photoStr = resultJson.getJSONObject(0).getString("image");
									LogUtil.print("\r\n\r\n" + photoStr + "\r\n\r\n");
									FileOutputStream out = null;
									try {
										byte[] decodedString = Base64.decode(photoStr, Base64.DEFAULT);
										Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,
												decodedString.length);
										out = new FileOutputStream(path);
										decodedByte.compress(Bitmap.CompressFormat.PNG, 100, out);
									} catch (Exception e) {
										e.printStackTrace();
										rllayout.setVisibility(View.GONE);
										mAnimation.stop();
									} finally {
										finish();
										rllayout.setVisibility(View.GONE);
										mAnimation.stop();
										try {
											if (out != null) {
												out.close();
											}
										} catch (IOException e) {
											e.printStackTrace();
											rllayout.setVisibility(View.GONE);
											mAnimation.stop();
										}
									}
								} else {
									finish();
									rllayout.setVisibility(View.GONE);
									mAnimation.stop();
								}
								rllayout.setVisibility(View.GONE);
								mAnimation.stop();
							} else {
								String error = object.getJSONObject("body").getString("error");
								ToastUtil.show(LoginActivity.this, error);
								rllayout.setVisibility(View.GONE);
								mAnimation.stop();
							}
							rllayout.setVisibility(View.GONE);
							mAnimation.stop();
						} catch (Exception e) {
							e.printStackTrace();
							rllayout.setVisibility(View.GONE);
							mAnimation.stop();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						Toast.makeText(LoginActivity.this, "登录失败请重试!", Toast.LENGTH_LONG).show();
						arg0.printStackTrace();
						rllayout.setVisibility(View.GONE);
						mAnimation.stop();
					}
				}, params);
		App.getQueue().add(req);
	}

	/**
	 * 注册
	 * */
	@OnClick(R.id.btn_regist)
	void toRegist() {
		String phone = mRegistPhone.getText().toString().trim();
		boolean mobileNO = DialogUtils.isMobileNO(phone);
		String pw = mRegistPW.getText().toString().trim();
		String pw_again = mRegistPWAgain.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			ToastUtil.show(this, "手机号为空");
			return;
		}
		if(mobileNO==false){
			ToastUtil.show(this, "请检查电话是否正确");
			return;
		}
		if (TextUtils.isEmpty(pw)) {
			ToastUtil.show(this, "密码为空");
			return;
		}
		if (!TextUtils.equals(pw, pw_again)) {
			mRegistTip.setVisibility(View.VISIBLE);
			return;
		} else {
			mRegistTip.setVisibility(View.GONE);
		}
		setRegistDialog();
		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("phone_num", phone);
		json.put("password", pw);
		json.put("channel_id", Kpush.getInstence().getClientId());
		Log.e("msgggchannel_id", Kpush.getInstence().getClientId());
		String params = ParamsUtil.getUrlParamsByMap(json);
		System.out.println("json-pre:" + params);
		MyRequest<Object> req = new MyRequest<Object>(Request.Method.POST, null, Constant.Regist,
				new Response.Listener<Object>() {

					@Override
					public void onResponse(Object arg0) {
						try {
							String result = (String) arg0;
							JSONObject object = new JSONObject(result.toString());
							if (object.getInt("result") == 1) {
								JSONObject resultJson = object.getJSONObject("body").getJSONObject("success");
								SpfsUtil.saveId(resultJson.getString("sid"));
								SpfsUtil.savePhone(resultJson.getString("phone_num"));
								rllayout.setVisibility(View.GONE);
								mAnimation.stop();
								finish();
							} else {
								String error = object.getJSONObject("body").getString("error");
								ToastUtil.show(LoginActivity.this, error);
								rllayout.setVisibility(View.GONE);
								mAnimation.stop();
							}
						} catch (Exception e) {
							e.printStackTrace();
							rllayout.setVisibility(View.GONE);
							mAnimation.stop();
						}
						rllayout.setVisibility(View.GONE);
						mAnimation.stop();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						arg0.printStackTrace();
						ToastUtil.show(LoginActivity.this, getString(R.string.C_NET_ERROR));
						rllayout.setVisibility(View.GONE);
						mAnimation.stop();
					}
				}, params);
		App.getQueue().add(req);
	}

	/**
	 * 忘记密码
	 * */
	@OnClick(R.id.btn_forget_pw)
	void toForgetPW() {
		Intent intent = new Intent(this, ResetPWActivity.class);
		startActivity(intent);
	}

	public void socketServer() {
		// 创建一个socket连接
		NioSocketConnector connector = new NioSocketConnector();
		// 消息核心处理器
		connector.setHandler(new MinaClientHanlder());
		// 设置链接超时时间
		connector.setConnectTimeoutCheckInterval(30);
		// 获取过滤器链
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		ProtocolCodecFilter filter = new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8")));
		// 添加编码过滤器 处理乱码、编码问题
		chain.addLast("objectFilter", filter);

		// 连接服务器，知道端口、地址
		ConnectFuture cf = connector.connect(new InetSocketAddress(App.address, App.bindPort));
		// 等待连接创建完成
		cf.awaitUninterruptibly();
		if (cf.isConnected()) {
			App.isConnected = true;
			System.out.println("等待连接断开");
			// 等待连接断开
			cf.getSession().getCloseFuture().awaitUninterruptibly();
		} else {
			System.out.println("连接断开");
			App.isConnected = false;
		}
		System.out.println("----dispose");
		connector.dispose();
		System.out.println("dispose----");
	}

}

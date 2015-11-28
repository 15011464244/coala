package com.ems.express.ui;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.arvin.koalapush.potter.Kpush;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.bean.City;
import com.ems.express.constant.Constant;
import com.ems.express.net.MyRequest;
import com.ems.express.util.DialogUtils;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RegisterActivity extends BaseActivityForRequest{
	
	@InjectView(R.id.tv_tab_check)
	TextView mTabCheck;
	
	@InjectView(R.id.tv_tab_setPwd)
	TextView mTabSetPwd;
	
	@InjectView(R.id.ll_setPwd)
	LinearLayout mSetPwdContainer;
	@InjectView(R.id.ll_check_sms)
	LinearLayout mCheckSmsContainer;
	
	@InjectView(R.id.et_regist_phone)
	EditText mRegistPhone;
	
	@InjectView(R.id.et_check_sms)
	EditText mCheckAuthCode;
	
	@InjectView(R.id.et_print_invited_phoneNumber)
	EditText mInvitedPhoneNumber;
	
	@InjectView(R.id.progress_chrysanthemum)
	ImageView imgProgress;
	
	@InjectView(R.id.progress_layout)
	RelativeLayout rllayout;
	
	@InjectView(R.id.et_regist_pw)
	EditText mRegistPW;
	@InjectView(R.id.et_regist_pw_again)
	EditText mRegistPWAgain;
	
	@InjectView(R.id.tv_invited_instruction)
	TextView invitedInstruction;
	
	@InjectView(R.id.btn_auth_code)
	Button btnAuthCode;
	
	InputMethodManager imm;
	
	private AnimationDrawable mAnimation;
	private boolean isOnclick = true; //是否可以点击
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				isOnclick =false;
				btnAuthCode.setEnabled(false);
				break;
            case 1:
            	isOnclick =true;
//            	btn_payment.setBackgroundColor(Color.parseColor("#277c09"));
            	btnAuthCode.setBackgroundResource(R.drawable.btn_selector);
//            	btnAuthCode.setBackgroundColor(Color.parseColor("#FFA500"));
            	btnAuthCode.setEnabled(true);
            	btnAuthCode.setText("获取验证码");
				break;
            case 2:
            	btnAuthCode.setText(msg.obj+"秒");
            	break;
			default:
				break;
			}
		};
	};
	private Context mContext;
	// 判断是否加载初始城市
		private boolean isFirstLoc = true;
		private GeoCoder coder;
		private BaiduMap mBaiduMap;
		private BDLocationListener myListener = new BDLocationListener() {

			@Override
			public void onReceivePoi(BDLocation location) {
				if (location != null) {
					System.out.println("onReceivePoi: " + location.getCity());
					System.out.println("onReceivePoi: " + location.getProvince());
					System.out.println("onReceivePoi: " + location.getDistrict());
				}
			}

			@Override
			public void onReceiveLocation(BDLocation location) {
				if (((BaseActivityForRequest) mContext).stayThisPage) {
					if (location != null) {
						if (isFirstLoc) {
							isFirstLoc = false;
							// 刷新地图
							LatLng ll = new LatLng(location.getLatitude(),
									location.getLongitude());

							ReverseGeoCodeOption option = new ReverseGeoCodeOption();
							option.location(ll);
							coder.reverseGeoCode(option);
						}
					}
				}
			}
		};
		   
    private String strpro = null;
	private String strcity = null;
	private String strcounty = null;
	private String strProvinceCode;
	private String strCityCode;
	private String strCountyCode;
	
	
		
	public static void startAction(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, RegisterActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		mContext = this;
		ButterKnife.inject(this);
		mTabCheck.setSelected(false);
		mTabSetPwd.setSelected(true);
		mTabSetPwd.setClickable(false);
		invitedInstruction.setText(Html.fromHtml("<font color=\'#FF0000\'>说明：</font><font color=\'#BEBEBE\'>受邀手机号必须为注册手机号</font>"));
		mCheckSmsContainer.setVisibility(View.VISIBLE);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		LocationClient mLocClient = new LocationClient(getApplicationContext());
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		Log.e("ajh", "code: " + mLocClient.requestLocation());
		coder = GeoCoder.newInstance();
		coder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
				if (result != null) {
//					myLocation.setText(result.getAddress());
					Log.e("gongjie",result.getAddress());
					// 地址输入框赋初始值

					if (true) {
						if (result.getAddressDetail() != null) {
							strpro = result.getAddressDetail().province;
							if (strpro.length() > 0) {
								strpro = strpro.subSequence(0,
										strpro.length() - 1).toString();
							}
							strcity = result.getAddressDetail().city;
							if (strcity.length() > 0) {
								if (strcity.contains("北京")
										|| strcity.contains("天津")
										|| strcity.contains("上海")
										|| strcity.contains("重庆")) {
									strcity = result.getAddressDetail().city
											.substring(0, 2);
								}
							}
							strcounty = result.getAddressDetail().district;
						}
							Log.e("gongjie", strpro+strcity+strcounty);
						List<City> queryProvinceList = App.dbHelper.queryProvinceList(App.db);
							Log.e("gongjie", queryProvinceList.toString());
						strProvinceCode = App.dbHelper.queryCodeByProvince(App.db,strpro);
							Log.e("gongjie", strProvinceCode);
						List<City> queryCityList = App.dbHelper.queryCityList(App.db, strProvinceCode);
							Log.e("gongjie", queryCityList.toString());
						String cityName;
						if(strProvinceCode.startsWith("11")||strProvinceCode.startsWith("12")||strProvinceCode.startsWith("31")||strProvinceCode.startsWith("50")){
							cityName = strcity+"市";
						}else {
							cityName = strcity;
						}
						strCityCode = App.dbHelper.queryCodeByCity(App.db, strProvinceCode,cityName);
						Log.e("gongjie", strProvinceCode+""+strCityCode);
						
						strCountyCode = App.dbHelper.queryCodeByCounty(App.db, strCityCode, strcounty);
						Log.e("gongjie", strProvinceCode+""+strCityCode+""+strCountyCode);
						if(strpro != null && strcity != null && strcounty != null){
//							rsu1.freshByName(strpro, strcity, strcounty);
						}
					} else {
					}
				}
			}

			@Override
			public void onGetGeoCodeResult(GeoCodeResult result) {
				if (result != null) {
					// ToastUtil.show(getApplicationContext(),
					// result.getAddress());
				}
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//友盟统计
		MobclickAgent.onResume(this);
	}
	@Override
	protected void onPause() {
		super.onPause();
		//友盟统计
		MobclickAgent.onPause(this);
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
	@OnClick(R.id.tv_tab_check)
	void toCheck(){
		mTabCheck.setSelected(false);
		mTabSetPwd.setSelected(true);
		mCheckSmsContainer.setVisibility(View.VISIBLE);
		mSetPwdContainer.setVisibility(View.GONE);
	}
	@OnClick(R.id.tv_tab_setPwd)
	void toSetPwd(){
		mTabCheck.setSelected(true);
		mTabSetPwd.setSelected(false);
		mCheckSmsContainer.setVisibility(View.GONE);
		mSetPwdContainer.setVisibility(View.VISIBLE);
	}
	@OnClick(R.id.btn_nextTep)
	void toNextTep(){
//		mTabCheck.setSelected(true);
//		mTabSetPwd.setSelected(false);
//		mCheckSmsContainer.setVisibility(View.GONE);
//		mSetPwdContainer.setVisibility(View.VISIBLE);
		String code = mCheckAuthCode.getText().toString().trim();
		String phone = mRegistPhone.getText().toString().trim();
		String invitedNumber = mInvitedPhoneNumber.getText().toString().trim();
		boolean mobileNO = DialogUtils.isMobileNO(phone);
		boolean mobileNO1 = DialogUtils.isMobileNO(phone);
		if (TextUtils.isEmpty(phone)) {
			ToastUtil.show(this, "手机号为空");
			return;
		}
		if(mobileNO==false){
			ToastUtil.show(this, "请检查电话是否正确");
			return;
		}
		if (TextUtils.isEmpty(code)) {
			ToastUtil.show(this, "验证码为空");
			return;
		}
		if (!mobileNO1&&(!invitedNumber.equals(""))) {
			ToastUtil.show(this, "请检查邀请电话是否正确");
			return;
		}
		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("phone_num", phone);
		json.put("verify_code", code);
		json.put("invitation_mobile", invitedNumber);
		String params = ParamsUtil.getUrlParamsByMap(json);
		Log.e("gongjie", "下一步参数"+params);
		MyRequest<Object> req = new MyRequest<Object>(Request.Method.POST,
				null, Constant.checkSmsCode, new Response.Listener<Object>() {

					@Override
					public void onResponse(Object arg0) {
						Log.e("gongjie", "下一步返回参数"+arg0.toString());
						try {
							String resultJson = (String) arg0;
							JSONObject object = new JSONObject(
									resultJson.toString());
							if (object.getInt("result") == 1) {
								mTabCheck.setSelected(true);
								mTabSetPwd.setSelected(false);
								mCheckSmsContainer.setVisibility(View.GONE);
								mSetPwdContainer.setVisibility(View.VISIBLE);
//								ToastUtil.show(RegisterActivity.this, success);
							} else {
								String errorMessage = object.getJSONObject("body")
										.getJSONObject("error").getString("errorMessage");
								ToastUtil.show(RegisterActivity.this, errorMessage);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						arg0.printStackTrace();
						ToastUtil.show(RegisterActivity.this,
								getString(R.string.C_NET_ERROR));

					}
				}, params);
		App.getQueue().add(req);
		
	}
	@OnClick(R.id.btn_back)
	void toBack() {
		finish();
	}
	@OnClick(R.id.tv_login)
	void toLogin() {
		finish();
	}
	@OnClick(R.id.btn_auth_code)
	void toGetAuthCode() {
		String phone = mRegistPhone.getText().toString().trim();
		boolean mobileNO = DialogUtils.isMobileNO(phone);
		if (TextUtils.isEmpty(phone)) {
			ToastUtil.show(this, "手机号为空");
			return;
		}
		if(mobileNO==false){
			ToastUtil.show(this, "请检查电话是否正确");
			return;
		}
		btnAuthCode.setBackgroundColor(Color.parseColor("#c0c0c0"));
		if (isOnclick) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					handler.sendEmptyMessage(0);
					for (int i = 1; i < 30; i++) {
						try {
							Thread.sleep(1000);
							int b = 30-i;
							Message message = Message.obtain();
							message.obj = b;
							message.what = 2;
							handler.sendMessage(message);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					handler.sendEmptyMessage(1);
					
				}
			}).start();
		}
		
		
		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("phone_num", phone);
		String params = ParamsUtil.getUrlParamsByMap(json);
		System.out.println("json-pre:" + params);
		MyRequest<Object> req = new MyRequest<Object>(Request.Method.POST,
				null, Constant.smsCode, new Response.Listener<Object>() {

					@Override
					public void onResponse(Object arg0) {
						Log.e("gongjie", "点击获取参数"+arg0.toString());
						try {
							String resultJson = (String) arg0;
							JSONObject object = new JSONObject(
									resultJson.toString());
							if (object.getInt("result") == 1) {
								ToastUtil.show(RegisterActivity.this, "验证码已发送");
							} else {
								String errorMessage = object.getJSONObject("body")
										.getJSONObject("error").getString("errorMessage");
								ToastUtil.show(RegisterActivity.this, errorMessage);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						arg0.printStackTrace();
						ToastUtil.show(RegisterActivity.this,
								getString(R.string.C_NET_ERROR));

					}
				}, params);
		App.getQueue().add(req);
	}
	@OnClick(R.id.btn_register)
	void toRegister(){
		String phone = mRegistPhone.getText().toString().trim();
		boolean mobileNO = DialogUtils.isMobileNO(phone);
//		boolean isRegistedUser = checkUser(mInvitedPhoneNumber);
		String pw = mRegistPW.getText().toString().trim();
		String pw_again = mRegistPWAgain.getText().toString().trim();
		String invitedNumber = mInvitedPhoneNumber.getText().toString().trim();
		String code = mCheckAuthCode.getText().toString().trim();
		boolean mobileNO1 = DialogUtils.isMobileNO(invitedNumber);
		if (TextUtils.isEmpty(phone)) {
			ToastUtil.show(this, "手机号为空");
			return;
		}
		if(mobileNO==false){
			ToastUtil.show(this, "请检查电话是否正确");
			return;
		}
		if (TextUtils.isEmpty(phone)) {
			ToastUtil.show(this, "手机号为空");
			return;
		}
		if (!mobileNO1&&(!invitedNumber.equals(""))) {
			ToastUtil.show(this, "请检查邀请电话是否正确");
			return;
		}
		if (TextUtils.isEmpty(pw)) {
			ToastUtil.show(this, "密码为空");
			return;
		}
		
//		if (isRegistedUser==false) {
//			DialogUtils.noticeDialog(mContext, "邀请号码为非注册用户！", "知道了");
//			return;
//		}
		
		//隐藏软键盘
		imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0); //强制隐藏键盘
		
		if (!TextUtils.equals(pw, pw_again)) {
//			mRegistTip.setVisibility(View.VISIBLE);
			mRegistPW.setText("");
			mRegistPWAgain.setText("");
			ToastUtil.show(RegisterActivity.this, "两次输入的密码不同请重新输入");
			return;
		} else {
//			mRegistTip.setVisibility(View.GONE);
		}
		setRegistDialog();
		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("phone_num", phone);
//		json.put("verify_code", code);
		json.put("password", pw);
		json.put("channel_id", Kpush.getInstence().getClientId());
		json.put("invitation_mobile", invitedNumber);
//		Log.e("msgggchannel_id", null == Kpush.getInstence().getClientId()? "没有网络，无法获取channelId":Kpush.getInstence().getClientId());
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
								//统计注册数量
								MobclickAgent.onEvent(RegisterActivity.this,"regist");
								JSONObject resultJson = object.getJSONObject("body").getJSONObject("success");
								SpfsUtil.saveId(resultJson.getString("sid"));
								SpfsUtil.savePhone(resultJson.getString("phone_num"));
								rllayout.setVisibility(View.GONE);
								mAnimation.stop();
//								finish();
								DialogUtils.registSuccess(RegisterActivity.this);
								SpfsUtil.setIsSign(false);
								updateMessage();
							} else {
								String error = object.getJSONObject("body").getJSONObject("error").getString("errorMessage");
								ToastUtil.show(RegisterActivity.this, error);
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
						ToastUtil.show(RegisterActivity.this, getString(R.string.C_NET_ERROR));
						rllayout.setVisibility(View.GONE);
						mAnimation.stop();
					}
				}, params);
		App.getQueue().add(req);
	}
	public void updateMessage(){
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				HashMap<String, Object> json = new HashMap<String, Object>();
				json.put("phone_num", mRegistPhone.getText().toString().trim());
				json.put("prov_code", strProvinceCode);
				json.put("city_code", strCityCode);
				json.put("county_code", strCountyCode);
				String params = ParamsUtil.getUrlParamsByMap(json);
				System.out.println("json-pre:" + params);
				MyRequest<Object> req = new MyRequest<Object>(Request.Method.POST, null, Constant.uploadLocation,
						new Response.Listener<Object>() {

							@Override
							public void onResponse(Object arg0) {
								try {
									String result = (String) arg0;
									JSONObject object = new JSONObject(result.toString());
									if (object.getInt("result") == 1) {
										JSONObject resultJson = object.getJSONObject("body").getJSONObject("success");
										ToastUtil.show(getApplicationContext(), "位置信息上传成功");
									} else {
										String error = object.getJSONObject("body").getJSONObject("error").getString("errorMessage");
//										ToastUtil.show(RegisterActivity.this, error);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError arg0) {
								arg0.printStackTrace();
								ToastUtil.show(getApplicationContext(), "位置信息上传失败");
							}
						}, params);
				App.getQueue().add(req);
				
			}
		});
		thread.start();
	}
}

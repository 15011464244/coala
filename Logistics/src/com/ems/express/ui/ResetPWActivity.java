package com.ems.express.ui;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.constant.Constant;
import com.ems.express.net.MyRequest;
import com.ems.express.util.DialogUtils;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.ToastUtil;

/**
 * Created by mengxianzheng on 15-2-10.
 */
public class ResetPWActivity extends BaseActivity {

	@InjectView(R.id.et_phone)
	EditText mPhone;

	@InjectView(R.id.et_auth_code)
	EditText mETAuthCode;

	@InjectView(R.id.btn_auth_code)
	TextView mTVauthCode;

	@InjectView(R.id.et_pw)
	EditText mPW;
	
	private Context mContext = ResetPWActivity.this;

	private ImageView imgProgress;

	private RelativeLayout rllayout;

	private AnimationDrawable mAnimation;
	private boolean isOnclick = true; //是否可以点击
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				isOnclick =false;
				mTVauthCode.setEnabled(false);
				break;
            case 1:
            	isOnclick =true;
//            	btn_payment.setBackgroundColor(Color.parseColor("#277c09"));
            	mTVauthCode.setBackgroundResource(R.drawable.btn_selector);
//            	btnAuthCode.setBackgroundColor(Color.parseColor("#FFA500"));
            	mTVauthCode.setEnabled(true);
            	mTVauthCode.setText("获取验证码");
				break;
            case 2:
            	mTVauthCode.setText(msg.obj+"秒");
            	break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_pw);
		ButterKnife.inject(this);
		imgProgress =(ImageView)findViewById(R.id.reset_chrysanthemum);
		rllayout =(RelativeLayout)findViewById(R.id.reset_layout);
		mPhone.setFilters(new InputFilter[] { new InputFilter.LengthFilter(11) });
//		mETAuthCode
//				.setFilters(new InputFilter[] { new InputFilter.LengthFilter(6) });
	}

	/**
	 * 获取验证码
	 * */
	@OnClick(R.id.btn_auth_code)
	void getAuthCode() {
		String phone = mPhone.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			ToastUtil.show(this, getString(R.string.C_HINT_REGIST_USER));
			return;
		}
		mTVauthCode.setBackgroundColor(Color.parseColor("#c0c0c0"));
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
		json.put("flag", "reset_pwd ");
		String params = ParamsUtil.getUrlParamsByMap(json);
		System.out.println("json-pre:" + params);
		MyRequest<Object> req = new MyRequest<Object>(Request.Method.POST,
				null, Constant.smsCode, new Response.Listener<Object>() {

					@Override
					public void onResponse(Object arg0) {
						try {
							String resultJson = (String) arg0;
							JSONObject object = new JSONObject(
									resultJson.toString());
							if (object.getInt("result") == 1) {
//								String success = object.getJSONObject("body")
//										.getString("success");
								ToastUtil.show(ResetPWActivity.this, "验证码已发送，请注意查收");
							} else {
								String errorMessage = object.getJSONObject("body").getJSONObject("error")
										.getString("errorMessage");
								ToastUtil.show(ResetPWActivity.this, errorMessage);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						arg0.printStackTrace();
						ToastUtil.show(ResetPWActivity.this,
								getString(R.string.C_NET_ERROR));

					}
				}, params);
		App.getQueue().add(req);
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
	/**
	 * 提交
	 * */
	@OnClick(R.id.btn_commit)
	void toCommit() {
		String phone = mPhone.getText().toString().trim();
		boolean mobileNO = DialogUtils.isMobileNO(phone);
		String authCode = mETAuthCode.getText().toString().trim();
		String pw = mPW.getText().toString().trim();
		
		if (TextUtils.isEmpty(phone)) {
			ToastUtil.show(this, getString(R.string.C_HINT_REGIST_USER));
			return;
		}
		if(mobileNO==false){
			ToastUtil.show(this, "请检查电话是否正确");
			return;
		}
		if (TextUtils.isEmpty(authCode)) {
			ToastUtil.show(this, getString(R.string.C_HINT_AUTHOR_CODE));
			return;
		}
		if (TextUtils.isEmpty(pw)) {
			ToastUtil.show(this, getString(R.string.C_HINT_LOGIN_PW));
			return;
		}
		setRegistDialog();
		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("phone_num", phone);
		json.put("verify_code", authCode);
		json.put("password", pw);
		String params = ParamsUtil.getUrlParamsByMap(json);
		System.out.println("json-pre:" + params);
		MyRequest<Object> req = new MyRequest<Object>(Request.Method.POST,
				null, Constant.ResetPwd, new Response.Listener<Object>() {

					@Override
					public void onResponse(Object arg0) {
						try {
							String resultJson = (String) arg0;
							JSONObject object = new JSONObject(
									resultJson.toString());
							if (object.getInt("result") == 1) {
								String success = object.getJSONObject("body")
										.getString("success");
								ToastUtil.show(ResetPWActivity.this, success);
								LoginActivity.startAction(mContext);
							} else {
								String error = object.getJSONObject("body")
										.getString("error");
								ToastUtil.show(ResetPWActivity.this, error);
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
						ToastUtil.show(ResetPWActivity.this,
								getString(R.string.C_NET_ERROR));
						rllayout.setVisibility(View.GONE);
						mAnimation.stop();
					}
				}, params);
		App.getQueue().add(req);
	}
	public void back(View v) {
		finish();
	}

}

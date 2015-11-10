package com.ems.express;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.ems.express.constant.Constant;
import com.ems.express.net.City;
import com.ems.express.net.MyRequest;
import com.ems.express.ui.BaiduMapActivity;
import com.ems.express.ui.LoginActivity;
import com.ems.express.ui.ServiceRangeActivity;
import com.ems.express.ui.ServiceRangeSelectActivity;
import com.ems.express.ui.mail.MailOrderListActivity;
import com.ems.express.ui.send.SendActivity;
import com.ems.express.util.AnimationUtil;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;

public class PriceActivity extends Activity implements OnClickListener, OnCheckedChangeListener {
	@InjectView(R.id.et_weight)
	EditText mEtWeight;
	@InjectView(R.id.layout_result)
	View mLayoutResult;
	@InjectView(R.id.mail_type_selection)
	RadioGroup mMailTypeSelection;
	@InjectView(R.id.tv_product)
	TextView mTvProduct;
	@InjectView(R.id.tv_weight)
	TextView mTvWeight;
	@InjectView(R.id.tv_final_price)
	TextView mTvFinalPrice;

	private Context mContext;
	private String mMailType = "2";
	private String mSourceProv;
	private String mSourceCity;
	private String mTargetProv;
	private ImageView imgProgress;
	private ImageView back;
	private TextView jumpSend;
	private AnimationUtil util;

	public static void actionStart(Context context) {
		Intent intent = new Intent(context, PriceActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_price);
		mContext = this;
		ButterKnife.inject(this);
		((TextView) findViewById(R.id.tex_title)).setText("运费查询");
		back  = (ImageView)findViewById(R.id.img_back);
		findViewById(R.id.layout_end_location).setOnClickListener(this);
		findViewById(R.id.layout_start_location).setOnClickListener(this);
		findViewById(R.id.layout_query).setOnClickListener(this);
		jumpSend =(TextView)findViewById(R.id.bt_jump_send);
		mMailTypeSelection.setOnCheckedChangeListener(this);
		
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		jumpSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(SpfsUtil.loadPhone().equals("")){
					Intent intent1 = new Intent(PriceActivity.this,LoginActivity.class);
					startActivity(intent1);
					Toast.makeText(PriceActivity.this,"请先登录", 1).show();
				}else{
					Intent intent = new Intent(PriceActivity.this, SendActivity.class);
					startActivity(intent);
				}
			}
		});
		
		 util =new AnimationUtil(this, R.style.dialog_animation);
		
	}
	

	@Override
	public void onClick(View v) {
		
		int type;
		if (mMailTypeSelection.getCheckedRadioButtonId() == R.id.premium) {
			type = 2;
		} else {
			type = 1;
		}
		switch (v.getId()) {
		case R.id.layout_end_location:
			ServiceRangeSelectActivity.start(ServiceRangeSelectActivity.QUERY_TYPE_EXPRENSE, City.TYPE_CITY,
					type, false,
					this);
			break;
		case R.id.layout_start_location:
			ServiceRangeSelectActivity.start(ServiceRangeSelectActivity.QUERY_TYPE_EXPRENSE, City.TYPE_CITY,
					type, true,
					this);
			break;
		case R.id.layout_query:
			if(mSourceProv == null || mSourceCity.isEmpty()){
				ToastUtil.show(mContext, "请选择寄件省份");
				return;
			}
			if(mMailType == "1"){
				if(mSourceCity == null || mSourceCity.isEmpty()){
					ToastUtil.show(mContext, "请选择寄件城市");
					return;
				}
			}
			String sendCity = mMailType == "1" ? mSourceProv + "\t" + mSourceCity : mTargetProv;
			if (sendCity == null || "".equals(sendAddress)) {
				ToastUtil.show(mContext, "请选择收件城市");
				return;
			}
			if(mEtWeight.getText().toString().isEmpty()){
				ToastUtil.show(mContext, "请填写重量");
				return;
			}
			util.show();
			HashMap<String, Object> json = new HashMap<String, Object>();
			json.put("queryFlag", mMailType);
			json.put("sendCity", mMailType == "2" ? mSourceProv + "\t" + mSourceCity : mSourceProv);
			json.put("recvCity", mTargetProv);
			json.put("weight", mEtWeight.getText().toString());
			
			String params = ParamsUtil.getUrlParamsByMap(json);
			System.out.println("json-pre:" + params);
			Log.e("msg", params);
			MyRequest<Object> req = new MyRequest<Object>(Method.POST, null,Constant.Costquery, new Listener<Object>() {
						@Override
						public void onResponse(Object resp) {
							if (resp == null || resp.toString().isEmpty()) {
								ToastUtil.show(mContext, "查询失败，请稍后重试");
								util.dismiss();
								return;
							}
							/**
							 * {"model":{"message1":29,"message2":21,"message":21},"cleared":false}
							 */
							System.out.println("prices: " + resp);
							try {
								JSONObject jso = new JSONObject(resp.toString());
								JSONObject model = jso.getJSONObject("model");
								String price = model.getString("message");
								mTvProduct.setText(mMailType == "1" ? "经济快递" : "特快专递");
								mTvWeight.setText(mEtWeight.getText().toString());
								mTvFinalPrice.setText(price);
								mLayoutResult.setVisibility(View.VISIBLE);
							} catch (Exception e) {
								e.printStackTrace();
								ToastUtil.show(mContext, "查询失败，请稍后重试");
								util.dismiss();
							}
							util.dismiss();
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							Toast.makeText(PriceActivity.this, "数据加载失败", Toast.LENGTH_LONG).show();
							arg0.printStackTrace();
							util.dismiss();
						}
					}, params);
			App.getQueue().add(req);
			break;

		
			
		default:
			break;
		}
	}

	
	private String sendAddress = "";
	private String endAddress = "";

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ServiceRangeSelectActivity.REQUEST_CODE_GET_ADRRESS) {
			if (resultCode == RESULT_OK) {
				City pro = (City) data.getSerializableExtra(ServiceRangeSelectActivity.CITY_PRO);
				City city = (City) data.getSerializableExtra(ServiceRangeSelectActivity.CITY_CITY);
				City county = (City) data.getSerializableExtra(ServiceRangeSelectActivity.CITY_COUNTY);
//				ToastUtil.show(this, "数据：" + pro + ":" + county + ":" + city);
				String str = "";
				if (pro != null) {
					Log.e("ajh", "pro.getName(): " + pro.getName());
					str += pro.getName() + " ";
				}
				if (city != null) {
					Log.e("ajh", "city.getName(): " + city.getName());
					str += city.getName() + "\t";
				}
				if (county != null) {
					Log.e("ajh", "county.getName(): " + county.getName());
					str += county.getName();
				}
				if (data.getBooleanExtra(ServiceRangeSelectActivity.KEY_IS_SEND_CITY, false)) {
					sendAddress = str;
					mSourceProv = pro.getName();
					mSourceCity = city.getName();
					TextView textView = (TextView) findViewById(R.id.text1);
					textView.setText(sendAddress);
				} else {
					endAddress = str;
					mTargetProv = pro.getName();
					TextView textView = (TextView) findViewById(R.id.text2);
					textView.setText(endAddress);
				}
			} else {
//				ToastUtil.show(this, "取消了");
			}
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		mMailType = checkedId == R.id.premium ? "2" : "1";
//		ToastUtil.show(this, mMailType);
	}
}

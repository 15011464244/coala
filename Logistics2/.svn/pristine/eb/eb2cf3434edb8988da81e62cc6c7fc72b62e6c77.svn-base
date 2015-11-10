package com.ems.express.ui.send;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.GsonPostParamsRequest;
import com.android.volley.toolbox.GsonRequest;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.PostParamsReqest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.bean.PeopleInfo;
import com.ems.express.constant.Constant;
import com.ems.express.net.City;
import com.ems.express.net.MyRequest;
import com.ems.express.net.resp.QueryAddressResp;
import com.ems.express.ui.ServiceRangeSelectActivity;
import com.ems.express.util.DialogUtils;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;
import com.google.gson.Gson;

public class SenderInfoActivity extends Activity implements OnClickListener {

	public static final int SENDER = 0;
	public static final int RECEIVER = 1;
	public static final String INFO_TYPE = "type";

	private int mType;

	@InjectView(R.id.et_name)
	EditText mEtName;
	@InjectView(R.id.et_phone)
	EditText mEtPhone;
	@InjectView(R.id.tv_zone)
	TextView mTvZone;
	@InjectView(R.id.et_location)
	EditText mEtLocation;
	@InjectView(R.id.query_address)
	ListView mLvQuery;
	@InjectView(R.id.iv_zone)
	ImageView mIvZone;

	private Context mContext;
	private City mProv;
	private City mCity;
	private City mCounty;
	private List<String> mData = new ArrayList<String>();
	ArrayAdapter mAdapter;
	Boolean flag = true;

	public static void actionStart(Context context, int flag) {
		Intent intent = new Intent(context, SenderInfoActivity.class);
		intent.putExtra(INFO_TYPE, flag);
		context.startActivity(intent);
	}

	public static void actionStartForResult(Activity context, int flag) {
		Intent intent = new Intent(context, SenderInfoActivity.class);
		intent.putExtra(INFO_TYPE, flag);
		context.startActivityForResult(intent, flag);
	}

	public void back(View v) {
		finish();
	}

	public void storeInfo(View v) {
		if (mProv == null) {
			ToastUtil.show(mContext, "请选择省份");
			return;
		}
		if (mCity == null) {
			ToastUtil.show(mContext, "请选择城市");
			return;
		}
		if (mCounty == null) {
			ToastUtil.show(mContext, "请选择区县");
			return;
		}
		if (mEtName.getText().toString().isEmpty()) {
			ToastUtil.show(mContext, "请填写姓名");
			return;
		}
		boolean mobileNO = DialogUtils.isMobileNO(mEtPhone.getText().toString());
		if (mEtPhone.getText().toString().isEmpty()) {
			ToastUtil.show(mContext, "请填写电话");
			return;
		}
		if(mobileNO==false){
			ToastUtil.show(mContext, "请检查电话是否正确");
			return;
		}
		if (mEtLocation.getText().toString().isEmpty()) {
			ToastUtil.show(mContext, "请填写详细地址");
			return;
		}
		PeopleInfo info = new PeopleInfo();
		info.setName(mEtName.getText().toString().trim());
		info.setPhone(mEtPhone.getText().toString().trim());
		info.setProv(mProv.getName());
		info.setCity(mCity.getName());
		info.setCounty(mCounty.getName());
		info.setProvCode(mProv.getCode() + "");
		info.setCityCode(mCity.getCode() + "");
		info.setCountyCode(mCounty.getCode() + "");
		info.setLocation(mEtLocation.getText().toString().trim());
		Intent intent = new Intent();
		intent.putExtra("info", info);
		setResult(RESULT_OK, intent);
		ToastUtil.show(mContext, "保存成功");
		Gson gson = new Gson();
		String infoStr = gson.toJson(info);
		SpfsUtil.saveString("people_info" + mType, infoStr);
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sender_info);
		mContext = this;
		ButterKnife.inject(this);
		Intent intent = getIntent();
		mType = intent.getIntExtra(INFO_TYPE, SENDER);
		((TextView) findViewById(R.id.tv_title))
				.setText(mType == SENDER ? "寄件人信息" : "收件人信息");
		mTvZone.setOnClickListener(this);
		mIvZone.setOnClickListener(this);
		mAdapter = new ArrayAdapter<String>(mContext,
				R.layout.simple_list_item_query, mData);
		mLvQuery.setAdapter(mAdapter);
		mLvQuery.setVisibility(View.GONE);
		mLvQuery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				flag = false;
				mLvQuery.setVisibility(View.GONE);
				mEtLocation.setText(mData.get(position));
			}
		});
		mEtLocation.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (flag)
					getQueryAddress(s);
				flag = true;
			}

		});

		mEtName.setOnClickListener(this);
		mEtPhone.setOnClickListener(this);
		mTvZone.setOnClickListener(this);
		mEtLocation.setOnClickListener(this);

		initData();
	}

	public void getQueryAddress(Editable s) {
		if (mProv != null && mCity != null) {
			mData.clear();
			mLvQuery.setVisibility(View.VISIBLE);
			HashMap<String, Object> json = new HashMap<String, Object>();
			json.put("provCode", mProv.getCode() + "");
			if(mProv.getCode() == 11||mProv.getCode()==12 || mProv.getCode()==31 || mProv.getCode() == 50){
				json.put("cityCode", mProv.getCode()+"");
			}else{
				json.put("cityCode", mCity.getCode()+"");
			}
			json.put("countyCode", mCounty.getCode() + "");
			json.put("addrName", s + "");
			String params = ParamsUtil.getUrlParamsByMap(json);
			System.out.println("json-pre:" + json);

			MyRequest<Object> req = new MyRequest<Object>(Method.POST, null,
					Constant.QueryAddress, new Listener<Object>() {

						@Override
						public void onResponse(Object arg0) {
							Log.e("msg", arg0.toString());

							JSONObject obj = JSONObject.parseObject(arg0
									.toString());
							if ("1".equals(obj.getString("result"))) {
								JSONObject bodyObj = obj.getJSONObject("body");
								JSONArray successObja = bodyObj
										.getJSONArray("success");
								JSONObject jsonDataObj = successObja
										.getJSONObject(0);
								String jsonData = jsonDataObj.getString("jsonData");
								if (jsonData.contains("prov_code")) {
									JSONObject addressListObj = JSONObject
											.parseObject(jsonData);
									JSONObject addressObj = addressListObj
											.getJSONObject("address_list");
									JSONArray addressObja = new JSONArray();
									try {
										addressObja = addressObj
												.getJSONArray("address");
									} catch (Exception e) {
										e.printStackTrace();
										return;
									}
									for (int i = 0; i < addressObja.size(); i++) {
										JSONObject jo = addressObja
												.getJSONObject(i);
										Log.e("msg", jo.getString("value"));
										mData.add(jo.getString("value"));
									}
									Log.e("msg", "list："+mData.toString());
									mAdapter.notifyDataSetChanged();
								}
							}
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							arg0.printStackTrace();

						}
					}, params);
			App.getQueue().add(req);
		}
	}

	private void initData() {
		String infoStr = SpfsUtil.getString("people_info" + mType);
		if (infoStr.isEmpty()) {
			return;
		}
		PeopleInfo info = new Gson().fromJson(infoStr, PeopleInfo.class);
		mEtName.setText(info.getName());
		mEtPhone.setText(info.getPhone());
		mTvZone.setText(info.getProv() + " " + info.getCity() + " "
				+ info.getCounty());
		mEtLocation.setText(info.getLocation());
		mProv = new City();
		mProv.setCode(Integer.parseInt(info.getProvCode()));
		mProv.setName(info.getProv());
		mCity = new City();
		mCity.setCode(Integer.parseInt(info.getCityCode()));
		mCity.setName(info.getCity());
		mCounty = new City();
		mCounty.setCode(Integer.parseInt(info.getCountyCode()));
		mCounty.setName(info.getCounty());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.et_name:
		// mLvQuery.setVisibility(View.GONE);
		// break;
		// case R.id.et_phone:
		// mLvQuery.setVisibility(View.GONE);
		// break;
		case R.id.tv_zone:
			mLvQuery.setVisibility(View.GONE);
			ServiceRangeSelectActivity.start(
					ServiceRangeSelectActivity.QUERY_TYPE_ADDRESS,
					City.TYPE_COUNTY, -1, false, (Activity) mContext);
			break;
		case R.id.iv_zone:
			mLvQuery.setVisibility(View.GONE);
			ServiceRangeSelectActivity.start(
					ServiceRangeSelectActivity.QUERY_TYPE_ADDRESS,
					City.TYPE_COUNTY, -1, false, (Activity) mContext);
			break;

		// case R.id.et_location:
		// break;

		default:
			mLvQuery.setVisibility(View.GONE);
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ServiceRangeSelectActivity.REQUEST_CODE_GET_ADRRESS) {
			if (resultCode == RESULT_OK) {
				mProv = (City) data
						.getSerializableExtra(ServiceRangeSelectActivity.CITY_PRO);
				mCity = (City) data
						.getSerializableExtra(ServiceRangeSelectActivity.CITY_CITY);
				mCounty = (City) data
						.getSerializableExtra(ServiceRangeSelectActivity.CITY_COUNTY);
				String str = "";
				if (mProv != null) {
					Log.e("ajh", "pro.getName(): " + mProv.getName());
					str += mProv.getName() + " ";
				}
				if (mCity != null) {
					Log.e("ajh", "city.getName(): " + mCity.getName());
					str += mCity.getName() + "\t";
				}
				if (mCounty != null) {
					Log.e("ajh", "county.getName(): " + mCounty.getName());
					str += mCounty.getName();
				}
				mTvZone.setText(str);
			} else {
//				ToastUtil.show(this, "取消了");
			}
		}
	}

}

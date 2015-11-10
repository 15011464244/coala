package com.ems.express.ui;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.net.City;
import com.ems.express.net.MyRequest;
import com.ems.express.net.UrlUtils;
import com.ems.express.ui.mail.MailOrderListActivity;
import com.ems.express.ui.send.SendActivity;
import com.ems.express.util.AnimationUtil;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;

public class TimeQueryActivity extends BaseActivity implements OnClickListener, OnDateChangedListener,
		OnTimeChangedListener {
	private RadioGroup group = null;
	private TextView result = null;
	private TextView resultHeader = null;
	private TextView send, recv;
	private TextView item_des;
	private View layoutResult = null;
	private ImageView mIvNext5;
	

	/**
	 * added by sweetvvck
	 */
	@InjectView(R.id.tv_selected_date)
	TextView mSelecteDate;
	@InjectView(R.id.arrow_date)
	ImageView mDateArrowView;
	@InjectView(R.id.date_selection)
	View mDateSelectionView;
	@InjectView(R.id.tv_date)
	TextView mTvDate;
	@InjectView(R.id.tv_time)
	TextView mTvTime;
	@InjectView(R.id.date_picker)
	DatePicker mDatePicker;
	@InjectView(R.id.time_picker)
	TimePicker mTimePicker;
	private Calendar now;
	private String mDate;
	private String mTime;
	
	private TextView jumpSend;
	private AnimationUtil util;
	private int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time);
		ButterKnife.inject(this);
		setHeadTitle("时效查询");
		group = (RadioGroup) findViewById(R.id.type);
		result = (TextView) findViewById(R.id.item_result);
		resultHeader = (TextView) findViewById(R.id.item_);
		layoutResult = findViewById(R.id.layout_result);
		jumpSend =(TextView)findViewById(R.id.bt_jump_send);
		findViewById(R.id.layout_1).setOnClickListener(this);
		findViewById(R.id.layout_2).setOnClickListener(this);
		findViewById(R.id.layout_4).setOnClickListener(this);
		findViewById(R.id.layout_5).setOnClickListener(this);

		 util =new AnimationUtil(this, R.style.dialog_animation);
		/**
		 * added by sweetvvck
		 */
		findViewById(R.id.select_date).setOnClickListener(this);
		mTvDate.setOnClickListener(this);
		mTvTime.setOnClickListener(this);
		now = Calendar.getInstance();
		mDate = now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DAY_OF_MONTH);
		mTime = now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND);
		mSelecteDate.setText(mDate + " " + mTime);
		mDatePicker.init(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), this);
		mTimePicker.setOnTimeChangedListener(this);

		send = (TextView) findViewById(R.id.item_value);
		recv = (TextView) findViewById(R.id.item_value_2);
		item_des = (TextView) findViewById(R.id.item_des);
		mIvNext5 = (ImageView) findViewById(R.id.next_5);
		
		jumpSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(SpfsUtil.loadPhone().equals("")){
					Intent intent1 = new Intent(TimeQueryActivity.this,LoginActivity.class);
					startActivity(intent1);
					Toast.makeText(TimeQueryActivity.this,"请先登录", 1).show();
				}else{
					Intent intent = new Intent(TimeQueryActivity.this, SendActivity.class);
					startActivity(intent);
				}
			}
		});
	}

	private void check() {
		
		if(sendAddress == null|| sendAddress.isEmpty()){
			ToastUtil.show(TimeQueryActivity.this, "请选择原寄地");
			return;
		}
		if(sendAddress == null|| endAddress.isEmpty()){
				ToastUtil.show(TimeQueryActivity.this, "请选择目的地");
				return;
		}
		
		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("queryFlag", type);
		
		String string="";
		try {
			string = new String(sendAddress.getBytes(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		json.put("sendCity", string);
		json.put("recvCity", endAddress);
		json.put("sendDate", mDate + " " + mTime);
		util.show();
		String params = ParamsUtil.getUrlParamsByMap(json);
		System.out.println("json-pre:" + params);
		MyRequest<Object> req = new MyRequest<Object>(Method.POST, null, UrlUtils.URL_QUERY_TIME,
				new Listener<Object>() {
					@Override
					public void onResponse(Object arg0) {
						if (TextUtils.isEmpty(arg0 + "")) {
							ToastUtil.show(TimeQueryActivity.this, "数据获取失败");
							util.dismiss();
						}  
						String aa = arg0.toString();
						if(aa.contains("traces")){
							util.dismiss();
							return;
						}
						JSONObject object = JSONObject.parseObject(aa);
						if (object.containsKey("model")) {
							JSONObject jsonObject = object.getJSONObject("model");
							if (jsonObject.containsKey("map")) {
								String days = jsonObject.getJSONObject("map").getString("limit");
								String endDate = jsonObject.getJSONObject("map").getString("endDate");
								
								resultHeader.setText(days);
								result.setText(endDate);
								
								layoutResult.setVisibility(View.VISIBLE);
								util.dismiss();
							}else {
								resultHeader.setText("暂无此类型");
								result.setText("");
								ToastUtil.show(TimeQueryActivity.this, "数据获取失败");
								util.dismiss();
								
							}
						}else {
							resultHeader.setText("暂无此类型");
							result.setText("");
							ToastUtil.show(TimeQueryActivity.this, "数据获取失败");
							util.dismiss();
						}
						
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						Toast.makeText(TimeQueryActivity.this, "数据加载失败", Toast.LENGTH_LONG).show();
						arg0.printStackTrace();
						util.dismiss();

					}
				}, params);
		App.getQueue().add(req);
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
				
				 if(pro != null){
				 Log.e("ajh", "pro.getName(): " + pro.getName());
				 //str+= pro.getName()+"\t";
				 }
				if (city != null && !city.isZhixiashi()) {
					Log.e("ajh", "city.getName(): " + city.getName());
					str += city.getName() + "\t";
				}
				if (county != null) {
					Log.e("ajh", "county.getName(): " + county.getName());
					str += county.getName();
				}

				// ItemAdapter itemAdapter = (ItemAdapter) list.getAdapter();
				if (data.getBooleanExtra(ServiceRangeSelectActivity.KEY_IS_SEND_CITY, false)) {
					sendAddress = str;
					send.setText(str);
				} else {
					endAddress = str;
					recv.setText(str);
				}
			} else {
//				ToastUtil.show(this, "取消了");
			}
		}
	}

	@Override
	public void onClick(View v) {
		int type = -1;
		if (group.getCheckedRadioButtonId() == R.id.type_1) {
			type = ServiceRangeSelectActivity.EXPRESS_JIANJI;
		} else {
			type = ServiceRangeSelectActivity.EXPRESS_JIANJI;
		}
		
		if (group.getCheckedRadioButtonId() == R.id.type_1) {
			this.type = 2;
		} else {
			this.type = 1;
		}
		
		switch (v.getId()) {
		case R.id.layout_1:
			ServiceRangeSelectActivity.start(ServiceRangeSelectActivity.QUERY_TYPE_TIME, City.TYPE_COUNTY, type, true,
					this);
			break;
		case R.id.layout_2:
			ServiceRangeSelectActivity.start(ServiceRangeSelectActivity.QUERY_TYPE_TIME, City.TYPE_COUNTY, type, false,
					this);
			break;
		case R.id.layout_4:
			check();
			break;
		case R.id.layout_5:
			if (item_des.getVisibility() == View.VISIBLE) {
				item_des.setVisibility(View.GONE);
				mIvNext5.setImageResource(R.drawable.icon_down);
			} else {
				item_des.setVisibility(View.VISIBLE);
				mIvNext5.setImageResource(R.drawable.icon_up);
			}
			break;

		/**
		 * added by sweetvvck
		 */
		case R.id.select_date:
			toggleDate();
			break;
		case R.id.tv_date:
			mTvDate.setTextColor(getResources().getColor(R.color.orange));
			mTvTime.setTextColor(getResources().getColor(R.color.black));
			mDatePicker.setVisibility(View.VISIBLE);
			mTimePicker.setVisibility(View.GONE);
			break;
		case R.id.tv_time:
			mTvDate.setTextColor(getResources().getColor(R.color.black));
			mTvTime.setTextColor(getResources().getColor(R.color.orange));
			mDatePicker.setVisibility(View.GONE);
			mTimePicker.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	private void toggleDate() {
		boolean selectionShown = mDateSelectionView.getVisibility() == View.VISIBLE;
		mDateArrowView.setImageResource(selectionShown ? R.drawable.icon_down : R.drawable.icon_up);
		mDateSelectionView.setVisibility(selectionShown ? View.GONE : View.VISIBLE);
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		mDate = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
		mSelecteDate.setText(mDate + " " + mTime);
	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		mTime = hourOfDay + ":" + minute + ":00";
		mSelecteDate.setText(mDate + " " + mTime);
	}
}

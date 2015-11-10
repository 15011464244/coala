package com.ems.express.ui.send;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.bean.PeopleInfo;
import com.ems.express.constant.Constant;
import com.ems.express.net.Carrier;
import com.ems.express.net.MyRequest;
import com.ems.express.ui.BaiduMapActivity;
import com.ems.express.ui.LoginActivity;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;
import com.google.gson.JsonSyntaxException;

/**
 * 我要寄件
 */
public class SendActivity extends Activity implements OnClickListener, OnCheckedChangeListener, OnDateChangedListener,
		OnTimeChangedListener {

	private Context mContext;
	@InjectView(R.id.arrow_weight)
	ImageView mWeightArrowView;
	@InjectView(R.id.weight_selection)
	RadioGroup mWeightSelectionView;
	@InjectView(R.id.selected_weight)
	TextView mSelectedWeight;
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
	@InjectView(R.id.payment_selection)
	RadioGroup mPaymentSelectionView;
	@InjectView(R.id.mail_type)
	RadioGroup mMailTypeSelectionView;
	@InjectView(R.id.sender_name)
	TextView mTvSenderName;
	@InjectView(R.id.receiver_name)
	TextView mTvReceiverName;
	@InjectView(R.id.et_weight)
	EditText mEtWeight;
	@InjectView(R.id.tv_info)
	TextView mTvInfo;
	
	@InjectView(R.id.postman_info)
	RelativeLayout mPostmanInfo;
	@InjectView(R.id.postman_name)
	TextView postmanName;

	private String[] mWeightValues = { "0--5kg", "5--10kg", "10kg以上" };
	private Calendar now;
	private String mDate;
	private String mTime;
	
	private GeoCoder coder;
	//临时用于保存信息
	private Carrier manInfo;

	private PeopleInfo senderInfo;
	private PeopleInfo receiverInfo;
	private RequestQueue mQueue;
	private int mPayment = 1;
	private String mMailType = "1";
	private ImageView imgProgress;
	private RelativeLayout rllayout;
	private AnimationDrawable mAnimation;
	
	public static void actionStart(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, SendActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send);
		ButterKnife.inject(this);
		mContext = this;
		mQueue = App.getQueue();
		initView();
		loadData();
	}

	public void back(View v) {
		finish();
	}

	public void initView() {
		((TextView) findViewById(R.id.tv_title)).setText("寄件");
		findViewById(R.id.sender_info).setOnClickListener(this);
		findViewById(R.id.receiver_info).setOnClickListener(this);
		findViewById(R.id.select_weight).setOnClickListener(this);
		findViewById(R.id.select_date).setOnClickListener(this);
		findViewById(R.id.tv_date).setOnClickListener(this);
		findViewById(R.id.tv_time).setOnClickListener(this);
		findViewById(R.id.postman_info).setOnClickListener(this);
		imgProgress =(ImageView)findViewById(R.id.send_chrysanthemum);
		rllayout =(RelativeLayout)findViewById(R.id.send_layout);
		// mTvInfo.setVisibility(View.VISIBLE);
		mTvInfo.setOnClickListener(this);
		mTvInfo.setText("寄件历史");
		mWeightSelectionView.setOnCheckedChangeListener(this);
		mPaymentSelectionView.setOnCheckedChangeListener(this);
		mMailTypeSelectionView.setOnCheckedChangeListener(this);
		now = Calendar.getInstance();
		mDate = now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DAY_OF_MONTH);
		mTime = now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE);// + ":" + now.get(Calendar.SECOND);
		mSelecteDate.setText(mDate + " " + mTime);
		mDatePicker.init(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), this);
		mTimePicker.setOnTimeChangedListener(this);
		
		
		Intent intent = getIntent();
		manInfo =(Carrier)intent.getSerializableExtra("carrier");
	}

	public void loadData() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sender_info:
			SenderInfoActivity.actionStartForResult(this, SenderInfoActivity.SENDER);
			break;
		case R.id.receiver_info:
			SenderInfoActivity.actionStartForResult(this, SenderInfoActivity.RECEIVER);
			break;
		case R.id.select_weight:
			toggleWeight();
			break;
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
		case R.id.tv_info:
			SendRecordActivity.actionStart(mContext);
			break;
		case R.id.postman_info:
			BaiduMapActivity.getPostmanInfo(this,true);
		default:
			break;
		}
	}

	private void setDiaolog() {
		imgProgress.setBackgroundResource(R.drawable.progress_villain);  
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
	 * http://localhost:8080/micro-channel-service/NetworkAction/sendMail?sname=
	 * %E5%B7%A7%E5%85%8B%E5%8A%9B&sphone=1231&send_prov=%E5%90%88%E8%82%A5&
	 * send_city=as&send_county=ccc&send_prov_code=123&send_city_code=1231&
	 * send_county_code
	 * =3141&slocation=qqqq&rname=%E7%B3%96%E8%B1%86&rphone=1231231
	 * &receive_prov=
	 * sllle&receive_city=wew&receive_county=xxxx&receive_prov_code
	 * =123141&receive_city_code
	 * =3423432&receive_county_code=12311&rlocation=ccccc&dateID=2015-02-08
	 * 2018:54&weight=1&payment=1&extraService=1,2
	 */
	public void submit(View v) {
		String userPhone = SpfsUtil.loadPhone();
		String userId = SpfsUtil.loadId();
		if (userId.isEmpty()) {
			AlertDialog dialog = new AlertDialog.Builder(mContext).setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							LoginActivity.startAction(mContext);
						}
					}).create();
			dialog.setMessage("您还未登录，请先登录。");
			dialog.show();
			return;
		}
		
		if (senderInfo == null) {
			ToastUtil.show(mContext, "请编辑发件人信息");
			return;
		}
		if (receiverInfo == null) {
			ToastUtil.show(mContext, "请编辑收件人信息");
			return;
		}
		if (mEtWeight.getText().toString().isEmpty()) {
			ToastUtil.show(mContext, "请填写重量");
			return;
		}
		if(null == manInfo){
			ToastUtil.show(mContext, "请选择邮递员");
			return;
		}
		
		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("sname", senderInfo.getName());
		json.put("sphone", senderInfo.getPhone());
		json.put("send_prov", senderInfo.getProv());
		json.put("send_city", senderInfo.getCity());
		json.put("send_county", senderInfo.getCounty());
		json.put("send_prov_code", senderInfo.getProvCode());
		json.put("send_city_code", senderInfo.getCityCode());
		json.put("send_county_code", senderInfo.getCountyCode());
		json.put("slocation", senderInfo.getLocation());
		json.put("rname", receiverInfo.getName());
		json.put("rphone", receiverInfo.getPhone());
		json.put("receive_prov", receiverInfo.getProv());
		json.put("receive_city", receiverInfo.getCity());
		json.put("receive_county", receiverInfo.getCounty());
		json.put("receive_prov_code", receiverInfo.getProvCode());
		json.put("receive_city_code", receiverInfo.getCityCode());
		json.put("receive_county_code", receiverInfo.getCountyCode());
		json.put("rlocation", receiverInfo.getLocation());
		json.put("dateID", mDate + " " + mTime);
		json.put("weight", mEtWeight.getText().toString());
		json.put("payment", mPayment);
		json.put("extraService", "1,2");
		json.put("objectstate", mMailType);
		json.put("tb_user_e_id", userPhone);
		json.put("ydyname", "");
		json.put("ydytel", "");
		json.put("jgh", "");
		json.put("ygh", "");
		json.put("weixinzhanghao", "");
		
		//添加参数    20150321
		json.put("senderTel", senderInfo.getPhone());
		json.put("deviceType", "android");
		TelephonyManager mTelephonyMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		json.put("deviceNo", mTelephonyMgr.getDeviceId());
		//经纬度
		Map<String,String> latlng = getLatAndLng();
		json.put("bdLat",latlng.get("lat"));
		json.put("bdLon",latlng.get("lng"));
		
		//临时添加 邮递员的信息
		json.put("org_code",manInfo.getCode());//机构号
		json.put("user_code",manInfo.getEmployeeNo());//员工号
//		json.put("org_code","88888888");//机构号
//		json.put("user_code","1006");//员工号

		String params = ParamsUtil.getUrlParamsByMap(json);
		setDiaolog();
		System.out.println("json-pre:" + params);
		MyRequest<Object> req = new MyRequest<Object>(Method.POST, null, Constant.DeliveryMail, new Listener<Object>() {

					@Override
					public void onResponse(Object resp) {
						if (resp == null || resp.toString().isEmpty()) {
							ToastUtil.show(mContext, "提交失败，请稍后重试11");
							rllayout.setVisibility(View.GONE);
							mAnimation.stop();
							return;
						}
						try {
							JSONObject jso = new JSONObject(resp.toString());
							if ("1".equals(jso.get("result"))) {
								String info = jso.getJSONObject("body").getString("success");
								ToastUtil.show(mContext, info.isEmpty() ? "未知错误" : "提交预约成功！");
								AlertDialog dialog = new AlertDialog.Builder(mContext).setPositiveButton("确定", null)
										.create();
								dialog.setMessage("您的寄件信息已经提交，请您耐心等待快递员上门取件");
								dialog.show();
								rllayout.setVisibility(View.GONE);
								mAnimation.stop();
							} else {
								ToastUtil.show(mContext, "提交失败，请稍后重试22");
								rllayout.setVisibility(View.GONE);
								mAnimation.stop();
							}
						} catch (JsonSyntaxException e) {
							e.printStackTrace();
							rllayout.setVisibility(View.GONE);
							mAnimation.stop();
						} catch (JSONException e) {
							e.printStackTrace();
							rllayout.setVisibility(View.GONE);
							mAnimation.stop();
						}
						rllayout.setVisibility(View.GONE);
						mAnimation.stop();
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError e) {
						e.printStackTrace();
						ToastUtil.show(mContext, "提交失败，请稍后重试33");
						rllayout.setVisibility(View.GONE);
						mAnimation.stop();
					}
				}, params);

		mQueue.add(req);
		mQueue.start();
	}

	private void toggleDate() {
		boolean selectionShown = mDateSelectionView.getVisibility() == View.VISIBLE;
		mDateArrowView.setImageResource(selectionShown ? R.drawable.icon_down : R.drawable.icon_up);
		mDateSelectionView.setVisibility(selectionShown ? View.GONE : View.VISIBLE);
	}

	private void toggleWeight() {
		boolean selectionShown = mWeightSelectionView.getVisibility() == View.VISIBLE;
		mWeightArrowView.setImageResource(selectionShown ? R.drawable.icon_down : R.drawable.icon_up);
		mWeightSelectionView.setVisibility(selectionShown ? View.GONE : View.VISIBLE);
		System.out.println("checked: " + mWeightSelectionView.getCheckedRadioButtonId());
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (group == mPaymentSelectionView) {
			mPayment = checkedId == R.id.pay_now ? 1 : 2;
		} else if (group == mWeightSelectionView) {
			int index = mWeightSelectionView.indexOfChild(mWeightSelectionView.findViewById(checkedId));
			mSelectedWeight.setText(mWeightValues[index]);
			toggleWeight();
		} else if (group == mMailTypeSelectionView) {
			mMailType = checkedId == R.id.file ? "1" : "2";
		}
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		mDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
		mSelecteDate.setText(mDate + " " + mTime);
	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		mTime = hourOfDay + ":" + minute;// + ":00";
		mSelecteDate.setText(mDate + " " + mTime);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && data != null) {
			if (requestCode == SenderInfoActivity.SENDER) {
				senderInfo = (PeopleInfo) data.getSerializableExtra("info");
				mTvSenderName.setText(senderInfo.getName());
			}else if(requestCode == 3){
				 manInfo = (Carrier) data.getSerializableExtra("carrier");
				Log.e("msgggPostmanInfo", manInfo.toString());
				postmanName.setText(manInfo.getPeople());
			} else {
				receiverInfo = (PeopleInfo) data.getSerializableExtra("info");
				mTvReceiverName.setText(receiverInfo.getName());
			}
			//临时添加邮递员信息
			/*Carrier manInfo = (Carrier) data.getSerializableExtra("carrier");
			Log.e("msgggPostmanInfo", manInfo.toString());
			postmanName.setText(manInfo.getPeople());
			*/
		}
	}
	
	/**
	 * 获取经度和纬度
	 * @return
	 */
	private Map<String,String> getLatAndLng(){
		final Map<String,String> map = new HashMap<String, String>();
		coder = GeoCoder.newInstance();
		coder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
				if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
					ToastUtil.show(mContext, "请求经纬度失败");
					return;
				}
				LatLng location = result.getLocation();
				//经度
				String lat = String.valueOf(location.latitude);
				//纬度
				String lng = String.valueOf(location.longitude);
				map.put("lat", lat);
				map.put("lng",lng);
			}
			
			@Override
			public void onGetGeoCodeResult(GeoCodeResult result) {
			}
		});
		
		return map;
	}
}

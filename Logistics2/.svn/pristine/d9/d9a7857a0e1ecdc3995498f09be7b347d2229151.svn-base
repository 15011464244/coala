package com.ems.express.ui;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
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
import com.ems.express.ui.send.SendActivity;
import com.ems.express.util.AnimationUtil;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;

public class ServiceRangeActivity extends Activity implements OnClickListener{
	private int intPro = 0;
	private int intCity = 0;
	private int intCount = 0;
	private TextView tv = null;
	private View reusltLayout = null;
	private TextView requestString = null;
	private TextView resutl = null;
	
	private TextView jumpSend;
	private AnimationUtil util;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_range);
		findViewById(R.id.item_service_range).setOnClickListener(this);
		findViewById(R.id.img_back).setOnClickListener(this);
		findViewById(R.id.layou_submit).setOnClickListener(this);
		tv = (TextView)findViewById(R.id.title_1);
		reusltLayout = findViewById(R.id.layout_result);
		jumpSend =(TextView)findViewById(R.id.bt_jump_send);
		reusltLayout.setVisibility(View.GONE);
		requestString = (TextView)findViewById(R.id.text_query_string);
		resutl = (TextView)findViewById(R.id.text_query_result);
		util =new AnimationUtil(this, R.style.dialog_animation);
		jumpSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(SpfsUtil.loadPhone().equals("")){
					Intent intent1 = new Intent(ServiceRangeActivity.this,LoginActivity.class);
					startActivity(intent1);
					Toast.makeText(ServiceRangeActivity.this,"请先登录", 1).show();
				}else{
					Intent intent = new Intent(ServiceRangeActivity.this, SendActivity.class);
					startActivity(intent);
				}
			}
		});
	}

	
	@Override
	public void onClick(View v) {
//		Intent intent = new Intent(this,ServiceRangeSelectActivity.class);
//		startActivityForResult(intent, 100);
		switch (v.getId()) {
		case R.id.item_service_range:
			ServiceRangeSelectActivity.start(ServiceRangeSelectActivity.QUERY_TYPE_ADDRESS,City.TYPE_COUNTY,-1,false, this);
			break;
		case R.id.img_back:
			finish();
			break;
		case R.id.layou_submit:
			submit();
			break;
		default:
			break;
		}
		
	}
	
	private void submit(){
		HashMap<String, Object> json = new HashMap<String, Object>();
		if(intPro > 0 ){
			json.put("PROV", intPro);
		}
		if(intCity > 0){
			json.put("CITY", intCity);
		}
		if(intCount > 0){
			json.put("COUNTY", intCount);
		}
		util.show();
		String params =  ParamsUtil.getUrlParamsByMap(json);
		System.out.println("json-pre:" +params);
		MyRequest<Object> req = new MyRequest<Object>(
				Method.POST,
				null,
				UrlUtils.URL_SERVICE_RANGE,
				new Listener<Object>() {

					@Override
					public void onResponse(Object arg0) {
						if(TextUtils.isEmpty(arg0 +"")){
							ToastUtil.show(ServiceRangeActivity.this, "访问失败，请重试");
							util.dismiss();
						}else{
							JSONObject json = JSONObject.parseObject("" + arg0);
							if(json.containsKey("body")){
								reusltLayout.setVisibility(View.VISIBLE);
								String str = json.getString("body");
								if(intCity <= 0 && intCount <= 0&& intPro <= 0){
									requestString.setText("全国");
								}else{
									requestString.setText(tv.getText());
								}
								
								if(str.contains("不支持收送")){
									resutl.setText("不支持收送");
								}else if(str.contains("出错")){
									resutl.setText("揽收范围查询接口出错");
								}else{
									JSONObject obj = json.getJSONObject("body");
									StringBuffer sb = new StringBuffer();
									for(String key: obj.keySet()){
										sb.append(key).append(":").append(obj.getString(key)).append("\n");
									}
									resutl.setText(sb.toString());
								}
							}else{
								ToastUtil.show(ServiceRangeActivity.this, "数据获取失败");
								util.dismiss();
							}
						}
						util.dismiss();
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						Toast.makeText(ServiceRangeActivity.this, "数据加载失败", Toast.LENGTH_LONG).show();
						arg0.printStackTrace();
						util.dismiss();
					}
				}, params);
		App.getQueue().add(req);
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ServiceRangeSelectActivity.REQUEST_CODE_GET_ADRRESS) {
			if (resultCode == RESULT_OK) {
				City pro = (City)data.getSerializableExtra(ServiceRangeSelectActivity.CITY_PRO);
				City city = (City)data.getSerializableExtra(ServiceRangeSelectActivity.CITY_CITY);
				City county = (City)data.getSerializableExtra(ServiceRangeSelectActivity.CITY_COUNTY);
//				ToastUtil.show(this, "数据：" + pro+":" + county+ ":" + city);
				String str = "";
				if(pro != null){
					Log.e("ajh", "pro.getName(): " + pro.getName());
					str+= pro.getName()+" ";
					intPro = pro.getCode();
				}
				if(city != null){
					Log.e("ajh", "city.getName(): " + city.getName());
					str+= city.getName()+"\t";
					intCity = city.getCode();
				}
				if(county != null){
					Log.e("ajh", "county.getName(): " + county.getName());
					str+= county.getName();
					intCount = county.getCode();
				}
				
				tv.setText(str);
				
			}else{
				tv.setText("请选择原寄地址");
//				ToastUtil.show(this, "取消了");
				intPro = 0;
				intCity = 0;
				intCity = 0;
			}
		}
	}
}

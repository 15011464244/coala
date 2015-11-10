package com.ems.express.ui.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.bean.ExpressRecordBean;
import com.ems.express.net.MyRequest;
import com.ems.express.ui.settle.CommentActivity;
import com.ems.express.util.AnimationUtil;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.ToastUtil;
import com.google.gson.JsonSyntaxException;

/**
 * 查询结果
 */
public class ResultActivity extends Activity implements OnClickListener {
	private ImageView mIvBack;// 返回键
	private ImageView mIvIcon;// icon
	private TextView mTvNumber;// 单号
	private ListView mLvRecord;// 查件记录列表
	private Context mContext;
	
	private AnimationUtil util;
	
	private TextView mTvComment;//评价
	static Intent intent = null;

	public static void actionStart(Context context, String mailNumber) {
		intent = new Intent(context, ResultActivity.class);
		intent.putExtra("MAIL_NUMBER", mailNumber);
		context.startActivity(intent);
	}
	
	/**
	 * 
	 * @param context
	 * @param mailNumber
	 * @param state 判断订单的状态
	 */
	public static void actionStart(Context context, String mailNumber,String state) {
		intent = new Intent(context, ResultActivity.class);
		intent.putExtra("MAIL_NUMBER", mailNumber);
		intent.putExtra("mailState",state);
		context.startActivity(intent);
	}

	private ResultAdapter mAdapter;
	private List<ExpressRecordBean> mData = new ArrayList<ExpressRecordBean>();
	private String mMailNumber;

	public static void startAction(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, ResultActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		mContext = this;
		Intent intent = getIntent();
		mMailNumber = intent.getStringExtra("MAIL_NUMBER");
		initView();
		loadData();
	}

	public void initView() {
		mIvBack = (ImageView) findViewById(R.id.back_button);
		mIvIcon = (ImageView) findViewById(R.id.icon);
		mTvNumber = (TextView) findViewById(R.id.number);
		mLvRecord = (ListView) findViewById(R.id.list);
		
		mTvComment = (TextView) findViewById(R.id.tv_to_comment);
		mTvNumber.setText("单号: " + mMailNumber);
		//菊花
		util = new AnimationUtil(mContext, R.style.dialog_animation);
		mIvBack.setOnClickListener(this);
		mIvIcon.setOnClickListener(this);
		mTvNumber.setOnClickListener(this);
		mTvComment.setOnClickListener(this);
		
		//评价按钮显现
		intent = getIntent();
		String mailState = intent.getStringExtra("mailState");
		if("4".equals(mailState)){
			mTvComment.setVisibility(View.VISIBLE);
		}

	}
	
	

	public void loadData() {
		//开启菊花
		util.show();
		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("expressNo", mMailNumber);
		String params = ParamsUtil.getUrlParamsByMap(json);
		System.out.println("json-pre:" + params);
		MyRequest<Object> req = new MyRequest<Object>(Method.POST, null,
				"http://211.156.220.129/micro-channel-service/UtilsAction/queryParcel", new Listener<Object>() {
					@Override
					public void onResponse(Object resp) {
						System.out.println("obj:" + resp);
						if (resp == null || resp.toString().isEmpty()) {
							ToastUtil.show(mContext, "提交失败，请稍后重试");
							//结束菊花
							util.dismiss();
							return;
						}
						try {
							JSONObject jso = new JSONObject(resp.toString());
							if ("1".equals(jso.get("result"))) {
								JSONArray info = jso.getJSONObject("body").getJSONArray("success").getJSONObject(0).getJSONArray("traces");
								if(info.length() == 0){
									ToastUtil.show(mContext, "未查询到该运单记录");
									//结束菊花
									util.dismiss();
									return;
								}
								for (int i = 0; i < info.length(); i++) {
									ExpressRecordBean bean = new ExpressRecordBean("3535", info.getJSONObject(i)
											.getString("source"), "110", info.getJSONObject(i).getString("acceptTime"),
											"1", "北京", "2", info.getJSONObject(i).getString("acceptAddress"), info
													.getJSONObject(i).getString("remark"), "13889877344");
									mData.add(bean);
								}
								mAdapter = new ResultAdapter(ResultActivity.this, mData);
								mLvRecord.setAdapter(mAdapter);
							} else {
								ToastUtil.show(mContext, "提交失败，请稍后重试");
								//结束菊花
								util.dismiss();
							}
						} catch (JsonSyntaxException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						}
						//结束菊花
						util.dismiss();
					}
					
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						arg0.printStackTrace();
						//结束菊花
						util.dismiss();

					}
				}, params);

		RequestQueue queue = App.getQueue();
		queue.add(req);
		queue.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
			finish();
			break;
		case R.id.icon:
			break;
		case R.id.number:
			break;
		case R.id.tv_to_comment:
			intent.setClass(ResultActivity.this, CommentActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}

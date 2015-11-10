package com.koala.emm.app;

import im.fir.sdk.FIR;

import java.util.List;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;
import com.arvin.koalapush.bean.MsgContentBean;
import com.arvin.koalapush.bean.ReceivedMsgBean;
import com.arvin.koalapush.constant.KpushConstant;
import com.arvin.koalapush.net.MyRequest;
import com.arvin.koalapush.net.resp.OfflineMessageBean;
import com.arvin.koalapush.net.resp.OfflineMessageRep;
import com.arvin.koalapush.potter.Kpush;
import com.arvin.koalapush.potter.ReceivedListener;
import com.arvin.koalapush.util.LogUtil;
import com.arvin.koalapush.util.ToastUtil;
import com.baidu.mapapi.SDKInitializer;
//import com.koala.emm.model.OfflineMessageRep;
import com.koala.emm.util.DBHelper;
import com.koala.emm.util.SpfsUtil;
import com.lidroid.xutils.util.LogUtils;

import android.app.Application;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class EmmApplication extends Application {

	public static Kpush push = null;
	private MsgContentBean entity;
	public static RequestQueue sQueue;
	public static EmmApplication instance;
	
	public static DBHelper dbHelper;
	public static SQLiteDatabase db;

	@Override
	public void onCreate() {
//		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//				.detectAll().penaltyLog().build());
//		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll()
//				.penaltyLog().penaltyDeath().build());
		// bughd崩溃信息获取
		instance = this;
		FIR.init(this);
		super.onCreate();
		SDKInitializer.initialize(this);
		
		dbHelper = new DBHelper(this, "mdm.db");
		db = dbHelper.getWritableDatabase();
		
		sQueue = Volley.newRequestQueue(getApplicationContext());
		sQueue.start();
		push = Kpush.getInstence().create(getApplicationContext())
				.showLog(true)/* .showToast(true) */.setTimeout(30)
				.setRecoverTimes(5);
		push.setListener(new ReceivedListener() {

			@Override
			public String onReceived(Object arg0) {
				SpfsUtil.setClientId(push.getClientId());
				if ("SDK初始化成功".equals(arg0.toString())) {
					//查询为成功推送
//					getMsg(SpfsUtil.getClientId(), "4");
					
					LogUtils.e(arg0.toString());
				} else {
					Log.e("接收到的一条推送消息:  ", "" + arg0.toString());
					List<OfflineMessageBean> msgList = (List<OfflineMessageBean>) arg0;
//					ReceivedMsgBean bean = (ReceivedMsgBean) arg0;
//					entity = bean.getMessageEntity();
//					if ("4".equals(entity.getMessageType())) {
//						getMsg(entity.getTarget(), entity.getMessageType());
//					}
					handleMsgList(msgList);
				}
				return "noIsSync";
			}

			@Override
			public void onError(Object arg0) {

			}
		});
		SpfsUtil.init(getApplicationContext());
	}
	
	public static EmmApplication getInstance(){
		return instance;
	}

	public static RequestQueue VolleyQueue() {
		if (sQueue == null) {
			LogUtil.v("VolleyQueue init not finished return null");
			return null;
		}
		return sQueue;
	}
	
	/**
	 * 处理消息列表
	 * @param msgList
	 */
	public void handleMsgList(List<OfflineMessageBean> msgList){
		if (null != msgList && msgList.size() > 0) {
			for (OfflineMessageBean var : msgList) {
				if ("4".equals(var.getMessage_type())) {
					Intent intent = new Intent();
					intent.setAction("MDM");
					intent.putExtra("relust",var.getContent());
					sendBroadcast(intent);
				}
			}
		} else {
			LogUtils.e("推送信息列表为空");
		}
	}

	public void getMsg(String clientId, final String msgType) {
		JSONObject json = new JSONObject();
		try {
			json.put("client_id", clientId);
			json.put("device_type", msgType);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		MyRequest<OfflineMessageRep> request = new MyRequest<OfflineMessageRep>(
				Method.POST, OfflineMessageRep.class, KpushConstant.getMsg,
				new Listener<OfflineMessageRep>() {

					@Override
					public void onResponse(OfflineMessageRep response) {
						LogUtils.e(response+"");
						List<OfflineMessageBean> msgList = response
								.getOfflineMsgList();

						if (null != msgList && msgList.size() > 0) {
							int type = Integer.parseInt(msgType);
							int len = msgList.size();
							if (len > 0) {
								switch (type) {
								case 4:// MDM
									for (OfflineMessageBean var : msgList) {
										if ("4".equals(var.getMessage_type())) {
											Intent intent = new Intent();
											intent.setAction("MDM");
											intent.putExtra("relust",var.getContent());
											sendBroadcast(intent);
										}
									}
									break;
								}
							}
						} else {
							LogUtils.e("推送信息列表为空");
						}
					}
				}, new com.android.volley.Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
						ToastUtil.show(getApplicationContext(), "消息获取失败！");
					}
				}, json.toString());

		sQueue.add(request);
	}
	
	

}

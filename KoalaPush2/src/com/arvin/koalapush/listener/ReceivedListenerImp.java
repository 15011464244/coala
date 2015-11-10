package com.arvin.koalapush.listener;

import android.content.Context;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;
import com.arvin.koalapush.bean.ReceivedMsgBean;
import com.arvin.koalapush.constant.KpushConstant;
import com.arvin.koalapush.net.MyRequest;
import com.arvin.koalapush.util.LogUtil;

public class ReceivedListenerImp implements ReceivedListener{
	private Context mContext;
	public ReceivedListenerImp(Context mContext){
		this.mContext = mContext;
	}

	@Override
	public String onReceived(Object response) {
		LogUtil.e("response:" + response.toString());
		if ("SDK初始化成功".equals(String.valueOf(response))) {
			return "noIsSync";
		} else {
			ReceivedMsgBean rmb = (ReceivedMsgBean) response;
			JSONObject object = new JSONObject();
			try {
				object.put("client_id", rmb.getMessageEntity()
						.getTarget());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			MyRequest<Object> req = new MyRequest<Object>(
					Method.POST, Object.class,
					KpushConstant.getMsg, new Listener<Object>() {

						@Override
						public void onResponse(Object arg0) {
							LogUtil.e("messageList："
									+ arg0.toString());
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {

						}
					}, object.toString());
			RequestQueue queue = Volley
					.newRequestQueue(mContext);
			queue.add(req);
			queue.start();
			
			return "noIsSync";
		}
	}

	@Override
	public void onError(Object message) {
		LogUtil.print("onError");
		if(message != null){
			LogUtil.print( ((ReceivedMsgBean)message).toString() + "接收的消息有误！");
		}else{
			LogUtil.print( "接收的消息为空！");
		}
		
	}
}

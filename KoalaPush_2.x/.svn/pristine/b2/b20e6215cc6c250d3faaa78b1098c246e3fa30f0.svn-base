package com.arvin.koalapush.receiver;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;
import com.arvin.koalapush.api.KpushConfig;
import com.arvin.koalapush.constant.KpushConstant;
import com.arvin.koalapush.net.MyRequest;
import com.arvin.koalapush.net.resp.OfflineMessageRep;
import com.arvin.koalapush.potter.PushService;
import com.arvin.koalapush.util.Log4jUtil;
import com.arvin.koalapush.util.LogUtil;
import com.arvin.koalapush.util.SendMail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PushAlarmReceiver extends BroadcastReceiver {

	private static Log4jUtil logger = Log4jUtil.getLogger(PushAlarmReceiver.class.getName());
    @Override
    public void onReceive(Context context, Intent intent) {
//    	LogUtil.e("AlarmReceiver");
    	String type = intent.getAction();
    	if(type.equals("com.amaker.ch08.app.action.BC_ACTION")){
    		if(KpushConfig.dbHelper==null){
//    			LogUtil.e("dbHelper为null");
    			return;
    		}
    		if(PushService.isNetworkAvailable(context)){
//    			String obj = KpushConfig.dbHelper.queryRequest(KpushConfig.dbHelper.getReadableDatabase());
//    			if(obj!=null){
//    				MyRequest<OfflineMessageRep> request = new MyRequest<OfflineMessageRep>(
//    						Method.POST, OfflineMessageRep.class, KpushConstant.savePushLog,
//    						new Listener<OfflineMessageRep>() {
//    							@Override
//    							public void onResponse(OfflineMessageRep arg0) {
//    								logger.info("AlarmReceiver_request  "+JSONObject.toJSONString(arg0));
//    								if("0".equals(arg0.getResCode())){
//    									logger.info("AlarmReceiver_request_success");
//    									//请求成功
//    									KpushConfig.dbHelper.deleteRequestById(KpushConfig.dbHelper.getWritableDatabase());
//    								}else{
//    									//请求失败
//    									logger.error("AlarmReceiver_request_error");
//    								}
//    								
//    							}
//    						}, new ErrorListener() {
//    							
//    							@Override
//    							public void onErrorResponse(VolleyError arg0) {
//    								logger.error("AlarmReceiver_request_error",arg0);
//    							}
//    						}, obj);
//    				if(null != PushService.sQueue){
//    					PushService.sQueue.add(request);
//    				}
//    				
//    			}else{
//    				logger.info("本地没有日志");
//    			}
    			SendMail.send();
    		}else{
    			logger.info("定时器，没有网络连接");
    		}
    	}
    }
}
//package com.arvin.koalapush;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//
//import com.alibaba.fastjson.JSONException;
//import com.alibaba.fastjson.JSONObject;
//import com.android.volley.Request.Method;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response.ErrorListener;
//import com.android.volley.Response.Listener;
//import com.android.volley.toolbox.Volley;
//import com.android.volley.VolleyError;
//import com.arvin.koalapush.bean.ReceivedMsgBean;
//import com.arvin.koalapush.constant.KpushConstant;
//import com.arvin.koalapush.net.MyRequest;
//import com.arvin.koalapush.potter.Kpush;
//import com.arvin.koalapush.potter.ReceivedListener;
//import com.arvin.koalapush.util.LogUtil;
//
//public class MainActivity extends Activity implements OnClickListener {
//
//	private Context mContext = MainActivity.this;
//	private Button mBtnInit;
//	private EditText mEdtContent,mClientId;
//	private Button mBtnSend;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		mBtnInit = (Button) findViewById(R.id.init);
//		mEdtContent = (EditText) findViewById(R.id.content);
//		mBtnSend = (Button) findViewById(R.id.send);
//		mClientId = (EditText) findViewById(R.id.clientId);
//		findViewById(R.id.close).setOnClickListener(this);
//		findViewById(R.id.recover).setOnClickListener(this);
//		mBtnInit.setOnClickListener(this);
//		mBtnSend.setOnClickListener(this);
//
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.init:
//			LogUtil.print("init");
//			// Kpush.setAppKey("805eb03b1346306b9e303f855949925d");
//			Kpush push = Kpush.getInstence()
//					.create(mContext)
//					.showLog(true)
//					.showToast(true)
//					.setTimeout(30)
//					.setRecoverTimes(5)
////					.setAppKey("805eb03b1346306b9e303f855949925d")
//					.close(false);
//			mClientId.setText(push.getClientId());
//			ReceivedListener listener = new ReceivedListener() {
//
//				@Override
//				public String onReceived(Object response) {
//					LogUtil.e("response:" + response.toString());
//					if ("SDK初始化成功".equals(String.valueOf(response))) {
//						return "noIsSync";
//					}else{
//						ReceivedMsgBean rmb = (ReceivedMsgBean) response;
//						JSONObject object = new JSONObject();
//						try {
//							object.put("client_id", rmb.getMessageEntity().getTarget());
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//						MyRequest<Object> req = new MyRequest<Object>(Method.POST, Object.class, KpushConstant.getMsg, new Listener<Object>() {
//							
//							@Override
//							public void onResponse(Object arg0) {
//								LogUtil.e("messageList："+arg0.toString());
//							}
//						}, new ErrorListener() {
//							
//							@Override
//							public void onErrorResponse(VolleyError arg0) {
//								
//							}
//						}, object.toString());
//						RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
//						queue.add(req);
//						queue.start();
//						return "noIsSync";
//					}
//				}
//
//				@Override
//				public void onError(Object message) {
//					LogUtil.print("onError");
//				}
//			};
//			push.setListener(listener);
//			
//			break;
//		case R.id.send:
//			break;
//		case R.id.close:
//			Kpush.getInstence().close(true);
//			break;
//		case R.id.recover:
//			Kpush.getInstence().recover(mContext);
//			break;
//		default:
//			break;
//		}
//	}
//
//
//}

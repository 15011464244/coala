package com.example.koalademo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.arvin.koalapush.bean.ReceivedMsgBean;
import com.arvin.koalapush.constant.KpushConstant;
import com.arvin.koalapush.net.MyRequest;
import com.arvin.koalapush.net.resp.BaseResp;
import com.arvin.koalapush.net.resp.OfflineMessageBean;
import com.arvin.koalapush.potter.Kpush;
import com.arvin.koalapush.potter.ReceivedListener;
import com.arvin.koalapush.util.LogUtil;
import com.arvin.koalapush.util.ToastUtil;
import com.example.koalademo.bean.ReceivedMsgResp;

public class MainActivity extends Activity implements OnClickListener {
	private Context mContext = MainActivity.this;
	private Button mBtnInit;
	private Button mBtnSend;
	private EditText mEdtContent;
	private EditText mEdtSource;
	private EditText mEdtTarget;
	private EditText mEdtNum;
	/** http服务 */
	protected static RequestQueue sQueue;
	private String client_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mBtnInit = (Button) findViewById(R.id.init);
		mEdtContent = (EditText) findViewById(R.id.content);
		mEdtSource = (EditText) findViewById(R.id.source);
		mEdtTarget = (EditText) findViewById(R.id.target);
		mBtnSend = (Button) findViewById(R.id.send);
		mEdtNum = (EditText) findViewById(R.id.target_num);
		mBtnInit.setOnClickListener(this);
		mBtnSend.setOnClickListener(this);

		sQueue = Volley.newRequestQueue(mContext);
		sQueue.start();
	}

	AtomicInteger mCount = new AtomicInteger();

	@Override
	protected void onDestroy() {
		super.onDestroy();

		Kpush.getInstence().close(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.init:

			final Kpush push = Kpush.getInstence().create(mContext)
					.showLog(true).setTimeout(30).setRecoverTimes(5);
			ReceivedListener listener = new ReceivedListener() {

				@Override
				public String onReceived(Object arg0) {
					if ("SDK初始化成功".equals(arg0.toString())) {
						mEdtContent.setText("SDK初始化成功");
						return "noIsSync";
					}
					
					List<OfflineMessageBean> list = (List<OfflineMessageBean>) arg0;
					for (OfflineMessageBean var : list) {
//						AlertDialog.Builder builder = new Builder(mContext);
//						builder.setMessage(JSONObject.toJSONString(var));
//						builder.setTitle("推送");
//						builder.setPositiveButton("确定", null);
//						builder.setCancelable(false);
//						builder.create().show();
//						;
						mCount.getAndIncrement();
						mEdtContent.setText("---" + mCount.get());
						mEdtSource.setText(push.getClientId());
						client_id+=var.getPkid()+";";
					}
					System.out.println("所有的client_id"+client_id);
					// if ("SDK初始化成功".equals(arg0.toString())) {
					// mEdtContent.setText(arg0.toString());
					// } else {
					// System.out.println("received666:" + arg0.toString());
					// ReceivedMsgBean received = (ReceivedMsgBean) arg0;
					// ToastUtil.show(mContext, arg0.toString());
					// mEdtContent.setText(received.getMessageEntity()
					// .getUrl() + "---" + mCount.get());
					// getMsg(received.getMessageEntity().getTarget(),
					// received.getMessageEntity().getMessageType());
					//
					// NotificationManager mNotificationManager =
					// (NotificationManager)
					// getSystemService(NOTIFICATION_SERVICE);
					// NotificationCompat.Builder mBuilder = new
					// NotificationCompat.Builder(
					// MainActivity.this);
					//
					// mBuilder.setContentTitle("KoalaPush")
					// .setTicker("您有新的消息了")
					// .setWhen(System.currentTimeMillis())
					// // .setPriority(Notification.PRIORITY_DEFAULT)
					// .setAutoCancel(true)
					// .setDefaults(Notification.DEFAULT_VIBRATE)
					// .setContentText(
					// received.getMessageEntity().getUrl())
					// .setSmallIcon(R.drawable.koala_push_show_icon);
					//
					// mNotificationManager.cancelAll();
					// mNotificationManager.notify(mCount.get(),
					// mBuilder.build());
					//
					// }
					return "";
				}

				@Override
				public void onError(Object arg0) {
					System.out.println("Error");

				}

//				@Override
//				public void onConnection(String message,String date) {
//					
//				}
//
//				@Override
//				public void onClose(String message,String date) {
//					
//				}
			};
			push.setListener(listener);

			break;

		case R.id.send:

			Kpush.getInstence().close(true);

			// if (!"".equals(mEdtSource.getText().toString())
			// && !"".equals(mEdtTarget.getText().toString())
			// && !"".equals(mEdtContent.getText().toString())) {
			// int ii = 1;
			// try {
			// ii = Integer.valueOf(mEdtNum.getText().toString());
			//
			// } catch (Exception e) {
			// ii = 1;
			// }
			//
			// for (int i = 0; i < ii; i++) {
			// sendMsg();
			// }
			// } else {
			// ToastUtil.show(mContext, "消息内容，发送方，接收方不能为空");
			// }

			break;
		default:
			break;
		}
	}

	public void sendMsg() {
		JSONObject json = new JSONObject();
		try {
			json.put("source", mEdtSource.getText().toString());
			json.put("target", mEdtTarget.getText().toString());
			json.put("message_type", "0");
			json.put("url", mEdtContent.getText().toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}
		MyRequest<BaseResp> request = new MyRequest<BaseResp>(Method.POST,
				BaseResp.class, KpushConstant.send, new Listener<BaseResp>() {

					@Override
					public void onResponse(BaseResp response) {
						LogUtil.d(response.toString());
						ToastUtil.show(mContext, "发送成功！");
					}
				}, new com.android.volley.Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
						ToastUtil.show(mContext, "发送失败！");
					}
				}, json.toString());

		sQueue.add(request);
	}

	public void getMsg(String clientId, String msgType) {
		System.out.println("getMsg");
		JSONObject json = new JSONObject();
		try {
			json.put("client_id", clientId);
			json.put("device_type", "0");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		MyRequest<ReceivedMsgResp> request = new MyRequest<ReceivedMsgResp>(
				Method.POST, ReceivedMsgResp.class, KpushConstant.getMsg,
				new Listener<ReceivedMsgResp>() {

					@Override
					public void onResponse(ReceivedMsgResp response) {
						System.out.println("ReceivedMsgResp:"
								+ response.toString());
						// ToastUtil.show(mContext, "消息获取成功！");
					}
				}, new com.android.volley.Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
						ToastUtil.show(mContext, "消息获取失败！");
					}
				}, json.toString());

		sQueue.add(request);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  
			System.exit(0);
            return true;  
        } else {
        	return super.onKeyDown(keyCode, event);
        }
	}
}

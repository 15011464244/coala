package com.example.koalademo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
import com.arvin.koalapush.potter.Kpush;
import com.arvin.koalapush.potter.ReceivedListener;
import com.arvin.koalapush.util.ToastUtil;
import com.example.koalademo.bean.ReceivedMsgResp;

public class MainActivity extends Activity implements OnClickListener {
	private Context mContext = MainActivity.this;
	private Button mBtnInit;
	private Button mBtnSend;
	private EditText mEdtContent;
	private EditText mEdtSource;
	private EditText mEdtTarget;
	/** http服务 */
	protected static RequestQueue sQueue;
	protected static int count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mBtnInit = (Button) findViewById(R.id.init);
		mEdtContent = (EditText) findViewById(R.id.content);
		mEdtSource = (EditText) findViewById(R.id.source);
		mEdtTarget = (EditText) findViewById(R.id.target);
		mBtnSend = (Button) findViewById(R.id.send);

		mBtnInit.setOnClickListener(this);
		mBtnSend.setOnClickListener(this);

		sQueue = Volley.newRequestQueue(mContext);
		sQueue.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.init:

			final Kpush push = Kpush.getInstence()
				.create(mContext)
				.showLog(true)
				.showToast(true)
				.setTimeout(30)
				.setRecoverTimes(5)
				.close(false);

			ReceivedListener listener = new ReceivedListener() {

				@Override
				public void onReceived(Object arg0) {
					mEdtSource.setText(push.getClientId());
					if ("SDK初始化成功".equals(arg0.toString())) {
						mEdtContent.setText(arg0.toString());
					} else {
						System.out.println("received666:" + arg0.toString());
						ReceivedMsgBean received = (ReceivedMsgBean) arg0;
						ToastUtil.show(mContext, arg0.toString());
						getMsg(received.getMessageEntity().getTarget(),
								received.getMessageEntity().getMessageType());
					}
				}

				@Override
				public void onError(Object arg0) {
					System.out.println("Error");

				}
			};
			push.setListener(listener);
			break;

		case R.id.send:
			if (!"".equals(mEdtSource.getText().toString())
					&& !"".equals(mEdtTarget.getText().toString())
					&& !"".equals(mEdtContent.getText().toString())) {
				sendMsg();

			} else {
				ToastUtil.show(mContext, "消息内容，发送方，接收方不能为空");
			}

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
			json.put("message_type", "1");
			json.put("url", mEdtContent.getText().toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}
		MyRequest<BaseResp> request = new MyRequest<BaseResp>(Method.POST,
				BaseResp.class, KpushConstant.send, new Listener<BaseResp>() {

					@Override
					public void onResponse(BaseResp response) {
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
}

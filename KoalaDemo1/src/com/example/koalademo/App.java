package com.example.koalademo;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.arvin.koalapush.net.resp.OfflineMessageBean;
import com.arvin.koalapush.potter.Kpush;
import com.arvin.koalapush.potter.ReceivedListener;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class App extends Application {

	private Context mContext;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
		final Kpush push = Kpush.getInstence().showLog(true).setTimeout(30)
				.setRecoverTimes(5);
		ReceivedListener listener = new ReceivedListener() {

			@Override
			public String onReceived(Object paramObject) {
				if ("SDK初始化成功".equals(paramObject.toString())) {
					Toast.makeText(mContext, "SDK初始化成功", 1).show();
					// mEdtContent.setText("SDK初始化成功");
					return "noIsSync";
				}

				List<OfflineMessageBean> list = (List<OfflineMessageBean>) paramObject;
				for (OfflineMessageBean var : list) {
					Toast.makeText(mContext, JSONObject.toJSONString(var), 1).show();
//					AlertDialog.Builder builder = new Builder(mContext);
//					builder.setMessage(JSONObject.toJSONString(var));
//					builder.setTitle("推送");
//					builder.setPositiveButton("确定", null);
//					builder.setCancelable(false);
//					builder.create().show();
				}
				return null;
			}

			@Override
			public void onError(Object paramObject) {
				// TODO Auto-generated method stub

			}

		};
		push.setListener(listener);
		push.create(mContext);
	}
}

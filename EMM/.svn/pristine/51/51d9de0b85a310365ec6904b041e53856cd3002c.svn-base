package com.koala.emm.supervision;

import com.lidroid.xutils.util.LogUtils;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.widget.Toast;

public class NetworkStateService extends Service {

	private ConnectivityManager connectivityManager;
	private NetworkInfo info;

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				Toast.makeText(context, "网络状态已经改变", Toast.LENGTH_SHORT).show();
				LogUtils.e("网络状态已经改变");
				connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				info = connectivityManager.getActiveNetworkInfo();
				if (info != null && info.isAvailable()) {
					if (info.getType() == ConnectivityManager.TYPE_WIFI) {
						Toast.makeText(context, "已经切换到WIFI网络，请放心使用",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(context, "已经切换到2G/3G/4G网络，请注意您的流量哦",
								Toast.LENGTH_SHORT).show();
					}
//					String name = info.getTypeName();
//					Toast.makeText(context, "当前网络名称：" + name,
//							Toast.LENGTH_SHORT).show();
//					LogUtils.e("当前网络名称：" + name);
					// 已经切换到2G/3G/4G网络，请注意您的流量哦
				} else {
					Toast.makeText(context, "没有可用网络", Toast.LENGTH_SHORT)
							.show();
					LogUtils.e("没有可用网络");
				}
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, mFilter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

}

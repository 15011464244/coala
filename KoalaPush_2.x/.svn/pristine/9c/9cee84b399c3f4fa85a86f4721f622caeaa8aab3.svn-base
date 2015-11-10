package com.arvin.koalapush.potter;

import com.arvin.koalapush.util.Log4jUtil;
import com.arvin.koalapush.util.LogUtil;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {

	private static Log4jUtil logger = Log4jUtil.getLogger(MyReceiver.class.getName());
	
	@Override
	public void onReceive(Context context, Intent intent) {
		LogUtil.i("Broadcast:" + intent.toString());
		logger.info("Broadcast:" + intent.toString());
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Intent startServiceIntent = new Intent(context, PushService.class);
			startServiceIntent
					.setAction("com.example.messengerservice.MessengerService");
			startServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(startServiceIntent);
		}
	}
}

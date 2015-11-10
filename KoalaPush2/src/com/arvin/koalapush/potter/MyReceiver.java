package com.arvin.koalapush.potter;

import com.arvin.koalapush.util.LogUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		LogUtil.print("Broadcast:" + intent.toString());
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Intent startServiceIntent = new Intent(context, PushService.class);
			startServiceIntent
					.setAction("com.example.messengerservice.MessengerService");
			startServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(startServiceIntent);
		}
	}
}

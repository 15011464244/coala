package com.cn.net.cnpl;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Toast;

public class MyAlarmReceive  extends  BroadcastReceiver  {

	@Override
	public void onReceive(Context context, Intent intent) {
//		Toast.makeText(context, "qqqqqqqqq", 1000).show();
		Long triggerAtTime = SystemClock.elapsedRealtime() +   1000;//1秒钟后
//		int interval =1000 * 60;
		int requestCode = 121;
		Intent newintent = new Intent(context,  MailFollowUploadService.class); 
		PendingIntent pendIntent = PendingIntent.getService(context, requestCode, newintent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmMgr = (AlarmManager) context
				.getSystemService(context.ALARM_SERVICE);
		//1小时一次  interval
		alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime,1000*60*60,
				pendIntent);
		
	}

}

package com.ems.express.util;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Fragment;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

import com.ems.express.R;
import com.ems.express.fragment.home.HomeIndexFragment;
import com.ems.express.ui.chat.ChatListActivity;
import com.ems.express.ui.message.receive.ReceiveMailActivity;
import com.ems.express.ui.message.send.SendMessageActivity;
import com.ems.express.ui.message.send.SendMessageListActivity;

public class NotificationUtil {

	public static final String ACTION_BRING_TO_FRONT = "neal.pushtest.action.BringToFront";

	private static NotificationUtil NotificationMessage = null;

	public static NotificationUtil getNotificationUtil() {
		if (NotificationMessage == null) {
			NotificationMessage = new NotificationUtil();
		}
		return NotificationMessage;
	}

	public void setNotification(Context context, String type) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);

		Notification notification = new Notification(R.drawable.ic_launcher,
				"快递帮手", System.currentTimeMillis());
		if (SpfsUtil.loadShake()) {
			// 获取系统震动声
			// notification.defaults |= Notification.DEFAULT_VIBRATE;
			// 自定义震动
			long[] vibrate = { 0, 150, 150, 200 };
			notification.vibrate = vibrate;
		} else {
			notification.vibrate = null;
		}
		if (SpfsUtil.loadTone()) {
			// 获取系统提示音
			notification.sound = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		} else {
			notification.sound = null;
		}

		// 点击通知栏是否销毁
		notification.flags = Notification.FLAG_AUTO_CANCEL;

		CharSequence contentTitle = "快递帮手";
		CharSequence contentText = "";
		Intent notificationIntent = null;
		//收件消息
		if (type.equals("1")) {
			contentText = "您有一个邮件预计2小时内到达！";
			notificationIntent = new Intent(context, ReceiveMailActivity.class);
		}
		//寄件消息：收到寄件消息
		if (type.equals("2")) {
			contentText = "您的寄件信息快递员已收到！";
//			notificationIntent = new Intent(context, SendMessageActivity.class);
			notificationIntent = new Intent(context, SendMessageListActivity.class);
		}
		//寄件消息：已妥投
		if (type.equals("4")) {
			contentText = "您的寄件已经成功送达！";
			notificationIntent = new Intent(context, SendMessageListActivity.class);
		}
		if (type.equals("3")) {
			contentText = "您有一条消息未读哦!";
			notificationIntent = new Intent(context, ChatListActivity.class);
//			notificationIntent = new Intent(context, ReceiveMailActivity.class);
		}
		if (type.equals("5")) {
			contentText = "您的寄件已揽收成功，正火速送达目的地！";
			notificationIntent = new Intent(context, SendMessageListActivity.class);
		}
		if (type.equals("6")) {
			contentText = "您的小伙伴将受邀请号码填写为您的手机号，您获取了积分";
			notificationIntent = new Intent(context, SendMessageListActivity.class);
		}
		if (type.equals("7")) {
			contentText = "您有未处理的收费订单，请处理";
			notificationIntent = new Intent(context, SendMessageListActivity.class);
		}

		notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PendingIntent contentItent = PendingIntent.getActivity(context, 0,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentItent);

		// 把Notification传递给NotificationManager
		notificationManager.notify(0, notification);
		//改变首页红圈
		if(HomeIndexFragment.indexFragment != null && HomeIndexFragment.indexFragment.isVisible()){
			Fragment fragment = HomeIndexFragment.indexFragment;
			
		}
		
		
		if(isBackground(context)){
			PowerManager pm =(PowerManager) context.getSystemService(Context.POWER_SERVICE);
			WakeLock mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK|PowerManager.ACQUIRE_CAUSES_WAKEUP|PowerManager.ON_AFTER_RELEASE, "");
			mWakeLock.acquire(1000*5);
			mWakeLock.release();
			
		}
	}

	/**
	 * 判断应用是在前台运行还是后台运行
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isBackground(Context context) {
		KeyguardManager mKeyguardManager = (KeyguardManager) context
				.getSystemService(context.KEYGUARD_SERVICE);
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					Log.i("后台", appProcess.processName);
					return true;
				} else {
					Log.i("前台", appProcess.processName);
					if (!mKeyguardManager.inKeyguardRestrictedInputMode()) {
						return false;
					} else {
						return true;
					}
				}
			}
		}
		return false;
	}

	
	public static void cancelAll(){
		if(NotificationMessage!=null){
			NotificationMessage.cancelAll();
		}
	}
	
}


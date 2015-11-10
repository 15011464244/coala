package com.arvin.koalapush.api;

import com.arvin.koalapush.potter.PushService;
import com.arvin.koalapush.util.Log4jUtil;
import com.arvin.koalapush.util.LogUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetworkManager extends BroadcastReceiver {

	private static Log4jUtil logger = Log4jUtil.getLogger(NetworkManager.class
			.getName());

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connectMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = connectMgr.getActiveNetworkInfo();
		String action = intent.getAction();
		Intent intent1 = new Intent(context, PushService.class);
		Intent intent2 = context.getPackageManager().getLaunchIntentForPackage(
				context.getPackageName());
		intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			if (mNetworkInfo != null) {
				if (mNetworkInfo.isAvailable()) {
					logger.info("当前网络环境：" + getNetworkState(mNetworkInfo));
					context.startService(intent1);
					if (KpushConfig.mReceivedListener == null) {
						context.startActivity(intent2);
					}
					LogUtil.i("有网络连接");
					logger.info("有网络连接");

				} else {
					LogUtil.i("没有网络连接");
					logger.info("没有网络连接");
				}
			} else {
				LogUtil.i("没有网络连接");
				logger.info("没有网络连接");
			}
		} else if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
			context.startService(intent1);
			if (KpushConfig.mReceivedListener == null) {
				context.startActivity(intent2);
			}
		} else if (action.equals(Intent.ACTION_USER_PRESENT)) {
			context.startService(intent1);
			if (KpushConfig.mReceivedListener == null) {
				context.startActivity(intent2);
			}
		} else if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
			context.startService(intent1);
			if (KpushConfig.mReceivedListener == null) {
				context.startActivity(intent2);
			}
		}
	}

	public String getNetworkState(NetworkInfo mNetworkInfo) {
		String state = null;
		if (mNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			state = "WIFI";
		} else if (mNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			String strSubTypeName = mNetworkInfo.getSubtypeName();
			int networkType = mNetworkInfo.getSubtype();
			switch (networkType) {
			case TelephonyManager.NETWORK_TYPE_GPRS:
			case TelephonyManager.NETWORK_TYPE_EDGE:
			case TelephonyManager.NETWORK_TYPE_CDMA:
			case TelephonyManager.NETWORK_TYPE_1xRTT:
			case TelephonyManager.NETWORK_TYPE_IDEN: // api<8 : replace by 11
				state = "2G";
				break;
			case TelephonyManager.NETWORK_TYPE_UMTS:
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
			case TelephonyManager.NETWORK_TYPE_HSDPA:
			case TelephonyManager.NETWORK_TYPE_HSUPA:
			case TelephonyManager.NETWORK_TYPE_HSPA:
			case TelephonyManager.NETWORK_TYPE_EVDO_B: // api<9 : replace by 14
			case TelephonyManager.NETWORK_TYPE_EHRPD: // api<11 : replace by 12
			case TelephonyManager.NETWORK_TYPE_HSPAP: // api<13 : replace by 15
				state = "3G";
				break;
			case TelephonyManager.NETWORK_TYPE_LTE: // api<11 : replace by 13
				state = "4G";
				break;
			default:
				// http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
				if (strSubTypeName.equalsIgnoreCase("TD-SCDMA")
						|| strSubTypeName.equalsIgnoreCase("WCDMA")
						|| strSubTypeName.equalsIgnoreCase("CDMA2000")) {
					state = "3G";
				} else {
					state = strSubTypeName;
				}

				break;
			}
		}
		return state;
	}

}

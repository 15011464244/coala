package com.koala.emm.supervision;

import java.math.BigDecimal;

import com.koala.emm.R;
import com.koala.emm.tools.SharePreferenceUtilDaoFactory;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

public class TrafficService extends Service {
	private long total;
	private String traffic;
	private String sharetraffic;
	private Double double1;
	private SharePreferenceUtilDaoFactory shareDao;

	/**
	 * 将service设置为前台模式
	 */
	private void startForegroundCompat(int startId, Notification notification) {
		try {
			if (Build.VERSION.SDK_INT < 18) {
				startForeground(1120, new Notification());
			} else {
				startForeground(0, notification);
			}
		} catch (Exception e) {
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Notification notification = new Notification(R.drawable.ic_launcher,
				"wf update service is running", System.currentTimeMillis());
		PendingIntent pendingIntent = PendingIntent.getService(
				getApplicationContext(), 0, intent, 0);
		notification.setLatestEventInfo(getApplicationContext(), "E速递", "",
				pendingIntent);
		notification.flags = BIND_AUTO_CREATE;
		startForegroundCompat(startId, notification);
		// shareDao = new SharePreferenceUtilDaoFactory();
		if (TrafficMonitoring.getNetType()==1) {
		shareDao = SharePreferenceUtilDaoFactory
				.getInstance(getApplicationContext());
		if (shareDao != null) {
			if (!"".equals(shareDao.getTraffic())) {
				sharetraffic = shareDao.getTraffic();
				total = traffic_Monitoring();
				traffic = convertTraffic(total);
				double1 = (Double.parseDouble(traffic.toString().trim()) + Double
						.parseDouble(sharetraffic.toString().trim()));
				traffic = String.valueOf(double1);
//				if (double1 > 3.00) {
					Toast.makeText(getApplicationContext(),
							"目前消耗流量：" + double1, Toast.LENGTH_SHORT).show();
//				}
			} else {
				total = traffic_Monitoring();
				traffic = convertTraffic(total);
				Double double1 = Double.parseDouble(traffic.toString().trim());
				Toast.makeText(getApplicationContext(), double1 + "",
						Toast.LENGTH_SHORT).show();
			}
		}
		//启动计时器：
		handler.postDelayed(runnable, 10000);//每两秒执行一次runnable.
//		new Thread(new MyThread()).start();
		}
		return START_REDELIVER_INTENT;// 避免intent为空
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	// 服务停止后，将当前流量存入
	@Override
	public void onDestroy() {
		super.onDestroy();
		StringBuffer sb = new StringBuffer();
		sb.append(traffic);
		shareDao.setTraffic(sb.toString());
	}
 Handler handler=new Handler();
	Runnable runnable=new Runnable(){
	@Override
	public void run() {
	//要做的事情
		if (shareDao != null) {
			if (!"".equals(shareDao.getTraffic())) {
				sharetraffic = shareDao.getTraffic();
				total = traffic_Monitoring();
				traffic = convertTraffic(total);
				double1 = (Double.parseDouble(traffic.toString().trim()) + Double
						.parseDouble(sharetraffic.toString().trim()));
				traffic = String.valueOf(double1);
					Toast.makeText(getApplicationContext(),
							"目前消耗流量：" + double1, Toast.LENGTH_SHORT).show();
//					停止计时器：
//					handler.removeCallbacks(runnable);
			} else {
				total = traffic_Monitoring();
				traffic = convertTraffic(total);
				Double double1 = Double.parseDouble(traffic.toString().trim());
				Toast.makeText(getApplicationContext(), double1 + "",
						Toast.LENGTH_SHORT).show();
			}
		}
	handler.postDelayed(this, 10000);
	}
	};

//	Handler handler = new Handler() {
//		public void handleMessage(Message msg) {
//			if (shareDao != null) {
//				if (!"".equals(shareDao.getTraffic())) {
//					sharetraffic = shareDao.getTraffic();
//					total = traffic_Monitoring();
//					traffic = convertTraffic(total);
//					double1 = (Double.parseDouble(traffic.toString().trim()) + Double
//							.parseDouble(sharetraffic.toString().trim()));
//					traffic = String.valueOf(double1);
//					if (double1 > 3.00) {
//						Toast.makeText(getApplicationContext(),
//								"目前消耗流量：" + double1, Toast.LENGTH_SHORT).show();
//					}
//				} else {
//					total = traffic_Monitoring();
//					traffic = convertTraffic(total);
//					Double double1 = Double.parseDouble(traffic.toString().trim());
//					Toast.makeText(getApplicationContext(), double1 + "",
//							Toast.LENGTH_SHORT).show();
//				}
//			}
//			super.handleMessage(msg);
//		}
//	};
//	// 定时提示
//	public class MyThread implements Runnable {
//		@Override
//		public void run() {
//			while (true) {
//				try {
//					Thread.sleep(10000);// 线程暂停10秒，单位毫秒
//					Message message = new Message();
//					message.what = 1;
//					handler.sendMessage(message);// 发送消息
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
//	

	// 查询手机总流量
	public static long traffic_Monitoring() {
		long recive_Total = TrafficStats.getTotalRxBytes();
		long send_Total = TrafficStats.getTotalTxBytes();
		long total = recive_Total + send_Total;
		return total;
	}

	// 流量转化
	public static String convertTraffic(long traffic) {
		BigDecimal trafficKB;
		BigDecimal trafficMB;
		BigDecimal trafficGB;

		BigDecimal temp = new BigDecimal(traffic);
		BigDecimal divide = new BigDecimal(1000);
		trafficKB = temp.divide(divide, 2, 1);
		// if (trafficKB.compareTo(divide) > 0) {
		trafficMB = trafficKB.divide(divide, 2, 1);
		// if (trafficMB.compareTo(divide) > 0) {
		// trafficGB = trafficMB.divide(divide, 2, 1);
		// return trafficGB.doubleValue() + "GB";
		// return trafficGB.doubleValue() + "";
		// } else {
		// return trafficMB.doubleValue() + "MB";
		return trafficMB.doubleValue() + "";
		// }
		// } else {
		// return trafficKB.doubleValue() + "KB";
		// return trafficKB.doubleValue() + "";
		// }
	}

}

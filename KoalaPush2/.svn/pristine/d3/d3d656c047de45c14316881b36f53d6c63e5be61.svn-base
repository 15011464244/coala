package com.arvin.koalapush.potter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.sax.StartElementListener;

import com.arvin.koalapush.listener.ReceivedListener;
import com.arvin.koalapush.util.LogUtil;
import com.arvin.koalapush.util.SpfsUtil;
import com.arvin.koalapush.util.ToastUtil;

public class Kpush {
	private static Kpush instance = null;
	private Intent intent;

	private MyConnecter myCon = new MyConnecter();

	private IBindService myBinder;

	private Context mContext;

	private Kpush() {
	}

	static {
		instance = new Kpush();
	}

	public static Kpush getInstence() {
		return instance;
	}

	public PushService mPushService;

	/**
	 * 初始化
	 * 
	 * @param mContext
	 */
	public Kpush create(Context mContext) {
		this.mContext = mContext;
		LogUtil.print("Kpush init");
		intent = new Intent(mContext, PushService.class);
		// PushService.actionStart(mContext);
		SpfsUtil.init(mContext);
		if (mPushService == null) {
			System.out.println("开启");
			mContext.startService(intent);
			System.out.println("绑定");
			mContext.bindService(intent, myCon, 1);
		}
		return instance;
	}

	/**
	 * 是否打印错误日志
	 * 
	 * @param show
	 */
	public Kpush SetShowErrorLog(Boolean show) {
		SpfsUtil.setShowErrorLog(show);
		return instance;
	}

	/**
	 * 是否打印日志
	 * 
	 * @param show
	 */
	public Kpush showLog(Boolean show) {
		LogUtil.isDebug(show);
		return instance;
	}

	/**
	 * 是否toast
	 * 
	 * @param show
	 */
	public Kpush showToast(Boolean show) {
		ToastUtil.isToast(show);
		return instance;
	}

	// /*
	// * 设置AppKey
	// */
	// public Kpush setAppKey(String key) {
	// instance.mPushService.setAppKey(key);
	// return instance;
	// }

	/**
	 * 连接超时时间设置接口（单位：秒）
	 * 
	 * @param timeout
	 */
	public Kpush setTimeout(int timeout) {
		SpfsUtil.setTimeOut(timeout);
		return instance;
	}

	/**
	 * 断开连接接口
	 * 
	 * @param arg0
	 */
	public Kpush close(Boolean arg0) {
		if (arg0) {
			if (instance != null) {
				if (instance.mPushService != null) {
					if (instance.mPushService.getSession() != null) {
						LogUtil.e("Kpush.class close：长连接关闭中...");
						instance.mPushService.getSession().getService()
								.dispose();
					} else {
						LogUtil.e("Kpush.class close：长连接不存在无法关闭");
					}
				} else {
					LogUtil.e("Kpush.class close：尚未初始化");
				}
			} else {
				LogUtil.e("Kpush.class close：Kpush为null");
			}
		}
		return instance;
	}

	/**
	 * 设置重连次数
	 * 
	 * @param times
	 */
	public Kpush setRecoverTimes(int times) {
		SpfsUtil.setTimes(times);
		return instance;
	}

	/**
	 * 判断长连接是否存在
	 * 
	 * @return
	 */
	public Boolean isConnected() {
//		if (instance != null) {
			if (instance.mPushService != null) {
				if (instance.mPushService.isConnected()) {
					// LogUtil.e("Kpush.class isConnected：存在长连接");
					return true;
				} else {
					// LogUtil.e("Kpush.class isConnected：不存在长连接");
					return false;
				}
			} else {
				LogUtil.e("Kpush.class isConnected：尚未初始化");
				LogUtil.e("Kpush.class isConnected：初始化....");
				this.create(mContext);
				return true;
			}
//		} else {
//			// LogUtil.e("Kpush.class isConnected：不存在长连接");
//			return false;
//		}
	}
	/*
	 * 断线重连
	 */
	public void recover(Context mContext) {
		if (!isConnected()) {
			// mContext.stopService(new Intent(mContext, PushService.class));
			if (!instance.getInstence().isConnected()) {
				// 断线重连
				instance.mPushService.recoverServer();
			}
		}
	}

	/**
	 * 判断网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public boolean isNetworkAvailable(Context context) {
		ConnectivityManager mgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info = mgr.getAllNetworkInfo();
		if (info != null) {
			for (int i = 0; i < info.length; i++) {
				if (info[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 设置消息回调接口
	 * 
	 * @param listener
	 */
	public void setListener(ReceivedListener listener) {
		PushService.mReceivedListener = listener;
	}

	/**
	 * 获取唯一标识ClientId
	 * 
	 * @return
	 */
	public static String getClientId() {
		return SpfsUtil.getClientId();
	}

	/**
	 * 获取推送长连接session
	 * 
	 * @return
	 */
	// private IoSession getSession() {
	// return instance.mPushService.getSession();
	// }
	/*
	 * Connector
	 */
	class MyConnecter implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			LogUtil.e("获取代理成功");
			myBinder = (IBindService) service;
			mPushService = myBinder.getService();
			// 解绑
			mContext.unbindService(myCon);
			System.out.println("onServiceConnected " + mPushService);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			LogUtil.e("程序异常关闭");
		}

	}

}

package com.arvin.koalapush.potter;

import android.content.Context;
import android.content.Intent;
import com.arvin.koalapush.api.KpushConfig;
import com.arvin.koalapush.util.DBHelper;
import com.arvin.koalapush.util.Log4jConfig;
import com.arvin.koalapush.util.Log4jUtil;
import com.arvin.koalapush.util.LogUtil;
import com.arvin.koalapush.util.SpfsUtil;

public class Kpush {
	private static Kpush instance = null;
	private static Log4jUtil logger;

	private Kpush() {
	}

	static {
		instance = new Kpush();
	}

	public static Kpush getInstence() {
		return instance;
	}

	/**
	 * 初始化
	 * 
	 * @param mContext
	 */
	public Kpush create(Context mContext) {
		Log4jConfig.appName = mContext.getPackageName();
		logger  = Log4jUtil.getLogger(Kpush.class.getName());
		LogUtil.i("Kpush init");
		logger.info("Kpush init");
		KpushConfig.dbHelper = new DBHelper(mContext, "push.db");
		Intent intent = new Intent(mContext, PushService.class);
		mContext.startService(intent);
		return instance;
	}

	/**
	 * 是否打印日志
	 * 
	 * @param show
	 */
	public Kpush showLog(Boolean show) {
		LogUtil.isDebug(show);
		Log4jConfig.showLog = false;
		Log4jConfig.sendMail = false;
		return instance;
	}

	/**
	 * 连接超时时间设置接口（单位：秒）
	 * 
	 * @param timeout
	 */
	public Kpush setTimeout(int timeout) {
		KpushConfig.timeOut = timeout;
		return instance;
	}

	/**
	 * 断开连接接口
	 * 
	 * @param arg0
	 */
	public Kpush close(Boolean arg0) {
		if (arg0) {
			if (KpushConfig.session != null) {
				KpushConfig.session.close(true);
			} else {
				LogUtil.i("Kpush.class close：尚未初始化");
				logger.info("Kpush.class close：尚未初始化");
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
		KpushConfig.reConnectCount = times;
		return instance;
	}

	/**
	 * 判断长连接是否存在
	 * 
	 * @return
	 */
	public Boolean isConnected() {
		if (KpushConfig.session!= null) {
			if (KpushConfig.session.isConnected()) {
				 LogUtil.i("Kpush.class isConnected：存在长连接");
				 logger.info("Kpush.class isConnected：存在长连接");
				return true;
			} else {
				 LogUtil.i("Kpush.class isConnected：不存在长连接");
				 logger.info("Kpush.class isConnected：不存在长连接");
				return false;
			}
		} else {
			LogUtil.i("Kpush.class isConnected：尚未初始化");
			logger.info("Kpush.class isConnected：尚未初始化");
			return false;
		}
	}

	/**
	 * 重新连接
	 * 
	 * @param mContext
	 */
	public void recover(Context mContext) {
		if (!isConnected()) {
			Intent intent = new Intent();
			intent.setAction(KpushConfig.RECONNECT);
			mContext.sendBroadcast(intent);
		}
	}

	/**
	 * 设置消息回调接口
	 * 
	 * @param listener
	 */
	public void setListener(ReceivedListener listener) {
		KpushConfig.mReceivedListener = listener;
	}

	/**
	 * 获取唯一标识ClientId
	 * 
	 * @return
	 */
	public String getClientId() {
		return SpfsUtil.getClientId();
	}
}

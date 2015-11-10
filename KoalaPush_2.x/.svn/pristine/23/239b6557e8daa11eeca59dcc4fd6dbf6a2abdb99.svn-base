package com.arvin.koalapush.api;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.arvin.koalapush.potter.ReceivedListener;
import com.arvin.koalapush.util.DBHelper;

public class KpushConfig {

	
	/**
	 * 连接超时时间设置接口（单位：秒）
	 */
	public static int timeOut = 30;
	/**
	 * 重连次数
	 */
	public static int reConnectCount = 5;
	
	/**
	 * 长连接Session对象
	 */
	public static IoSession session;
	
	/** mina连接成功 */
	public static final int OPENED = 0;

	/** 让service 发送一个消息 */
	public static final int MSG_SEND = 1;

	/** 让service 接收一个消息 */
	public static final int HBIND = 2;

	/** 让service 接收一个消息 */
//	public static final int RECEIVED = 3;

	/** 接收错误 ，mina断开连接... */
	public static final int RECEIVED_ERROR = 4;

	/** 让service 显示一个消息的命令 */
	public static final int PUSH_INIT = 5;
	
	public static final int PUSH_LOGIN = 6;
	/** 有新消息需要查询**/
	public static final int QUERY_MESSAGE = 8;
	/**
	 * 重新连接
	 */
	public static final int SESSION_RECONNECT = 7;
	/**
	 * 新消息推送给应用APP
	 */
	public static final int PUSH_APP_MSG = 9;
	/**
	 * 连接后查询所有未读消息推送给应用APP
	 */
	public static final int PUSH_APP_ALL_MSG = 10;
	public static DBHelper dbHelper;
	
	
	public static ReceivedListener mReceivedListener;
	public static final String BIND = "com.koalapush.pushservice.action.BIND";
	public static final String RECEIVED = "com.koalapush.pushservice.action.RECEIVED";
	public static final String RECONNECT = "com.koalapush.pushservice.action.RECONNECT";
}

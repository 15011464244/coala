package com.arvin.koalapush.potter;

import java.lang.Thread.State;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.apache.mina.filter.logging.MdcInjectionFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.arvin.koalapush.api.KpushConfig;
import com.arvin.koalapush.bean.ReceivedMsgBean;
import com.arvin.koalapush.constant.KpushConstant;
import com.arvin.koalapush.keep.ClientKeepAliveFactoryImpl;
import com.arvin.koalapush.net.MyRequest;
import com.arvin.koalapush.net.resp.BaseResp;
import com.arvin.koalapush.net.resp.LoginResp;
import com.arvin.koalapush.net.resp.OfflineMessageBean;
import com.arvin.koalapush.net.resp.OfflineMessageRep;
import com.arvin.koalapush.receiver.PushAlarmReceiver;
import com.arvin.koalapush.util.DBHelper;
import com.arvin.koalapush.util.DeviceUuidFactory;
import com.arvin.koalapush.util.Log4jUtil;
import com.arvin.koalapush.util.LogUtil;
import com.arvin.koalapush.util.SpfsUtil;

public class PushService extends Service {
	/** http服务 */
	public static RequestQueue sQueue;
	/** 获取设备id */
	protected DeviceUuidFactory factory;
	/** 获取设备id */
	protected static String sessionId;
	// protected static String wifi_id;
	/** mina服务器地址 */
	protected static String address;
	/** mina服务器端口 */
	protected static int port;
	/** appKey **/
	private String appKey;
	// 创建连接客户端
	protected NioSocketConnector connector;

	protected static ConnectFuture cf;
	private List<OfflineMessageBean> returnResources = new ArrayList<OfflineMessageBean>();

	ReceivedMsgBean mReceivedMsgBean = new ReceivedMsgBean();
	public MyHandler myHandler = new MyHandler();
	public KThread thread = null;
	private boolean connectTcp = false;
	private boolean minaReConnect = true;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static AlarmManager am;
	public static PendingIntent pendingIntent;
	private static Log4jUtil logger = Log4jUtil.getLogger(PushService.class.getName());
	
	public void init() {
		// 检查有没有长连接线程
		threadWhetherNew();
		if (!isNetworkAvailable(getApplicationContext())) {
			LogUtil.w("没有可用网络，请检查网络状态");
			logger.warn("没有可用网络，请检查网络状态");
			return;
		}

		if (isConnected()) {
			return;
		}
		// 获取AppKey
		if (appKey == null) {
			try {
				appKey = this.getPackageManager().getApplicationInfo(
						getPackageName(), PackageManager.GET_META_DATA).metaData
						.getString("app_key");
			} catch (NameNotFoundException e) {
				LogUtil.e("异常状态码：3031");
				logger.error("异常状态码：3031", e);
			}
		}

		if (null == SpfsUtil.getWifiMacAddress()
				|| "".equals(SpfsUtil.getWifiMacAddress())) {
			new Handler().post(new Runnable() {

				@Override
				public void run() {
					// 获取wifi_id
					WifiManager wifi = (WifiManager) PushService.this
							.getSystemService(Context.WIFI_SERVICE);
					WifiInfo info = wifi.getConnectionInfo();
					if (info.getMacAddress() != null) {
						SpfsUtil.setWifiMacAddress(info.getMacAddress()
								.replace(":", ""));

					} else {
						while (info.getMacAddress() == null) {
							tryOpenMAC(wifi);
							info = wifi.getConnectionInfo();
							if (info.getMacAddress() != null) {
								SpfsUtil.setWifiMacAddress(info.getMacAddress()
										.replace(":", ""));
								tryCloseMAC(wifi);
								break;
							}
						}
					}
				}
			});
		} else {
			LogUtil.i("wifiMacAddress：" + SpfsUtil.getWifiMacAddress());
			logger.info("wifiMacAddress：" + SpfsUtil.getWifiMacAddress());
		}
		// 开始登陆
		Message msg = new Message();
		msg.what = KpushConfig.PUSH_LOGIN;
		myHandler.sendMessage(msg);
	}

	// 尝试打开wifi
	private static boolean tryOpenMAC(WifiManager manager) {
		boolean softOpenWifi = false;
		int state = manager.getWifiState();
		if (state != WifiManager.WIFI_STATE_ENABLED
				&& state != WifiManager.WIFI_STATE_ENABLING) {
			manager.setWifiEnabled(true);
			softOpenWifi = true;
		}
		return softOpenWifi;
	}

	// 尝试关闭wifi
	private static void tryCloseMAC(WifiManager manager) {
		manager.setWifiEnabled(false);
	}

	protected class MyHandler extends Handler {

		public MyHandler() {
		}

		public MyHandler(Looper L) {
			super(L);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case KpushConfig.PUSH_LOGIN:
				login();
				break;
			case KpushConfig.HBIND:
				LogUtil.i("查询所有的离线消息");
				logger.info("查询所有的离线消息");
				bind();
				getMsg();
				break;
			case KpushConfig.QUERY_MESSAGE:
				ReceivedMsgBean mReceivedMsgBean = (ReceivedMsgBean) msg.obj;
				if (mReceivedMsgBean != null
						&& mReceivedMsgBean.getResCode().equals("0")) {
					String messageType = mReceivedMsgBean.getMessageEntity()
							.getMessageType();
					if (messageType.equals("1") || messageType.equals("2")) {
						LogUtil.i("资源消息："
								+ JSONObject.toJSONString(mReceivedMsgBean
										.getMessageEntity()));
						logger.info("资源消息："
								+ JSONObject.toJSONString(mReceivedMsgBean
										.getMessageEntity()));
						returnResourceMessage(mReceivedMsgBean);
						syncMsgTag(mReceivedMsgBean.getMessageEntity()
								.getSource(), mReceivedMsgBean
								.getMessageEntity().getTarget(),
								mReceivedMsgBean.getMessageEntity()
										.getMessage_id());
					} else {
						LogUtil.i("业务消息");
						logger.info("业务消息");
						getMsg();
					}
				}
				break;
			case KpushConfig.PUSH_APP_MSG:
				List<OfflineMessageBean> pushAppMsgList = (List<OfflineMessageBean>) msg.obj;
				if (pushAppMsgList.size() > 0) {
					if (KpushConfig.mReceivedListener != null) {
						KpushConfig.mReceivedListener
								.onReceived(pushAppMsgList);
					} else {
						LogUtil.e("应用消息回调为null");
						logger.error("应用消息回调为null");
					}
				} else {
					LogUtil.i("消息为空");
					logger.info("消息为空");
				}
				break;
			}
		}
	}

	public BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(KpushConfig.BIND)) {
				LogUtil.i("Receiver: BIND...");
				logger.info("Receiver: BIND...");
				Message msg = new Message();
				msg.what = KpushConfig.HBIND;
				myHandler.sendMessage(msg);
			} else if (action.equals(KpushConfig.RECEIVED)) {
				LogUtil.d("Receiver: RECEIVED...");
				logger.info("Receiver: RECEIVED...");
				mReceivedMsgBean = (ReceivedMsgBean) intent
						.getSerializableExtra("message");
				if(!TextUtils.isEmpty(SpfsUtil.getClientId())){
					if(SpfsUtil.getClientId().equals(mReceivedMsgBean.getMessageEntity().getTarget())){
						Message msg = new Message();
						msg.what = KpushConfig.QUERY_MESSAGE;
						msg.obj = mReceivedMsgBean;
						myHandler.sendMessage(msg);
					}else{
						LogUtil.w("接收方clientId不匹配，不进行查询");
						logger.warn("接收方clientId不匹配，不进行查询");
					}
				}
			} else if (action.equals(KpushConfig.RECONNECT)) {
				LogUtil.i("Receiver: RECONNECT...");
				logger.info("Receiver: RECONNECT...");
				login();
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		LogUtil.i("onBind");
		logger.info("onBind");
		return null;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		LogUtil.i("onUnbind");
		logger.info("onUnbind");
		return super.onUnbind(intent);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		SpfsUtil.init(getApplicationContext());
		// 建立网络请求队列
		sQueue = Volley.newRequestQueue(this);
		sQueue.start();
//		timer.schedule(tast, 0, 1000 * 60 * 1);
		IntentFilter bind = new IntentFilter();
		bind.addAction(KpushConfig.BIND);
		bind.addAction(KpushConfig.RECEIVED);
		bind.addAction(KpushConfig.RECONNECT);
		registerReceiver(mBroadcastReceiver, bind);
		threadWhetherNew();
		//请求失败的重新请求
		repeatReq();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LogUtil.i("onStartCommand");
		logger.info("onStartCommand");
		init();
		// startForeground(1, new Notification());
		flags = START_STICKY;
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		KpushConfig.session.close(true);
		connector = null;
		unregisterReceiver(mBroadcastReceiver);
		if(thread!=null){
			thread.stop();
			thread =null;
		}
		Intent intent = new Intent(this, PushService.class);
		this.startService(intent);
		super.onDestroy();
	}
	/**
	 * 定时发送请求广播
	 */
	public void repeatReq(){
		Intent intent = new Intent(this,PushAlarmReceiver.class);                                
		intent.setAction("com.amaker.ch08.app.action.BC_ACTION");
		pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
		am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 180*60*1000, pendingIntent);
	}

	/**
	 * 先启动长连接线程
	 */
	protected void connectTcp() {
		thread = new KThread("Kthread");
		thread.start();
	}

	/**
	 * 判断线程是否存在，判断状态是否为销毁状态
	 */
	protected void threadWhetherNew() {
		if (thread == null) {
			connectTcp();
		} else if (thread.getState() == Thread.State.TERMINATED) {
			thread = null;
			connectTcp();
		}
	}

	protected void socketServer(String address, int port) {
		minaReConnect = false;
		LogUtil.i("socketServer");
		logger.info("socketServer");
		// 创建连接客户端
		if (connector == null) {
			connector = new NioSocketConnector();
			// 设置链接超时时间
			connector.setConnectTimeoutMillis(KpushConfig.timeOut * 1000); // 设置连接超时
			// 添加处理器
			connector.setHandler(new MinaClientHanlder(getApplication()));
			// 断线重连回调拦截器
			connector.getFilterChain().addFirst("reconnection",
					new IoFilterAdapter() {
						@Override
						public void sessionClosed(NextFilter nextFilter,
								IoSession ioSession) throws Exception {
							logger.error("异常：3039，session is close");
							minaReConnect = false;
							if (KpushConfig.session != null
									&& KpushConfig.session.isConnected()) {
								return;
							}
							// 如何没有网络直接跳出循环，并且打开重新登录开关
							if (!isNetworkAvailable(getApplicationContext())) {
								minaReConnect = true;
								return;
							}
							LogUtil.i("mina开始自动重连");
							logger.info("mina开始自动重连");
//							KpushConfig.mReceivedListener.onConnection("自动重连中",sdf.format(new Date()));
							for (int i = 0; i < KpushConfig.reConnectCount; i++) {
								try {
									Thread.sleep(3000);
									ConnectFuture future = connector.connect();
									future.awaitUninterruptibly();// 等待连接创建成功

									KpushConfig.session = future.getSession();// 获取会话
									if (KpushConfig.session.isConnected()) {
										LogUtil.w("断线重连["
												+ connector
														.getDefaultRemoteAddress()
														.getHostName()
												+ ":"
												+ connector
														.getDefaultRemoteAddress()
														.getPort() + "]成功");
										logger.warn("断线重连["
												+ connector
														.getDefaultRemoteAddress()
														.getHostName()
												+ ":"
												+ connector
														.getDefaultRemoteAddress()
														.getPort() + "]成功");
										break;
									}
								} catch (Exception ex) {
									LogUtil.w("重连服务器登录失败,3秒再连接一次:"
											+ ex.getMessage());
									logger.warn("重连服务器登录失败,3秒再连接一次:"
											+ ex.getMessage());
									if (i == KpushConfig.reConnectCount - 1) {
										minaReConnect = true;
										PushService.this.init();
									}
								}
							}
						}
					});
			ClientKeepAliveFactoryImpl ckafi = new ClientKeepAliveFactoryImpl();
//			KeepAliveFilter kaf = new KeepAliveFilter(ckafi,
//					IdleStatus.READER_IDLE,KeepAliveRequestTimeoutHandler.CLOSE);
			KeepAliveFilter kaf = new KeepAliveFilter(ckafi);
			kaf.setForwardEvent(true);
			kaf.setRequestInterval(60 * 2);
			kaf.setRequestTimeout(30);
			connector.getFilterChain().addLast("mdc", new MdcInjectionFilter());
			ProtocolCodecFilter filter = new ProtocolCodecFilter(
					new TextLineCodecFactory(Charset.forName("UTF-8")));
			// TextLineCodecFactory factory = new TextLineCodecFactory(
			// Charset.forName("UTF-8"), LineDelimiter.WINDOWS.getValue(),
			// LineDelimiter.WINDOWS.getValue());
			// factory.setDecoderMaxLineLength(10240);
			// factory.setEncoderMaxLineLength(10240);
			// 加入解码器
			connector.getFilterChain().addLast("codec", filter);
			connector.getFilterChain().addLast("heart", kaf);
			connector.getSessionConfig().setReaderIdleTime(5);
			connector.getSessionConfig().setReceiveBufferSize(10240); // 设置接收缓冲区的大小
			connector.getSessionConfig().setSendBufferSize(10240);// 设置输出缓冲区的大小
			// connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE,
			// 60*3);
			// // 读写都空闲时间:30秒
			// connector.getSessionConfig().setIdleTime(IdleStatus.READER_IDLE,
			// 5);// 读(接收通道)空闲时间:40秒
			// connector.getSessionConfig().setIdleTime(IdleStatus.WRITER_IDLE,
			// 60);// 写(发送通道)空闲时间:50秒
		}
		connector.setDefaultRemoteAddress(new InetSocketAddress(address, port));// 设置访问地址
		for (int i = 0; i < KpushConfig.reConnectCount; i++) {
			try {
				ConnectFuture future = connector.connect();
				// 等待连接创建成功
				future.awaitUninterruptibly();
				// 获取会话
				KpushConfig.session = future.getSession();
				LogUtil.i("连接服务端" + address + ":" + port + "[成功]" + ",,时间:"
						+ sdf.format(new Date()));
				logger.info("连接服务端" + address + ":" + port + "[成功]" + ",,时间:"
						+ sdf.format(new Date()));
				break;
			} catch (RuntimeIoException e) {
				if (i == KpushConfig.reConnectCount - 1) {
					minaReConnect = true;
					init();
				}
				LogUtil.e("连接服务端" + address + ":" + port + "失败" + ",时间:"
						+ sdf.format(new Date()) + ",异常内容:\n" + e.getMessage());
				logger.error("连接服务端" + address + ":" + port + "失败" + ",时间:"
						+ sdf.format(new Date()) + ",异常内容:\n" ,e);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
					logger.error("连接异常信息",e1);
				}// 连接失败后,重连10次,间隔30s
			}
		}

	}

	public void recoverServer() {
		LogUtil.i("重新连接剩余次数" + KpushConfig.reConnectCount);
		logger.info("重新连接剩余次数" + KpushConfig.reConnectCount);
		if (KpushConfig.reConnectCount > 0) {
			KpushConfig.reConnectCount--;
			new Thread(new Thread() {
				@Override
				public void run() {
					socketServer(address, port);
				}
			}).start();
		}
	}

	protected void login() {
		LogUtil.i("login");
		JSONObject json = new JSONObject();
		json.put("app_key", appKey);
		json.put("device_id", SpfsUtil.getWifiMacAddress());
		json.put("device_type", "0");
		json.put("client_id", SpfsUtil.getClientId());
		logger.info("login 参数："+json.toString());
		MyRequest<LoginResp> request = new MyRequest<LoginResp>(Method.POST,
				LoginResp.class, KpushConstant.login,
				new Listener<LoginResp>() {

					@Override
					public void onResponse(LoginResp response) {
						String repson = JSONObject.toJSONString(response);
						LogUtil.i("LoginResp:" + repson);
						logger.info("LoginResp:" + repson);
						if ("0".equals(response.getResCode())) {

							address = response.getServerDeviceModel()
									.getIpAddress();
							port = response.getServerDeviceModel().getPort();
							SpfsUtil.setClientId(response.getClient_id());
							// socketServer(address, port);
							// 当mina正在进行重新连接时为了保证只有一个重新连接，故让mina来控制login后是否连接。
							if (minaReConnect) {
								connectTcp = true;
								thread.resume();
							} else {
								LogUtil.i("mina自动重连中...");
								logger.info("mina自动重连中...");
							}
						} else {
							LogUtil.e("初始化错误：请检查APPkey是否可用！");
							logger.error("初始化错误：请检查APPkey是否可用！");
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						LogUtil.e("3032:Proxy Exception" + ";errorLog:"
								+ error.getLocalizedMessage());
						logger.error("3032:Proxy Exception" + ";errorLog:"
								,error);
						if (KpushConfig.session != null) {
							KpushConfig.session.close(true);
						}
						init();
					}
				}, json.toString());

		sQueue.add(request);

	}

	protected void bind() {
		LogUtil.d("bind");
		if (KpushConfig.mReceivedListener != null) {
			KpushConfig.mReceivedListener.onReceived("SDK初始化成功");
		} else {
			LogUtil.e("应用消息回调为null");
			logger.error("应用消息回调为null");
		}
	}

	protected void returnResourceMessage(ReceivedMsgBean mReceivedMsgBean) {
		returnResources.clear();
		OfflineMessageBean omb = new OfflineMessageBean();
		omb.setMessage_type(mReceivedMsgBean.getMessageEntity()
				.getMessageType());
		omb.setPkid(Integer.parseInt(mReceivedMsgBean.getMessageEntity()
				.getMessage_id()));
		omb.setSource(mReceivedMsgBean.getMessageEntity().getSource());
		omb.setTarget(mReceivedMsgBean.getMessageEntity().getTarget());
		omb.setUrl(mReceivedMsgBean.getMessageEntity().getUrl());
		returnResources.add(omb);
		if (KpushConfig.mReceivedListener != null) {
			KpushConfig.mReceivedListener.onReceived(returnResources);
		} else {
			LogUtil.e("应用消息回调为null");
			logger.error("应用消息回调为null");
		}
	}
	/**
	 * 存储日志
	 */
	public void saveLog(List<OfflineMessageBean> offlineMsgList){
		final JSONArray jArray = new JSONArray();
		if(null != offlineMsgList && offlineMsgList.size() > 0){
			for (OfflineMessageBean bean : offlineMsgList) {
				String content = JSONObject.toJSONString(bean);
				KpushConfig.dbHelper.insertRequest(KpushConfig.dbHelper.getWritableDatabase(), SpfsUtil.getClientId(), 
				System.currentTimeMillis()/1000, "log", content, bean.getPkid(), 2);
			}
		}else{
			LogUtil.i("存储日志失败，查询数据为null");
			logger.info("存储日志失败，查询数据为null");
			return;
		}
	}

	/**
	 * 查询未读消息
	 */
	protected void getMsg() {
		if (SpfsUtil.getClientId() != null) {
			JSONObject obj = new JSONObject();
			obj.put("client_id", SpfsUtil.getClientId());
			MyRequest<OfflineMessageRep> request = new MyRequest<OfflineMessageRep>(
					Method.POST, OfflineMessageRep.class, KpushConstant.getMsg,
					new Listener<OfflineMessageRep>() {

						@Override
						public void onResponse(OfflineMessageRep arg0) {
							String result = JSONObject.toJSONString(arg0);
							LogUtil.i("未读消息结果："+result);
							logger.info("未读消息结果："+result);
							List<OfflineMessageBean> offlineMsgList = arg0.getOfflineMsgList();
							//提交日志
//							saveLog(offlineMsgList);
							
							if (arg0 != null && arg0.getResCode().equals("0")) {
								LogUtil.d(JSONObject.toJSONString(arg0));
								Message msg = new Message();
								msg.what = KpushConfig.PUSH_APP_MSG;
								msg.obj = arg0.getOfflineMsgList();
								myHandler.sendMessage(msg);
							}
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							LogUtil.e("查询未读消息失败"+arg0.getMessage());
							logger.error("查询未读消息失败",arg0);
						}
					}, obj.toString());
			sQueue.add(request);
		}
	}

	protected void syncMsgTag(String source, String target, String message_id) {
		LogUtil.d("syncMsgTag");

		JSONObject json = new JSONObject();
		try {
			json.put("source", source);
			json.put("target", target);
			json.put("message_id", message_id);
		} catch (JSONException e) {
			LogUtil.e("异常状态码：3030");
			logger.error("异常状态码：3030",e);
		}
		MyRequest<BaseResp> request = new MyRequest<BaseResp>(Method.POST,
				BaseResp.class, KpushConstant.sync, new Listener<BaseResp>() {
					@Override
					public void onResponse(BaseResp response) {
						String result = JSONObject.toJSONString(response);
						LogUtil.i("syncMsgTag:" + result);
						logger.info("syncMsgTag:" + result);
					}
				}, new com.android.volley.Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						LogUtil.e("3034:Sure Exception");
						logger.error("3034:Sure Exception",error);
					}
				}, json.toString());

		sQueue.add(request);

	}

	/**
	 * 网络是否可用
	 * 
	 * @param context
	 * @return true or false
	 */
	public static boolean isNetworkAvailable(Context context) {
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

	protected Boolean isConnected() {
		if (KpushConfig.session != null) {
			if (KpushConfig.session.isConnected()) {
				LogUtil.i("PushService.class isConnected：存在长连接");
				logger.info("PushService.class isConnected：存在长连接");
				return true;
			} else {
				LogUtil.i("PushService.class isConnected：不存在长连接");
				logger.info("PushService.class isConnected：不存在长连接");
				return false;
			}
		} else {
			LogUtil.i("PushService.class isConnected：不存在长连接");
			logger.info("PushService.class isConnected：不存在长连接");
			return false;
		}
	}
	
	class KThread implements Runnable {

		private Thread t;
		private String threadName;
		private boolean suspended = false;
		private boolean isStop = false;

		KThread(String name) {
			threadName = name;
			logger.info("Creating " + threadName);
		}

		public void run() {
			logger.info("Running " + threadName);
			while (!isStop) {
				try {
					logger.info("Thread: " + threadName);
					synchronized (this) {
						if (connectTcp) {
							connectTcp = false;
							LogUtil.i("address:" + address + "\nport:" + port);
							LogUtil.i("建立长连接...");
							logger.info("address:" + address + "\nport:" + port);
							logger.info("建立长连接...");
							socketServer(address, port);
//							KpushConfig.mReceivedListener.onConnection("长连接建立中",sdf.format(new Date()));
						}
						suspend();
						while (suspended) {
							wait();
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					isStop = true;
					logger.error("连接线程出现异常", e);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("连接线程出现异常", e);
				} finally {
					logger.info("go sth finally");
				}
			}
			logger.info("Thread " + threadName + " exiting.");
		}
		public void start() {
			synchronized (this) {
				logger.info("Starting " + threadName);
				if (t == null) {
					t = new Thread(this, threadName);
					t.start();
				}
			}
		}

		public void stop() {
			synchronized (this) {
				if (t != null && !t.isInterrupted()) {
					t.interrupt();
				}
			}
		}

		public void suspend() {
			synchronized (this) {
				logger.info("Thread " + threadName + " suspend.");
				suspended = true;
			}
		}

		public synchronized void resume() {
			synchronized (this) {
				logger.info("Thread " + threadName + " resume.");
				suspended = false;
				notify();
			}
		}

		public State getState() {
			return t.getState();
		}
	}
}

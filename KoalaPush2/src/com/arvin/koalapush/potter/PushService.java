package com.arvin.koalapush.potter;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.logging.MdcInjectionFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.arvin.koalapush.adapter.ReConnAdapter;
import com.arvin.koalapush.bean.ReceivedMsgBean;
import com.arvin.koalapush.bean.ReceivedSessionBean;
import com.arvin.koalapush.constant.KpushConstant;
import com.arvin.koalapush.constant.ParamConstant;
import com.arvin.koalapush.keep.ClientKeepAliveFactoryImpl;
import com.arvin.koalapush.listener.ReceivedListener;
import com.arvin.koalapush.net.MyRequest;
import com.arvin.koalapush.net.resp.BaseResp;
import com.arvin.koalapush.net.resp.LoginResp;
import com.arvin.koalapush.util.DateTimeUtil;
import com.arvin.koalapush.util.DeviceUuidFactory;
import com.arvin.koalapush.util.LogUtil;
import com.arvin.koalapush.util.SpfsUtil;

public class PushService extends Service {
	protected static Context mContext;

	/** http服务 */
	protected static RequestQueue sQueue;

	/** 获取设备id */
	protected DeviceUuidFactory factory;
	/** 获取设备id */
	protected static String sessionId;
	protected static String address;
	protected static int port;

	// 创建连接客户端
	protected NioSocketConnector connector;

//	/** mina服务器地址 */
//	protected static String appKey;

	/** mina服务器端口 */
	protected static IoSession session;

	protected static ReceivedListener mReceivedListener;

	protected static IoSession mIoSession;
	
//	private static String mClientId;

	protected static ConnectFuture cf;

	ReceivedMsgBean mReceivedMsgBean = new ReceivedMsgBean();

	public MyHandler myHandler = new MyHandler();
	
	private Binder myBinder;

	protected static void actionStart(Context context) {
		LogUtil.print("actionStart");
		mContext = context;
		Intent intent = new Intent(mContext, PushService.class);
		mContext.startService(intent);
	}

	public void init() {
		SpfsUtil.init(getApplicationContext());
		if (isConnected()) {
			return;
		}
		
		this.saveAppKey();

		new Handler().post(new Runnable() {
			@Override
			public void run() {
				if (null == SpfsUtil.getWifiMacAddress()
						|| "".equals(SpfsUtil.getWifiMacAddress())) {
					//保存wifi_id
					saveWifiId();
				} else {
					Log.e("msg", "wifiMacAddress" + SpfsUtil.getWifiMacAddress());
				}
			}
		});

		// 建立网络请求队列
		sQueue = Volley.newRequestQueue(this);
		sQueue.start();

		// 开始登陆
		Message msg = new Message();
		msg.what = ParamConstant.PUSH_LOGIN;
		myHandler.sendMessage(msg);
	}
	
	//获取wifi_id 
	public void saveWifiId(){
		WifiManager wifi = (WifiManager) PushService.this
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		if (info.getMacAddress() != null) {
			SpfsUtil.setWifiMacAddress(info.getMacAddress().replace(":", ""));
			
		} else {
			while (info.getMacAddress() == null) {
				tryOpenMAC(wifi);
				info = wifi.getConnectionInfo();
				if (info.getMacAddress() != null) {
					SpfsUtil.setWifiMacAddress(info.getMacAddress().replace(":", ""));
					tryCloseMAC(wifi);
					break;
				}
			}
		}
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

//		private int count = 0;
		public MyHandler() {}
		public MyHandler(Looper L) {
			super(L);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ParamConstant.BIND:
				ReceivedSessionBean bean = (ReceivedSessionBean) msg.obj;
				sessionId = bean.getSessionId();
				bind();
				break;

			case ParamConstant.RECEIVED:
				LogUtil.print("RECEIVED...");
				if (mReceivedListener != null) {
					mReceivedMsgBean = (ReceivedMsgBean) msg.obj;
					LogUtil.print(mReceivedMsgBean.toString());
					String bet = mReceivedListener.onReceived(mReceivedMsgBean);
					if (!"noIsSync".equals(bet)) {
						syncMsgTag(mReceivedMsgBean.getMessageEntity()
								.getSource(), mReceivedMsgBean
								.getMessageEntity().getTarget(), bet);
					}else{
						mReceivedListener.onError(mReceivedMsgBean);
					}
				}else{
					mReceivedListener.onError(null);
				}
				break;
			case ParamConstant.RECEIVED_ERROR:
				if (mReceivedListener != null) {
					ReceivedMsgBean be = (ReceivedMsgBean) msg.obj;
					mReceivedListener.onError(be);
				}else{
					mReceivedListener.onError(null);
				}
				break;
			case ParamConstant.PUSH_INIT:
				break;
			case ParamConstant.MSG_SEND:
				break;
			case ParamConstant.PUSH_LOGIN:
				login();
				break;
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		LogUtil.print("onBind");
		myBinder = new MyBinder();
		return myBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		LogUtil.print("onUnbind");
		init();
		return super.onUnbind(intent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LogUtil.print("onStartCommand");
		flags = START_STICKY;
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	protected void socketServer(String address, int port) {
		LogUtil.print("socketServer");
		// 创建连接客户端
		if (connector == null) {
			connector = new NioSocketConnector();
		}
		// 设置链接超时时间
//		 connector.setConnectTimeoutCheckInterval(TIMEOUT);
		connector.setConnectTimeoutMillis(SpfsUtil.getTimeOut() * 1000); // 设置连接超时

		if (!connector.isActive()) {
			connector.setHandler(new MinaClientHanlder(PushService.this));
		}
		if (connector.getFilterChain().contains("reconnection")) {
			connector.getFilterChain().remove("reconnection");
		}
		
		// 断线重连回调拦截器
		connector.getFilterChain().addFirst("reconnection",new ReConnAdapter(session, connector));
		ClientKeepAliveFactoryImpl ckafi = new ClientKeepAliveFactoryImpl();
		KeepAliveFilter kaf = new KeepAliveFilter(ckafi, IdleStatus.BOTH_IDLE);
		kaf.setForwardEvent(true);
		kaf.setRequestInterval(60 * 3 + 30);
		if (connector.getFilterChain().contains("mdc")) {
			connector.getFilterChain().remove("mdc");
		}
		connector.getFilterChain().addLast("mdc", new MdcInjectionFilter());
		ProtocolCodecFilter filter = new ProtocolCodecFilter(
				new TextLineCodecFactory(Charset.forName("UTF-8")));
		// 加入解码器
		if (connector.getFilterChain().contains("codec")) {
			connector.getFilterChain().remove("codec");
		}
		connector.getFilterChain().addLast("codec", filter);
		if (connector.getFilterChain().contains("heart")) {
			connector.getFilterChain().remove("heart");
		}
		connector.getFilterChain().addLast("heart", kaf);
		connector.getSessionConfig().setReaderIdleTime(5);
		connector.getSessionConfig().setReceiveBufferSize(10240); // 设置接收缓冲区的大小
		connector.getSessionConfig().setSendBufferSize(10240);// 设置输出缓冲区的大小

		connector.setDefaultRemoteAddress(new InetSocketAddress(address, port));// 设置默认访问地址
		for (;;) {
			try {
				ConnectFuture future = connector.connect();
				// 等待连接创建成功
				future.awaitUninterruptibly();
				// 获取会话
				session = future.getSession();
				LogUtil.e("连接服务端"
						+ address
						+ ":"
						+ port
						+ "[成功]"
						+ ",,时间:"
						+ DateTimeUtil.getCurrentTime());
				break;
			} catch (RuntimeIoException e) {
				LogUtil.e("连接服务端"
						+ address
						+ ":"
						+ port
						+ "失败"
						+ ",时间:"
						+ DateTimeUtil.getCurrentTime() + ",异常内容:\n"
						+ e.getMessage());
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}// 连接失败后,重连10次,间隔30s
			}
		}

		// connector.dispose();
	}

	public void recoverServer() {
		LogUtil.print("重新连接剩余次数" + SpfsUtil.getTimes());
		int times = SpfsUtil.getTimes();
		if (times > 0) {
			times--;
			new Thread(new Thread() {
				@Override
				public void run() {
					socketServer(address, port);
				}
			}).start();
		}
	}

	protected void login() {
		LogUtil.print("login");
		JSONObject json = new JSONObject();
		try {
			json.put("app_key", SpfsUtil.getAppKey());
			json.put("device_id", SpfsUtil.getWifiMacAddress());
			json.put("device_type", "0");

		} catch (JSONException e) {
			if (SpfsUtil.isShowErrorLog()) {
				e.printStackTrace();
			}
			LogUtil.e("异常状态码：3030");
		}
		MyRequest<LoginResp> request = new MyRequest<LoginResp>(Method.POST,
				LoginResp.class, KpushConstant.login,
				new Listener<LoginResp>() {

					@Override
					public void onResponse(LoginResp response) {
						LogUtil.w("LoginResp:" + response.toString());
						if ("0".equals(response.getResCode())) {

							address = response.getServerDeviceModel()
									.getIpAddress();
							port = response.getServerDeviceModel().getPort();
//							mClientId = response.getClient_id();
							SpfsUtil.setClientId(response.getClient_id());
							System.out.println("clientId == " + SpfsUtil.getClientId());
							new Thread(new Thread() {
								@Override
								public void run() {
									LogUtil.print("address:" + address
											+ "\nport:" + port);
									LogUtil.print("建立长连接...");
									socketServer(address, port);
								}
							}).start();

						} else {
							LogUtil.e("初始化错误：请检查APPkey是否可用！");
						}
					}
				}, new com.android.volley.Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						LogUtil.e("3032:Proxy Exception" + ";errorLog:"
								+ error.getLocalizedMessage());
						error.printStackTrace();
						if (session != null) {
							session.close(true);
						}
					}
				}, json.toString());

		sQueue.add(request);

	}

	protected void bind() {
		LogUtil.print("bind");
		JSONObject json = new JSONObject();
		try {
			json.put("client_id", SpfsUtil.getClientId());
			json.put("session_id", sessionId);
		} catch (JSONException e) {
			LogUtil.e("异常状态码：3030");
		}
		MyRequest<BaseResp> request = new MyRequest<BaseResp>(Method.POST,
				BaseResp.class, KpushConstant.bind, new Listener<BaseResp>() {

					@Override
					public void onResponse(BaseResp response) {
						LogUtil.w("BindResp:" + response.toString());
						if ("0".equals(response.getResCode())) {
							if (mReceivedListener != null) {
								mReceivedListener.onReceived("SDK初始化成功");
							}
						} else {
							bind();
						}
					}
				}, new com.android.volley.Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// error.printStackTrace();
						LogUtil.e("异常状态码：3033");
						bind();
					}
				}, json.toString());

		request.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 3, 1.0f));// 设置超时时间
		sQueue.add(request);

	}

	protected void syncMsgTag(String source, String target, String message_id) {
		LogUtil.print("syncMsgTag");

		JSONObject json = new JSONObject();
		try {
			json.put("source", source);
			json.put("target", target);
			json.put("message_id", message_id);
		} catch (JSONException e) {
			LogUtil.e("异常状态码：3030");
		}
		MyRequest<BaseResp> request = new MyRequest<BaseResp>(Method.POST,
				BaseResp.class, KpushConstant.sync, new Listener<BaseResp>() {
					@Override
					public void onResponse(BaseResp response) {
						LogUtil.w("syncMsgTag:" + response.toString());

					}
				}, new com.android.volley.Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						LogUtil.e("3034:Sure Exception");
						error.printStackTrace();
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
	protected boolean isNetworkAvailable(Context context) {
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


	protected void saveAppKey() {
		// 获取AppKey
		if (SpfsUtil.getAppKey() == null) {
			try {
				String appKey = this.getPackageManager().getApplicationInfo(
						getPackageName(), PackageManager.GET_META_DATA).metaData
						.getString("app_key");
				SpfsUtil.setAppKey(appKey);
			} catch (NameNotFoundException e) {
				if (SpfsUtil.isShowErrorLog()) {
					e.printStackTrace();
				}
				LogUtil.e("异常状态码：3031");
			}
		}
	}
	
	protected IoSession getSession() {
		return session;
	}

	protected Boolean isConnected() {
		if (session != null) {
			if (session.isConnected()) {
				LogUtil.e("PushService.class isConnected：存在长连接");
				return true;
			} else {
				LogUtil.e("PushService.class isConnected：不存在长连接");
				return false;
			}
		} else {
			LogUtil.e("PushService.class isConnected：不存在长连接");
			return false;
		}

	}

	protected void setListener(ReceivedListener listener) {
		mReceivedListener = listener;
	}
	
	public PushService getService() {
	        return this;
	    }
	
	private class MyBinder extends Binder implements IBindService{
		@Override
		public PushService getService() {
			return PushService.this.getService();
		}
		
	}
}

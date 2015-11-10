package com.ems.express.core.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.GsonPostParamsRequest;
import com.arvin.koalapush.bean.MsgContentBean;
import com.arvin.koalapush.bean.ReceivedMsgBean;
import com.arvin.koalapush.potter.Kpush;
import com.arvin.koalapush.potter.ReceivedListener;
import com.ems.express.App;
import com.ems.express.adapter.message.SendNoticeBean;
import com.ems.express.adapter.message.SignMessageBean;
import com.ems.express.bean.DeliveryMessageBean;
import com.ems.express.bean.MessageCenterBean;
import com.ems.express.bean.OfflineMessageRep;
import com.ems.express.constant.Constant;
import com.ems.express.core.msg.MessageManager;
import com.ems.express.net.MyRequest;
import com.ems.express.net.MyRequest2;
import com.ems.express.ui.MessageCenterActivity;
import com.ems.express.ui.check.ResultActivity;
import com.ems.express.ui.message.MessageActivity;
import com.ems.express.util.DateTimeUtil;
import com.ems.express.util.NotificationUtil;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.SpfsUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.text.format.DateUtils;
import android.util.Log;

public class ChatService extends Service implements OnPreparedListener {

	private ChatBinder mChatBinder = new ChatBinder();
	static String TAG = "ChatService";
	private MsgContentBean entity;
	private MediaPlayer mMediaPlayer = null;
	/** 录音存储路径 */
	private static final String PATH = "/sdcard/express/speechAccept/";
	private PlayState state = PlayState.Stop;
	public PlayState playState = PlayState.Error;
	double longitude;
	double latitude;
	String channelId;
	String employeeNo;
	String people;
	String code;
	String sid;
	String orgcode;
	String username;
	String mobile;

	public enum PlayState {
		Stop, Buffering, Playing, Pause, Error,
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.e("chatService", "ChatService is onBind");
		return mChatBinder;
	}

	public class ChatBinder extends Binder {
		public ChatService getService() {
			return ChatService.this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.e("chatService", "ChatService is onCreate");
		setUpPlayer();
		App.addListener(new ReceivedListener() {
			@Override
			public String onReceived(Object arg0) {
				Log.e("IM", "onReceived:" + String.valueOf(arg0));
				if ("SDK初始化成功".equals(String.valueOf(arg0))) {
					connectionQuery();
					return "noIsSync";
				}
				ReceivedMsgBean bean = (ReceivedMsgBean) arg0;
				entity = bean.getMessageEntity();
				if ("1".equals(entity.getMessageType())
						|| "2".equals(entity.getMessageType())) {
					setting(entity, null);
					return entity.getMessage_id();
				} else {
					connectionQuery();
				}
				return "noIsSync";
			}

			@Override
			public void onError(Object arg0) {
				Log.d("IM", "onError:" + String.valueOf(arg0));
			}
		});

	}

	public void setting(MsgContentBean entity, OfflineMessageRep messageRep) {
		if ("0".equals(entity.getMessageType())
				|| "1".equals(entity.getMessageType())
				|| "2".equals(entity.getMessageType())) {
			int speechMessageTime = 0;
			if (messageRep != null) {
				if (messageRep.getResCode().equals("0")) {
					if (messageRep.getOfflineMsgList() != null
							&& messageRep.getOfflineMsgList().size() > 0) {
						for (int i = 0; i < messageRep.getOfflineMsgList()
								.size(); i++) {
							String url = "";
							if (messageRep.getOfflineMsgList().get(i).getUrl() != null
									&& !"".equals(messageRep
											.getOfflineMsgList().get(i)
											.getUrl())) {
								url = messageRep.getOfflineMsgList().get(i)
										.getUrl().replace("\\", "/");
								speechMessageTime = getSpeechMessageTime(url);
							}
							String employeeNo = queryEmployeeNoIsClientId(messageRep
									.getOfflineMsgList().get(i).getSource());
							if (employeeNo != null) {
								int id = App.dbHelper.insertMessage(App.db,
										employeeNo, "1", "0", "0", messageRep
												.getOfflineMsgList().get(i)
												.getMessage_type(), messageRep
												.getOfflineMsgList().get(i)
												.getContent(), null,
										new Date(), url, speechMessageTime);
								if (url != null && !"".equals(url)) {
									dowloadSpeech(url, id + "");
								}
								if (id > 0) {
									MessageManager.getInstance().asyncNotify(
											"0");
								}
							} else {
								Log.e(TAG,
										"client_id："
												+ messageRep
														.getOfflineMsgList()
														.get(i).getSource()
												+ "在本地没有聊天记录，原因是客户没有与快递员主动联系，本地没有存储，所有快递员不能给客户端主动发消息");
							}
						}

						notificationChat();
					}
				}
			} else {
				String url = "";
				if (entity.getUrl() != null && !"".equals(entity.getUrl())) {
					url = entity.getUrl().replace("\\", "/");
					speechMessageTime = getSpeechMessageTime(url);
				}
				String employeeNo = queryEmployeeNoIsClientId(entity
						.getSource());
				if (employeeNo != null) {
					int id = App.dbHelper.insertMessage(App.db, employeeNo,
							"1", "0", "0", entity.getMessageType(), null, null,
							new Date(), url, speechMessageTime);
					dowloadSpeech(url, id + "");
					if (id > 0) {
						notificationChat();
						MessageManager.getInstance().asyncNotify("0");
					}
				}
			}
		} else if ("3".equals(entity.getMessageType()) /*|| "5".equals(entity.getMessageType())*/) {
			Log.e("msg", "返回数据" + JSONObject.toJSONString(messageRep));
			if (messageRep != null) {
				if (messageRep.getOfflineMsgList() != null
						&& messageRep.getOfflineMsgList().size() > 0) {
					Log.e("msg", "content"
							+ messageRep.getOfflineMsgList().get(0)
									.getContent());
					JSONObject obj = JSONObject.parseObject(messageRep
							.getOfflineMsgList().get(0).getContent());
					if ("2".equals(obj.getString("messageType"))) {
						orgcode = obj.getJSONObject("content")
								.getJSONObject("body").getJSONObject("success")
								.getString("orgcode");
						username = obj.getJSONObject("content")
								.getJSONObject("body").getJSONObject("success")
								.getString("username");
						String mailNum = obj.getJSONObject("content")
								.getJSONObject("body").getJSONObject("success")
								.getString("mailNum");
						queryFindEmlpoyeeMessageByPhone(orgcode, username,mailNum);

					} else if ("5".equals(obj.getString("messageType"))) {
						
						JSONObject jsonObject = obj.getJSONObject("content").getJSONObject("body").getJSONObject("success");
						String mailStatus = jsonObject.getString("orderStatus");
						if("10".equals(mailStatus)){
							String date = DateTimeUtil.validateDateTime(new Date());
							String messageStatus = "1d";
							String mailNum = jsonObject.getString("mailNum");
							//添加已妥投数据
							App.dbHelper.insertSendNotice2(App.db, date,
										messageStatus, mailStatus, mailNum);
							boolean background = NotificationUtil
									.isBackground(getApplicationContext());
							NotificationUtil notificationUtil = NotificationUtil
									.getNotificationUtil();
							if (background == true
									|| SpfsUtil.loadPhone().equals("")) {
								notificationUtil.setNotification(
										getApplication(), "2");
							}
							MessageManager.getInstance().asyncNotify("2");
							
						}else if("3".equals(mailStatus)){
							//邮递员取件推送处理
							String orderNo = jsonObject.getString("orderNo");
//							String orderStatusDesc = jsonObject.getString("orderStatusDesc");
							String employeeNo = jsonObject.getString("employeeNo");
							String orgCode = jsonObject.getString("orgCode");
							querySignMessage(orgCode, employeeNo,orderNo,mailStatus);
						}
					}
				}
			}
		}
	}
	
	private void querySignMessage(final String orgCode, final String employeeNo,final String orderNo,final String mailStatus) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("orgcode", orgCode);
		map.put("username", employeeNo);
		GsonPostParamsRequest<SignMessageBean> rep = new GsonPostParamsRequest<SignMessageBean>(
				Method.POST, Constant.QueryNextSection, null,
				new Listener<SignMessageBean>() {

					@Override
					public void onResponse(SignMessageBean arg0) {
						Log.e("msg", JSONObject.toJSONString(arg0));
						if (arg0 != null) {
							if ("1".equals(arg0.getResult())) {
								SendNoticeBean dmb = arg0.getBody()
										.getSuccess();

								double longitude2 = dmb.getLongitude();
								double latitude2 = dmb.getLatitude();
								String clientId = dmb.getChannelId();
								String employeeNo2 = dmb.getEmployeeNo();
								String people2 = dmb.getPeople();
								String code2 = dmb.getCode();
								String sid2 = dmb.getSid();
								String mobile2 = dmb.getMobile();
//								String mailStatus = "3";

								SimpleDateFormat dateFormat = new SimpleDateFormat(
										"yyyy-MM-dd HH:mm:ss");
								String format = dateFormat.format(new Date());

								App.dbHelper.insertSendNotice(App.db, format,
										"1", longitude2, latitude2, mobile2,
										clientId, employeeNo2, people2, code2,
										sid2, "",mailStatus,orderNo, orgCode, employeeNo);
								boolean background = NotificationUtil
										.isBackground(getApplicationContext());
								NotificationUtil notificationUtil = NotificationUtil
										.getNotificationUtil();
								if (background == true
										|| SpfsUtil.loadPhone().equals("")) {
									notificationUtil.setNotification(
											getApplication(), "2");
								}
								MessageManager.getInstance().asyncNotify("2");

							} else if ("0".equals(arg0.getResult())) {
								Log.e("IM", arg0.getBody().getError());
							}
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						Log.e(TAG, "查询信息失败" + arg0.getMessage());
					}
				}, SignMessageBean.class, map);
		App.getQueue().add(rep);
	}

	private void insertCode() {

		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("orgcode", orgcode);
		json.put("username", username);
		json.put("mobile", SpfsUtil.getString("phone"));
		String params = ParamsUtil.getUrlParamsByMap(json);

		MyRequest<Object> request = new MyRequest<Object>(Method.POST, null,
				Constant.MailInfo, new Listener<Object>() {
					@Override
					public void onResponse(Object arg0) {
						JSONObject obj = JSONObject.parseObject(arg0.toString());
						if ("1".equals(obj.getString("result"))) {
							JSONObject jsonObject = obj.getJSONObject("body");
							JSONObject jsonObject2 = jsonObject
									.getJSONObject("success");
							String string = jsonObject2.getString("mailNum");

							SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							Date date = new Date();
							String format = simpleDateFormat.format(date);

							if (orgcode == null || username == null
									|| people == null) {
								return;
							} else {
								if (mobile == null) {
									App.dbHelper.insertDeliveryMessage(App.db,
											longitude, latitude, "", channelId,
											employeeNo, people, code, sid,
											null, "1", format, "1", string,
											orgcode, username, "1");
									boolean background = NotificationUtil
											.isBackground(getApplicationContext());
									NotificationUtil notificationUtil = NotificationUtil
											.getNotificationUtil();
									if (background == true
											|| SpfsUtil.loadPhone().equals("")) {
										notificationUtil.setNotification(
												getApplication(), "1");
									}
									MessageManager.getInstance().asyncNotify(
											"2");
								} else {
									App.dbHelper.insertDeliveryMessage(App.db,
											longitude, latitude, mobile,
											channelId, employeeNo, people,
											code, sid, null, "1", format, "1",
											string, orgcode, username, "1");
									boolean background = NotificationUtil
											.isBackground(getApplicationContext());
									NotificationUtil notificationUtil = NotificationUtil
											.getNotificationUtil();
									if (background == true
											|| SpfsUtil.loadPhone().equals("")) {
										notificationUtil.setNotification(
												getApplication(), "1");
									}
									MessageManager.getInstance().asyncNotify(
											"2");
								}
							}
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						arg0.printStackTrace();
					}
				}, params);
		App.getQueue().add(request);
	}

	/**
	 * 查询下段的信息
	 * 
	 * @param phoneNum
	 */
	public void queryFindEmlpoyeeMessageByPhone(final String orgcode,
			final String username,final String mailNum) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("orgcode", orgcode);
		map.put("username", username);
		GsonPostParamsRequest<MessageCenterBean> rep = new GsonPostParamsRequest<MessageCenterBean>(
				Method.POST, Constant.QueryNextSection, null,
				new Listener<MessageCenterBean>() {

					@Override
					public void onResponse(MessageCenterBean arg0) {
						Log.e("msg", JSONObject.toJSONString(arg0));
						if (arg0 != null) {
							if ("1".equals(arg0.getResult())) {
								DeliveryMessageBean dmb = arg0.getBody()
										.getSuccess();

								longitude = dmb.getLongitude();
								latitude = dmb.getLatitude();
								channelId = dmb.getChannelId();
								employeeNo = dmb.getEmployeeNo();
								people = dmb.getPeople();
								code = dmb.getCode();
								sid = dmb.getSid();
								mobile = dmb.getMobile();
								
								String date = DateTimeUtil.validateDateTime(new Date());
								
								if (mobile == null) {
									App.dbHelper.insertDeliveryMessage(App.db,
											longitude, latitude, "", channelId,
											employeeNo, people, code, sid,
											null, "1", date, "1", mailNum,
											orgcode, username, "1");
								} else {
									App.dbHelper.insertDeliveryMessage(App.db,
											longitude, latitude, mobile,
											channelId, employeeNo, people,
											code, sid, null, "1", date, "1",
											mailNum, orgcode, username, "1");
								}
								
								boolean background = NotificationUtil
										.isBackground(getApplicationContext());
								NotificationUtil notificationUtil = NotificationUtil
										.getNotificationUtil();
								if (background == true
										|| SpfsUtil.loadPhone().equals("")) {
									notificationUtil.setNotification(
											getApplication(), "1");
								}
								MessageManager.getInstance().asyncNotify(
										"2");

//								insertCode();
							} else if ("0".equals(arg0.getResult())) {
								Log.e("IM", arg0.getBody().getError());
							}
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						Log.e(TAG, "查询下段信息失败" + arg0.getMessage());
					}
				}, MessageCenterBean.class, map);
		App.getQueue().add(rep);
	}

	/**
	 * 接受推送后，查询未读消息
	 */
	public void connectionQuery() {
		JSONObject object = new JSONObject();
		try {
			object.put("client_id", Kpush.getInstence().getClientId());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MyRequest2<OfflineMessageRep> req = new MyRequest2<OfflineMessageRep>(
				Method.POST, OfflineMessageRep.class, Constant.Query,
				new Listener<OfflineMessageRep>() {

					@Override
					public void onResponse(OfflineMessageRep arg0) {
						// ToastUtil.show(getApplicationContext(),
						// JSONObject.toJSONString(arg0));
						Log.e("msg", "返回数据" + JSONObject.toJSONString(arg0));
						if (entity == null) {
							entity = new MsgContentBean();
							entity.setMessageType("0");
						}
						setting(entity, arg0);
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						arg0.printStackTrace();
					}
				}, object.toString());	
		App.getQueue().add(req);
	}

	public int getSpeechMessageTime(String url) {
		int speechMessageTime = 0;
		if (url != null && !"".equals(url)) {
			try {
				App.getmChatService().getmMediaPlayer().reset();
				App.getmChatService().getmMediaPlayer().setDataSource(url);
				App.getmChatService().getmMediaPlayer().prepare();
				speechMessageTime = App.getmChatService().getmMediaPlayer()
						.getDuration() / 1000;
				// App.mToastUtil.show(speechMessageTime+"");
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return speechMessageTime;
	}

	/**
	 * 下载语音
	 * 
	 * @param url
	 * @param messageId
	 */
	public void dowloadSpeech(final String url, final String messageId) {
		String fileName = PATH + UUID.randomUUID().toString() + ".amr";
		HttpUtils http = new HttpUtils();
		http.download(url, fileName, true, true, new RequestCallBack<File>() {

			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				App.dbHelper.updateMessageUrl(App.db, arg0.result.getPath(),
						messageId);
				MessageManager.getInstance().asyncNotify("0");
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {

			}
		});

	}

	public String queryEmployeeNoIsClientId(String clientId) {
		return App.dbHelper.queryEmployeeNoIsClientId(App.db, clientId);
	}

	public void sendBaiduPush(String message) {
		// Log.e(TAG, message);
		// if(message!=null||!"".equals(message)){
		// JSONObject obj = JSONObject.parseObject(message);
		// JSONObject mobileObj = obj.getJSONObject("custom_content");
		// if("4".equals(mobileObj.getString("messageType"))){
		// getPushMessage(mobileObj.getString("mobile"));
		// }
		// }
	}

	private synchronized void setUpPlayer() {
		if (mMediaPlayer != null) {
			try {
				mMediaPlayer.setOnPreparedListener(null);
				mMediaPlayer.stop();
			} catch (IllegalStateException e) {
			}
			mMediaPlayer.release();
		}
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setOnPreparedListener(this);
	}

	public MediaPlayer getmMediaPlayer() {
		return mMediaPlayer;
	}

	/**
	 * 播放语音
	 * 
	 * @param url
	 *            语音地址
	 * @param bo
	 *            true本地语音 false网络语音
	 */
	public void playSpeech(final String url, final boolean bo) {
		new Handler().post(new Runnable() {

			@Override
			public void run() {
				try {
					if (bo) {
						mMediaPlayer.setDataSource(url);
					} else {
						mMediaPlayer.setDataSource(getApplicationContext(),
								Uri.parse(url));
					}
					mMediaPlayer.prepareAsync();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void notificationChat() {
		boolean background = NotificationUtil
				.isBackground(getApplicationContext());
		NotificationUtil notificationUtil = NotificationUtil
				.getNotificationUtil();
		if (background == true) {
			notificationUtil.setNotification(getApplication(), "3");
		}
	}

	public void stopSpeech() {
		mMediaPlayer.stop();
		state = PlayState.Stop;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.e("chatService", "ChatService is onStart");
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		state = PlayState.Playing;
		if (playState.equals(PlayState.Buffering)) {
			mp.start();
		}
		playState = PlayState.Error;
	}

	public synchronized PlayState getPlaySate() {
		if (mMediaPlayer != null && mMediaPlayer.isPlaying())
			state = PlayState.Playing;
		return state;
	}
}

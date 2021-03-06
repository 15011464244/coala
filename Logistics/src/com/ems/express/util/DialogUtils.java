package com.ems.express.util;

import im.fir.sdk.version.AppVersion;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient.ConnectCallback;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.baidu.mapapi.model.LatLng;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.adapter.message.SendNoticeBean;
import com.ems.express.bean.ChatListItemBean;
import com.ems.express.bean.DeliveryMessageBean;
import com.ems.express.bean.MailInfo;
import com.ems.express.bean.PeopleInfo;
import com.ems.express.constant.Constant;
import com.ems.express.net.Carrier;
import com.ems.express.net.MyRequest;
import com.ems.express.net.ServicePoint;
import com.ems.express.net.UrlUtils;
import com.ems.express.ui.Home2Activity;
import com.ems.express.ui.LoginActivity;
import com.ems.express.ui.RegisterActivity;
import com.ems.express.ui.chat.MainActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class DialogUtils{
	private static Dialog dialogfuj;
	private static Dialog dialog ;
	public static Dialog getServicePointDialog(ServicePoint point,
			final Context context) {
		dialog = new Dialog(context,R.style.DialogStyle4);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_service_point, null);
		TextView title = (TextView) view.findViewById(R.id.dialog_title);
		title.setText(point.getAUTHNAME());
		TextView people = (TextView) view.findViewById(R.id.dialog_people);
		people.setText("联系人："+point.getCONTACTNAME());
		TextView mobile = (TextView) view.findViewById(R.id.dialog_mobile);
		mobile.setText("电话："+point.getCONTACTMOBILE());
		TextView address = (TextView) view.findViewById(R.id.dialog_address);
		address.setText("地址："+point.getADDRESS());
		TextView startTime = (TextView) view.findViewById(R.id.dialog_start_time);
		startTime.setText("开门时间：09:00");
		TextView stopTime = (TextView) view.findViewById(R.id.dialog_stop_time);
		stopTime.setText("关门时间：18:00");
		final String strmobile = point.getCONTACTMOBILE();
		view.findViewById(R.id.dialog_call).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+strmobile));
				 context.startActivity(intent);//内部类
				 dialog.dismiss();
			}
		});
		dialog.setContentView(view);
		dialog.show();
		return dialog;
	}
	
	public static Dialog getCarrierDialog(final Carrier point,
			final Context context,LatLng mylocaltion) {
		//融云的token
		final String token = "OhyNtlNKch8S7Tte+b1FsVFLSVXAg7Gh8iQKGtH8crvais9T1CXsOGHjDbaSSQP3L+CypE1DURe8i0tWgB8aOg==";
		dialogfuj = new Dialog(context,R.style.DialogStyle4);
		dialogfuj.requestWindowFeature(Window.FEATURE_NO_TITLE);

		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_carrier, null);
		TextView people = (TextView) view.findViewById(R.id.dialog_people);
		people.setText(point.getPeople());
		TextView mobile = (TextView) view.findViewById(R.id.dialog_mobile);
		mobile.setText("电话:"+point.getMobile());
		
		TextView distane = (TextView) view.findViewById(R.id.dialog_distance);
		distane.setText("距我 " + DistanceUtils.covert(DistanceUtils.GetShortDistance(point.getLongitude(), point.getLatitude()
				, mylocaltion.longitude, mylocaltion.latitude)));
		ImageView imageView = (ImageView)view.findViewById(R.id.img);
		if(!point.getSID().equals("null")&&!point.getSID().equals("")){
			Log.e("gongjie", "头像是sid"+point.getSID());
			String url = UrlUtils.URL_CARRIER_IMG+"?sid=" + point.getSID();
			LogUtil.print("获取头像地址："+url);
			DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.defualt_header)
			.showImageForEmptyUri(R.drawable.ic_empty)
			.showImageOnFail(R.drawable.ic_error)
			.displayer(new RoundedBitmapDisplayer(5))
			.cacheOnDisc(true)   
			.build();
		  ImageLoader.getInstance().displayImage(url, imageView, options);
		}else{
			Bitmap aa = BitmapFactory.decodeResource(context.getResources(), R.drawable.defualt_header);
			imageView.setImageBitmap(aa);
		}
		view.findViewById(R.id.dialog_notebook).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(point.getClientId().equals("null")||point.getClientId()==null||point.getClientId().equals("")){
					dialogfuj.dismiss();
					ToastUtil.show(context, "不能与离线快递员聊天");
					Log.e("DialogUtils", "clientId为空不能聊天");
					return;
				}
				App.dbHelper.insertChatList(App.db, point);
				RongIM.connect(token, new ConnectCallback() {

					@Override
					public void onError(ErrorCode arg0) {
						Toast.makeText(context, "connect onError", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(String arg0) {
						Toast.makeText(context, "connect onSuccess", Toast.LENGTH_SHORT).show();
						/**
						 * 设置用户信息的提供者，供 RongIM 调用获取用户名称和头像信息。
						 *
						 * @param userInfoProvider 用户信息提供者。
						 * @param isCacheUserInfo  设置是否由 IMKit 来缓存用户信息。<br>
						 *                         如果 App 提供的 UserInfoProvider。
						 *                         每次都需要通过网络请求用户数据，而不是将用户数据缓存到本地内存，会影响用户信息的加载速度；<br>
						 *                         此时最好将本参数设置为 true，由 IMKit 将用户信息缓存到本地内存中。
						 * @see UserInfoProvider
						 */
						RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

						    @Override
						    public UserInfo getUserInfo(String userId) {
						    	Log.e("gongjie", "userId"+userId);
						    	String dlvorgcode = "33004607";
						    	String username = "9401";
						    	String url = "http://111.75.223.93:9008/post-carrier-service/PhoneAction/findEmployeeImage?"+"dlvorgcode="+dlvorgcode+"&username="+username;
						    	UserInfo info = new UserInfo(userId, "gongjie", Uri.parse(url));
						        return info;//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
						    }

						}, true);
						ChatListItemBean bean = new ChatListItemBean();
						bean.setImage(point.getSID());
						bean.setMobile(point.getMobile());
						bean.setName(point.getPeople());
						bean.setClientId(point.getClientId());
						RongIM.getInstance().startConversation(context, Conversation.ConversationType.PRIVATE, "11", "聊天标题");
					}

					@Override
					public void onTokenIncorrect() {
						// TODO Auto-generated method stub
						
					}
				});
				
				
//				MainActivity.startAction(context, bean);
				dialogfuj.dismiss();
			}
		});
		dialogfuj.setContentView(view);
		dialogfuj.show();
		return dialogfuj;
	}
	public static Dialog getNullCommentDialog(final Context context,String str) {
		Dialog dialog = new Dialog(context,R.style.DialogStyle4);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_null_comment, null);
		TextView tip = (TextView) view.findViewById(R.id.tip);
		tip.setText(str);
		dialog.setContentView(view);
		dialog.show();
		return dialog;
	}
	//评价，寄件成功弹出dialog
	public static Dialog successMessage(final Context context,String str) {
		Dialog dialog = new Dialog(context,R.style.DialogStyle4);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//设置成系统级别弹窗
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.setCancelable(true);
		
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_null_comment, null);
		TextView tip = (TextView) view.findViewById(R.id.tip);
		tip.setText(str);
		dialog.setContentView(view);
		dialog.show();
		return dialog;
	}
	//点击收费之后的显示的支付途径
	    static int checkId = 0; 
		public static Dialog getPaymentDialog(final Context context,String str) {
			final Dialog dialog = new Dialog(context,R.style.DialogStyle4);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			View view = LayoutInflater.from(context).inflate(R.layout.dialog_payment, null);
			Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
			Button btn_makesure = (Button) view.findViewById(R.id.btn_makesure);
			RelativeLayout rl_wx = (RelativeLayout) view.findViewById(R.id.rl_wx);
			RelativeLayout rl_zfb = (RelativeLayout) view.findViewById(R.id.rl_zfb);
			RelativeLayout rl_money = (RelativeLayout) view.findViewById(R.id.rl_money);
			final ImageView iv_wx_right = (ImageView) view.findViewById(R.id.iv_wx_right);
			final ImageView iv_zfb_right = (ImageView) view.findViewById(R.id.iv_zfb_right);
			final ImageView iv_face2face_right = (ImageView) view.findViewById(R.id.iv_face2face_right);
			rl_wx.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					iv_wx_right.setBackgroundResource(R.drawable.color);
					iv_zfb_right.setBackgroundResource(R.drawable.nocolor);
					iv_face2face_right.setBackgroundResource(R.drawable.nocolor);
					checkId = 1;
				}
			});
			rl_zfb.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					iv_wx_right.setBackgroundResource(R.drawable.nocolor);
					iv_zfb_right.setBackgroundResource(R.drawable.color);
					iv_face2face_right.setBackgroundResource(R.drawable.nocolor);
					checkId = 2;
				}
			});
			rl_money.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					iv_wx_right.setBackgroundResource(R.drawable.nocolor);
					iv_zfb_right.setBackgroundResource(R.drawable.nocolor);
					iv_face2face_right.setBackgroundResource(R.drawable.color);
					checkId = 3;
				}
			});
			btn_makesure.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					switch (checkId) {
					   // 没有选择
					case 0:
						
						Toast.makeText(context, "您还没有选择支付方式", Toast.LENGTH_LONG).show();
						checkId = 0;
						break;
						// 选择了微信
					case 1:
						checkId = 0;
						break;
						// 选择了支付宝
					case 2:
						checkId = 0;
						break;
						// 选择了现金支付
					case 3:
						Toast.makeText(context, "您选择了现金支付", Toast.LENGTH_LONG).show();
						checkId = 0;
						break;

					default:
						break;
						
					}
					
				}
			});
			dialog.setContentView(view);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
			return null;
		}
	
	public static Dialog showTwoButton(final Context context){
		final Dialog dialog = new Dialog(context,R.style.DialogStyle4);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
		TextView content = (TextView) view.findViewById(R.id.dialog_content);
		content.setText("您确定要注销吗？");
		Button buttonCancel = (Button) view.findViewById(R.id.dialog_cancel);
		buttonCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		Button buttonDetermine = (Button) view.findViewById(R.id.dialog_determine);
		buttonDetermine.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SpfsUtil.saveAddress("");
				SpfsUtil.saveHeadImageUrl("");
				SpfsUtil.saveName("");
				SpfsUtil.saveId("");
				SpfsUtil.savePhone("");
				SpfsUtil.saveTelephone("");
				//清除查询中autocompletetextview里的缓存
				SpfsUtil.saveHistory("");
				PeopleInfo info = new PeopleInfo();
				info.setName("");
				info.setPhone("");
				info.setProv("");
				info.setCounty("");
				info.setCity("");
				info.setProvCode("");
				info.setCityCode("");
				info.setCountyCode("");
				info.setLocation("");
				SpfsUtil.saveDefaultSender(info);
//				((TextView)HomeActivity.slideMenu.findViewById(R.id.btn_regist)).setText("登录");
				Bitmap aa = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_head);
//				HomeActivity.mIconView.setImageBitmap(aa);
//				HomeActivity.mUserNameView.setText("");
				dialog.dismiss();
				Intent intent = new Intent(context,LoginActivity.class);
				context.startActivity(intent);
			}
		});
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		return null;
	}
	
	//是否清除缓存
	public static Dialog showTwoButton2(final Context context){
		final Dialog dialog = new Dialog(context,R.style.DialogStyle4);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
		TextView content = (TextView) view.findViewById(R.id.dialog_content);
		content.setText("清除的数据不能恢复！\n您确定要清除缓存吗？");
		Button buttonCancel = (Button) view.findViewById(R.id.dialog_cancel);
		buttonCancel.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		Button buttonDetermine = (Button) view.findViewById(R.id.dialog_determine);
		buttonDetermine.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			     App.dbHelper.deleteAllCarrier(App.db);
			     App.dbHelper.deleteAllMessage(App.db);
			     App.dbHelper.deleteAllDeliveryMessage(App.db);
			     App.dbHelper.deleteAllSendMessage(App.db);
			     //清除查询中autocompletetextview里的缓存
			     SpfsUtil.saveHistory("");
				ToastUtil.show(context, "成功清除缓存");
			}
		});
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		return null;
	}
	
	public static Dialog getMessageDialog(
			final Context context,List<DeliveryMessageBean> dmList,int position) {
		dialog = new Dialog(context,R.style.DialogStyle4);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_message_center, null);
		TextView title = (TextView) view.findViewById(R.id.dialog_message_title);
		title.setText("快递员");
		TextView people = (TextView) view.findViewById(R.id.dialog_message_people);
		people.setText("联系人："+dmList.get(position).getPeople());
		TextView mobile = (TextView) view.findViewById(R.id.dialog_message_mobile);
		mobile.setText("电话："+dmList.get(position).getMobile());
		
		
		
		view.findViewById(R.id.message_dialog_call).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel: 150111111111"));
				 context.startActivity(intent);//内部类
				 dialog.dismiss();
			}
		});
		dialog.setContentView(view);
		dialog.show();
		return dialog;
	}
	
public static boolean isMobileNO(String mobiles){  
//	Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//	Pattern p = Pattern.compile("((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)");
	Pattern p = Pattern.compile("^[1][3,4,5,7,8]\\d{9}$");
//	Pattern p1 = Pattern.compile("^(((0\\d{3}[\\-])?\\d{7}|(0\\d{2}[\\-])?\\d{8}))([\\-]\\d{2,4})?$");
	
	
//	Matcher m1 = p1.matcher(mobiles);  
	Matcher m = p.matcher(mobiles);  

	return m.matches();
	} 
public static boolean isPhoneNO(String mobiles){  
//	Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//	Pattern p = Pattern.compile("((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)");
	Pattern p = Pattern.compile("^[1][3,4,5,7,8]\\d{9}$");
	Pattern p1 = Pattern.compile("^(((0\\d{3}[\\-])?\\d{7}|(0\\d{2}[\\-])?\\d{8}))([\\-]\\d{2,4})?$");
	
	
	Matcher m1 = p1.matcher(mobiles);  
	Matcher m = p.matcher(mobiles);  
	if (m.matches()|m1.matches()) {
		return true;
	}
	return false;
	} 
/**
 * 退单确认
 * @param context
 * @return
 */
public static Dialog turnBackConfirm(final Context context,final String sid,final AnimationUtil animationUtil,/*刷新用到*/final List<MailInfo> mList){
	final Dialog dialog = new Dialog(context,R.style.DialogStyle4);
	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
	TextView content = (TextView) view.findViewById(R.id.dialog_content);
	content.setText("您确定要取消寄件订单吗？");
	Button buttonCancel = (Button) view.findViewById(R.id.dialog_cancel);
	buttonCancel.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			dialog.dismiss();
		}
	});
	Button buttonDetermine = (Button) view.findViewById(R.id.dialog_determine);
	buttonDetermine.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			dialog.dismiss();
			RequestUtil.turnBackStyle(context, sid,animationUtil,mList);
		}
	});
	dialog.setContentView(view);
	dialog.setCanceledOnTouchOutside(false);
	dialog.show();
	return null;
}



/**
 * 更新确认
 * @param context
 * @return
 */
public static Dialog updateConfirm(final Context context,final String path){
	final Dialog dialog = new Dialog(context,R.style.DialogStyle4);
	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
	TextView content = (TextView) view.findViewById(R.id.dialog_content);
	content.setText("检测到新版本，是否更新？\n版本更新消耗流量，建议在WiFi下进行更新！");
	
	Button buttonCancel = (Button) view.findViewById(R.id.dialog_cancel);
	buttonCancel.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			dialog.dismiss();
		}
	});
	Button buttonDetermine = (Button) view.findViewById(R.id.dialog_determine);
	buttonDetermine.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			dialog.dismiss();
			
		}
	});
	dialog.setContentView(view);
	dialog.setCanceledOnTouchOutside(false);
	dialog.show();
	return null;
}
//删除确认
public static Dialog deleteConfirm(final Context context,final String path){
	final Dialog dialog = new Dialog(context,R.style.DialogStyle4);
	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
	TextView content = (TextView) view.findViewById(R.id.dialog_content);
	content.setText("确认要删除此信息吗？\n(若未收件，建议保留)");;
	
	Button buttonCancel = (Button) view.findViewById(R.id.dialog_cancel);
	buttonCancel.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			dialog.dismiss();
		}
	});
	Button buttonDetermine = (Button) view.findViewById(R.id.dialog_determine);
	buttonDetermine.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			dialog.dismiss();
		}
	});
	dialog.setContentView(view);
	dialog.setCanceledOnTouchOutside(false);
	dialog.show();
	return null;
}

public static void downApp(String updateUrl){
	
}

/**
 * 给快递员电话确认
 * @param context
 * @return
 */
public static Dialog callConfirm(final Context context,final String phone){
	final Dialog dialog = new Dialog(context,R.style.DialogStyle4);
	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
	TextView content = (TextView) view.findViewById(R.id.dialog_content);
	content.setText("拨号："+phone);
	
	Button buttonCancel = (Button) view.findViewById(R.id.dialog_cancel);
	buttonCancel.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			dialog.dismiss();
		}
	});
	Button buttonDetermine = (Button) view.findViewById(R.id.dialog_determine);
	buttonDetermine.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			dialog.dismiss();
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            context.startActivity(intent);
		}
	});
	dialog.setContentView(view);
	dialog.setCanceledOnTouchOutside(false);
	dialog.show();
	return null;
}


/**
 * 应用退出确认
 * @param context
 * @return
 */
	public static Dialog outConfirm(final Context context){
		final Dialog dialog = new Dialog(context,R.style.DialogStyle4);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
		TextView content = (TextView) view.findViewById(R.id.dialog_content);
		content.setText("您确定要退出应用？");
		Button buttonCancel = (Button) view.findViewById(R.id.dialog_cancel);
		buttonCancel.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		Button buttonDetermine = (Button) view.findViewById(R.id.dialog_determine);
		buttonDetermine.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				((Activity)context).finish();
//				System.exit(0);
			}
		});
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		return  null;
	}
	
	
/**
 * 通知提示框
 * @param context
 * @param Content
 * @param btnStr
 * @return
 */
	public static Dialog noticeDialog(final Context context,final String Content,String btnStr){
		final Dialog dialog = new Dialog(context,R.style.DialogStyle4);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_notice, null);
		TextView content = (TextView) view.findViewById(R.id.dialog_content);
		content.setText(Content);
		
		Button buttonDetermine = (Button) view.findViewById(R.id.dialog_determine);
		buttonDetermine.setText(btnStr);
		buttonDetermine.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.setContentView(view);
		dialog.show();
		return null;
	}
	
	/**
	 * 通知提示框
	 * @param context
	 * @param Content
	 * @param btnStr
	 * @param title
	 * @return
	 */
		public static Dialog noticeDialog(final Context context,final String Content,String btnStr,String title){
			final Dialog dialog = new Dialog(context,R.style.DialogStyle4);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			
			View view = LayoutInflater.from(context).inflate(R.layout.dialog_notice2, null);
			TextView titleView = (TextView) view.findViewById(R.id.dialog_title2);
			titleView.setVisibility(View.VISIBLE);
			titleView.setText(title);
			View line = (View)view.findViewById(R.id.line_1);
			line.setVisibility(view.VISIBLE);
					
			TextView content = (TextView) view.findViewById(R.id.dialog_content);
			content.setText(Content);
			
			Button buttonDetermine = (Button) view.findViewById(R.id.dialog_determine);
			buttonDetermine.setText(btnStr);
			buttonDetermine.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			dialog.setContentView(view);
			dialog.show();
			return null;
		}
	/**
	 * 签到成功
	 * @return
	 */
	public static Dialog signSuccess(final Context context,final int score,final int days){
		final Dialog dialog = new Dialog(context,R.style.dialog_circle_sign);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_sign_success, null);
		
		TextView content = (TextView) view.findViewById(R.id.dialog_content);
		String str = "已签到"+days+"天，共累计"+score+"分";
		
		int index1 = str.indexOf("到")+1;
		int index2 = str.indexOf("天");
		
		int index3 = str.indexOf("计")+1;
		int index4 = str.indexOf("分");
		
//		int index5 = str.length() - 8;
//		int index6 = str.length() - 6;
		
		SpannableStringBuilder style=new SpannableStringBuilder(str);  
        style.setSpan(new ForegroundColorSpan(0xfffff500),index1,index2,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
        style.setSpan(new ForegroundColorSpan(0xfffff500),index3,index4,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
//        style.setSpan(new ForegroundColorSpan(0xfffff500),index5,index6,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
        
        content.setText(style); 
        
		view.findViewById(R.id.iv_get_score).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				
			}
		});
		
		dialog.setContentView(view);
		dialog.show();
		
		return null;
	}
	
	/**
	 * 注册成功
	 * @return
	 */
	public static Dialog registSuccess(final Context context){
		final Dialog dialog = new Dialog(context,R.style.dialog_circle_sign);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_regist_success, null);
		view.findViewById(R.id.iv_get_score).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
//				((RegisterActivity)context).finish();
				Intent intent = new Intent(context, Home2Activity.class);
				context.startActivity(intent);
				
			}
		});
		dialog.setCanceledOnTouchOutside(false);
//		view.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//				
//			}
//		});
		
		dialog.setContentView(view);
		dialog.show();
		
		return null;
	}

	/**
	 * 二维码dialog
	 * @return 
	 */
	public static Dialog getQrcodeDialog(Context context) {
		dialog = new Dialog(context,R.style.DialogStyle4);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_qrcode, null);
		dialog.setContentView(view);
		dialog.show();
		return dialog;
	}
	
	public static void getImageUrl(Context context) {
		
		final AnimationUtil util = new AnimationUtil(context, R.style.dialog_animation);
		util.show();

		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("signer_name", SpfsUtil.getString("name"));
		String params = ParamsUtil.getUrlParamsByMap(json);

		MyRequest<Object> erq = new MyRequest<Object>(Method.POST, null,
				Constant.SignHandle, new Listener<Object>() {
					@Override
					public void onResponse(Object arg0) {
						JSONObject obj = JSONObject.parseObject(arg0.toString());
						if ("1".equals(obj.getString("result"))) {
							util.dismiss();
						} else if ("0".equals(obj.getString("result"))) {
							util.dismiss();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						arg0.printStackTrace();
						util.dismiss();
					}
				}, params);

		App.getQueue().add(erq);
	}
}

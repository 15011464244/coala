package com.ems.express.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.adapter.message.SendNoticeBean;
import com.ems.express.bean.ChatListItemBean;
import com.ems.express.bean.DeliveryMessageBean;
import com.ems.express.bean.MailInfo;
import com.ems.express.net.Carrier;
import com.ems.express.net.ServicePoint;
import com.ems.express.net.UrlUtils;
import com.ems.express.ui.HomeActivity;
import com.ems.express.ui.LoginActivity;
import com.ems.express.ui.chat.MainActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

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
			String url = UrlUtils.URL_CARRIER_IMG+"?sid=" + point.getSID();
			LogUtil.print("获取头像地址："+url);
			ImageLoader.getInstance().displayImage(url, imageView);	
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
				ChatListItemBean bean = new ChatListItemBean();
				bean.setImage(point.getSID());
				bean.setMobile(point.getMobile());
				bean.setName(point.getPeople());
				bean.setClientId(point.getClientId());
				MainActivity.startAction(context, bean);
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
		
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_null_comment, null);
		TextView tip = (TextView) view.findViewById(R.id.tip);
		tip.setText(str);
		dialog.setContentView(view);
		dialog.show();
		return dialog;
	}
	
	public static Dialog showTwoButton(final Context context){
		final Dialog dialog = new Dialog(context,R.style.DialogStyle4);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_two_button, null);
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
				((TextView)HomeActivity.slideMenu.findViewById(R.id.btn_regist)).setText("登录");
				Bitmap aa = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_head);
				HomeActivity.mIconView.setImageBitmap(aa);
				HomeActivity.mUserNameView.setText("");
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
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_clear_confirm, null);
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
	Pattern p = Pattern.compile("((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)");
	Matcher m = p.matcher(mobiles);  
	return m.matches(); 
	} 

/**
 * 退单确认
 * @param context
 * @return
 */
public static Dialog turnBackConfirm(final Context context,final String sid,final AnimationUtil animationUtil,/*刷新用到*/final List<MailInfo> mList){
	final Dialog dialog = new Dialog(context,R.style.DialogStyle4);
	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	View view = LayoutInflater.from(context).inflate(R.layout.dialog_turnback_confirm, null);
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


}

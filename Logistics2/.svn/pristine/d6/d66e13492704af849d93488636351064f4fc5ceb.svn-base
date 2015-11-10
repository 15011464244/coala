package com.ems.express.adapter.message;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.bean.DeliveryMessageBean;
import com.ems.express.constant.Constant;
import com.ems.express.net.MyRequest;
import com.ems.express.scan.activity.CaptureActivity;
import com.ems.express.ui.BaiduMapActivity;
import com.ems.express.ui.message.MessageActivity;
import com.ems.express.util.AnimationUtil;
import com.ems.express.util.DialogUtils;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;

public class MailDeliverAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private static Context mContext;
	private static List<DeliveryMessageBean> mList;
	private Dialog dialog;
	private String messageIsSign = "1";
	private static ViewHolder holder;
	private static AnimationUtil util;

	private static MailDeliverAdapter MyAdapter;

	public MailDeliverAdapter(Context context, List<DeliveryMessageBean> list) {
		this.mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.mList = list;
		this.MyAdapter = this;
		this.util = new AnimationUtil(mContext, R.style.dialog_animation);
	}

	@Override
	public int getCount() {
		if (mList != null) {
			return mList.size();
		}
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final int i = position;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.mail_deliver_item, null);
		}
		holder = (ViewHolder) convertView.getTag();
		if (holder == null) {
			holder = new ViewHolder();
			holder.commonName = (TextView) convertView
					.findViewById(R.id.tv_news_item);
			holder.courierName = (TextView) convertView
					.findViewById(R.id.tv_courier_item);
			holder.phoneNumber = (TextView) convertView
					.findViewById(R.id.tv_phone__item);
			holder.expressTime = (TextView) convertView
					.findViewById(R.id.tv_fragment_time);
			holder.fragmentLayout = (LinearLayout) convertView
					.findViewById(R.id.fragment_layout);
			holder.fragmentLayoutsign = (TextView) convertView
					.findViewById(R.id.fragment_layout_sign);
			holder.fragmentImg = (ImageView) convertView
					.findViewById(R.id.fragment_reddot);
			holder.courier = (TextView) convertView
					.findViewById(R.id.tv_courier);
			holder.handle = (TextView) convertView.findViewById(R.id.tv_handle);
			holder.handleOther = (TextView) convertView.findViewById(R.id.tv_other);

			convertView.setTag(holder);
		}
		//邮递员位置
		holder.courier.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				expressCourier(i);
				MessageActivity.setPromptMessage();
			}
		});
		//扫件签收
		holder.handle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 跳转到扫件页面
				Intent intent = new Intent(mContext, CaptureActivity.class);
				intent.putExtra("mailNum", mList.get(i).getMailNum());
				intent.putExtra("index", i);
				intent.putExtra("type", CaptureActivity.RECEIVE_MAIL);
				((Activity) mContext).startActivityForResult(intent, 999);
				// 签收
				// receiptHandle(i);
				MessageActivity.setPromptMessage();
			}
		});
		//其他签收
		holder.handleOther.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				receiptHandleOther(i);
				MessageActivity.setPromptMessage();
			}
			
		});

		String receiptStatus = mList.get(i).getReceiptStatus();
		if (receiptStatus.equals("0")) {
			holder.fragmentLayout.setVisibility(View.GONE);
		} else {
			holder.fragmentLayout.setVisibility(View.VISIBLE);
		}
		int delId = mList.get(i).getDelId();
		String messageStatus = App.dbHelper.seleteDeliveryMessageRed(App.db, ""
				+ delId);
		String seleteMessageRed = App.dbHelper.seleteMessageRed(App.db, ""
				+ delId);
		if (messageStatus.equals("0")) {
			holder.fragmentImg.setVisibility(View.GONE);
		} else {
			holder.fragmentImg.setVisibility(View.VISIBLE);
		}
		if (seleteMessageRed.equals("0")) {
			holder.fragmentLayoutsign.setVisibility(View.VISIBLE);
		} else {
			holder.fragmentLayoutsign.setVisibility(View.GONE);
		}

		if (mList != null) {
			holder.expressTime.setText("" + mList.get(i).getMessageTime()+"     邮件号："+mList.get(i).getMailNum());
			holder.commonName.setTextColor(Color.RED);
			holder.commonName.setText("2");
			holder.courierName.setText("快递员:" + mList.get(i).getPeople());
			if (mList.get(position).getMobile() == null
					|| "".equals(mList.get(position).getMobile())) {
				holder.phoneNumber.setText("电话:");
			} else {
				holder.phoneNumber.setText("电话:" + mList.get(i).getMobile());
			}
		}
		return convertView;
	}

	static class ViewHolder {
		TextView commonName, courierName, phoneNumber, expressTime, courier,
				handle, fragmentLayoutsign,handleOther;
		LinearLayout fragmentLayout;
		ImageView fragmentImg;
	} 

	private void expressCourier(final int courierId) {
		setReddot(courierId);

		Intent intent = new Intent(mContext, BaiduMapActivity.class);
		intent.putExtra(BaiduMapActivity.KEY_TYPE,
				BaiduMapActivity.TYPE_CARRIER);
		intent.putExtra("LONGITUDE", mList.get(courierId).getLongitude());
		intent.putExtra("LATITUDE", mList.get(courierId).getLatitude());
		intent.putExtra("orgcode", mList.get(courierId).getOrgcode());
		intent.putExtra("username", mList.get(courierId).getUsername());
		intent.putExtra("phoneNum", mList.get(courierId).getMobile());
		intent.putExtra("deliveryMessage", mList.get(courierId));
		intent.putExtra("messageIsSign", messageIsSign);
		intent.putExtra("activity", "messageCenter");
		mContext.startActivity(intent);
		this.notifyDataSetChanged();
	}
	/**
	 * 邮件签收
	 * @param handleId
	 */
	public static void receiptHandle(final int handleId) {
		setReddot(handleId);
		MessageActivity.setPromptMessage();
		util.show();

		HashMap<String, Object> json = new HashMap<String, Object>();
		json.put("mail_num", mList.get(handleId).getMailNum());
		json.put("signer_name", SpfsUtil.getString("name"));
		String params = ParamsUtil.getUrlParamsByMap(json);

		MyRequest<Object> erq = new MyRequest<Object>(Method.POST, null,
				Constant.SignHandle, new Listener<Object>() {

					@Override
					public void onResponse(Object arg0) {
						JSONObject obj = JSONObject.parseObject(arg0.toString());
						if ("1".equals(obj.getString("result"))) {
							int delId = mList.get(handleId).getDelId();
							App.dbHelper.updateDeliveryMessageIsDelId(App.db,
									"" + delId);
							App.dbHelper.updateMessageIsDelId(App.db, ""
									+ delId);
							mList.get(handleId).setReceiptStatus("0");
							mList.get(handleId).setSignStatus("0");
							mList.get(handleId).getReceiptStatus();
							MyAdapter.notifyDataSetChanged();
							DialogUtils.successMessage(mContext, "签收成功!");
							util.dismiss();
						} else if ("0".equals(obj.getString("result"))) {
							DialogUtils.successMessage(mContext, "签收失败!");
							util.dismiss();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						arg0.printStackTrace();
						ToastUtil.show(mContext, "签收异常，请检查您的网络");
						util.dismiss();
					}
				}, params);

		App.getQueue().add(erq);
		MyAdapter.notifyDataSetChanged();
	}
	/*
	 * 	签收其他
	 */
	private void receiptHandleOther(final int handleId) {
		dialog = new Dialog(mContext,R.style.DialogStyle2);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View myView = LayoutInflater.from(mContext).inflate(
				R.layout.dialog_message_fragment, null);
		
		Button mobile = (Button) myView.findViewById(R.id.message_dialog_fragment);
		
		mobile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setReddot(handleId);
				MessageActivity.setPromptMessage();
				util.show();
				
				HashMap<String, Object> json = new HashMap<String, Object>();
				json.put("mail_num",mList.get(handleId).getMailNum());
				json.put("signer_name", SpfsUtil.getString("name"));
				String params = ParamsUtil.getUrlParamsByMap(json);
				
				MyRequest<Object> erq = new MyRequest<Object>(Method.POST, null,
						Constant.SignHandle,
						new Listener<Object>() {

							@Override
							public void onResponse(Object arg0) {
								JSONObject obj = JSONObject.parseObject(arg0.toString());
								if ("1".equals(obj.getString("result"))) {
									int delId = mList.get(handleId).getDelId();
									App.dbHelper.updateDeliveryMessageIsDelId(App.db,""+delId);
									App.dbHelper.updateMessageIsDelId(App.db,""+delId);
									mList.get(handleId).setReceiptStatus("0");
									mList.get(handleId).setSignStatus("0");
									mList.get(handleId).getReceiptStatus();
									MyAdapter.notifyDataSetChanged();
									Toast.makeText(mContext, "签收成功", 1).show();
									util.dismiss();
									dialog.dismiss();
								}else if("0".equals(obj.getString("result"))){
									Toast.makeText(mContext, "签收失败", 1).show();
									dialog.dismiss();
									util.dismiss();
								}
							}
						}, new ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError arg0) {
								arg0.printStackTrace();
								util.dismiss();
								dialog.dismiss();
							}
						}, params);
				
				App.getQueue().add(erq);
				
				notifyDataSetChanged();
			}
		});
		dialog.setContentView(myView);
		dialog.show();
	}
	

	public static void setReddot(int handelId) {
		int delId = mList.get(handelId).getDelId();
		App.dbHelper.updateDeliveryMessageRed(App.db, "" + delId);
		mList.get(handelId).setMessageStatus("0");
		String messageStatus = App.dbHelper.seleteDeliveryMessageRed(App.db, ""
				+ delId);
		if (messageStatus.equals("0")) {
			holder.fragmentImg.setVisibility(View.GONE);
		}
	}
}

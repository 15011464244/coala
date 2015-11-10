package com.ems.express.adapter.message;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ems.express.R;
import com.ems.express.global.GlobalVar;
import com.ems.express.ui.BaiduMapActivity;
import com.ems.express.ui.check.ResultActivity;

public class MailMeassageAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context mContext;
	private List<SendNoticeBean> mList;
	private String messageIsSign = "2";
	private ViewHolder holder ;
	
	public MailMeassageAdapter(Context context,List<SendNoticeBean> list ) {
		this.mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.mList = list;
	}

	@Override
	public int getCount() {
		if(mList != null){
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		final int index = position;
		SendNoticeBean bean = mList.get(index);
		String mailStatus = bean.getMailStatus();
//		final String sid = bean.getSid();
		Log.e("msggg", "mailState:"+bean.getMailStatus());
		if("10".equals(mailStatus)){
			//已妥投处理
			convertView = hand10(convertView, bean);
		}else if("3".equals(mailStatus)){
			//已取件处理
			convertView = hand3(mContext,convertView, bean);
		}
		return convertView;
	} 
	/*
	 * 已妥投处理
	 */
	public View hand10(View convertView, final SendNoticeBean bean){
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.mail_message_item, null);
			holder.commonData = (TextView) convertView.findViewById(R.id.tv_message_item);
			holder.messageName = (TextView) convertView.findViewById(R.id.tv_message_below_item);
			holder.courier=(TextView) convertView.findViewById(R.id.textView1_1111111);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
			holder.commonData = (TextView) convertView.findViewById(R.id.tv_message_item);
			holder.messageName = (TextView) convertView.findViewById(R.id.tv_message_below_item);
			holder.courier=(TextView) convertView.findViewById(R.id.textView1_1111111);
		}
		
		if(mList != null){
			holder.commonData.setText(""+bean.getMessageTime());
			String mailNum = bean.getMailNum();
			holder.messageName.setText("您的"+mailNum+"邮件已经成功送达！");
			holder.courier.setText("详情");
		}
		
		holder.courier.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ResultActivity.actionStart(mContext, bean.getMailNum(), "4");
			}
		});
		return convertView;
	}
	/*
	 * 已取件处理
	 */
	public View hand3(Context context,View convertView, final SendNoticeBean bean){
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.mail_message_item, null);
			holder.commonData = (TextView) convertView.findViewById(R.id.tv_message_item);
			holder.messageName = (TextView) convertView.findViewById(R.id.tv_message_below_item);
			holder.courier=(TextView) convertView.findViewById(R.id.textView1_1111111);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(mList != null){
		holder.commonData.setText(""+bean.getMessageTime());
		holder.messageName.setText(/*bean.getOrderNo()+*/"快递员已收到寄件信息请耐心等待快递员上门收取!");
		holder.courier.setText("快递员位置");
		}
		
		
		holder.courier.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,BaiduMapActivity.class);
				intent.putExtra(BaiduMapActivity.KEY_TYPE,
						BaiduMapActivity.TYPE_CARRIER);
				intent.putExtra("LONGITUDE",bean.getLongitude());
				intent.putExtra("LATITUDE",bean.getLatitude());
				intent.putExtra("orgcode",bean.getOrgcode());
				intent.putExtra("username",bean.getUsername());
				intent.putExtra("phoneNum",bean.getMobile());
				intent.putExtra("sendNoticeBean",bean);
				intent.putExtra("messageIsSign", messageIsSign);
				intent.putExtra("activity", "signMessage");
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}
	
	class ViewHolder{
		TextView commonData;
		TextView messageName;
		TextView courier;
	}
}

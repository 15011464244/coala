package com.ems.express.adapter.mail;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.bean.MailInfo;
import com.ems.express.constant.Constant;
import com.ems.express.global.GlobalVar;
import com.ems.express.net.MyRequest;
import com.ems.express.ui.SettingActivity;
import com.ems.express.util.AnimationUtil;
import com.ems.express.util.DateTimeUtil;
import com.ems.express.util.DeviceUtil;
import com.ems.express.util.DialogUtils;
import com.ems.express.util.ParamsUtil;
import com.ems.express.util.RequestUtil;
import com.ems.express.util.SpfsUtil;
import com.ems.express.util.ToastUtil;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MailHistoryItemAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context mContext;
	private List<MailInfo> mList;
	
	private AnimationUtil animationUtil;
	
	public MailHistoryItemAdapter(Context context,List<MailInfo> list) {
		this.mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.mList = list;
		this.animationUtil = new AnimationUtil(context, R.style.dialog_animation);
	}

	@Override
	public int getCount() {
		if(mList != null){
			return mList.size();
		}
		return 0;
	}
	@Override
	public Object getItem(int position) {
		return null;
	}
	@Override
	public long getItemId(int position) {
		return 0;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int index = position;
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.mail_history_item, null);
			holder.commonName = (TextView) convertView.findViewById(R.id.tv_history_item);
			holder.urge = (TextView) convertView.findViewById(R.id.tv_history_urge);
			holder.middleLine = (View) convertView.findViewById(R.id.tv_middle_line);
			holder.invalid = (TextView) convertView.findViewById(R.id.tv_history_invalid);
			//催单
			holder.urge.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.e("msgggSid", mList.get(index).getSid());
					RequestUtil.urge(mContext,mList.get(index).getSid(),animationUtil);
				}
				
			});
			holder.middleLine.setOnClickListener(null);
			//退单
			holder.invalid.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.e("msgggSid", mList.get(index).getSid());
					DialogUtils.turnBackConfirm(mContext,mList.get(index).getSid(),animationUtil,mList);
				}
			});
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(mList != null){
			holder.commonName.setText("订单号："+mList.get(position).getSid());
			//判定催单是否隐藏
			if("3".equals(mList.get(position).getMailState())){
				//快递员已经收到寄件信息
				GlobalVar.getInstance().urgeVisibility.put(position, View.VISIBLE);
			}
			// 给存储状态赋值
			if (null != GlobalVar.getInstance().urgeVisibility.get(position)
			&& GlobalVar.getInstance().urgeVisibility.get(position) != View.GONE) {
				holder.urge.setVisibility(View.VISIBLE);
			} else {
				holder.urge.setVisibility(View.GONE);
			}
			
		}
		return convertView;
	}  
				
	
	class ViewHolder{
		TextView commonName;
		TextView invalid;
		View middleLine;
		TextView urge;
	}
	
	

}

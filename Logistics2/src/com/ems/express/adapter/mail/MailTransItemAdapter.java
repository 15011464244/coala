package com.ems.express.adapter.mail;

import java.util.List;

import com.ems.express.R;
import com.ems.express.bean.MailInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MailTransItemAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context mContext;
	private List<String> mList;
	
	public MailTransItemAdapter(Context context,List<String> list) {
		this.mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.mList = list;
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
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.mail_follow_item, null);
			holder.commonName = (TextView) convertView.findViewById(R.id.tv_follow_item);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(mList != null){
				holder.commonName.setText("运单号："+mList.get(position));
		}
		return convertView;
	}  
	
	class ViewHolder{
		TextView commonName;
	}

	
}

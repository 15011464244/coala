package com.cn.net.cnpl.tools;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.net.cnpl.R;

public class MultiAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	
	private List<Map<String, String>> list = null;

	public MultiAdapter(Context context,List<Map<String, String>> list) {
		mInflater = LayoutInflater.from(context);
		this.list=list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;

		if (convertView == null) {

			holder = new Holder();
			convertView = mInflater.inflate(R.layout.connet_compitem2, null);
			holder.num = (TextView) convertView.findViewById(R.id.num);
			holder.mailid = (TextView) convertView.findViewById(R.id.mailid);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			holder.type = (TextView) convertView.findViewById(R.id.type);
			holder.responsible_id = (TextView) convertView.findViewById(R.id.responsible_id);
			convertView.setTag(holder);
		} else {

			holder = (Holder) convertView.getTag();

		}

		holder.num.setText(list.get(position).get("num").toString());
		holder.mailid.setText(list.get(position).get("mailid").toString());
		holder.date.setText(list.get(position).get("date").toString());
		holder.type.setText(list.get(position).get("type").toString());
		LinearLayout lay = (LinearLayout) convertView.findViewById (R.id.vlayout);
		if("".equals(list.get(position).get("principal").toString())){
			lay.setVisibility(View.GONE);
		}else{
			lay.setVisibility(View.VISIBLE);
			holder.responsible_id.setText(list.get(position).get("principal").toString());
		}

		return convertView;

	}
	
    private class Holder {
		public TextView num;
		public TextView mailid;
		public TextView date;
		public TextView type;
		TextView responsible_id;
	}
}

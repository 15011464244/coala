package com.cn.net.cnpl.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.net.cnpl.R;

public class MycheckAdapter extends BaseAdapter{

	private LayoutInflater mInflater;
	
	private List<Map<String, String>> list = null;

	public static Map<Integer, Boolean> isSelected;

	public MycheckAdapter(Context context,List<Map<String, String>> listtemp) {
		mInflater = LayoutInflater.from(context);
		isSelected = new HashMap<Integer, Boolean>();
		list = listtemp;
		for (int i=0; i<list.size(); i++) {
			isSelected.put(i, false);
		}
	}
	public void setlist(List<Map<String, String>> listtemp){
		list = listtemp;
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
		ViewHolder holder = null;

		if (convertView == null) {

			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.out_selectitem, null);
			holder.cBox = (CheckBox) convertView.findViewById(R.id.cb);
			holder.num = (TextView) convertView.findViewById(R.id.num);
			holder.mailno_txt = (TextView) convertView.findViewById(R.id.conout);
			holder.type_txt = (TextView) convertView.findViewById(R.id.time);
			holder.principal = (TextView) convertView.findViewById(R.id.total);
			convertView.setTag(holder);
		} else {

			holder = (ViewHolder) convertView.getTag();

		}

		holder.num.setText(list.get(position).get("num").toString());
		holder.mailno_txt.setText(list.get(position).get("mailno_txt").toString());
		holder.type_txt.setText(list.get(position).get("type_txt").toString());
		LinearLayout lay = (LinearLayout) convertView.findViewById (R.id.vlayout);
		if("".equals(list.get(position).get("principal").toString())){
			lay.setVisibility(View.GONE);
		}else{
			lay.setVisibility(View.VISIBLE);
			holder.principal.setText(list.get(position).get("principal").toString());
		}

		holder.cBox.setChecked(isSelected.get(position));
		return convertView;

	}
	
	public final class ViewHolder {
		public CheckBox cBox;
		public TextView num;
		public TextView mailno_txt;
		public TextView type_txt;
		public TextView principal;
	}

}

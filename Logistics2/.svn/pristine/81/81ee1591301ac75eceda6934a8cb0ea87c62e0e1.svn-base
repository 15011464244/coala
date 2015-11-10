package com.ems.express.ui.check;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ems.express.R;
import com.ems.express.bean.ExpressBean;
import com.ems.express.util.LogUtil;

/**
 * 快递公司列表
 */
public class ExpressAdapter extends BaseAdapter {
	private List<ExpressBean> mData;
	private LayoutInflater mInflater;
	private Context mContext;

	public ExpressAdapter(Context context, List<ExpressBean> mData) {
		super();
		this.mData = mData;
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
	}

	/**
	 * Update all the data
	 * 
	 * @param mData
	 *            the new data of list
	 */
	public void updateData(List<ExpressBean> mData) {
		this.mData = mData;
		notifyDataSetChanged();
	}

	/**
	 * add a list of data to adapter's data
	 * 
	 * @param mData
	 *            a list of data
	 */
	public void append(List<ExpressBean> mData) {
		this.mData.addAll(mData);
		notifyDataSetChanged();
	}

	/**
	 * add a single data to adapter's data
	 * 
	 * @param mData
	 *            a single data
	 */
	public void append(ExpressBean mData) {
		this.mData.add(mData);
		notifyDataSetChanged();
	}

	/**
	 * clear all of Adapter's data
	 */
	public void clear() {
		this.mData.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public ExpressBean getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.adapter_express, null);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		if (holder == null) {
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		}
			holder.icon.setImageResource(Integer.parseInt(mData.get(position)
					.getImage()));
//			holder.icon.setImageResource(R.drawable.test_adapter_icon3);
			holder.name.setText(mData.get(position).getName());
		return convertView;
	}

	class ViewHolder {
		ImageView icon;
		TextView name;
	}

}

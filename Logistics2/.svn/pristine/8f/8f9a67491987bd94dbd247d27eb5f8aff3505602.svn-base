package com.ems.express.ui.send;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ems.express.R;
import com.ems.express.bean.CourierBean;

/**
 * 周边快递员 Adapter
 */
// TODO AdapterTemplate->Adapter name
// TODO Bean-> Adapter data
public class NearbyAdapter extends BaseAdapter {
	private List<CourierBean> mData;
	private LayoutInflater mInflater;
	private Context mContext;

	public NearbyAdapter(Context context, List<CourierBean> mData) {
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
	public void updateData(List<CourierBean> mData) {
		this.mData = mData;
		notifyDataSetChanged();
	}

	/**
	 * add a list of data to adapter's data
	 * 
	 * @param mData
	 *            a list of data
	 */
	public void append(List<CourierBean> mData) {
		this.mData.addAll(mData);
		notifyDataSetChanged();
	}

	/**
	 * add a single data to adapter's data
	 * 
	 * @param mData
	 *            a single data
	 */
	public void append(CourierBean mData) {
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
	public CourierBean getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.adapter_nearby, null);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		if (holder == null) {
			holder = new ViewHolder();
			holder.name = (TextView) convertView
					.findViewById(R.id.courier_name);
			holder.phone = (TextView) convertView
					.findViewById(R.id.courier_phone);
			convertView.setTag(holder);
		}
		holder.name.setText(mData.get(position).getName());
		holder.phone.setText(mData.get(position).getPhone());

		return convertView;
	}

	class ViewHolder {
		TextView name;
		TextView phone;
	}

}

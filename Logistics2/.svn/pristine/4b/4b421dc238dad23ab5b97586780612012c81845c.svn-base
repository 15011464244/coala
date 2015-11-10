package com.ems.express.ui.check;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ems.express.R;
import com.ems.express.bean.ExpressRecordBean;

/**
 * 查询结果 Adapter
 */
// TODO AdapterTemplate->Adapter name
// TODO Bean-> Adapter data
public class ResultAdapter extends BaseAdapter {
	private List<ExpressRecordBean> mData = new ArrayList<ExpressRecordBean>();
	private LayoutInflater mInflater;
	private Context mContext;

	public ResultAdapter(Context context, List<ExpressRecordBean> mData) {
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
	public void updateData(List<ExpressRecordBean> mData) {
		this.mData = mData;
		notifyDataSetChanged();
	}

	/**
	 * add a list of data to adapter's data
	 * 
	 * @param mData
	 *            a list of data
	 */
	public void append(List<ExpressRecordBean> mData) {
		this.mData.addAll(mData);
		notifyDataSetChanged();
	}

	/**
	 * add a single data to adapter's data
	 * 
	 * @param mData
	 *            a single data
	 */
	public void append(ExpressRecordBean mData) {
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
	public ExpressRecordBean getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.adapter_result, null);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		if (holder == null) {
			holder = new ViewHolder();
			holder.time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.target = (TextView) convertView.findViewById(R.id.source_target);
			holder.receiver = (TextView) convertView.findViewById(R.id.receiver);
			convertView.setTag(holder);
		}
		holder.time.setText(mData.get(position).getTime());
//		holder.target.setText("从" + mData.get(position).getTarget() + "的派件已经签收");
		holder.receiver.setText(mData.get(position).getWho());

		return convertView;
	}

	static class ViewHolder {
		TextView time;
		TextView target;
		TextView receiver;
	}

}

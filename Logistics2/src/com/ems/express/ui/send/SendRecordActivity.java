package com.ems.express.ui.send;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.ems.express.R;
import com.ems.express.util.ToastUtil;

/**
 * 寄件记录
 */
public class SendRecordActivity extends Activity implements OnClickListener {
	@InjectView(R.id.record)
	ListView mLvRecord;// 查件记录列表
	@InjectView(R.id.tv_info)
	TextView mTvInfo;

	private ArrayAdapter<String> mAdapter;
	private Context mContext;

	public static void actionStart(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, SendRecordActivity.class);
		context.startActivity(intent);
	}

	public void back(View v) {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_record);
		ButterKnife.inject(this);
		mContext = this;
		((TextView) findViewById(R.id.tv_title)).setText("寄件历史");
		initView();
		loadData();
	}

	public void initView() {
		mTvInfo.setText("编辑");
		mTvInfo.setVisibility(View.VISIBLE);
		mTvInfo.setOnClickListener(this);
	}

	public void loadData() {
		ArrayList<String> data = new ArrayList<String>();
		data.add("运单号:123123");
		data.add("运单号:123123");
		data.add("运单号:123123");
		mAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1);
		mAdapter.addAll(data);
		mLvRecord.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_info:
			ToastUtil.show(mContext, "编辑");
			break;
		default:
			break;
		}
	}
}

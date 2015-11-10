package com.ems.express.ui.settle;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ems.express.R;
import com.ems.express.bean.SettleRecordBean;
import com.ems.express.ui.check.ResultActivity;
/**
 * 理赔记录
 */
public class SettleRecordActivity extends Activity implements OnClickListener {
	private Context mContext = SettleRecordActivity.this;
	private ImageView mIvBack;// 返回键
	private RelativeLayout mRelExpress;// 选择快递公司
	private TextView mTvExpress;// 选择快递公司
	private ListView mLvList;// 快递公司列表
	private ListView mLvRecord;// 查件记录列表
	private ImageView mIvSelect;// 选择快递公司箭头

	private SettleRecordAdapter mSettleRecordAdapter;
	private List<SettleRecordBean> mData = new ArrayList<SettleRecordBean>();

	
	public static void startAction(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, SettleRecordActivity.class);
		context.startActivity(intent);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settle_record);

		initView();

		loadData();
	}

	public void loadData() {
		SettleRecordBean bean1 = new SettleRecordBean("12213111","5.00");
		SettleRecordBean bean2 = new SettleRecordBean("12213111","5.00");
		SettleRecordBean bean3 = new SettleRecordBean("12213111","5.00");

		mData.add(bean1);
		mData.add(bean2);
		mData.add(bean3);
		mSettleRecordAdapter = new SettleRecordAdapter(SettleRecordActivity.this, mData);
		mLvRecord.setAdapter(mSettleRecordAdapter);
		mSettleRecordAdapter.notifyDataSetChanged();

	}

	public void initView() {
		mIvBack = (ImageView) findViewById(R.id.back_button);
		mRelExpress = (RelativeLayout) findViewById(R.id.record_express);
		mTvExpress = (TextView) findViewById(R.id.express_text);
		mLvList = (ListView) findViewById(R.id.list);
		mIvSelect = (ImageView) findViewById(R.id.select);
		mLvRecord = (ListView) findViewById(R.id.record);

		mIvBack.setOnClickListener(this);
		mRelExpress.setOnClickListener(this);

		
		mLvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});
		mLvRecord.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(mContext, ResultActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
			finish();
			break;

		case R.id.finish:
			if (mLvRecord.getVisibility() == View.GONE) {
				mLvRecord.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.undone:
			if (mLvRecord.getVisibility() == View.VISIBLE) {
				mLvRecord.setVisibility(View.GONE);
			}
			break;
		case R.id.record_express:
			if (mLvList.getVisibility() == View.GONE) {
				mLvList.setVisibility(View.VISIBLE);
				mIvSelect.setImageResource(R.drawable.back_button);
			} else {
				mLvList.setVisibility(View.GONE);
				mIvSelect.setImageResource(R.drawable.next);
			}
			break;
		default:
			break;
		}
	}
}

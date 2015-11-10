package com.ems.express.ui.check;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ems.express.R;
import com.ems.express.bean.ExpressRecordBean;
/**
 * 查件记录
 */
public class RecordActivity extends Activity implements OnClickListener {
//	private Context mContext = RecordActivity.this;
	private ImageView mIvBack;// 返回键
	private LinearLayout mLinSettle;// 理赔
	private ImageView mIvIcon;// icon
	private TextView mTvNumber;// 单号
//	private LinearLayout mLinCare;// 关注
	private ListView mLvRecord;// 查件记录列表

	private ResultAdapter mAdapter;
	private List<ExpressRecordBean> mData = new ArrayList<ExpressRecordBean>();
	
	public static void startAction(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, RecordActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);

		initView();
		loadData();
	}

	public void initView() {
		mIvBack = (ImageView) findViewById(R.id.back_button);
		mLinSettle = (LinearLayout) findViewById(R.id.settle);
		mIvIcon = (ImageView) findViewById(R.id.icon);
		mTvNumber = (TextView) findViewById(R.id.number);
//		mLinCare = (LinearLayout) findViewById(R.id.care);
		mLvRecord = (ListView) findViewById(R.id.list);

		mIvBack.setOnClickListener(this);
		mLinSettle.setOnClickListener(this);
//		mLinCare.setOnClickListener(this);
		mIvIcon.setOnClickListener(this);
		mTvNumber.setOnClickListener(this);

		
	}

	public void loadData() {
		ExpressRecordBean bean1 = new ExpressRecordBean("3535", "韵达", "110",
				"2015-01-26 17:48:56", "1", "北京", "2", "上海", "张三",
				"13889877344");
		ExpressRecordBean bean2 = new ExpressRecordBean("3534", "顺丰", "100",
				"2015-01-26 17:48:56", "1", "杭州", "2", "北京", "张三",
				"13889877344");
		ExpressRecordBean bean3 = new ExpressRecordBean("3532", "中通", "180",
				"2015-01-26 17:48:56", "1", "天津", "2", "山西", "张三",
				"13889877344");

		mData.add(bean1);
		mData.add(bean2);
		mData.add(bean3);
		mAdapter = new ResultAdapter(RecordActivity.this, mData);
		mLvRecord.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
			finish();
			break;
		case R.id.settle:
			break;
//		case R.id.care:
//			ToastUtil.showTip(mContext, "您已关注了此订单\n实时订单跟踪提醒开始");
//			break;
		case R.id.icon:
			break;
		case R.id.number:
			break;
		default:
			break;
		}
	}
}

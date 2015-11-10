package com.ems.express.ui.check;

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

import com.ems.express.R;
import com.ems.express.bean.ExpressRecordBean;

/**
 * 淘宝快递汇总
 */
public class GatherActivity extends Activity implements OnClickListener {
	private Context mContext = GatherActivity.this;
	private ImageView mIvBack;// 返回键
	private ListView mLvGather;// 返回键
	
	private RecordAdapter mRecordAdapter;
	private List<ExpressRecordBean> mData = new ArrayList<ExpressRecordBean>();
	
	public static void startAction(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, GatherActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gather);

		initView();
		
		loadData();
	}

	public void initView() {
		mIvBack = (ImageView) findViewById(R.id.back_button);
		mLvGather = (ListView) findViewById(R.id.list);

		mIvBack.setOnClickListener(this);
		mLvGather.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ResultActivity.startAction(mContext);
			}
		});
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
		mRecordAdapter.notifyDataSetChanged();
		mRecordAdapter = new RecordAdapter(mContext, mData);
		mLvGather.setAdapter(mRecordAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
			finish();
			break;

		default:
			break;
		}
	}
}

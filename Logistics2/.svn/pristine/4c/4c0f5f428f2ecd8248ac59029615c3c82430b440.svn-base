package com.ems.express.ui.send;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.ems.express.R;
import com.ems.express.bean.CourierBean;
/**
 * 周边快递员
 */
public class NearbyActivity extends Activity implements OnClickListener {
	private Context mContext = NearbyActivity.this;
	private ImageView mIvBack;// 返回键
	private ListView mLvCourier;// 返回键

	private List<CourierBean> mData = new ArrayList<CourierBean>();// 快递员数据
	private NearbyAdapter mNearbyAdapter;// 返回键

	public static void startAction(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, NearbyActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearby);

		initView();
		loadData();
	}

	public void initView() {
		mIvBack = (ImageView) findViewById(R.id.back_button);
		mLvCourier = (ListView) findViewById(R.id.list_courier);

		mIvBack.setOnClickListener(this);

	}

	public void loadData() {
		CourierBean bean1 = new CourierBean("001", "", "快递员1", "18647343328");
		CourierBean bean2 = new CourierBean("002", "", "快递员2", "18874534939");
		CourierBean bean3 = new CourierBean("003", "", "快递员3", "15734437752");
		CourierBean bean4 = new CourierBean("004", "", "快递员4", "13945383493");

		mData.add(bean1);
		mData.add(bean2);
		mData.add(bean3);
		mData.add(bean4);
		mNearbyAdapter = new NearbyAdapter(mContext, mData);
		mLvCourier.setAdapter(mNearbyAdapter);
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

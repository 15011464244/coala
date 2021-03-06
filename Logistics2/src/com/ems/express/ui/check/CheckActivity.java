package com.ems.express.ui.check;

import java.io.Serializable;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ems.express.R;
import com.ems.express.bean.ExpressBean;
import com.ems.express.ui.settle.SettleActivity;
import com.ems.express.ui.settle.SettleRecordActivity;

/**
 * 我要查件
 */
public class CheckActivity extends Activity implements OnClickListener {
	private Context mContext = CheckActivity.this;
	private ImageView mIvBack;// 返回键
	private RelativeLayout mRelExpress;// 选择快递公司
	private TextView mTvExpress;// 选择快递公司
	private ImageView mIvSelect;// 选择快递公司箭头
	private EditText mEdtNumber;// 单号
	private ImageView mIvVoice;// 语音
	private ImageView mIvScan;// 扫一扫
	private Button mBtnSubmit;// 提交
	private Button mBtnRecord;// 查件记录
	private ListView mLvList;// 快递公司列表

	private List<ExpressBean> mData = new ArrayList<ExpressBean>();// 快递公司数据
	private ExpressAdapter mAdapter;
	
	public static void startAction(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, CheckActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check);

		initView();

		loadData();

	}

	public void loadData() {
		ExpressBean bean = new ExpressBean();
		bean.setCode("0001");
		bean.setImage(R.drawable.test_adapter_icon1 + "");
		bean.setName("顺丰快递");
		ExpressBean bean1 = new ExpressBean();
		bean1.setCode("0002");
		bean1.setImage(R.drawable.test_adapter_icon2 + "");
		bean1.setName("EMS速运");
		ExpressBean bean2 = new ExpressBean();
		bean2.setCode("0003");
		bean2.setImage(R.drawable.test_adapter_icon3 + "");
		bean2.setName("圆通速递");

		mData.add(bean);
		mData.add(bean1);
		mData.add(bean2);
		mAdapter.notifyDataSetChanged();
	}

	public void initView() {
		mIvBack = (ImageView) findViewById(R.id.back_button);
		mRelExpress = (RelativeLayout) findViewById(R.id.express);
		mTvExpress = (TextView) findViewById(R.id.express_text);
		mIvSelect = (ImageView) findViewById(R.id.select);
		mEdtNumber = (EditText) findViewById(R.id.number);
		mIvVoice = (ImageView) findViewById(R.id.voice);
		mIvScan = (ImageView) findViewById(R.id.scan);
		mBtnSubmit = (Button) findViewById(R.id.submit);
		mBtnRecord = (Button) findViewById(R.id.record);
		mLvList = (ListView) findViewById(R.id.list);

		mIvBack.setOnClickListener(this);
		mRelExpress.setOnClickListener(this);
		mIvVoice.setOnClickListener(this);
		mIvScan.setOnClickListener(this);
		mBtnSubmit.setOnClickListener(this);
		mBtnRecord.setOnClickListener(this);
		
		mAdapter = new ExpressAdapter(CheckActivity.this, mData);
		mLvList.setAdapter(mAdapter);
		mLvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mLvList.setVisibility(View.GONE);
				mIvSelect.setImageResource(R.drawable.next);
				mTvExpress.setText(mData.get(position).getName());
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
			finish();
			break;
		case R.id.express:
			if (mLvList.getVisibility() == View.GONE) {
				mLvList.setVisibility(View.VISIBLE);
				mIvSelect.setImageResource(R.drawable.back_button);
			} else {
				mLvList.setVisibility(View.GONE);
				mIvSelect.setImageResource(R.drawable.next);
			}
			break;
		case R.id.voice:
			break;
		case R.id.scan:
			break;
		case R.id.submit:
			break;
		case R.id.record:
			Intent intent = new Intent();
			intent.setClass(mContext, SettleRecordActivity.class);
			intent.putExtra("express", (Serializable)mData);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}

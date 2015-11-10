package com.koala.emm.activity;

import com.koala.emm.R;
import com.koala.emm.R.id;
import com.koala.emm.R.layout;
import com.koala.emm.app.EmmApplication;
import com.koala.emm.basic.BaseActivity;
import com.koala.emm.model.WarnType;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageTypeListActivity extends BaseActivity implements OnClickListener{
	
	private LinearLayout mLine1,mLine2,mLine3,mLine4;
	
	private TextView mTvTime1,mTvTime2,mTvTime3,mTvTime4;
	
	private TextView mTvClear1,mTvClear2,mTvClear3,mTvClear4;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_typelist);
		
		initView();
		loadView();
	}
	
	private void initView(){
		mLine1 = (LinearLayout) findViewById(R.id.ll_line1);
		mLine2 = (LinearLayout) findViewById(R.id.ll_line2);
		mLine3 = (LinearLayout) findViewById(R.id.ll_line3);
		mLine4 = (LinearLayout) findViewById(R.id.ll_line4);
		
		mTvTime1 = (TextView) findViewById(R.id.tv_time1);
		mTvTime2 = (TextView) findViewById(R.id.tv_time2);
		mTvTime3 = (TextView) findViewById(R.id.tv_time3);
		mTvTime4 = (TextView) findViewById(R.id.tv_time4);
		
		mTvClear1 = (TextView) findViewById(R.id.tv_clear1);
		mTvClear2 = (TextView) findViewById(R.id.tv_clear2);
		mTvClear3 = (TextView) findViewById(R.id.tv_clear3);
		mTvClear4 = (TextView) findViewById(R.id.tv_clear4);
		
		mLine1.setOnClickListener(this);
		mLine2.setOnClickListener(this);
		mLine3.setOnClickListener(this);
		mLine4.setOnClickListener(this);
		
		mTvClear1.setOnClickListener(this);
		mTvClear2.setOnClickListener(this);
		mTvClear3.setOnClickListener(this);
		mTvClear4.setOnClickListener(this);
		
		setHeadTitle("消息");
		
		
	}
	
	public void loadView(){
		mTvTime1.setText(EmmApplication.dbHelper.getLastTime(EmmApplication.db, new String[]{WarnType.DATA_ENCRYPTION,WarnType.DATA_DECRYPTION}).replace(" ", "\n"));
		mTvTime2.setText(EmmApplication.dbHelper.getLastTime(EmmApplication.db, new String[]{WarnType.APPLICATION_UPDATE_FORCED,WarnType.APPLICATION_DELETE,WarnType.APPLICATION_NOT_UPDATE_FORCED}).replace(" ", "\n"));
		mTvTime3.setText(EmmApplication.dbHelper.getLastTime(EmmApplication.db, new String[]{WarnType.RECEIVE_MESSAGE}).replace(" ", "\n"));
		mTvTime4.setText(EmmApplication.dbHelper.getLastTime(EmmApplication.db, new String[]{WarnType.MEMORY_CLEANUP}).replace(" ", "\n"));
	}
	

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.ll_line1:
			Intent intent1 = new Intent(this, MessageListActivity.class);
			intent1.putExtra("messageTypes",new String[]{WarnType.DATA_ENCRYPTION,WarnType.DATA_DECRYPTION});
			startActivity(intent1);
			break;
		case R.id.ll_line2:
			Intent intent2 = new Intent(this, MessageListActivity.class);
			intent2.putExtra("messageTypes",new String[]{WarnType.APPLICATION_UPDATE_FORCED,WarnType.APPLICATION_DELETE,WarnType.APPLICATION_NOT_UPDATE_FORCED});
			startActivity(intent2);
			break;
		case R.id.ll_line3:
			Intent intent3 = new Intent(this, MessageListActivity.class);
			intent3.putExtra("messageTypes",new String[]{WarnType.RECEIVE_MESSAGE});
			startActivity(intent3);
			break;
		case R.id.ll_line4:
			Intent intent4 = new Intent(this, MessageListActivity.class);
			intent4.putExtra("messageTypes",new String[]{WarnType.MEMORY_CLEANUP});
			startActivity(intent4);
			
			break;
		case R.id.tv_clear1:
			EmmApplication.dbHelper.removeMessage(EmmApplication.db, new String[]{WarnType.DATA_ENCRYPTION,WarnType.DATA_DECRYPTION});
			mTvTime1.setText("no\nmessage");
			break;
		case R.id.tv_clear2:
			EmmApplication.dbHelper.removeMessage(EmmApplication.db, new String[]{WarnType.APPLICATION_UPDATE_FORCED,WarnType.APPLICATION_DELETE,WarnType.APPLICATION_NOT_UPDATE_FORCED});
			mTvTime2.setText("no\nmessage");
			break;
		case R.id.tv_clear3:
			EmmApplication.dbHelper.removeMessage(EmmApplication.db, new String[]{WarnType.RECEIVE_MESSAGE});
			mTvTime3.setText("no\nmessage");
			break;
		case R.id.tv_clear4:
			EmmApplication.dbHelper.removeMessage(EmmApplication.db, new String[]{WarnType.MEMORY_CLEANUP});
			mTvTime4.setText("no\nmessage");
			break;
		default:
			break;
		}
		
	}
}

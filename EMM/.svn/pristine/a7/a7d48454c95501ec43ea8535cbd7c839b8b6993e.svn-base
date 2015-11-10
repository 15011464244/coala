package com.koala.emm.activity;

import com.koala.emm.R;
import com.koala.emm.R.layout;
import com.koala.emm.basic.BaseActivity;
import com.koala.emm.model.WarnPushMessage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MessageDetailActivity extends BaseActivity {
	private TextView mTitle,mContent,mReceiveTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_detail);
		
		setHeadTitle("消息详情");
		
		mTitle = (TextView) findViewById(R.id.tv_message_title);
		mContent = (TextView) findViewById(R.id.tv_message_content);
		mReceiveTime = (TextView) findViewById(R.id.tv_receive_time);
		
		Intent intent = getIntent();
		
		mTitle.setText(intent.getStringExtra("title"));
		mContent.setText(intent.getStringExtra("content"));
		mReceiveTime.setText(intent.getStringExtra("receiveTime"));
		
	}
}

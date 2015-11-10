package com.ems.express.ui.settle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.ems.express.R;

/**
 * 评价2
 */
public class CommentActivity2 extends Activity implements OnClickListener {
	private Context mContext = CommentActivity2.this;
	private ImageView mIvBack;// 返回键

	public static void startAction(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, CommentActivity2.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_comment2);

		initView();
		loadData();
	}

	public void initView() {
		mIvBack = (ImageView) findViewById(R.id.back_button);

		mIvBack.setOnClickListener(this);
	}

	public void loadData() {
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

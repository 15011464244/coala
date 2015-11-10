package com.ems.express.ui.check;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ems.express.R;
/**
 * 授权登录UI  淘宝
 */
public class AuthorizedActivity extends Activity implements OnClickListener {
	private Context mContext = AuthorizedActivity.this;
	private ImageView mIvBack;// 返回键
	private EditText mEdtUser;// 账号
	private EditText mEdtPsw;// 密码
	private Button mBtnLogin;// 登录
	
	public static void startAction(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, AuthorizedActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authorized);

		mIvBack = (ImageView) findViewById(R.id.back_button);
		mEdtUser = (EditText) findViewById(R.id.user);
		mEdtPsw = (EditText) findViewById(R.id.password);
		mBtnLogin = (Button) findViewById(R.id.login);

		mIvBack.setOnClickListener(this);
		mBtnLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_button:
			finish();
			break;
		case R.id.login:

			break;

		default:
			break;
		}
	}
}

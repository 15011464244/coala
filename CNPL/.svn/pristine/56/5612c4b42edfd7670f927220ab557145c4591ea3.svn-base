package com.cn.net.cnpl;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.net.cnpl.model.Head;
import com.cn.net.cnpl.tools.BaseActivity;
import com.cn.net.cnpl.tools.MyCode;
import com.cn.net.cnpl.tools.MyDialog;
import com.cn.net.cnpl.tools.NetHelper;
import com.cn.net.cnpl.tools.RegexPattern;

public class ModifyPasswordActivity extends BaseActivity {

	private Button codeButton = null;
	public final static int TASK_LOOP_COMPLETE = 0;
	public ProgressDialog myDialog = null;
	NetHelper client = null;
	Head head = new Head();
	private static final int NONE = -1;
	EditText oldPassword = null;
	EditText newPassword = null;
	EditText confirmPassword = null;
	private static final int WEBSERVICE_OVER = 1;// webservice结束
	private int currentMessage = NONE;
	
	private Button resetpasswordBtn=null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.modifypassword);

		oldPassword = (EditText)findViewById(R.id.modifypoldp);
		newPassword = (EditText)findViewById(R.id.modifypnewp);
		confirmPassword = (EditText)findViewById(R.id.modifypconfirmp);
		
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bg_new);
		TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText(getString(R.string.password));
		
		Button modifyButton = (Button)findViewById(R.id.modifypsubmit);
		modifyButton.setOnClickListener(onClickListener);
		resetpasswordBtn=(Button)findViewById(R.id.resetpasswordBtn);
		resetpasswordBtn.setOnClickListener(onClickListener);
		
		codeButton = (Button) findViewById(R.id.btncode);
		codeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyCode.Builder builder = new MyCode.Builder(
						ModifyPasswordActivity.this);
				builder.setTitle("二维码:");
				builder.setPositiveButton("", null);
				builder.create().show();
			}

		});
	}
	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()){
					case  R.id.modifypsubmit:
						dialog(getString(R.string.save_password), getString(R.string.hint));
						break;
					case  R.id.resetpasswordBtn:
						oldPassword.setText("");
						newPassword.setText("");
						confirmPassword.setText("");
						break;
					default: break;
			}
		}
	};


	/**
	 * 对话框，确认是否保存修改
	 * 
	 * @param msg
	 *            对话框内容
	 * @param title
	 *            对话框标题
	 */
	private void dialog(String msg, String title) {
		MyDialog.Builder builder = new MyDialog.Builder(this);
		builder.setMessage(msg);
		builder.setTitle(title);
		builder.setPositiveButton("", CancelListener());
		builder.setNegativeButton("", null);
		builder.create().show();
	}
	
	protected DialogInterface.OnClickListener CancelListener() {
		return new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
				if (sendData()) {
					ModifyPasswordActivity.this.finish();
				}
			}
		};
	}
	
	/**
	 * 验证数据
	 * @return
	 */
	private boolean verifyData() {
		
		if (!RegexPattern.matchs(ModifyPasswordActivity.this, "^.{1,16}$", oldPassword
				.getText().toString().trim(), getString(R.string.hint1))
				|| !RegexPattern.matchs(ModifyPasswordActivity.this, "^.{1,16}$", newPassword
						.getText().toString().trim(), getString(R.string.hint2))
				|| !RegexPattern.matchs(ModifyPasswordActivity.this, "^.{1,16}$", confirmPassword
						.getText().toString().trim(), getString(R.string.hint3))) {
			return false;
		}
		if(!newPassword.getText().toString().trim().equals(confirmPassword.getText().toString().trim())){
			Toast.makeText(ModifyPasswordActivity.this, getString(R.string.hint4),Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}
	
	private boolean sendData() {
		if (!verifyData()) {
			return false;
		}
		new Thread() {
			public void run() {
				try {
					client = new NetHelper();
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					/*if("2".equals(Global.M_USERFLAG)){// 签约用户
						client.Create(Global.JSON_SERVER_URL_RELEASE
								+ Global.URL_CUSTMODIFYPASSWORD);
					}else{*/
						client.Create(Global.BASE_URL
								+ Global.URL_MODIFYPASSWORD);
					//}
					params.add(new BasicNameValuePair("logName",
							getlogName()));
					params.add(new BasicNameValuePair("oldPwd", oldPassword.getText().toString().trim()));
					params.add(new BasicNameValuePair("newPwd", newPassword.getText().toString().trim()));
					params.add(new BasicNameValuePair("accessId", Global.M_ACCESSID));
					//params.add(new BasicNameValuePair("userFlag", Global.M_USERFLAG));
					client.execute(params, head);
				} catch (Exception e) {
					Log.e( Global.DIALOG_NAME, e.getMessage());
//					Toast.makeText(ModifyPasswordActivity.this, e.getMessage(),
//							Toast.LENGTH_LONG).show();
				} finally {
					currentMessage = WEBSERVICE_OVER;
				}
			}
		}.start();
		while (currentMessage != WEBSERVICE_OVER) {
		}
		currentMessage = NONE;

		if ("0".equals(head.getRet())) {
			Toast.makeText(ModifyPasswordActivity.this, getString(R.string.password_suc), Toast.LENGTH_LONG)
					.show();
			return true;
		} else {
			Toast.makeText(ModifyPasswordActivity.this, getString(R.string.password_fail), Toast.LENGTH_LONG)
					.show();
			return false;
		}
	}
}

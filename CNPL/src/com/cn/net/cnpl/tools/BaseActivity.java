package com.cn.net.cnpl.tools;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.cn.net.cnpl.Global;
import com.cn.net.cnpl.Mail_DlvDetailN;
import com.cn.net.cnpl.R;

public class BaseActivity extends Activity {

	private InputMethodManager imm = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Global.activities.add(this);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		super.onCreate(savedInstanceState);
	}

	/**
	 * 菜单栏
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.e("111111", "1111111");
		// AlertDialog.Builder builder = new AlertDialog.Builder(this);
		MyDialog.Builder builder = new MyDialog.Builder(this);
		switch (item.getItemId()) {
		case R.id.menu_reset:
			builder.setTitle("提 示:");
			builder.setMessage("是否确认重新加载？");
			builder.setPositiveButton("", resetListener());
			builder.setNegativeButton("", null);
			builder.create().show();
			return true;
		case R.id.menu_out:
			Log.e("2222", "1111111");
			builder.setTitle("提 示:");
			builder.setMessage("是否确认退出？");
			builder.setPositiveButton("", exitlListener());
			builder.setNegativeButton("", null);
			builder.create().show();
			return true;
		default:
			return false;
		}
	}

	protected DialogInterface.OnClickListener exitlListener() {
		return new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
				for (int i = 0; i < Global.activities.size(); i++) {
					Global.activities.get(i).finish();
				}
				Global.exit();
			}
		};
	}

	protected DialogInterface.OnClickListener resetListener() {
		return new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
				onReset();
			}
		};
	}

	protected void onReset() {
		this.finish();
		Intent intent = this.getIntent();
		intent.setClass(this, this.getClass());
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_reset, menu);
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			System.out.println("down");
			if (getCurrentFocus() != null) {
				if (getCurrentFocus().getWindowToken() != null) {
					imm.hideSoftInputFromWindow(getCurrentFocus()
							.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		}
		return super.onTouchEvent(event);
	}

	public String getlogName() {
		String tempLogName = Global.getLogin_name(this.getApplicationContext());
		if (null == tempLogName || "".equals(tempLogName)) {
			Toast.makeText(BaseActivity.this, getString(R.string.time_over),
					1000).show();
			return "";
		}
		return tempLogName;
	}

	public String getorgCode() {
		String tempOrg = Global.getOrg_code(this.getApplicationContext());
		if (null == tempOrg || "".equals(tempOrg)) {
			Toast.makeText(BaseActivity.this, getString(R.string.time_over),
					1000).show();
			return "";
		}
		return tempOrg;
	}
	

	public String StringFormate(Object str) {
		if (str != null && !"null".equals(str)) {
			return str.toString();
		} else {
			return "";
		}
	}
}

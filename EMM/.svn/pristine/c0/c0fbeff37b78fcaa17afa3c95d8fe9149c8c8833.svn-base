package com.koala.emm.activity;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.arvin.koalapush.util.LogUtil;
import com.arvin.koalapush.util.ToastUtil;
import com.koala.emm.R;
import com.koala.emm.basic.BaseActivity;
import com.koala.emm.business.PolicyUpdate;
import com.koala.emm.constant.Constant;
import com.koala.emm.tools.NetHelper;
import com.koala.emm.tools.PhoneMessageUtils;
import com.koala.emm.util.DeviceUtil;
import com.koala.emm.util.SpfsUtil;
import com.lidroid.xutils.util.LogUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AuthActivity extends BaseActivity implements OnClickListener {
	EditText mEtComNum, mEtUser, mEtPsd;
	Button btn;
	public static String SP_RESULT = "result";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auth);
		mEtComNum = (EditText) findViewById(R.id.compnum);
		mEtUser = (EditText) findViewById(R.id.worknumber);
		mEtPsd = (EditText) findViewById(R.id.password);
		btn = (Button) findViewById(R.id.btn_login);
		btn.setOnClickListener(this);
		SharedPreferences sp = getPreferences(MODE_PRIVATE);
		String result = sp.getString(SP_RESULT, "");
		if (result.equals("true")) {
		if(true){
			PolicyUpdate.strategyStorage(new PhoneMessageUtils(getApplicationContext()),PolicyUpdate.ELECTRICITY);
			PolicyUpdate.strategyStorage(new PhoneMessageUtils(getApplicationContext()),PolicyUpdate.FLOW);
			PolicyUpdate.strategyStorage(new PhoneMessageUtils(getApplicationContext()),PolicyUpdate.MEMORY);
		}
			
			mEtPsd.setText(SpfsUtil.getPsw());
			mEtUser.setText(SpfsUtil.getUserId());
			mEtComNum.setText(SpfsUtil.getOrgId());
			login();
			
		}
		LogUtil.v("login result:" + result);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.auth, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onClick(View v) {
		SpfsUtil.setPsw(mEtPsd
				.getText().toString());
		login();
		
	}
	/**
	 * 登录
	 */
	private void login(){
		final ProgressDialog pd = ProgressDialog
				.show(this, "网络", "请求网络中......");

		AsyncTask<?, ?, ?> task = new AsyncTask<Object, String, String>() {

			@Override
			protected String doInBackground(Object... params) {

				String result = NetHelper.doPostJson(Constant.auth,
						loginPrams(), "json");
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				if (!result.equals("请求服务器失败")) {
					try {
						JSONObject obj = JSONObject.parseObject(result);
						if ("true".equals(obj.getString("result"))) {
							//保存登录信息
							//{"result":"true","msg":"合法用户","userInfo":{"organization_id":"888888","user_id":"1001","user_name":"wanjun"}}
							JSONObject userInfo = obj.getJSONObject("userInfo");
							String userName = userInfo.getString("user_name");
							String orgId = userInfo.getString("organization_id");
							String userId = userInfo.getString("user_id");
							
							SpfsUtil.setUserName(userName);
							SpfsUtil.setUserId(userId);
							SpfsUtil.setOrgId(orgId);
							
							//如果是新设备，请求策略
							if(true){
								PolicyUpdate.strategyStorage(new PhoneMessageUtils(getApplicationContext()),PolicyUpdate.ELECTRICITY);
								PolicyUpdate.strategyStorage(new PhoneMessageUtils(getApplicationContext()),PolicyUpdate.FLOW);
								PolicyUpdate.strategyStorage(new PhoneMessageUtils(getApplicationContext()),PolicyUpdate.MEMORY);
							}
							
							
							SharedPreferences sp = getPreferences(MODE_PRIVATE);
							sp.edit()
									.putString(SP_RESULT, obj.getString(SP_RESULT))
									.commit();
							Intent intent = new Intent(currentContext, MainActivity.class);
							currentContext.startActivity(intent);
							finish();
							
//							Intent intent = new Intent(currentContext, ShowDevice2Activity.class);
//							currentContext.startActivity(intent);
//							finish();
							
						}
						ToastUtil.show(AuthActivity.this, obj.getString("msg"));
					} catch (Exception e) {
						e.printStackTrace();
						LogUtils.e("登录返回数据错误："+result);
					}
					
				}
				pd.dismiss();
				LogUtil.e("onPostExecute:" + result);
			}
		};
		task.execute();
	}

	private String loginPrams() {

		JSONObject obj = new JSONObject();
		try {
			obj.put("user_id", mEtUser.getText().toString());// 厂商
			obj.put("organization_id", mEtComNum.getText().toString());// 设备型号
			obj.put("user_password", mEtPsd.getText().toString());
			obj.put("imei", DeviceUtil.getDeviceNo());// 系统型号
		} catch (JSONException e) {
			e.printStackTrace();
		}
		LogUtils.e(obj.toString());
		return obj.toString();
	}
	
}

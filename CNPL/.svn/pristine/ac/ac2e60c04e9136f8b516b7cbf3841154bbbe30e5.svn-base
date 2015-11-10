package com.cn.net.cnpl;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.ReasonPhraseCatalog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.db.dao.LoginBandleDao;
import com.cn.net.cnpl.db.dao.ProjReasonDao;
import com.cn.net.cnpl.model.User;
import com.cn.net.cnpl.tools.BaseActivity;
import com.cn.net.cnpl.tools.MyDialog;
import com.cn.net.cnpl.tools.NetHelper;

public class IndexActivity extends BaseActivity {
	
	
	public static ProgressDialog myDialog = null;
	
	private Button dlvdown = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.index);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bg_new);
		TextView name=(TextView)findViewById(R.id.name);
		TextView transitCode=(TextView)findViewById(R.id.transitCode);
		TextView change=(TextView)findViewById(R.id.change);
		
		User user =User.FindUser(this.getApplicationContext());
		if(user!=null){
//			name.setVisibility(View.VISIBLE);
//			transitCode.setVisibility(View.VISIBLE);
//			change.setVisibility(View.VISIBLE);
//			
//			name.setText(getString(R.string.gonghao)+user.getLoginName());
//			transitCode.setText(getString(R.string.jigou)+user.getTransitCode());
//			
//			LoginBandleDao loginBandleDao = DaoFactory
//				.getInstance().getLoginBandleDao(this.getApplicationContext());
//			String device = loginBandleDao.FindLoginBandle(getlogName());
//			if(device!=null)
//				change.setText(getString(R.string.on_duty));
//			else
//				change.setText(getString(R.string.off_duty));
		}else{
			name.setVisibility(View.INVISIBLE);
			transitCode.setVisibility(View.INVISIBLE);
			change.setVisibility(View.INVISIBLE);
		}
		
		GridView gridview = (GridView) findViewById(R.id.GridView);
		ArrayList<HashMap<String, Object>> meumList = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.youjian);
		map.put("ItemText", getString(R.string.mail_deliver).toString());
		meumList.add(map);

		map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.logistics);
		map.put("ItemText", getString(R.string.logistic).toString());
		meumList.add(map);

		map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.change);
		map.put("ItemText", getString(R.string.mail_change).toString());
		meumList.add(map);

		/*
		 * map = new HashMap<String, Object>(); map.put("ItemImage",
		 * R.drawable.btn6styte); map.put("ItemText",
		 * getString(R.string.password).toString()); meumList.add(map);
		 */
		map = new HashMap<String, Object>();
		map.put("ItemImage", R.drawable.logout);
		map.put("ItemText", getString(R.string.logout).toString());
		meumList.add(map);

		SimpleAdapter saItem = new SimpleAdapter(this, meumList, // 数据源
				R.layout.index_item, // xml实现
				new String[] { "ItemImage", "ItemText" }, // 对应map的Key
				new int[] { R.id.ItemImage, R.id.ItemText }); // 对应R的Id

		// 添加Item到网格中
		gridview.setAdapter(saItem);
		// 添加点击事件
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				LoginBandleDao loginBandleDao = DaoFactory.getInstance()
						.getLoginBandleDao(IndexActivity.this);
				String device = loginBandleDao.FindLoginBandle(getlogName());
				switch (arg2) {
				case 0:// 邮件投递
//					if (device == null) {
//						Toast.makeText(IndexActivity.this,
//								getString(R.string.is_on), 1000).show();
//					} else {
						Intent mainIntent = new Intent(IndexActivity.this,
								MailDlvActivity.class);
						startActivity(mainIntent);
//					}
					break;
				case 1:// 物流
//					if (device == null) {
//						Toast.makeText(IndexActivity.this,
//								getString(R.string.is_on), 1000).show();
//					} else {
//						Intent intent1 = new Intent(IndexActivity.this,
//								LogisticsActivity.class);
//						startActivity(intent1);
//					}
					break;
				case 2:// 换班交接
//					Intent intent = new Intent(IndexActivity.this,
//							MailChange.class);
//					startActivity(intent);
					break;/*
						 * case 2://密码修改 Intent intent = new
						 * Intent(IndexActivity.this,
						 * ModifyPasswordActivity.class); startActivity(intent);
						 * break;
						 */
				case 3:// 注销
					if (device != null) {
						Toast.makeText(IndexActivity.this,
								getString(R.string.cancel_user), 1000).show();
					} else {
						MyDialog.Builder builder = new MyDialog.Builder(
								IndexActivity.this);
						builder.setTitle("提 示:");
						builder.setMessage("是否确认注销？");
						builder.setPositiveButton("", LogOutListener());
						builder.setNegativeButton("", null);
						builder.create().show();
					}
					break;
				}
			}
		});

		// 数据上传
		Intent serviceIntent = new Intent(this.getApplicationContext(),
				DlvUploadService.class);
		startService(serviceIntent);
		
		// 数据上传 电子返单
		Intent serviceIntent2 = new Intent(this.getApplicationContext(),
				MailImgUploadService.class);
		startService(serviceIntent2);
		
		dlvdown = (Button)findViewById(R.id.dlvdown);
		dlvdown.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				download();
			}
		});
		
		if("1".equals(getIntent().getExtras().get("tbtong")))
			download();

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 如果是返回键,直接返回到桌面
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			MyDialog.Builder builder = new MyDialog.Builder(this);
			builder.setTitle("提 示:");
			builder.setMessage("是否确认退出？");
			builder.setPositiveButton("", CancelListener());
			builder.setNegativeButton("", null);
			builder.create().show();
		}

		return super.onKeyDown(keyCode, event);
	}

	protected DialogInterface.OnClickListener CancelListener() {
		return new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
				Global.exit();

			}
		};
	}

	protected DialogInterface.OnClickListener LogOutListener() {
		return new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
				User.UpdateUser(IndexActivity.this);// 取消自动登录
				Global.exit();
				Intent intent = new Intent();
				intent.setClass(IndexActivity.this, MainActivity.class);
				startActivity(intent);
			}
		};
	}
	
	//未妥投 异常信息下载
	private void download() {
		final User user= User.FindUser(this.getApplicationContext());
		new AsyncTask<Object, Void, JSONObject>() {
			@Override
			protected JSONObject doInBackground(Object... params) {
				try {
				NetHelper client = new NetHelper();
				String url = Global.BASE_URL
						+ Global.GETPROJREASONCDINFOSBYID;
				client.Create(url);
				JSONObject jsonObject=new JSONObject();
			
				jsonObject.put("key", user.getKey());
				Log.i("tgxx", user.getKey());
				jsonObject.put("userCode", user.getLoginName());
				Log.i("tgxx", user.getLoginName());
				JSONObject resultJsonObject = client.executeCnpl(jsonObject);
				return resultJsonObject;
				} catch (Exception e) {
					return null ;
				}
			}

			@Override
			protected void onPostExecute(JSONObject json) {
				if (myDialog.isShowing()) {
					myDialog.dismiss();
				}
				try {
					if (json != null) {
						if(json.getBoolean("success")){// 成功
							ProjReasonDao dao=DaoFactory.getInstance().getProjReasonDao(IndexActivity.this);
							dao.deletePro();
							JSONArray jsonArray= json.getJSONArray("data");
							Log.e("jsonArray", jsonArray.toString());
							for(int i=0;i<jsonArray.length();i++){
								if("1".equals(jsonArray.getJSONObject(i).getString("feedBackType"))){
									JSONArray	excpReasons =jsonArray.getJSONObject(i).getJSONArray("excpReasons");
									for(int j=0;j<excpReasons.length();j++){
										JSONObject	reasons =excpReasons.getJSONObject(j);
										reasons.put("feedBackType", jsonArray.getJSONObject(i).getString("feedBackType"));
										reasons.put("projId", jsonArray.getJSONObject(i).getString("projId"));
										reasons.put("projName", jsonArray.getJSONObject(i).getString("projName"));
										reasons.put("projCd", jsonArray.getJSONObject(i).getString("projCd"));
										reasons.put("cd", excpReasons.getJSONObject(j).getString("cd"));
										reasons.put("desc", excpReasons.getJSONObject(j).getString("desc"));
										dao.SaveReason(reasons);
									}
									if(excpReasons.length()==0){
										JSONObject	reasons =new JSONObject();
										reasons.put("feedBackType", jsonArray.getJSONObject(i).getString("feedBackType"));
										reasons.put("projId", jsonArray.getJSONObject(i).getString("projId"));
										reasons.put("projName", jsonArray.getJSONObject(i).getString("projName"));
										reasons.put("projCd", jsonArray.getJSONObject(i).getString("projCd"));
										reasons.put("cd", "");
										reasons.put("desc", "");
										dao.SaveReason(reasons);
									}
								}else{
									JSONArray	excpReasons =jsonArray.getJSONObject(i).getJSONArray("excpReasons");
									for(int j=0;j<excpReasons.length();j++){
										JSONObject	reasons =excpReasons.getJSONObject(j);
										reasons.put("feedBackType", jsonArray.getJSONObject(i).getString("feedBackType"));
										reasons.put("projId", jsonArray.getJSONObject(i).getString("projId"));
										reasons.put("projName", jsonArray.getJSONObject(i).getString("projName"));
										reasons.put("projCd", jsonArray.getJSONObject(i).getString("projCd"));
										dao.SaveReason(reasons);
									}
								}
							}
								Toast.makeText(IndexActivity.this, getString(R.string.download_ok),
									Toast.LENGTH_LONG).show();
						}else{
							Toast.makeText(IndexActivity.this, getString(R.string.download_fail),
									Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(IndexActivity.this, getString(R.string.download_fail),
								Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					Toast.makeText(IndexActivity.this, getString(R.string.download_fail),
							Toast.LENGTH_LONG).show();
				}
				super.onPostExecute(json);
			}

			@Override
			protected void onPreExecute() {
				if (myDialog != null && myDialog.isShowing()) {
					myDialog.dismiss();
				}
				myDialog = new ProgressDialog(IndexActivity.this, 0);
				myDialog = ProgressDialog.show(IndexActivity.this,
						getString(R.string.app_name),
						getString(R.string.downloading), true, true);
				super.onPreExecute();
			}
		}.execute();
	}
	
}

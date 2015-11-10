package com.cn.net.cnpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.db.dao.TransferDao;
import com.cn.net.cnpl.model.User;
import com.cn.net.cnpl.tools.BaseActivity;
import com.cn.net.cnpl.tools.NetHelper;
import com.google.zxing.client.android.CaptureActivity;

public class MailDlvDataDownloadActivity extends BaseActivity {
	
	public static ProgressDialog myDialog = null;
	private EditText dlvDataDownloadPeiSongText;
	private TextView title;
	private Button top_back;
	private Button addListBtn;
	private TextView dlvDataDownloadBtn;
	private ListView peisongdanlist;
	private List<HashMap<String,String>> listItems = null;
	private SimpleAdapter adapter = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.mail_dlv_data_download_new);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bg_new);
		title = (TextView) findViewById(R.id.new_name);
		title.setText("配送单下载");
		dlvDataDownloadPeiSongText = (EditText) findViewById(R.id.dlvDataDownloadPeiSongText);
		// 扫描邮件号
		Button camera = (Button) findViewById(R.id.dlvDataDownloadScanBtn);
		camera.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MailDlvDataDownloadActivity.this,
						CaptureActivity.class);
				startActivityForResult(intent, 1);
			}
		});
		top_back = (Button) findViewById(R.id.top_left);
		top_back.setVisibility(View.VISIBLE);
		top_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MailDlvDataDownloadActivity.this.finish();
			}
		});
		addListBtn = (Button) findViewById(R.id.addListBtn);
		addListBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(dlvDataDownloadPeiSongText.getText() == null || dlvDataDownloadPeiSongText.getText().toString().trim().length() == 0){
					Toast.makeText(MailDlvDataDownloadActivity.this, "请输入或者扫描配送单号",Toast.LENGTH_SHORT).show();
					dlvDataDownloadPeiSongText.requestFocus();
					return ;
				}
				HashMap<String,String> data = new HashMap<String,String>();
				data.put("peisongdanhao", dlvDataDownloadPeiSongText.getText().toString());
				listItems.add(data);
				adapter.notifyDataSetChanged();
				dlvDataDownloadPeiSongText.setText("");
			}
		});
		listItems = new ArrayList<HashMap<String,String>>(); 
		peisongdanlist = (ListView) findViewById(R.id.peisongdanlist);
		adapter = new SimpleAdapter(MailDlvDataDownloadActivity.this, listItems, 
				R.layout.mail_dlv_data_download_list, new String[]{"peisongdanhao"}, new int[]{R.id.maildlvpeisongdanhao});
		peisongdanlist.setAdapter(adapter);
		dlvDataDownloadBtn = (TextView) findViewById(R.id.top_download);
		dlvDataDownloadBtn.setVisibility(View.VISIBLE);
		dlvDataDownloadBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				downloadData();
			}
		});
	}
	
	private void downloadData(){
		if(listItems == null || listItems.size() == 0){
			Toast.makeText(MailDlvDataDownloadActivity.this, "请输入或者扫描配送单号",Toast.LENGTH_SHORT).show();
			return ;
		}
		final User user = User.FindUser(this.getApplicationContext());
		new AsyncTask<Object, Void, JSONObject>() {
			@Override
			protected JSONObject doInBackground(Object... params) {
				try {
				NetHelper client = new NetHelper();
				String url = Global.BASE_URL
						+ Global.URL_GETDLVDETAILSBYNUM;
				client.Create(url);
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("key", user.getKey());
				jsonObject.put("userCode", user.getLoginName());
				JSONArray dlvNums  = new JSONArray();
				for(int i=0;i<listItems.size();i++){
					dlvNums.put(listItems.get(i).get("peisongdanhao"));
				}
				jsonObject.put("dlvNums", dlvNums);
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
							
							TransferDao dao=DaoFactory.getInstance().getTransferDao(MailDlvDataDownloadActivity.this);
							JSONArray jsonArray= json.getJSONArray("data");
							if(jsonArray != null && jsonArray.length() > 0){
								JSONArray dlvNums  = new JSONArray();
								for(int i=0;i<listItems.size();i++){
									dlvNums.put(listItems.get(i).get("peisongdanhao"));
								}
								dao.DeleteTransferByDlvNum(dlvNums,user.getLoginName());
								for(int i=0;i<jsonArray.length();i++){
									JSONObject data = new JSONObject(); 
									data.put("dlvNum", jsonArray.getJSONObject(i).isNull("dlvNum")?"":jsonArray.getJSONObject(i).getString("dlvNum"));
									data.put("ticketNum", jsonArray.getJSONObject(i).isNull("ticketNum")?"":jsonArray.getJSONObject(i).getString("ticketNum"));
									data.put("custNum", jsonArray.getJSONObject(i).isNull("custNum")?"":jsonArray.getJSONObject(i).getString("custNum"));
									data.put("projId", jsonArray.getJSONObject(i).isNull("projId")?"":jsonArray.getJSONObject(i).getString("projId"));
									data.put("projName", jsonArray.getJSONObject(i).isNull("projName")?"":jsonArray.getJSONObject(i).getString("projName"));
									data.put("projCd", jsonArray.getJSONObject(i).isNull("projCd")?"":jsonArray.getJSONObject(i).getString("projCd"));
									data.put("rcverCntct", jsonArray.getJSONObject(i).isNull("rcverCntct")?"":jsonArray.getJSONObject(i).getString("rcverCntct"));
									data.put("rcverCompany", jsonArray.getJSONObject(i).isNull("rcverCompany")?"":jsonArray.getJSONObject(i).getString("rcverCompany"));
									dao.SaveTransfer(data, user.getLoginName());
								}
								Toast.makeText(MailDlvDataDownloadActivity.this, getString(R.string.download_ok),
										Toast.LENGTH_LONG).show();
								listItems.clear();
								adapter.notifyDataSetChanged();
							}else{
								Toast.makeText(MailDlvDataDownloadActivity.this, getString(R.string.download_fail3),
								Toast.LENGTH_LONG).show();
							}
							
						}else{
							Toast.makeText(MailDlvDataDownloadActivity.this, getString(R.string.download_fail2),
									Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(MailDlvDataDownloadActivity.this, getString(R.string.download_fail2),
								Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					Toast.makeText(MailDlvDataDownloadActivity.this, getString(R.string.download_fail2),
							Toast.LENGTH_LONG).show();
				}
				super.onPostExecute(json);
			}

			@Override
			protected void onPreExecute() {
				if (myDialog != null && myDialog.isShowing()) {
					myDialog.dismiss();
				}
				myDialog = new ProgressDialog(MailDlvDataDownloadActivity.this, 0);
				myDialog = ProgressDialog.show(MailDlvDataDownloadActivity.this,
						getString(R.string.app_name),
						getString(R.string.downloading2), true, true);
				super.onPreExecute();
			}
		}.execute();
	}
	
	/**
	 * 通过扫描得到配送单号
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == 1) {
			// 获取跳转来源
			Bundle bundle = intent.getExtras();
			String strDnNumber = bundle.getString("txtResult");
			if (strDnNumber != null && strDnNumber.length() > 0)
				dlvDataDownloadPeiSongText.setText(strDnNumber);
		}
	}
}

package com.cn.net.cnpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.cn.net.cnpl.tools.BaseActivity;
import com.cn.net.cnpl.tools.MyCode;
import com.cn.net.cnpl.tools.MySimpleAdapter;

public class MailChangeOnDt extends BaseActivity {

	private Button codeButton = null;
	private ListView listView = null;
	private SimpleAdapter adapter = null;
	private ArrayList<Map<String, String>> sourceList = null;

	private TextView mailcon_out_no = null;
	private TextView codetype_txt= null;
	private TextView exchange_time_txt = null;
	private TextView mailtotal_txt = null;
	private TextView disrepair_no_txt = null;
	private TextView lose_no_txt = null;
	private TextView upload_txt = null;
	private TextView no_upload_txt = null;

	private String out_code = "";
	private String codetype = "";
	private String begin_time = "";
	private String total_txt = "";
	private String disrepair_txt = "";
	private String lose_txt = "";
	private String upload = "";
	private String no_upload ="";
	private String time_txt ="";
	private String strList ="";
	
	public ProgressDialog myDialog = null;
	private int pageNo = 1;
	private boolean loadOver = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.con_suc_dt);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bg_new);

		// 获取数据
		Intent intent = getIntent();
		out_code = intent.getStringExtra("out_code");
		codetype = intent.getStringExtra("codetype");
		time_txt = intent.getStringExtra("time_txt");
		begin_time = intent.getStringExtra("begin_time");
		total_txt = intent.getStringExtra("total_txt");
		disrepair_txt = intent.getStringExtra("disrepair_txt");
		lose_txt = intent.getStringExtra("lose_txt");
		upload = intent.getStringExtra("upload_txt");
		no_upload = intent.getStringExtra("no_upload_txt");
		strList = intent.getStringExtra("strList");

		mailcon_out_no = (TextView) findViewById(R.id.mailcon_out_no);
		mailcon_out_no.setText(out_code);
		codetype_txt = (TextView) findViewById(R.id.codetype);
		codetype_txt.setText(codetype);
		exchange_time_txt = (TextView) findViewById(R.id.exchange_time_txt);
		exchange_time_txt.setText(time_txt);
		mailtotal_txt= (TextView) findViewById(R.id.mailtotal_txt);
		mailtotal_txt.setText(total_txt);
		disrepair_no_txt= (TextView) findViewById(R.id.disrepair_no_txt);
		disrepair_no_txt.setText(disrepair_txt);
		lose_no_txt= (TextView) findViewById(R.id.lose_no_txt);
		lose_no_txt.setText(lose_txt);
		
		upload_txt= (TextView) findViewById(R.id.upload_txt);
		upload_txt.setText(upload);
		no_upload_txt= (TextView) findViewById(R.id.no_upload_txt);
		no_upload_txt.setText(no_upload);
		codeButton = (Button) findViewById(R.id.btncode);
		codeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyCode.Builder builder = new MyCode.Builder(
						MailChangeOnDt.this);
				builder.setTitle("二维码:");
				builder.setPositiveButton("", null);
				builder.create().show();
			}

		});
		sourceList = new ArrayList<Map<String, String>>();
		listView = (ListView) findViewById(R.id.comListView);

		adapter = new MySimpleAdapter(this, sourceList, R.layout.con_suc_dtitem,
				new String[] { "num", "mailno_txt", "time_txt", "type_txt",
						"isup_txt", "outcode_txt" }, new int[] { R.id.num,
						R.id.mailno_txt, R.id.time_txt, R.id.type_txt,
						R.id.isup_txt, R.id.outcode_txt });

		listView.setAdapter(adapter);
		// 向下滑动
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				// 判断listview是否停止滑动并且处于底部
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& view.getLastVisiblePosition() == view.getCount() - 1) {
					// loadBool = false; // 用布尔作为开关，防止在加载数据时，出现多次启动线程加载数据 数据加载
					if (loadOver) {
						myDialog = ProgressDialog.show(MailChangeOnDt.this,
								 Global.DIALOG_NAME, getString(R.string.loading), true, true);

						new Thread() {
							public void run() {
								try {
									//loadData();

								} catch (Exception e) {
									// e.printStackTrace();
								} finally {
									messageListener
											.sendEmptyMessage(Global.TASK_LOOP_COMPLETE);
								}
							}
						}.start();
					}

				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
		loadData();
		
	}

	// 显示信息
	private void loadData() {

		try {
			JSONArray objArray = new JSONArray(strList);
			int tempSize = objArray.length();
			String str="";
			for (int i = 0; i < tempSize; i++) {
				Map<String, String> tempHashMap = new LinkedHashMap<String, String>();
				tempHashMap.put("num", "" + pageNo);
				tempHashMap.put("mailno_txt", objArray.getJSONObject(i).get("mailNo").toString());
				tempHashMap.put("time_txt", objArray.getJSONObject(i).get("create_time").toString());
				
				if (Global.MAILDISREPAIR.equals(objArray.getJSONObject(i).get("isMangle").toString()))
					str = getString(R.string.disrepair);
				else if (Global.MAILLOSE.equals(objArray.getJSONObject(i).get("isMangle").toString()))
					str = getString(R.string.lose);
				else if (Global.MAILINTACT.equals(objArray.getJSONObject(i).get("isMangle").toString()))
					str = getString(R.string.comp);
				
				tempHashMap.put("type_txt", str);
				if(!"".equals(objArray.getJSONObject(i).get("upload_time").toString()))
					tempHashMap.put("isup_txt", getString(R.string.upload));
				else
					tempHashMap.put("isup_txt", getString(R.string.no_upload));
				tempHashMap.put("outcode_txt", out_code);
				
				sourceList.add(tempHashMap);
				pageNo++;
			}
			
		} catch (Exception e) {
		}
	
	}
	private Handler messageListener = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Global.TASK_LOOP_COMPLETE:
				myDialog.dismiss();
				adapter.notifyDataSetChanged();
				break;
			}
		}
	};
}

package com.cn.net.cnpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.db.dao.MailHandDetailDao;
import com.cn.net.cnpl.tools.BaseActivity;
import com.cn.net.cnpl.tools.MultiAdapter;
import com.cn.net.cnpl.tools.MyCode;


public class Mail_ConnectCancelDt extends BaseActivity {

	private Button codeButton = null;
	private MultiAdapter adapter = null;
	private ListView listview = null;
	private List<Map<String, String>> list = null;
	public ProgressDialog myDialog = null;
	private int pageNo = 1;
	private boolean loadOver = true;
	
	private MailHandDetailDao mailhanddetaildao = null;
	
	private String org_type="";
	private String out_code="";
	private String sid_time;
	private String begin_time;
	String abnormity="1";
	
	private TextView mailcon_out_no = null;
	private TextView exchange_time_txt = null;
	private TextView mailtotal_txt = null;
	private TextView disrepair_no_txt = null;
	private TextView lose_no_txt = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.con_canceldt);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bg_new);

		
		//获取数据
		Intent intent = getIntent();
		org_type = intent.getStringExtra("org_type");
		out_code = intent.getStringExtra("out_code");
		sid_time = intent.getStringExtra("sid_time");
		begin_time = intent.getStringExtra("begin_time");
		mailcnt = intent.getStringExtra("mailtotal_txt");
		disrepair = intent.getStringExtra("disrepair_no_txt");
		lose = intent.getStringExtra("lose_no_txt");
		
		mailcon_out_no = (TextView)findViewById(R.id.mailcon_out_no);
		mailcon_out_no.setText(out_code);
		exchange_time_txt = (TextView) findViewById(R.id.exchange_time_txt);
		exchange_time_txt.setText(begin_time);
		mailtotal_txt= (TextView) findViewById(R.id.mailtotal_txt);
		mailtotal_txt.setText(mailcnt);
		disrepair_no_txt= (TextView) findViewById(R.id.disrepair_no_txt);
		disrepair_no_txt.setText(disrepair);
		lose_no_txt= (TextView) findViewById(R.id.lose_no_txt);
		lose_no_txt.setText(lose);
		codeButton = (Button) findViewById(R.id.btncode);
		codeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyCode.Builder builder = new MyCode.Builder(
						Mail_ConnectCancelDt.this);
				builder.setTitle("二维码:");
				builder.setPositiveButton("", null);
				builder.create().show();
			}

		});
		listview = (ListView) findViewById(R.id.comListView);
		if (list == null)
			list = new ArrayList<Map<String, String>>();
	
		adapter = new MultiAdapter(Mail_ConnectCancelDt.this, list);
		listview.setAdapter(adapter);
		
		// 向下滑动
		listview.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				// 判断listview是否停止滑动并且处于底部
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& view.getLastVisiblePosition() == view.getCount() - 1) {
					// loadBool = false; // 用布尔作为开关，防止在加载数据时，出现多次启动线程加载数据 //
					// 数据加载
					if (loadOver) {
						myDialog = ProgressDialog.show(Mail_ConnectCancelDt.this,
								 Global.DIALOG_NAME, getString(R.string.loading), true, true);

						new Thread() {
							public void run() {
								try {
									loadData();
									
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
			List<Map<String, Object>> rList = null;
			mailhanddetaildao = DaoFactory.getInstance().getMailHandDetailDao(Mail_ConnectCancelDt.this);
			rList = mailhanddetaildao.FindMail(sid_time,"",pageNo);
			
			if (rList == null || rList.size() == 0) {
				loadOver = false;
				return;
			}
			int tempSize = rList.size();
			String str = "";
			
			for (int i = 0; i < tempSize; i++) {
				Map<String, String> tempHashMap = new LinkedHashMap<String, String>();
				tempHashMap.put("mailid",(String) rList.get(i).get("mail_num"));
				tempHashMap.put("num", "" + ((pageNo - 1) * 10 + 1 + i));
				tempHashMap.put("date",rList.get(i).get("create_time").toString());
				
				if(Global.MAILDISREPAIR.equals(rList.get(i).get("mail_type")))
					str = getString(R.string.disrepair);
				else if(Global.MAILLOSE.equals(rList.get(i).get("mail_type")))
					str = getString(R.string.lose);
				else if(Global.MAILINTACT.equals(rList.get(i).get("mail_type")))
					str = getString(R.string.comp);
				
				tempHashMap.put("type", str);
				tempHashMap.put("principal",rList.get(i).get("principal").toString());
				
				list.add(tempHashMap);
			}

			pageNo++;
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

	String mailcnt="0";
	String disrepair="0";
	String lose="0";
	String upload="1";
	String unupload="0";

}

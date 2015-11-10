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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.db.dao.MailHandDao;
import com.cn.net.cnpl.db.dao.MailHandDetailDao;
import com.cn.net.cnpl.tools.BaseActivity;
import com.cn.net.cnpl.tools.MyCode;

public class MailOutSuc extends BaseActivity {

	private Button codeButton = null;
	private SimpleAdapter adapter = null;
	private ListView listview = null;
	private List<Map<String, String>> list = null;
	public ProgressDialog myDialog = null;
	private int pageNo = 1;
	private boolean loadOver = true;
	private String in_code="";
	//private EnterDao enterdao = null;
	private MailHandDao mailhanddao = null;
	private MailHandDetailDao mailhanddetaildao = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.out_suc);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bg_new);

		//获取数据
		Intent intent = getIntent();
		in_code = intent.getStringExtra("in_code");
		
		codeButton = (Button) findViewById(R.id.btncode);
		codeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyCode.Builder builder = new MyCode.Builder(
						MailOutSuc.this);
				builder.setTitle("二维码:");
				builder.setPositiveButton("", null);
				builder.create().show();
			}

		});
		
		listview = (ListView) findViewById(R.id.comListView);
		if (list == null)
			list = new ArrayList<Map<String, String>>();

		adapter = new SimpleAdapter(MailOutSuc.this, list,
				R.layout.out_sucitem, new String[] { "num","conout_txt", "time_txt",
				"total_txt", "disrepair_txt", "lose_txt",
				"upload_txt", "no_upload_txt" }, new int[] { R.id.num, R.id.conout_txt,
				R.id.time_txt, R.id.total_txt, R.id.disrepair_txt,
				R.id.lose_txt, R.id.upload_txt, R.id.no_upload_txt });

		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String sid_time = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("sid_time");
				String time_txt = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("time_txt");
				String begin_time = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("begin_time");
				String in_code = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("conout_txt").toString();
				Intent intent = new Intent();
				intent.putExtra("in_code", in_code);
				intent.putExtra("sid_time", sid_time);
				intent.putExtra("time_txt", time_txt);
				intent.putExtra("begin_time", begin_time);
				intent.putExtra("suc_secform", "1");
				intent.setClass(MailOutSuc.this, MailOutDetail.class);
				startActivityForResult(intent, 2);
			
			}
		});

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
						myDialog = ProgressDialog.show(MailOutSuc.this,
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
			mailhanddao = DaoFactory.getInstance().getMailHandDao(MailOutSuc.this);
			mailhanddetaildao = DaoFactory.getInstance().getMailHandDetailDao(
					MailOutSuc.this);
			rList = mailhanddao.FindMail(getlogName(),Global.HANDOUT,Global.MAILCOM,pageNo);
			
			if (rList == null || rList.size() == 0) {
				loadOver = false;
				return;
			}
			int tempSize = rList.size();
			String sid_time_temp="";
			String mailcnt="";
			String disrepair="";
			String lose="";
			String upload="";
			String no_upload="";

			for (int i = 0; i < tempSize; i++) {
				Map<String, String> tempHashMap = new LinkedHashMap<String, String>();
				tempHashMap.put("num", "" + ((pageNo - 1) * 10 + 1 + i));
				tempHashMap.put("conout_txt", rList.get(i).get("in_code").toString());
				
				sid_time_temp=rList.get(i).get("sid").toString();
				disrepair = mailhanddetaildao.Findcount(sid_time_temp, Global.MAILDISREPAIR);
				lose = mailhanddetaildao.Findcount(sid_time_temp, Global.MAILLOSE);
				mailcnt = mailhanddetaildao.Findcount(sid_time_temp, Global.MAILINTACT);
				mailcnt = (Integer.parseInt(mailcnt)+Integer.parseInt(disrepair)+Integer.parseInt(lose))+"";
				upload = mailhanddetaildao.FindcountUpload(sid_time_temp);
				no_upload = (Integer.parseInt(mailcnt)-Integer.parseInt(upload))+"";
				
				
				tempHashMap.put("time_txt", rList.get(i).get("begin_time").toString()+"—"+rList.get(i).get("end_time").toString());
				tempHashMap.put("total_txt",mailcnt);
				tempHashMap.put("disrepair_txt",disrepair);
				tempHashMap.put("lose_txt",lose);
				tempHashMap.put("upload_txt",upload);
				tempHashMap.put("no_upload_txt",no_upload);
				tempHashMap.put("begin_time", rList.get(i).get("begin_time").toString());
				tempHashMap.put("sid_time", sid_time_temp);
				
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

}

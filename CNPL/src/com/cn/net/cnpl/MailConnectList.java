package com.cn.net.cnpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AbsListView.OnScrollListener;

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.db.dao.MailHandDao;
import com.cn.net.cnpl.db.dao.MailHandDetailDao;
import com.cn.net.cnpl.tools.BaseActivity;
import com.cn.net.cnpl.tools.MyCode;
import com.cn.net.cnpl.tools.MyDialog;

public class MailConnectList extends BaseActivity {

	private Button codeButton = null;
	private SimpleAdapter adapter = null;
	private ListView listview = null;
	private List<Map<String, String>> list = null;
	
	public ProgressDialog myDialog = null;
	private int pageNo = 1;
	private boolean loadOver = true;
	
	//private EnterDao enterdao = null;
	private MailHandDao mailhanddao = null;
	private MailHandDetailDao mailhanddetaildao = null;
	String begin_time = "";
	private int tempI=-1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.con_list);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bg_new);
		
		BeginConnect.activities.add(MailConnectList.this);
		
		listview = (ListView) findViewById(R.id.comListView);
		if (list == null)
			list = new ArrayList<Map<String, String>>();
		
		adapter = new SimpleAdapter(MailConnectList.this, list,
				R.layout.con_sucitem, new String[] { "num","conout_txt", "time_txt",
				"total_txt", "disrepair_txt", "lose_txt",
				"upload_txt", "no_upload_txt" }, new int[] {R.id.num, R.id.conout_txt,
				R.id.time_txt, R.id.total_txt, R.id.disrepair_txt,
				R.id.lose_txt, R.id.upload_txt, R.id.no_upload_txt });

		listview.setAdapter(adapter);
		
		loadData();
		
		listview.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				tempI = arg2;
				MyDialog.Builder builder = new MyDialog.Builder(MailConnectList.this);
				builder.setTitle(getString(R.string.hint));
				builder.setMessage(getString(R.string.shifoudelete));
				builder.setPositiveButton("",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								mailhanddao = DaoFactory.getInstance().getMailHandDao(MailConnectList.this);
								mailhanddetaildao = DaoFactory.getInstance().getMailHandDetailDao(
										MailConnectList.this);
								mailhanddao.deleteMail(getlogName(),list.get(tempI).get("sid_time"));
								mailhanddetaildao.deleteMail(list.get(tempI).get("sid_time"));
								list.remove(tempI);
						        adapter.notifyDataSetChanged();
							}
						});
				builder.setNegativeButton("", null);
				builder.create().show();
				return false;
			}
			
		});
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				begin_time = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("begin_time");
				String out_type = "";
				String sid_time = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("sid_time");
				String out_code = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("conout_txt");
				String mailcnt = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("total_txt");
				String disrepair = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("disrepair_txt");
				String lose = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("lose_txt");
				Intent intent = new Intent();
				intent.putExtra("out_type", out_type);
				intent.putExtra("out_code", out_code);
				intent.putExtra("sid_time", sid_time);
				intent.putExtra("begin_time", begin_time);
				intent.putExtra("mailtotal_txt", mailcnt);
				intent.putExtra("disrepair_no_txt", disrepair);
				intent.putExtra("lose_no_txt", lose);
				intent.setClass(MailConnectList.this, MailConnectComp.class);
				startActivity(intent);
				
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
						myDialog = ProgressDialog.show(MailConnectList.this,
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
		codeButton = (Button) findViewById(R.id.btncode);
		codeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyCode.Builder builder = new MyCode.Builder(
						MailConnectList.this);
				builder.setTitle("二维码:");
				builder.setPositiveButton("", null);
				builder.create().show();
			}

		});
	}
	// 显示信息
	private void loadData() {
		try {
			List<Map<String, Object>> rList = null;
			mailhanddao = DaoFactory.getInstance().getMailHandDao(MailConnectList.this);
			mailhanddetaildao = DaoFactory.getInstance().getMailHandDetailDao(
					MailConnectList.this);
			rList = mailhanddao.FindMail(getlogName(),Global.HANDIN,Global.MAILEX,pageNo);
			
			if (rList == null || rList.size() == 0) {
				loadOver = false;
				return;
			}
			int tempSize = rList.size();
			String sid_time_temp="";
			String mailcnt="";
			String disrepair="";
			String lose="";
			
			for (int i = 0; i < tempSize; i++) {
				Map<String, String> tempHashMap = new LinkedHashMap<String, String>();
				tempHashMap.put("num", "" + ((pageNo - 1) * 10 + 1 + i));
				tempHashMap.put("conout_txt", rList.get(i).get("out_code").toString());
				
				sid_time_temp=rList.get(i).get("sid").toString();
				mailcnt = mailhanddetaildao.Findcount( sid_time_temp, Global.MAILINTACT);
				disrepair = mailhanddetaildao.Findcount( sid_time_temp, Global.MAILDISREPAIR);
				lose = mailhanddetaildao.Findcount( sid_time_temp, Global.MAILLOSE);
				mailcnt = (Integer.parseInt(mailcnt)+Integer.parseInt(disrepair)+Integer.parseInt(lose))+"";
				
				tempHashMap.put("time_txt", rList.get(i).get("begin_time").toString());
				tempHashMap.put("total_txt",mailcnt);
				tempHashMap.put("disrepair_txt",disrepair);
				tempHashMap.put("lose_txt",lose);
				tempHashMap.put("upload_txt","0");
				tempHashMap.put("no_upload_txt","0");
				tempHashMap.put("begin_time", rList.get(i).get("begin_time").toString());
				tempHashMap.put("sid_time", sid_time_temp);
				
				if("0".equals(mailcnt)){
					
				}else
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

package com.cn.net.cnpl;


import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.db.dao.MailHandDao;
import com.cn.net.cnpl.db.dao.MailHandDetailDao;
import com.cn.net.cnpl.db.dao.WorkLogDao;
import com.cn.net.cnpl.tools.BaseActivity;
import com.cn.net.cnpl.tools.MultiAdapter;
import com.cn.net.cnpl.tools.MyCode;
import com.cn.net.cnpl.tools.MyDialog;

public class MailConnectComp extends BaseActivity {

	private Button codeButton = null;
	private MultiAdapter adapter = null;
	private ListView listview = null;
	private List<Map<String, String>> list = null;
	public ProgressDialog myDialog = null;
	private int pageNo = 1;
	private boolean loadOver = true;
	
	private Button enterbtn = null;
	private Button compbtn = null;
	private Button cancelbtn = null;
	
	private JSONObject params = null;
	private MailHandDao mailhanddao = null;
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
	int tempI = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.connect_comp);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bg_new);
		BeginConnect.activities.add(MailConnectComp.this);
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
						MailConnectComp.this);
				builder.setTitle("二维码:");
				builder.setPositiveButton("", null);
				builder.create().show();
			}

		});
		listview = (ListView) findViewById(R.id.comListView);
		if (list == null)
			list = new ArrayList<Map<String, String>>();
	
		adapter = new MultiAdapter(MailConnectComp.this, list);
		listview.setAdapter(adapter);
		
		listview.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				tempI = arg2;
				MyDialog.Builder builder = new MyDialog.Builder(MailConnectComp.this);
				builder.setTitle(getString(R.string.hint));
				builder.setMessage(getString(R.string.shifoudelete));
				builder.setPositiveButton("",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								String mailid = list.get(tempI).get("mailid");
						        String sid = list.get(tempI).get("sid");
						        String date = list.get(tempI).get("date");
						        mailhanddetaildao = DaoFactory.getInstance().getMailHandDetailDao(MailConnectComp.this);
						        mailhanddao = DaoFactory.getInstance().getMailHandDao(MailConnectComp.this);
						        mailhanddetaildao.deleteMail(mailid,date);
						        list.remove(tempI);
						        adapter.notifyDataSetChanged();
						        
						        String mail = mailhanddetaildao.Findcount( sid, Global.MAILINTACT);
						        String dis = mailhanddetaildao.Findcount( sid, Global.MAILDISREPAIR);
						        String lo = mailhanddetaildao.Findcount( sid, Global.MAILLOSE);
						        mail = (Integer.parseInt(mail)+Integer.parseInt(dis)+Integer.parseInt(lo))+"";
								
						        if("0".equals(mail))
						        	mailhanddao.deleteMail(getlogName(),sid);
							}
						});
				builder.setNegativeButton("", null);
				builder.create().show();
				return false;
			}
			
		});
		/*
		listview.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
                menu.setHeaderTitle("操作");
                menu.add(0, 0, 0, "删除");
			}
		});
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String maio = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("dlvmailid");
				String createDate = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("dlvName");
				Intent intent = new Intent();
				intent.putExtra("mailid", maio);
				intent.putExtra("date", createDate);
				intent.setClass(MailConnectComp.this, Mail_CompDetail.class);
				startActivityForResult(intent, 2);
			}
		});
		*/
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
						myDialog = ProgressDialog.show(MailConnectComp.this,
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
		
		//继续录入
		enterbtn = (Button)findViewById(R.id.enterbtn);
		/*if(jieru.equals("ing"))
			enterbtn.setVisibility(View.GONE);
		else{
			enterbtn.setVisibility(View.VISIBLE);*/
			enterbtn.setOnClickListener(onClickListener);
		//}
		//接入完成
		compbtn = (Button)findViewById(R.id.compbtn);
		compbtn.setOnClickListener(onClickListener);
		//撤销
		cancelbtn = (Button)findViewById(R.id.cancelbtn);
		cancelbtn.setOnClickListener(onClickListener);
	}
/*
	@Override
	public boolean onContextItemSelected(MenuItem item) {
        
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();  
        int start = info.position; 
        String mailid = list.get(start).get("mailid");
        String sid = list.get(start).get("sid");
        String date = list.get(start).get("date");
        mailhanddetaildao = DaoFactory.getInstance().getMailHandDetailDao(MailConnectComp.this);
        mailhanddao = DaoFactory.getInstance().getMailHandDao(MailConnectComp.this);
        mailhanddetaildao.deleteMail(mailid,date);
        list.remove(start);
        adapter.notifyDataSetChanged();
        
        String mail = mailhanddetaildao.Findcount( sid, Global.MAILINTACT);
        String dis = mailhanddetaildao.Findcount( sid, Global.MAILDISREPAIR);
        String lo = mailhanddetaildao.Findcount( sid, Global.MAILLOSE);
        mail = (Integer.parseInt(mail)+Integer.parseInt(dis)+Integer.parseInt(lo))+"";
		
        if("0".equals(mail))
        	mailhanddao.deleteMail(sid);
        
        return super.onContextItemSelected(item);
	}
	*/
	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.enterbtn://继续录入
				intent.putExtra("org_type", org_type);
				intent.putExtra("out_code", out_code);
				intent.putExtra("sid_time", sid_time);
				intent.putExtra("begin_time", begin_time);
				intent.putExtra("jieru", "ing");
				intent.setClass(MailConnectComp.this, MailConnectInfo.class);
				startActivity(intent);
				BeginConnect.allfinish();
				break;
			case R.id.compbtn://接入完成
				mailhanddetaildao = DaoFactory.getInstance().getMailHandDetailDao(MailConnectComp.this);
				String str=mailhanddetaildao.IsSaveMail(Global.MAILIN);
				int size = list.size();
				if(size==0){
					Toast.makeText(MailConnectComp.this, str+getString(R.string.no_jieru), 1000).show();
				} else if(!"".equals(str)){
					Toast.makeText(MailConnectComp.this, str+getString(R.string.rep_in), 1000).show();
				}else{
					MyDialog.Builder builder = new MyDialog.Builder(MailConnectComp.this);
					builder.setTitle(getString(R.string.hint));
					builder.setMessage(getString(R.string.shifouwancheng));
					builder.setPositiveButton("",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									
									if (saveMail()) {
										Intent intent = new Intent();
										intent.putExtra("out_code", out_code);
										intent.setClass(MailConnectComp.this,
												MailConnectSuc.class);
										startActivity(intent);
										BeginConnect.allfinish();
										
										//邮件上传
										Intent mailserviceIntent = new Intent(MailConnectComp.this,
												PlUploadService.class);
										startService(mailserviceIntent);
										}
								}
							});
					builder.setNegativeButton("", null);
					builder.create().show();
					
				}
				break;
			case R.id.cancelbtn://撤销
				MyDialog.Builder builder = new MyDialog.Builder(MailConnectComp.this);
				builder.setTitle(getString(R.string.hint));
				builder.setMessage(getString(R.string.shifouce));
				builder.setPositiveButton("",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								
								mailhanddao = DaoFactory.getInstance().getMailHandDao(
										MailConnectComp.this);
								if(mailhanddao.updateMail(getlogName(),sid_time, Global.MAILCANCEL,"","")){
									Toast.makeText(MailConnectComp.this, getString(R.string.cancel_suc), 1000).show();
									Intent intent = new Intent();
									intent.setClass(MailConnectComp.this, MailConnectCancel.class);
									startActivity(intent);
									BeginConnect.allfinish();
								}
							}
						});
				builder.setNegativeButton("", null);
				builder.create().show();
				break;
			}
		}

	};
	
	// 显示信息
	private void loadData() {
		try {
			List<Map<String, Object>> rList = null;
			mailhanddetaildao = DaoFactory.getInstance().getMailHandDetailDao(MailConnectComp.this);
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
				tempHashMap.put("sid",rList.get(i).get("sid").toString());
				
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
	// 保存数据
	private boolean saveMail() {
		boolean isSave = false;
		try {
			if (params == null)
				params = new JSONObject();
			mailhanddao = DaoFactory.getInstance().getMailHandDao(this.getApplicationContext());
			
			String time = DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date()).toString();
			mailhanddao.updateMail(getlogName(),sid_time, Global.MAILCOM,time,Global.UP);
			
			try {
				MailHandDetailDao mailHandDetailDao=DaoFactory.getInstance().getMailHandDetailDao(this.getApplicationContext());
				List<Map<String, Object>> list=	mailHandDetailDao.FindMail(sid_time, "",-1);
				WorkLogDao workLogDao=DaoFactory.getInstance().getWorkLogDao(this.getApplicationContext());
				for(int i=0;i<list.size();i++){
					workLogDao.SaveWorkLog(list.get(i).get("mail_num").toString(), Global.HANDIN);
					}
				} catch (Exception e) {
				}
			
			if (params != null)
				params = null;
			isSave = true;
		} catch (Exception e) {

		}
		return isSave;
	}

}

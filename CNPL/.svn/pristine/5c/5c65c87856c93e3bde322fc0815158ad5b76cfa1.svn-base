package com.cn.net.cnpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.db.dao.MailHandDao;
import com.cn.net.cnpl.db.dao.MailHandDetailDao;
import com.cn.net.cnpl.db.dao.WorkLogDao;
import com.cn.net.cnpl.tools.BaseActivity;
import com.cn.net.cnpl.tools.CodeDictionary;
import com.cn.net.cnpl.tools.MyCode;
import com.cn.net.cnpl.tools.MyDatePickerDialog;
import com.google.zxing.client.android.CaptureActivity;

public class MailQuery extends BaseActivity {

	private Button codeButton = null;
	private SimpleAdapter adapter = null;
	private ListView listview = null;
	private List<Map<String, String>> list = null;
	public ProgressDialog myDialog = null;
	private int pageNo = 1;
	private boolean loadOver = true;
	
	//private MailHandDao mailhanddao = null;
	//private MailHandDetailDao mailhanddetaildao = null;
	private WorkLogDao worklogdao  = null;
	
	//邮件号查询
	private EditText reqmail = null;
	private String maillike = "";
	
	//查询
	private EditText reqdate = null;
	private MyDatePickerDialog my_datePickerDialog;
	private Button button = null;
	private Button respcamera = null;
	private int my_Year;
	private int my_Month;
	private int my_Day;
	Calendar my_Calendar;
	private String datelike = "";
	
	private void initDate() {
		/* 从Calendar抽象基类获得实例对象，并设置成中国时区 */
		my_Calendar = Calendar.getInstance(Locale.CHINA);
		/* 从日历对象中获取当前的：年、月、日、时、分 */
		my_Year = my_Calendar.get(Calendar.YEAR);
		my_Month = my_Calendar.get(Calendar.MONTH) + 1;
		my_Day = my_Calendar.get(Calendar.DAY_OF_MONTH);
		/*reqdate.setText(new StringBuffer().append(my_Year).append("-")
				.append(FormatString(my_Month)).append("-")
				.append(FormatString(my_Day)));*/
	}
	
	/* 日期改变设置事件监听器 */
	private OnDateSetListener myDateSetListener = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			/* 把设置修改后的日期赋值给我的年、月、日变量 */
			my_Year = year;
			my_Month = monthOfYear + 1;
			my_Day = dayOfMonth;
			/* 显示设置后的日期*/
			reqdate.setText(new StringBuffer().append(my_Year).append("-")
					.append(FormatString(my_Month)).append("-")
					.append(FormatString(my_Day))); 
		}
	};
	private String FormatString(int x) {
		String s = Integer.toString(x);
		if (s.length() == 1) {
			s = "0" + s;
		}
		return s;
	}
	
	private void setdialog(){
		my_datePickerDialog = new MyDatePickerDialog(
				MailQuery.this, myDateSetListener, my_Year,
				my_Month - 1, my_Day);
		my_datePickerDialog.show();
		my_datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
				new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						reqdate.setText("");
					}
		});
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.mail_query);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bg_new);
		
		listview = (ListView) findViewById(R.id.comListView);
		if (list == null)
			list = new ArrayList<Map<String, String>>();
		
		//查询
		reqdate = (EditText) findViewById(R.id.reqdate);
		reqmail = (EditText) findViewById(R.id.reqmail);
		reqdate.setInputType(0);
		initDate();
		reqdate.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setdialog();
				
			}
			
		});
		codeButton = (Button) findViewById(R.id.btncode);
		codeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyCode.Builder builder = new MyCode.Builder(
						MailQuery.this);
				builder.setTitle("二维码:");
				builder.setPositiveButton("", null);
				builder.create().show();
			}

		});
		button = (Button) findViewById(R.id.queryButton);
		button.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// 初始化列表信息
				if (list != null) {
					list.clear();
				}
				loadOver = true;
				pageNo = 1;
				
				adapter = new SimpleAdapter(MailQuery.this, list,
						R.layout.queryitem, new String[] { "num","mail_num","action_date", "action"
						}, new int[] {R.id.num, R.id.mail_num,R.id.action_date,
						R.id.action});

				listview.setAdapter(adapter);
				loadData();
				
				}
		});
		
		respcamera = (Button) findViewById(R.id.respcamera);
		respcamera.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MailQuery.this, CaptureActivity.class);
				startActivityForResult(intent, 1);
			}
		});
		
/*
		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String out_code = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("conout_txt");
				String codetype = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("codetype");
				String sid_time = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("sid_time");
				String begin_time = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("begin_time");
				String total_txt = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("total_txt");
				String disrepair_txt = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("disrepair_txt");
				String lose_txt = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("lose_txt");
				String upload_txt = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("upload_txt");
				String no_upload_txt = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("no_upload_txt");
				Intent intent = new Intent();
				intent.putExtra("out_code", out_code);
				intent.putExtra("codetype", codetype);
				intent.putExtra("sid_time", sid_time);
				intent.putExtra("begin_time", begin_time);
				intent.putExtra("total_txt", total_txt);
				intent.putExtra("disrepair_txt", disrepair_txt);
				intent.putExtra("lose_txt", lose_txt);
				intent.putExtra("upload_txt", upload_txt);
				intent.putExtra("no_upload_txt", no_upload_txt);
				intent.setClass(MailQuery.this, Mail_CompDetail.class);
				startActivity(intent);

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
						myDialog = ProgressDialog.show(MailQuery.this,
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
		
		
	}
	// 返回
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == 1) {
			Bundle bundle = intent.getExtras();
			String strDnNumber = bundle.getString("txtResult");
			if (strDnNumber != null && strDnNumber.length() > 0)
			{
				reqmail.setText(strDnNumber);
			}
		}
	}
	// 显示信息
	private void loadData() {
		try {
			List<Map<String, Object>> rList = null;
			datelike = reqdate.getText().toString();
			maillike = reqmail.getText().toString();
			Map<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("action_date", datelike);
			hashMap.put("mail_num", maillike);
			
			worklogdao = DaoFactory.getInstance().getWorkLogDao(MailQuery.this);
			rList =  worklogdao.FindWorkLogByParams(hashMap,pageNo);
			
			if (rList == null || rList.size() == 0) {
				loadOver = false;
				Toast.makeText(MailQuery.this, getString(R.string.no_mail), 1000).show();
				return;
			}
			int tempSize = rList.size();
			
			for (int i = 0; i < tempSize; i++) {
				Map<String, String> tempHashMap = new LinkedHashMap<String, String>();
				tempHashMap.put("num", "" + ((pageNo - 1) * 10 + 1 + i));
				tempHashMap.put("mail_num", rList.get(i).get("mail_num").toString());
				tempHashMap.put("action_date",rList.get(i).get("action_date").toString());
				if(Global.HANDIN.equals(rList.get(i).get("action").toString()))
					tempHashMap.put("action",getString(R.string.in_mail));
				else
					tempHashMap.put("action",getString(R.string.out_mail));
				
				list.add(tempHashMap);
			}
			
			/*
			List<Map<String, Object>> rList = null;
			
			datelike = reqdate.getText().toString();
			maillike = reqmail.getText().toString();
			mailhanddetaildao = DaoFactory.getInstance().getMailHandDetailDao(
					MailQuery.this);
			mailhanddao = DaoFactory.getInstance().getMailHandDao(MailQuery.this);
			
			rList = mailhanddao.FindOutMail(Global.LOG_NAME,datelike,maillike,true,pageNo);
			
			
			if (rList == null || rList.size() == 0) {
				loadOver = false;
				Toast.makeText(MailQuery.this, getString(R.string.no_mail), 1000).show();
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
				if(Global.MAILIN.equals(rList.get(i).get("hand_type").toString())){
					tempHashMap.put("codetype", getString(R.string.mailcon_out));
					tempHashMap.put("conout_txt", rList.get(i).get("out_code").toString());
				}else{
					tempHashMap.put("codetype", getString(R.string.out_con));
					tempHashMap.put("conout_txt", rList.get(i).get("in_code").toString());
				}
				
				sid_time_temp=rList.get(i).get("sid").toString();
				mailcnt = mailhanddetaildao.Findcount( sid_time_temp, Global.MAILINTACT);
				disrepair = mailhanddetaildao.Findcount( sid_time_temp, Global.MAILDISREPAIR);
				lose = mailhanddetaildao.Findcount( sid_time_temp, Global.MAILLOSE);
				mailcnt = (Integer.parseInt(mailcnt)+Integer.parseInt(disrepair)+Integer.parseInt(lose))+"";
				
				tempHashMap.put("time_txt", rList.get(i).get("begin_time").toString()+"-"+rList.get(i).get("end_time").toString());
				tempHashMap.put("total_txt",mailcnt);
				tempHashMap.put("disrepair_txt",disrepair);
				tempHashMap.put("lose_txt",lose);
				tempHashMap.put("upload_txt","0");
				tempHashMap.put("no_upload_txt","0");
				tempHashMap.put("begin_time", rList.get(i).get("begin_time").toString());
				tempHashMap.put("sid_time", sid_time_temp);
				
				list.add(tempHashMap);
			}*/

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

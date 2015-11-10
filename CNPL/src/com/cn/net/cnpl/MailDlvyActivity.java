package com.cn.net.cnpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.jar.Pack200.Unpacker;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.db.dao.MailDao;
import com.cn.net.cnpl.tools.BaseActivity;
import com.cn.net.cnpl.tools.MyCode;
import com.cn.net.cnpl.tools.MyDatePickerDialog;

public class MailDlvyActivity extends BaseActivity {
	private TextView title;
	private Button back;
	private TextView upload,unUpload;
	private Button codeButton = null;
	private SimpleAdapter adapter = null;
	private ListView listview = null;

	private Button dlventer = null;
	// 上传，未上传Radio
	private RadioGroup radiogroup = null;
	private RadioButton mailTypeButton1 = null;
	private RadioButton mailTypeButton2 = null;

	// 上传，未上传，保存信息列表
	private List<Map<String, String>> list = null;
	private MailDao maildao = null;

	private int pageNo = 1;

	private boolean loadOver = true;
	public ProgressDialog myDialog = null;
	public final static int TASK_LOOP_COMPLETE = 0;
	private String isupload = "";
	
	//查询
	private EditText reqdate = null;
	private MyDatePickerDialog my_datePickerDialog;
	private Button button = null;
	private int my_Year;
	private int my_Month;
	private int my_Day;
	Calendar my_Calendar;
	private String datelike = "";
	
	private Button	dlvbatchenter=null;
	
	private void initDate() {
		/* 从Calendar抽象基类获得实例对象，并设置成中国时区 */
		my_Calendar = Calendar.getInstance(Locale.CHINA);
		/* 从日历对象中获取当前的：年、月、日、时、分 */
		my_Year = my_Calendar.get(Calendar.YEAR);
		my_Month = my_Calendar.get(Calendar.MONTH) + 1;
		my_Day = my_Calendar.get(Calendar.DAY_OF_MONTH);
		reqdate.setText(new StringBuffer().append(my_Year).append("-")
				.append(FormatString(my_Month)).append("-")
				.append(FormatString(my_Day)));
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
			/* 显示设置后的日期 */
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.mail_dlvy_new);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bg_new);
		initTitle();
		changeState();
		listview = (ListView) findViewById(R.id.DlvListView);
		if (list == null)
			list = new ArrayList<Map<String, String>>();
		
		//查询
		reqdate = (EditText) findViewById(R.id.reqdate);
		reqdate.setInputType(0);
		initDate();
		reqdate.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				my_datePickerDialog = new MyDatePickerDialog(
						MailDlvyActivity.this, myDateSetListener, my_Year,
						my_Month - 1, my_Day);
				my_datePickerDialog.show();
				
			}
			
		});
		/*
		codeButton = (Button) findViewById(R.id.btncode);
		codeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyCode.Builder builder = new MyCode.Builder(
						MailDlvyActivity.this);
				builder.setTitle("二维码:");
				builder.setPositiveButton("", null);
				builder.create().show();
			}

		});
		*/
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
				if (upload.isSelected()) {
					adapter = new SimpleAdapter(MailDlvyActivity.this, list,
							R.layout.mail_dlvyqueryitem, new String[] { "num",
									"dlvmailid", "dlvDate", "dlvName" },
							new int[] { R.id.num, R.id.dlvmailid, R.id.dlvDate,
									R.id.dlvName });

					listview.setAdapter(adapter);
					isupload = Global.UPLOAD;
					loadData(Global.DLVCODE, Global.UPLOAD);
				}
				if (unUpload.isSelected()) {

					adapter = new SimpleAdapter(MailDlvyActivity.this, list,
							R.layout.mail_dlvyqueryitemn, new String[] {
									"numn", "dlvmailid", "dlvName",
									"dlvoperation" }, new int[] { R.id.numn,
									R.id.dlvmailid, R.id.dlvName,
									R.id.dlvoperation });
					listview.setAdapter(adapter);
					isupload = Global.UNUPLOAD;
					loadData(Global.DLVCODE, Global.UNUPLOAD);
				}
			

			
			}
			
		});
		
		
		// 录入按钮
		dlventer = (Button) findViewById(R.id.dlventer);
		dlventer.setText(getString(R.string.deliver_enter).toString());
		dlventer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MailDlvyActivity.this, MailDlvyEnter.class);
				startActivityForResult(intent,1);
			}

		});
		
		dlvbatchenter  = (Button) findViewById(R.id.dlvbatchenter);
		dlvbatchenter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MailDlvyActivity.this, MailBatchDlvyEnter.class);
				startActivityForResult(intent,1);
			}

		});
		

		adapter = new SimpleAdapter(this, list, R.layout.mail_dlvyqueryitem,
				new String[] {"num", "dlvmailid", "dlvDate", "dlvName"}, new int[] { R.id.num,
						R.id.dlvmailid, R.id.dlvDate, R.id.dlvName });

		isupload = Global.UPLOAD;
		listview.setAdapter(adapter);
		loadData(Global.DLVCODE, Global.UPLOAD);
		
		/*
		radiogroup = (RadioGroup) findViewById(R.id.dlvradioGroup);
		mailTypeButton1 = (RadioButton) findViewById(R.id.dlvradio0);
		mailTypeButton1.setText(getString(R.string.upload).toString());
		mailTypeButton2 = (RadioButton) findViewById(R.id.dlvradio1);
		mailTypeButton2.setText(getString(R.string.no_upload).toString());
		radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				//初始化列表信息
				if (list != null){
					list.clear();
				}
				loadOver=true;
				pageNo = 1;
				switch (checkedId) {
				case R.id.dlvradio0:
					adapter = new SimpleAdapter(MailDlvyActivity.this, list,
							R.layout.mail_dlvyqueryitem, new String[] {"num",
									"dlvmailid", "dlvDate", "dlvName"},
							new int[] { R.id.num, R.id.dlvmailid, R.id.dlvDate,
									R.id.dlvName});

					listview.setAdapter(adapter);
					isupload = Global.UPLOAD;
					loadData(Global.DLVCODE, Global.UPLOAD);
					break;
				case R.id.dlvradio1:

					adapter = new SimpleAdapter(MailDlvyActivity.this, list,
							R.layout.mail_dlvyqueryitemn, new String[] {"numn",
									"dlvmailid", "dlvName" ,"dlvoperation"}, new int[] { R.id.numn,
									R.id.dlvmailid, R.id.dlvName ,R.id.dlvoperation});
					listview.setAdapter(adapter);
					isupload = Global.UNUPLOAD;
					loadData(Global.DLVCODE, Global.UNUPLOAD);
					break;
				}
			}

		});
		*/

		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String maio = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("dlvmailid");
				String createDate = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("dlvName");
				Intent intent = new Intent();
				intent.putExtra("mailno", maio);
				intent.putExtra("isupload", MailDlvyActivity.this.isupload);
				intent.putExtra("create_time", createDate);
				intent.setClass(MailDlvyActivity.this, Mail_DlvDetail.class);
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
						myDialog = ProgressDialog.show(MailDlvyActivity.this,
								 Global.DIALOG_NAME, getString(R.string.loading), true, true);

						new Thread() {
							public void run() {
								try {
									if(mailTypeButton2.isChecked()){
										loadData(Global.DLVCODE, Global.UNUPLOAD);
									}else{
										loadData(Global.DLVCODE, Global.UPLOAD);
									}
									
								} catch (Exception e) {
									// e.printStackTrace();
								} finally {
									messageListener
											.sendEmptyMessage(TASK_LOOP_COMPLETE);
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

	private void initTitle() {
		// TODO Auto-generated method stub
		title = (TextView) findViewById(R.id.new_name);
		title.setText("妥投");
		back = (Button) findViewById(R.id.top_left);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MailDlvyActivity.this.finish();
			}
		});
		upload = (TextView) findViewById(R.id.tab_upload);
		upload.setSelected(true);
		unUpload = (TextView) findViewById(R.id.tab_no_upload);
		unUpload.setSelected(false);
	}
	private void changeState() {
		// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			upload.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (list != null){
						list.clear();
					}
					loadOver=true;
					pageNo = 1;
					upload.setSelected(true);
					unUpload.setSelected(false);
					upload.setTextColor(Color.WHITE);
					unUpload.setTextColor(Color.GRAY);
					adapter = new SimpleAdapter(MailDlvyActivity.this, list,
							R.layout.mail_dlvyqueryitem, new String[] {"num",
									"dlvmailid", "dlvDate", "dlvName"},
							new int[] { R.id.num, R.id.dlvmailid, R.id.dlvDate,
									R.id.dlvName});

					listview.setAdapter(adapter);
					isupload = Global.UPLOAD;
					loadData(Global.DLVCODE, Global.UPLOAD);
				}
			});
			unUpload.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (list != null){
						list.clear();
					}
					loadOver=true;
					pageNo = 1;
					unUpload.setSelected(true);
					upload.setSelected(false);
					unUpload.setTextColor(Color.WHITE);
					upload.setTextColor(Color.GRAY);
					adapter = new SimpleAdapter(MailDlvyActivity.this, list,
							R.layout.mail_dlvyqueryitemn, new String[] {"numn",
									"dlvmailid", "dlvName" ,"dlvoperation"}, new int[] { R.id.numn,
									R.id.dlvmailid, R.id.dlvName ,R.id.dlvoperation});
					listview.setAdapter(adapter);
					isupload = Global.UNUPLOAD;
					loadData(Global.DLVCODE, Global.UNUPLOAD);
				}
			});
	}

	private Handler messageListener = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TASK_LOOP_COMPLETE:
				myDialog.dismiss();
				adapter.notifyDataSetChanged();
				break;
			}
		}
	};

	// 显示上传，未上传，保存信息
	private void loadData(String dlvStsCode, String isupload) {
		try {
			String interstr = "";
			maildao = DaoFactory.getInstance().getMailDao(MailDlvyActivity.this);

			List<Map<String, Object>> rList = null;

			datelike = reqdate.getText().toString();
			if(!"".equals(datelike)){
				datelike = datelike.replace("-", "");
				rList = maildao.FindMailByUpload(getlogName(),
					dlvStsCode, isupload, datelike, pageNo);
			}
			else
			{
				rList = maildao.FindMailByUpload(getlogName(),
						dlvStsCode, isupload, pageNo);
			}
			if (rList == null || rList.size() == 0) {
				loadOver = false;
				return;
			}
			int tempSize = rList.size();
			// if (rList.size() > 0) {
			// if (rList.size() > 10) {
			// tempLimit = 10;
			// }
			// }
			// list.clear();
			String tempop = "";
			String tempcr = "";
			SimpleDateFormat f=new SimpleDateFormat("yyyyMMddkkmmss");
			for (int i = 0; i < tempSize; i++) {
				Map<String, String> tempHashMap = new LinkedHashMap<String, String>();
				tempHashMap.put("dlvmailid", (String)rList.get(i).get("mailCode"));
				tempcr = rList.get(i).get("createDate").toString();
				tempHashMap.put("dlvName", ""+DateFormat.format("yyyy-MM-dd kk:mm:ss", f.parse(tempcr)));
//				if (Global.INTERNATIONAL.equals(rList.get(i).get("interFlag")))
//					interstr = getString(R.string.international);
//				else if (Global.INTERNAL.equals(rList.get(i).get("interFlag")))
//					interstr =  getString(R.string.internal);
//				tempHashMap.put("dlvName", interstr);
				if (isupload.equals(Global.UPLOAD)){
					tempop = rList.get(i).get("operationTime").toString();
					tempHashMap.put("dlvDate", ""+DateFormat.format("yyyy-MM-dd kk:mm:ss", f.parse(tempop)));
					tempHashMap.put("num", "" + ((pageNo - 1) * 10 + 1 + i));
				}else if (isupload.equals(Global.UNUPLOAD)) {
					tempHashMap.put("numn", "" + ((pageNo - 1) * 10 + 1 + i));
					if (rList.get(i).get("operationMode").toString()
							.equals("I"))
						tempHashMap
								.put("dlvoperation", getString(R.string.add));
					else if (rList.get(i).get("operationMode").toString()
							.equals("D"))
						tempHashMap.put("dlvoperation",
								getString(R.string.delete));
				}
				list.add(tempHashMap);
			}

			pageNo++;
		} catch (Exception e) {
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == 1){
			Intent intent = new Intent();
			intent.setClass(MailDlvyActivity.this, MailDlvyActivity.class);
			startActivity(intent);
			finish();
		}else if(resultCode == 2){
			Intent intent = new Intent();
			intent.setClass(MailDlvyActivity.this, MailDlvyActivity.class);
			startActivity(intent);
			finish();
		}
	}
	
}

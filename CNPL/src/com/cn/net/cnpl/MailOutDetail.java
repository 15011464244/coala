package com.cn.net.cnpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.db.dao.MailHandDao;
import com.cn.net.cnpl.db.dao.MailHandDetailDao;
import com.cn.net.cnpl.db.dao.WorkLogDao;
import com.cn.net.cnpl.tools.BaiduGps;
import com.cn.net.cnpl.tools.BaseActivity;
import com.cn.net.cnpl.tools.BaseCommand;
import com.cn.net.cnpl.tools.CodeDictionary;
import com.cn.net.cnpl.tools.MyCode;
import com.cn.net.cnpl.tools.MyDialog;
import com.google.zxing.client.android.CaptureActivity;

public class MailOutDetail extends BaseActivity {

	private Button codeButton = null;
	private ListView listView = null;
	private MyAdapter adapter = null;
	private ArrayList<Map<String, String>> sourceList = null;
	public ProgressDialog myDialog = null;
	
	private Button beforebtn = null;
	private Button okbtn = null;
	private Button nextbtn = null;
	private LinearLayout textid = null;
	
	private String in_type="";
	private String in_code="";
	private String begin_time="";
	
	private MailHandDetailDao mailhanddetaildao = null;
	
	private int pageNo = 1;
	private boolean loadOver = true;
	String str2code="";
	int cnt=0;
	
	private TextView mailcon_out_no = null;
	private TextView exchange_time_txt = null;
	private TextView mailtotal_txt = null;
	private TextView disrepair_no_txt = null;
	private TextView lose_no_txt = null;
	
	private boolean is_ok=false;
	
	int mailcnt =0;
	int disrepair =0;
	int lose =0;
	String suc_secform="";
	String begin_times;
	String sid_time;
	String time_txt;
	int tempI=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.out_dt);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bg_new);
		
		BeginConnect.activities.add(MailOutDetail.this);
		
		BeginConnect.activities.add(MailOutDetail.this);
		baiduGps  = BaiduGps.getInstance(this.getApplicationContext());
		baiduGps.getLocation();
		
		Intent intent = getIntent();
		in_code = intent.getStringExtra("in_code");
		in_type = intent.getStringExtra("in_type");
		sid_time = intent.getStringExtra("sid_time");
		begin_time = intent.getStringExtra("begin_time");
		suc_secform = intent.getStringExtra("suc_secform");
		
		if("2".equals(suc_secform)){
			disrepair = Integer.parseInt(intent.getStringExtra("discnt"));
			lose = Integer.parseInt(intent.getStringExtra("losecnt"));
			mailcnt = Integer.parseInt(intent.getStringExtra("selcnt"))+disrepair+lose;
			begin_times = intent.getStringExtra("jason");
			time_txt=begin_time;
		}else {
			time_txt = intent.getStringExtra("time_txt");
			mailhanddetaildao = DaoFactory.getInstance().getMailHandDetailDao(
					MailOutDetail.this);
			mailcnt = Integer.parseInt(mailhanddetaildao.Findcount(
					sid_time, Global.MAILINTACT));
			disrepair = Integer.parseInt(mailhanddetaildao.Findcount(
					sid_time, Global.MAILDISREPAIR));
			lose = Integer.parseInt(mailhanddetaildao.Findcount(sid_time,
						Global.MAILLOSE));
			mailcnt = mailcnt+disrepair+lose;
		}
		mailcon_out_no = (TextView) findViewById(R.id.mailcon_out_no);
		mailcon_out_no.setText(in_code);
		exchange_time_txt = (TextView) findViewById(R.id.exchange_time_txt);
		exchange_time_txt.setText(time_txt);
		mailtotal_txt = (TextView) findViewById(R.id.mailtotal_txt);
		mailtotal_txt.setText(""+mailcnt);
		disrepair_no_txt = (TextView) findViewById(R.id.disrepair_no_txt);
		disrepair_no_txt.setText(""+disrepair);
		lose_no_txt = (TextView) findViewById(R.id.lose_no_txt);
		lose_no_txt.setText(""+lose);
		
		sourceList = new ArrayList<Map<String, String>>();
		
		
		listView = (ListView) findViewById(R.id.comListView);  
		
		if("2".equals(suc_secform)){
			listView.setOnItemLongClickListener(new OnItemLongClickListener(){

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					tempI = arg2;
					MyDialog.Builder builder = new MyDialog.Builder(MailOutDetail.this);
					builder.setTitle(getString(R.string.hint));
					builder.setMessage(getString(R.string.shifoudelete));
					builder.setPositiveButton("",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									try{
									sourceList.remove(tempI);
							        adapter.notifyDataSetChanged();
							        
							        JSONObject jsonObject = new JSONObject();
									JSONArray objArray = new JSONArray();
									for (int i = 0; i < sourceList.size(); i++) {										
										jsonObject = null;
										if(jsonObject == null)
											jsonObject = new JSONObject();
										jsonObject.put("mailNo", sourceList.get(i).get("mailno_txt").toString());
										if(getString(R.string.disrepair).equals(sourceList.get(i).get("type_txt").toString()))
											jsonObject.put("isMangle", Global.MAILDISREPAIR);
										else if(getString(R.string.lose).equals(sourceList.get(i).get("type_txt").toString()))
											jsonObject.put("isMangle", Global.MAILLOSE);
										else if(getString(R.string.comp).equals(sourceList.get(i).get("type_txt").toString()))
											jsonObject.put("isMangle", Global.MAILINTACT);
										jsonObject.put("responsible", sourceList.get(i).get("principal").toString());
										jsonObject.put("principal_type", sourceList.get(i).get("principal_type").toString());
										jsonObject.put("abnormity_time", sourceList.get(i).get("abnormity_time").toString());
										jsonObject.put("create_time", sourceList.get(i).get("create_time").toString());
										jsonObject.put("upload_time", sourceList.get(i).get("upload_time").toString());
										jsonObject.put("sid", sourceList.get(i).get("sid").toString());
										jsonObject.put("oldSid", sourceList.get(i).get("oldSid").toString());
										jsonObject.put("signatureImg", sourceList.get(i).get("signatureImg").toString());
										objArray.put(jsonObject);
									}
									
									begin_times = objArray.toString();
									}catch(Exception e){
										
									}
								}
							});
					builder.setNegativeButton("", null);
					builder.create().show();
					return false;
				}
				
			});
		}
		
		if("2".equals(suc_secform)){
			loadData(begin_times);
		}else{
			loadData();
		}
		
		adapter = new MyAdapter(MailOutDetail.this);

		listView.setAdapter(adapter);

		
		//new ListViewUtils().setListViewHeightBasedOnChildren(listView); 
		
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& view.getLastVisiblePosition() == view.getCount() - 1) {
					if (loadOver) {
						myDialog = ProgressDialog.show(MailOutDetail.this,
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
		
		codeButton = (Button) findViewById(R.id.btncode);
		codeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyCode.Builder builder = new MyCode.Builder(
						MailOutDetail.this);
				builder.setTitle("二维码:");
				builder.setPositiveButton("", null);
				builder.create().show();
			}

		});
		textid = (LinearLayout)findViewById(R.id.textid);
		beforebtn = (Button)findViewById(R.id.beforebtn);
		nextbtn = (Button)findViewById(R.id.nextbtn);
		okbtn = (Button)findViewById(R.id.okbtn);
		if("1".equals(suc_secform)){
			beforebtn.setVisibility(View.GONE);
			nextbtn.setVisibility(View.GONE);
			okbtn.setVisibility(View.GONE);
			textid.setVisibility(View.GONE);
		}else if("2".equals(suc_secform)){
			beforebtn.setVisibility(View.VISIBLE);
			nextbtn.setVisibility(View.VISIBLE);
			okbtn.setVisibility(View.VISIBLE);
			textid.setVisibility(View.VISIBLE);
		beforebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("in_type", in_type);
				intent.putExtra("in_code", in_code);
				intent.putExtra("sid_time", sid_time);
				intent.putExtra("begin_time", begin_time);
				intent.setClass(MailOutDetail.this, MailOutSelect.class);
				startActivity(intent);
				BeginConnect.allfinish();
			}
		});
		}
		okbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MailOutDetail.this, CaptureActivity.class);
				startActivityForResult(intent, 1);
			}
		
		});
		
		nextbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				Intent intent = new Intent();
				intent.putExtra("in_type", in_type);
				intent.putExtra("in_code", in_code);
				intent.putExtra("mailcnt", ""+mailcnt);
				intent.putExtra("disrepair", ""+disrepair);
				intent.putExtra("lose", ""+lose);
				intent.putExtra("sid_time", sid_time);
				intent.putExtra("begin_time", begin_time);
				intent.putExtra("begin_times", begin_times);
				intent.putExtra("str2code", str2code);
				intent.setClass(MailOutDetail.this, MailOutForm.class);
				startActivity(intent);
				
				Intent intent = new Intent();
				intent.putExtra("in_type", in_type);
				intent.putExtra("in_code", in_code);
				intent.putExtra("mailcnt", ""+mailcnt);
				intent.putExtra("disrepair", ""+disrepair);
				intent.putExtra("lose", ""+lose);
				intent.putExtra("sid_time", sid_time);
				intent.putExtra("begin_time", begin_time);
				intent.putExtra("begin_times", begin_times);
				intent.putExtra("suc_secform", suc_secform);
				intent.putExtra("str2code", str2code);
				intent.setClass(MailOutDetail.this, MailOut2Code.class);
				startActivity(intent);*/
				
				MyDialog.Builder builder = new MyDialog.Builder(MailOutDetail.this);
				builder.setTitle(getString(R.string.hint));
				if(!is_ok){
				builder.setMessage(getString(R.string.scan_is));
				builder.setPositiveButton("",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								
								saveMail(begin_times);
								
								Intent intent = new Intent();
								intent.putExtra("in_code", in_code);
								intent.putExtra("sid_time", sid_time);
								intent.putExtra("begin_time", begin_time);
								intent.setClass(MailOutDetail.this, MailOutSuc.class);
								startActivity(intent);
								BeginConnect.allfinish();
								
								//邮件上传
								Intent mailserviceIntent = new Intent(MailOutDetail.this,
										PlUploadService.class);
								startService(mailserviceIntent);
							}
						});
				builder.setNegativeButton("", null);
				builder.create().show();
			}else{
				builder.setMessage(getString(R.string.ok_out));
				builder.setPositiveButton("",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								
								saveMail(begin_times);
								
								Intent intent = new Intent();
								intent.putExtra("in_code", in_code);
								intent.putExtra("sid_time", sid_time);
								intent.putExtra("begin_time", begin_time);
								intent.setClass(MailOutDetail.this, MailOutSuc.class);
								startActivity(intent);
								BeginConnect.allfinish();
								
								//邮件上传
								Intent mailserviceIntent = new Intent(MailOutDetail.this,
										PlUploadService.class);
								startService(mailserviceIntent);
							}
						});
				builder.setNegativeButton("", null);
				builder.create().show();
			}
			}
		});
		
	}
	
	// ??????
	private void loadData(String begin_times) {
		try {
			JSONArray objArray = new JSONArray(begin_times);
			int tempSize = objArray.length();
			
			for (int i = 0; i < tempSize; i++) {
				Map<String, String> tempHashMap = new LinkedHashMap<String, String>();
				tempHashMap.put("num", "" + pageNo);
				tempHashMap.put("mailno_txt", objArray.getJSONObject(i).get("mailNo").toString());
				if(Global.MAILDISREPAIR.equals(objArray.getJSONObject(i).get("isMangle").toString()))
					tempHashMap.put("type_txt", getString(R.string.disrepair));
				else if(Global.MAILLOSE.equals(objArray.getJSONObject(i).get("isMangle").toString()))
					tempHashMap.put("type_txt", getString(R.string.lose));
				else if(Global.MAILINTACT.equals(objArray.getJSONObject(i).get("isMangle").toString()))
					tempHashMap.put("type_txt", getString(R.string.comp));
				tempHashMap.put("principal", objArray.getJSONObject(i).get("responsible").toString());
				tempHashMap.put("principal_type", objArray.getJSONObject(i).get("principal_type").toString());
				tempHashMap.put("abnormity_time", objArray.getJSONObject(i).get("abnormity_time").toString());
				tempHashMap.put("create_time", objArray.getJSONObject(i).get("create_time").toString());
				tempHashMap.put("upload_time", objArray.getJSONObject(i).get("upload_time").toString());
				tempHashMap.put("sid", objArray.getJSONObject(i).get("sid").toString());
				tempHashMap.put("oldSid", objArray.getJSONObject(i).get("oldSid").toString());
				tempHashMap.put("signatureImg", objArray.getJSONObject(i).get("signatureImg").toString());
				
				str2code = str2code + objArray.getJSONObject(i).get("mailNo").toString() + objArray.getJSONObject(i).get("isMangle").toString();
				sourceList.add(tempHashMap);
				pageNo++;
			}
			
		} catch (Exception e) {
		}
	}
	String tempinCode="";
	String tempoutCode="";
	String temptotlecnt="";
	// 返回
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == 1) {
			Bundle bundle = intent.getExtras();
			String strDnNumber = bundle.getString("txtResult");
			strDnNumber = BaseCommand.decodeStr(strDnNumber);
			if (strDnNumber != null && strDnNumber.length() > 0)
			{
				try{
				JSONObject object = new JSONObject(strDnNumber);
			    
				tempinCode=object.get("inCode").toString();
				tempoutCode=object.get("outCode").toString();
				temptotlecnt=object.get("total").toString();
				
				if(tempinCode.equals(in_code)&&tempoutCode.equals(getorgCode())){
					if(temptotlecnt.equals(""+mailcnt)){
						is_ok=true;
						Toast.makeText(MailOutDetail.this, getString(R.string.scan_ok), 3000).show();
					}else{
						Toast.makeText(MailOutDetail.this, getString(R.string.scan_no), 3000).show();
						
					}
				}else{
					Toast.makeText(MailOutDetail.this, getString(R.string.scan_nocode), 3000).show();
					
				}
				}catch(Exception e){
					
				}
			}
		}
	}
	
	// ??????
	private void loadData() {
		try {
			List<Map<String, Object>> rList = null;
			mailhanddetaildao = DaoFactory.getInstance().getMailHandDetailDao(MailOutDetail.this);
			if("1".equals(suc_secform)){
				rList = mailhanddetaildao.FindMail(sid_time,Global.MAILOUT,-1);
			}else{
				rList = mailhanddetaildao.FindMail(sid_time,Global.MAILIN,-1);
			}
			if (rList == null || rList.size() == 0) {
				loadOver = false;
				return;
			}
			int tempSize = rList.size();
			String str = "";
			JSONObject jsonObject = new JSONObject();
			JSONArray objArray = new JSONArray();
			
			for (int i = 0; i < tempSize; i++) {
				Map<String, String> tempHashMap = new LinkedHashMap<String, String>();
				tempHashMap.put("mailno_txt",(String) rList.get(i).get("mail_num"));
				tempHashMap.put("num", "" + pageNo);//((pageNo - 1) * 10 + 1 + i));
				
				if(Global.MAILDISREPAIR.equals(rList.get(i).get("mail_type")))
					str = getString(R.string.disrepair);
				else if(Global.MAILLOSE.equals(rList.get(i).get("mail_type")))
					str = getString(R.string.lose);
				else if(Global.MAILINTACT.equals(rList.get(i).get("mail_type")))
					str = getString(R.string.comp);
				
				tempHashMap.put("type_txt", str);
				tempHashMap.put("principal",(String) rList.get(i).get("principal"));
				tempHashMap.put("createDate",(String) rList.get(i).get("create_time"));
				str2code = str2code + rList.get(i).get("mail_num") + rList.get(i).get("mail_type");
				sourceList.add(tempHashMap);
				
				jsonObject = null;
				if(jsonObject == null)
					jsonObject = new JSONObject();
				jsonObject.put("mailNo", rList.get(i).get("mail_num").toString());
				jsonObject.put("isMangle", rList.get(i).get("mail_type").toString());
				jsonObject.put("responsible", rList.get(i).get("principal").toString());
				jsonObject.put("principal_type", rList.get(i).get("principal_type").toString());
				jsonObject.put("abnormity_time", rList.get(i).get("abnormity_time").toString());
				jsonObject.put("create_time", rList.get(i).get("create_time").toString());
				jsonObject.put("upload_time", rList.get(i).get("upload_time").toString());
				jsonObject.put("sid", rList.get(i).get("sid").toString());
				
				objArray.put(jsonObject);
				
				pageNo++;
			}
			
			begin_times = objArray.toString();
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
	
	
	
	public class MyAdapter extends BaseAdapter{

		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return sourceList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			Holder holder = null;

			if (convertView == null) {

				holder = new Holder();
				convertView = mInflater.inflate(R.layout.out_dtitem, null);
				holder.num = (TextView) convertView.findViewById(R.id.num);
				holder.mailno_txt = (TextView) convertView.findViewById(R.id.mailno_txt);
				holder.type_txt = (TextView) convertView.findViewById(R.id.type_txt);
				holder.principal = (TextView) convertView.findViewById(R.id.principal);
				convertView.setTag(holder);
			} else {

				holder = (Holder) convertView.getTag();

			}

			holder.num.setText(sourceList.get(position).get("num").toString());
			holder.mailno_txt.setText(sourceList.get(position).get("mailno_txt").toString());
			holder.type_txt.setText(sourceList.get(position).get("type_txt").toString());
			LinearLayout lay = (LinearLayout) convertView.findViewById (R.id.vlayout);
			if("".equals(sourceList.get(position).get("principal").toString())){
				lay.setVisibility(View.GONE);
			}else{
				lay.setVisibility(View.VISIBLE);
				holder.principal.setText(sourceList.get(position).get("principal").toString());
			}

			return convertView;

		}
		
	}
	public class Holder {
		public TextView num;
		public TextView mailno_txt;
		public TextView type_txt;
		public TextView principal;
	}
	
	
	String end_time ="";
	private MailHandDao mailhanddao = null;
	private BaiduGps baiduGps;
	
	private void saveMail(String jason) {
		try {
			if(!"".equals(getlogName())){
				
			mailhanddetaildao = DaoFactory.getInstance().getMailHandDetailDao(MailOutDetail.this);
			JSONArray objArray = new JSONArray(jason);
			int tempSize = objArray.length();
			for (int i = 0; i < tempSize; i++) {
				JSONObject	params = new JSONObject();
				if(mailhanddetaildao.ExitMail(objArray.getJSONObject(i).get("mailNo").toString(),Global.MAILOUT)){
					Toast.makeText(MailOutDetail.this, objArray.getJSONObject(i).get("mailNo").toString()+getString(R.string.is_out), 1000).show();
					continue;
				}else{
					end_time = DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date()).toString();
				params.put("mail_num", objArray.getJSONObject(i).get("mailNo"));
			
				params.put("mail_type",objArray.getJSONObject(i).get("isMangle"));
				params.put("principal", objArray.getJSONObject(i).get("responsible"));
				params.put("principal_type", objArray.getJSONObject(i).get("principal_type"));
				
				params.put("abnormity_time", objArray.getJSONObject(i).get("abnormity_time"));
				params.put("create_time", objArray.getJSONObject(i).get("create_time"));
//				params.put("upload_time", objArray.getJSONObject(i).get("upload_time"));
				
				params.put("sid", sid_time);
				params.put("is_out", Global.MAILOUT);
				params.put("out_time", end_time);
				
				params.put("code2d_num", "");
				params.put("paper_num", "");
				params.put("operatorType", "I");
				params.put("oldSid", objArray.getJSONObject(i).get("oldSid"));
				params.put("signatureImg", objArray.getJSONObject(i).get("signatureImg"));
				
				mailhanddetaildao.SaveMail(params);
				mailhanddetaildao.updateMail(objArray.getJSONObject(i).get("mailNo").toString(),objArray.getJSONObject(i).get("sid").toString(), Global.MAILOUT,end_time,"");
				
				}
			}
			saveMail_S();
			}
		} catch (Exception e) {

		}
	}
	private boolean saveMail_S() {
		boolean isSave = false;
		try {
			
			mailhanddao = DaoFactory.getInstance().getMailHandDao(
					MailOutDetail.this);
			JSONObject	params = new JSONObject();
			params.put("sid", sid_time);
			params.put("out_code", getorgCode());
			params.put("in_code", in_code);
			params.put("org_type", in_type);
			params.put("hand_type", Global.HANDOUT);
			
			params.put("hand_state",Global.MAILCOM);
			params.put("begin_time", begin_time);
			params.put("end_time", end_time);
			
			params.put("create_by", getlogName());
			params.put("is_shift_stop", Global.UP);
			
			params.put("shift_time", "");
			params.put("certificate", Global.ELECTRON);
			
			if( baiduGps != null && baiduGps.getBdLocation() != null ){
				params.put("longitude", ""+baiduGps.getBdLocation().getLongitude());// 经度
				params.put("latitude", ""+baiduGps.getBdLocation().getLatitude());// 纬度
				params.put("province", ""+baiduGps.getBdLocation().getProvince());
				params.put("city", ""+baiduGps.getBdLocation().getCity());
				params.put("county", ""+baiduGps.getBdLocation().getDistrict());
				params.put("street", ""+baiduGps.getBdLocation().getStreet());
			}else{
				params.put("longitude", "");// 经度
				params.put("latitude", "");// 纬度
				params.put("province", "");
				params.put("city", "");
				params.put("county", "");
				params.put("street", "");
			}
			
			params.put("actualCount", temptotlecnt);
			
			isSave = mailhanddao.SaveMail(params);
			
			try {
				MailHandDetailDao mailHandDetailDao=DaoFactory.getInstance().getMailHandDetailDao(this.getApplicationContext());
				List<Map<String, Object>> list=	mailHandDetailDao.FindMail(sid_time, "",-1);
				WorkLogDao workLogDao=DaoFactory.getInstance().getWorkLogDao(this.getApplicationContext());
				for(int i=0;i<list.size();i++){
					workLogDao.SaveWorkLog(list.get(i).get("mail_num").toString(), Global.HANDOUT);
					}
				} catch (Exception e) {
				}
			
			isSave = true;
			
			if (params != null)
				params = null;
		} catch (Exception e) {

		}
		return isSave;
	}
}

package com.cn.net.cnpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.db.dao.MailHandDao;
import com.cn.net.cnpl.db.dao.MailHandDetailDao;
import com.cn.net.cnpl.tools.BaiduGps;
import com.cn.net.cnpl.tools.BaseActivity;
import com.cn.net.cnpl.tools.CodeDictionary;
import com.cn.net.cnpl.tools.MyCode;
import com.cn.net.cnpl.tools.MyDialog;
import com.google.zxing.client.android.CaptureActivity;

public class MailChangeOn extends BaseActivity{

	private Button codeButton = null;
	private Button scanbtn = null;
	private Button compbtn = null;
	private SimpleAdapter adapter = null;
	private ListView listview = null;
	private List<Map<String, String>> list = null;
	private List<String> strList = null;
	public ProgressDialog myDialog = null;
	
	private JSONObject params = null;
	private MailHandDao mailhanddao = null;
	private MailHandDetailDao mailhanddetaildao = null;
	
	private int pageNo = 1;
	String sid = "";
	String begin_time = "";
	String out_code="";
	String org_type="";
	String hand_type="";
	String shift_time = "";
	int tempI=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.change_on);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bg_new);

		baiduGps = BaiduGps.getInstance(this.getApplicationContext());
		baiduGps.getLocation();
		codeButton = (Button) findViewById(R.id.btncode);
		codeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyCode.Builder builder = new MyCode.Builder(
						MailChangeOn.this);
				builder.setTitle("二维码:");
				builder.setPositiveButton("", null);
				builder.create().show();
			}

		});
		scanbtn = (Button) findViewById(R.id.scanbtn);
		scanbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MailChangeOn.this, CaptureActivity.class);
				startActivityForResult(intent, 1);
			}

		});

		listview = (ListView) findViewById(R.id.comListView);
		if (list == null)
			list = new ArrayList<Map<String, String>>();
		
		if (strList == null)
			strList = new ArrayList<String>();
		

		adapter = new SimpleAdapter(MailChangeOn.this, list,
				R.layout.con_sucitem, new String[] { "num","conout_txt", "time_txt",
						"total_txt", "disrepair_txt", "lose_txt",
						"upload_txt", "no_upload_txt" }, new int[] {R.id.num, R.id.conout_txt,
						R.id.time_txt, R.id.total_txt, R.id.disrepair_txt,
						R.id.lose_txt, R.id.upload_txt, R.id.no_upload_txt });

		listview.setAdapter(adapter);
		
		listview.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				tempI = arg2;
				MyDialog.Builder builder = new MyDialog.Builder(MailChangeOn.this);
				builder.setTitle(getString(R.string.hint));
				builder.setMessage(getString(R.string.shifoudelete));
				builder.setPositiveButton("",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								strList.remove(tempI);
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
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try{
				String out_code = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("conout_txt");
				String time_txt = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("time_txt");
				String begin_time = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("begin_time");
				String total_txt = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("total_txt");
				String disrepair_txt = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("disrepair_txt");
				String lose_txt = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("lose_txt");
				String upload_txt = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("upload_txt");
				String no_upload_txt = ((Map<String, String>)listview.getItemAtPosition(arg2)).get("no_upload_txt");
				
				JSONObject json = new JSONObject();
				json = CodeDictionary.decodeCode2jsonObject(strList.get(arg2));
				
				Intent intent = new Intent();
				intent.putExtra("out_code", out_code);
				intent.putExtra("codetype", getString(R.string.mailcon_out));
				intent.putExtra("time_txt", time_txt);
				intent.putExtra("begin_time", begin_time);
				intent.putExtra("total_txt", total_txt);
				intent.putExtra("disrepair_txt", disrepair_txt);
				intent.putExtra("lose_txt", lose_txt);
				intent.putExtra("upload_txt", upload_txt);
				intent.putExtra("no_upload_txt", no_upload_txt);
				intent.putExtra("strList", json.get("body").toString());
				intent.setClass(MailChangeOn.this, MailChangeOnDt.class);
				startActivity(intent);
				}catch(Exception e){
					
				}

			}
		});
		
		compbtn = (Button) findViewById(R.id.compbtn);
		
	}
	@Override
	protected void onResume() {
		if(list==null||list.size()==0){
			compbtn.setVisibility(View.GONE);
		}else{
			compbtn.setVisibility(View.VISIBLE);
		compbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				MyDialog.Builder builder = new MyDialog.Builder(MailChangeOn.this);
				builder.setTitle(getString(R.string.hint));
				builder.setMessage(getString(R.string.shifousave));
				builder.setPositiveButton("",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								
								if(saveData(strList)){
									Toast.makeText(MailChangeOn.this, getString(R.string.save_comp), 1000).show();
									finish();
								}
							}
						});
				builder.setNegativeButton("", null);
				builder.create().show();
				
			}

		});
		}
		super.onResume();
	}
	//返回数据
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == 1) {
			// 返回数据
			boolean isscan=false;
			Bundle bundle = intent.getExtras();
			String strDnNumber = bundle.getString("txtResult");
			if (strDnNumber != null && strDnNumber.length() > 0)
			{
				for(int i=0;i<strList.size();i++)
				if(strDnNumber.equals(strList.get(i))){
					isscan=true;
				}
				if(isscan){
					Toast.makeText(MailChangeOn.this, getString(R.string.shifouscan), 1000).show();
				}else{
					loadData(strDnNumber);
					strList.add(strDnNumber);
				}
			}
		}
	}
	// 显示数据
	private void loadData(String strDnNumber) {
		try {
			mailhanddao = DaoFactory.getInstance().getMailHandDao(MailChangeOn.this);
			int upload_txt=0;
			int disrepaircnt=0;
			int losecnt=0;
			JSONObject json = new JSONObject();
			JSONObject jsonObject = new JSONObject();
			json = CodeDictionary.decodeCode2jsonObject(strDnNumber);
			jsonObject = json.getJSONObject("header");
			
			
			HashMap<String, String> tempHashMap = new HashMap<String, String>();
			
			tempHashMap.put("num", ""+pageNo);
			tempHashMap.put("conout_txt", jsonObject.get("code").toString());
			tempHashMap.put("time_txt", jsonObject.get("connectStartTime").toString()+"—"+jsonObject.get("connectEndTime").toString());
			
			JSONArray objArray = new JSONArray(json.get("body").toString());
			int tempsize = objArray.length();
			for (int i = 0; i < tempsize; i++) {
				if(Global.MAILDISREPAIR.equals(objArray.getJSONObject(i).get("isMangle").toString()))
					disrepaircnt++;
				else if(Global.MAILLOSE.equals(objArray.getJSONObject(i).get("isMangle").toString()))
					losecnt++;
				else if(!"".equals(objArray.getJSONObject(i).get("upload_time").toString()))
					upload_txt++;
			}
			
			tempHashMap.put("total_txt", ""+tempsize);
			tempHashMap.put("disrepair_txt", ""+disrepaircnt);
			tempHashMap.put("lose_txt", ""+losecnt);
			tempHashMap.put("upload_txt", ""+upload_txt);
			tempHashMap.put("no_upload_txt", ""+(tempsize - upload_txt));
			
			list.add(tempHashMap);
			adapter.notifyDataSetChanged();
			
			pageNo++;
		} catch (Exception e) {
		}
	}
	private 	BaiduGps baiduGps ;
	// 保存
	private boolean saveData(List<String> strList) {
		boolean issave =false;
		try {
			if(!"".equals(getlogName())){
			List<Map<String, Object>> rList = null;
			if (params == null)
				params = new JSONObject();
			mailhanddao = DaoFactory.getInstance().getMailHandDao(
					MailChangeOn.this);
			int tempsizeL = strList.size();
			
			for (int i = 0; i < tempsizeL; i++) {
				JSONObject json = new JSONObject();
				JSONObject jsonObject = new JSONObject();
				json = CodeDictionary.decodeCode2jsonObject(strList.get(i));
				jsonObject = json.getJSONObject("header");

				out_code = jsonObject.get("code").toString();
				org_type = jsonObject.get("type").toString();
				hand_type = jsonObject.get("connectType").toString();
				begin_time = jsonObject.get("connectStartTime").toString();
				end_time = jsonObject.get("connectEndTime").toString();

				rList = mailhanddao.FindShift(getlogName(), begin_time,
						end_time);
				int tempsize = rList.size();

				Date date = new Date();
				shift_time = ""
						+ DateFormat.format("yyyy-MM-dd kk:mm:ss", date);
				if (tempsize > 0) {
					sid = rList.get(0).get("sid").toString();
				} else {
					sid = date.getTime() + "";
					params.put("sid", sid);
					if(Global.HANDIN.equals(hand_type)){
						params.put("out_code", out_code);
						params.put("in_code", getorgCode());
					}else{
						params.put("out_code", getorgCode());
						params.put("in_code", out_code);
					}
					params.put("org_type", org_type);
					params.put("hand_type", hand_type);
					
					params.put("hand_state",Global.MAILCOM);
					params.put("begin_time", begin_time);
					params.put("end_time", end_time);
					
					params.put("create_by", getlogName());
					params.put("is_shift_stop", Global.UP);
					
					params.put("shift_time", shift_time);
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
					
					params.put("actualCount", "");
					
					mailhanddao.SaveMail(params);
				}
				
				
				issave = saveMail(json.get("body").toString());
				
				if (params != null)
					params = null;
			}
			}
		} catch (Exception e) {
		}
		return issave;
	}
	String abnormity_time="";
	String end_time="";
	String in_code="";
	
	// 保存
	private boolean saveMail(String strDnNumber) {
		boolean isSave = false;
		try {
			
			JSONArray objArray = new JSONArray(strDnNumber);
			if (params == null)
				params = new JSONObject();
			mailhanddetaildao = DaoFactory.getInstance().getMailHandDetailDao(MailChangeOn.this);
			int tempsize = objArray.length();
			String str="";
			for (int i = 0; i < tempsize; i++) {
				str = objArray.getJSONObject(i).get("mailNo").toString();
				if(mailhanddetaildao.ExitMail(str,Global.MAILIN)){
					Toast.makeText(MailChangeOn.this, str+getString(R.string.rep_in), 1000).show();
					continue;
				}else{
				params.put("mail_num",  objArray.getJSONObject(i).get("mailNo").toString());
				params.put("mail_type",  objArray.getJSONObject(i).get("isMangle").toString());
				params.put("principal",  objArray.getJSONObject(i).get("responsible").toString());
				params.put("principal_type", "");
				params.put("abnormity_time",  objArray.getJSONObject(i).get("abnormity_time").toString());
				params.put("create_time",  objArray.getJSONObject(i).get("create_time").toString());
				params.put("upload_time",  objArray.getJSONObject(i).get("upload_time").toString());
				
				params.put("sid", sid);
				if(Global.HANDIN.equals(hand_type))
					params.put("is_out", Global.MAILIN);
				else
					params.put("is_out", Global.MAILOUT);
				params.put("out_time", "");
				params.put("code2d_num", "");
				params.put("paper_num", "");
				params.put("operatorType", "I");
				params.put("oldSid", "");
				params.put("signatureImg", "");
				
				isSave = mailhanddetaildao.SaveMail(params);
				}
			}
			if (params != null)
				params = null;
		} catch (Exception e) {

		}
		return isSave;
	}
	
}

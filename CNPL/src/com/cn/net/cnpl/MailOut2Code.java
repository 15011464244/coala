package com.cn.net.cnpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class MailOut2Code extends BaseActivity {

	private Button codeButton = null;
	private String in_type ="";
	private String in_code ="";
	private String sid_time ="";
	private String begin_time ="";
	private String jason ="";
	private String suc_secform ="";
	private int Page =1;
	
	private MyImgAdapter adapter = null;
	private ListView listview = null;
	private List<Map<String, Object>> list = null;
	
	private MailHandDao mailhanddao = null;
	private MailHandDetailDao mailhanddetaildao = null;
	
	String end_time ="";
	private BaiduGps baiduGps;
	private Button okbtn = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.out_2code);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bg_new);
		
		BeginConnect.activities.add(MailOut2Code.this);
		baiduGps  = BaiduGps.getInstance(this.getApplicationContext());
		baiduGps.getLocation();
		Intent intent = getIntent();
		in_code = intent.getStringExtra("in_code");
		in_type = intent.getStringExtra("in_type");
		sid_time = intent.getStringExtra("sid_time");
		jason = intent.getStringExtra("begin_times");
		begin_time = intent.getStringExtra("begin_time");
		suc_secform = intent.getStringExtra("suc_secform");
		
		codeButton = (Button) findViewById(R.id.btncode);
		codeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyCode.Builder builder = new MyCode.Builder(
						MailOut2Code.this);
				builder.setTitle("二维码:");
				builder.setPositiveButton("", null);
				builder.create().show();
			}

		});
		okbtn = (Button)findViewById(R.id.okbtn);
		if("1".equals(suc_secform))
			okbtn.setVisibility(View.GONE);
		else{
			okbtn.setVisibility(View.VISIBLE);
		okbtn.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				MyDialog.Builder builder = new MyDialog.Builder(MailOut2Code.this);
				builder.setTitle(getString(R.string.hint));
				builder.setMessage(getString(R.string.ok_out));
				builder.setPositiveButton("",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								
								saveMail(jason);
								
								Intent intent = new Intent();
								intent.putExtra("in_code", in_code);
								intent.putExtra("sid_time", sid_time);
								intent.putExtra("begin_time", begin_time);
								intent.setClass(MailOut2Code.this, MailOutSuc.class);
								startActivity(intent);
								BeginConnect.allfinish();
								
								//邮件上传
								Intent mailserviceIntent = new Intent(MailOut2Code.this,
										PlUploadService.class);
								startService(mailserviceIntent);
							}
						});
				builder.setNegativeButton("", null);
				builder.create().show();
			}
			
		});
		}
		
		listview = (ListView) findViewById(R.id.comListView);
		if (list == null)
			list = new ArrayList<Map<String, Object>>();
		
		adapter = new MyImgAdapter(this);

		listview.setAdapter(adapter);
		
		loadData(jason);
	}
	
	private void loadData(String jason) {
		try {
			List<String> rList = null;
			JSONArray objArray = new JSONArray(jason);
			int tempsize=objArray.length();
			for(int j=0;j<tempsize;j++){
				objArray.getJSONObject(j).remove("sid");
				objArray.getJSONObject(j).remove("upload_time");
				objArray.getJSONObject(j).remove("create_time");
			}
			
			rList = CodeDictionary.createCode2Str(objArray);
			int tempSize = rList.size();
			String temp = "";
			
			for (int i = 0; i < tempSize; i++) {
				Map<String, Object> tempHashMap = new LinkedHashMap<String, Object>();
				Bitmap bmp = null;
				temp = rList.get(i);
				if (temp != null && !"".equals(temp)) {
					bmp = BaseCommand.CreateTwoDCode(temp);
				}

				if (bmp != null) {
					tempHashMap.put("num", Page);
					tempHashMap.put("img", bmp);

					list.add(tempHashMap);
				}
				Page++;
			}
		} catch (Exception e) {
		}
	}
	
	public final class ViewHolderImg {
		public TextView num;
		public ImageView img;
	}

	public class MyImgAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyImgAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
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
			ViewHolderImg holder = null;

			if (convertView == null) {

				holder = new ViewHolderImg();
				convertView = mInflater.inflate(R.layout.change_offimgitem,
						null);
				holder.num = (TextView) convertView
						.findViewById(R.id.num);
				holder.img = (ImageView) convertView
						.findViewById(R.id.image2code);
				convertView.setTag(holder);
			} else {

				holder = (ViewHolderImg) convertView.getTag();

			}
			holder.num.setText(list.get(position).get("num").toString());
			holder.img.setImageBitmap((Bitmap) list.get(position).get("img"));
			return convertView;

		}

	}
	
	private void saveMail(String jason) {
		try {
			if(!"".equals(getlogName())){
				
			mailhanddetaildao = DaoFactory.getInstance().getMailHandDetailDao(MailOut2Code.this);
			JSONArray objArray = new JSONArray(jason);
			int tempSize = objArray.length();
			for (int i = 0; i < tempSize; i++) {
				JSONObject	params = new JSONObject();
				if(mailhanddetaildao.ExitMail(objArray.getJSONObject(i).get("mailNo").toString(),Global.MAILOUT)){
					Toast.makeText(MailOut2Code.this, objArray.getJSONObject(i).get("mailNo").toString()+getString(R.string.is_out), 1000).show();
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
				params.put("signatureImg", "");
				
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
					MailOut2Code.this);
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
			params.put("actualCount", "");
			
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

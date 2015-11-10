package com.cn.net.cnpl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.db.dao.MailHandDao;
import com.cn.net.cnpl.db.dao.MailHandDetailDao;
import com.cn.net.cnpl.tools.BaiduGps;
import com.cn.net.cnpl.tools.BaseActivity;
import com.cn.net.cnpl.tools.BaseCommand;
import com.cn.net.cnpl.tools.MyCode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class MailOutForm extends BaseActivity {

	private Button codeButton = null;
	// 按钮
	private Button beforebtn = null;
	private Button conok = null;
	private Button conokpp = null;
	
	private ImageView codeImg;
	private String str2code = "";
	private String in_code="";
	private String begin_time;
	
	private JSONObject params = null;
	
	//private EnterDao enterdao = null;
	private MailHandDao mailhanddao = null;
	private MailHandDetailDao mailhanddetaildao = null;
	String begin_times;
	String in_type="";
	String sid_time="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.out_form);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bg_new);
		//获得数据
		Intent intent = getIntent();
		str2code = intent.getStringExtra("str2code");
		in_type = intent.getStringExtra("in_type");
		in_code = intent.getStringExtra("in_code");
		mailcnt = intent.getStringExtra("mailcnt");
		disrepair = intent.getStringExtra("disrepair");
		begin_times=intent.getStringExtra("begin_times");
		lose = intent.getStringExtra("lose");
		sid_time = intent.getStringExtra("sid_time");
		begin_time = intent.getStringExtra("begin_time");
		
		codeButton = (Button) findViewById(R.id.btncode);
		codeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyCode.Builder builder = new MyCode.Builder(
						MailOutForm.this);
				builder.setTitle("二维码:");
				builder.setPositiveButton("", null);
				builder.create().show();
			}

		});
		// **
		beforebtn = (Button) findViewById(R.id.beforebtn);
		beforebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("in_code", in_code);
				intent.putExtra("sid_time", sid_time);
				intent.putExtra("begin_time", begin_time);
				intent.putExtra("suc_secform", "2");
				intent.setClass(MailOutForm.this, MailOutDetail.class);
				startActivity(intent);
			}

		});
		codeImg = (ImageView)findViewById(R.id.image2code);
		
		conok = (Button) findViewById(R.id.conok);
		conok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				  Intent intent = new Intent();
				  intent.setClass(MailOutForm.this, MailOutSuc.class);
				  startActivity(intent);
				 */
				Bitmap bmp = null;
				try {
					if (str2code != null && !"".equals(str2code)) {
						bmp = BaseCommand.CreateTwoDCode(str2code);
					}
				} catch (WriterException e) {
					e.printStackTrace();
				}
				if (bmp != null) {
					codeImg.setImageBitmap(bmp);
				}

			}

		});
		conokpp = (Button) findViewById(R.id.conokpp);
		conokpp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
					saveMail(begin_times);
					
					Intent intent = new Intent();
					intent.putExtra("in_code", in_code);
					intent.putExtra("sid_time", sid_time);
					intent.putExtra("begin_time", begin_time);
					intent.setClass(MailOutForm.this, MailOutSuc.class);
					startActivity(intent);
			}

		});
	}

	String mailcnt="0";
	String disrepair="0";
	String lose="0";
	String upload="1";
	String unupload="0";
	String end_time ="";
	// 保存	
	private void saveMail(String jason) {
		try {
			if(!"".equals(getlogName())){
			if (params == null)
				params = new JSONObject();
			mailhanddetaildao = DaoFactory.getInstance().getMailHandDetailDao(MailOutForm.this);
			JSONArray objArray = new JSONArray(begin_times);
			int tempSize = objArray.length();
			String str="";
			for (int i = 0; i < tempSize; i++) {
				if(mailhanddetaildao.ExitMail(objArray.getJSONObject(i).get("mail_num").toString(),Global.MAILOUT)){
					Toast.makeText(MailOutForm.this, objArray.getJSONObject(i).get("mail_num").toString()+getString(R.string.is_out), 1000).show();
					continue;
				}else{
					end_time = DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date()).toString();
				params.put("mail_num", objArray.getJSONObject(i).get("mail_num"));//		
				if(getString(R.string.disrepair).equals(objArray.getJSONObject(i).get("mail_type")))
					str=Global.MAILDISREPAIR;
				else if(getString(R.string.lose).equals(objArray.getJSONObject(i).get("mail_type")))
					str=Global.MAILLOSE;
				else if(getString(R.string.comp).equals(objArray.getJSONObject(i).get("mail_type")))
					str=Global.MAILINTACT;
				params.put("mail_type",str);//
				params.put("principal", objArray.getJSONObject(i).get("principal"));//
				params.put("principal_type", objArray.getJSONObject(i).get("principal_type"));//
				params.put("abnormity_time", objArray.getJSONObject(i).get("abnormity_time"));//
				params.put("create_time", objArray.getJSONObject(i).get("create_time"));//
				params.put("upload_time", "");//
				
				params.put("sid", sid_time);//
				params.put("is_out", Global.MAILOUT);//
				params.put("out_time", end_time);//
				
				params.put("code2d_num", "");//
				params.put("paper_num", "");//		
				params.put("operatorType", "I");
				params.put("oldSid", "");
				params.put("signatureImg", "");
				mailhanddetaildao.SaveMail(params);
				mailhanddetaildao.updateMail(objArray.getJSONObject(i).get("mail_num").toString(),objArray.getJSONObject(i).get("sid").toString(), Global.MAILOUT,end_time,"");
				
				}
			}
			saveMail_S();
			if (params != null)
				params = null;
			}
		} catch (Exception e) {

		}
	}
	private boolean saveMail_S() {
		boolean isSave = false;
		try {
			if (params == null)
				params = new JSONObject();
			BaiduGps baiduGps = BaiduGps.getInstance(this);
			baiduGps.getLocation();
			
			mailhanddao = DaoFactory.getInstance().getMailHandDao(
					MailOutForm.this);
			params.put("sid", sid_time);//
			params.put("out_code", getorgCode());//
			params.put("in_code", in_code);//
			params.put("org_type", in_type);//
			params.put("hand_type", Global.HANDOUT);//
			
			params.put("hand_state",Global.MAILCOM);//
			params.put("begin_time", begin_time);//
			params.put("end_time", end_time);//
			
			params.put("create_by", getlogName());//
			params.put("is_shift_stop", Global.UP);//
			
			params.put("shift_time", "");//
			params.put("certificate", Global.ELECTRON);//
			
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
			
			isSave = true;
			
			if (params != null)
				params = null;
		} catch (Exception e) {

		}
		return isSave;
	}
}

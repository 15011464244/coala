package com.cn.net.cnpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.db.dao.DlvStateDao;
import com.cn.net.cnpl.db.dao.FollowActionDao;
import com.cn.net.cnpl.db.dao.MailDao;
import com.cn.net.cnpl.db.dao.ProjReasonDao;
import com.cn.net.cnpl.tools.BaiduGps;
import com.cn.net.cnpl.tools.BaseActivity;
import com.cn.net.cnpl.tools.MyCode;
import com.cn.net.cnpl.tools.MyDialog;

public class Mail_DlvDetailN extends BaseActivity {
	private TextView title;
	private Button back;
	private Button codeButton = null;
	// 邮件号
	private TextView mail_no = null;

	// 邮件类型
	private TextView mail_type = null;

	// 原因
	private TextView reson = null;

	// 项目
	private TextView project = null;
	//备注
	private TextView send_remark = null;
	
	private TextView send_followaction =null;

	// 删除
	private Button delbutton = null;
	private JSONObject params = null;

	private Map<String, Object> map = null;
	private MailDao maildao = null;
//	private DlvStateDao dlvdao = null;
	private FollowActionDao fllowdao = null;
//	private List<Map<String, String>> list = null;
	private List<Map<String, String>> followlist = null;

	String mailno = "";
	String create_time = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.mail_dlvdtn_new);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bg_new);
		initTitle();
		// 邮件号
		mail_no = (TextView) findViewById(R.id.send_name_mailn);
		Intent intent = getIntent();
		mailno = intent.getStringExtra("mailno");
		is_upload = intent.getStringExtra("isupload");
		create_time = intent.getStringExtra("create_time");
		mail_no.setText(mailno);
		
		try{
			SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
			create_time = ""+DateFormat.format("yyyyMMddkkmmss", f.parse(create_time));
		}catch(Exception e){
			
		}
		/*
		codeButton = (Button) findViewById(R.id.btncode);
		codeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyCode.Builder builder = new MyCode.Builder(
						Mail_DlvDetailN.this);
				builder.setTitle("二维码:");
				builder.setPositiveButton("", null);
				builder.create().show();
			}

		});
		*/
		showData(mailno, is_upload, create_time);

		// 删除按钮
		/*
		delbutton = (Button) findViewById(R.id.delmailn);

		delbutton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyDialog.Builder builder = new MyDialog.Builder(Mail_DlvDetailN.this);
				builder.setTitle(getString(R.string.hint));
				builder.setMessage(getString(R.string.shifoudelete));
				builder.setPositiveButton("", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (is_upload.equals(Global.UPLOAD)) {
							// 回到MailDlvyActivity
							if("".equals(maildao.Findcount(getlogName(), Global.UNDLVCODE, mailno)))
							{
								Toast.makeText(Mail_DlvDetailN.this,getString(R.string.isdelete), 1000)
								.show();
							}else{
							if (deleteEnter()){
								Intent serviceIntent = new Intent(Mail_DlvDetailN.this.getApplicationContext(), DlvUploadService.class); 
								startService(serviceIntent); 
								Intent intent = new Intent();
								setResult(2, intent);
								finish();
							}else
								Toast.makeText(Mail_DlvDetailN.this,getString(R.string.delete_fail), 1000)
										.show();
							}
						} else if (is_upload.equals(Global.UNUPLOAD)) {
							maildao = DaoFactory.getInstance().getMailDao(
									Mail_DlvDetailN.this);
							maildao.deleteMail(mailno, getlogName(), Global.UNDLVCODE);// 删除信息
							Intent serviceIntent = new Intent(Mail_DlvDetailN.this.getApplicationContext(), DlvUploadService.class); 
							startService(serviceIntent); 
							Intent intent = new Intent();
							setResult(2, intent);
							finish();
						}
					}
				} );
				builder.setNegativeButton("",null);
				builder.create().show();
			}
		});
		*/

	}

	private void initTitle() {
		// TODO Auto-generated method stub
		title = (TextView) findViewById(R.id.new_name);
		title.setText("未妥投");
		back = (Button) findViewById(R.id.top_left);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Mail_DlvDetailN.this.finish();
			}
		});
	}

	// 显示上传，未上传，保存信息
	private void showData(String mailno, String upload, String createData) {
		try {
			maildao = DaoFactory.getInstance().getMailDao(Mail_DlvDetailN.this);
			map = maildao.FindMail(mailno, getlogName(),
					Global.UNDLVCODE, upload, createData);
			if(map != null && map.size()>0){
			// 邮件类型
			mail_type = (TextView) findViewById(R.id.mailtypeidn);
			String type_str = "";
			interFlag =map.get("interFlag").toString();
			if (interFlag.equals(Global.INTERNATIONAL))
				type_str = getString(R.string.international);
			else
				type_str = getString(R.string.internal);
			mail_type.setText(type_str);
//			dlvdao = DaoFactory.getInstance().getDlvStateDao(
//					Mail_DlvDetailN.this);
//			list = dlvdao.FindDlvStateBystateType(Global.UNDLVCODE);

			ProjReasonDao projdao = DaoFactory.getInstance().getProjReasonDao(
					Mail_DlvDetailN.this);
			
			// 原因
			reson = (TextView) findViewById(R.id.send_reson_mail);
			undlvCauseCode = map.get("undlvCauseCode").toString();
			
			String restr = map.get("undlvCauseDesc").toString();//projdao.findProReasons(undlvCauseCode,false);
			reson.setText(restr);
			
//			for (Map<String, String> maplist : list) {
//				if (undlvCauseCode.equals(maplist.get("stateCode"))) {
//					if (getResources().getConfiguration().locale.getCountry()
//							.equals("CN"))
//						reson.setText(maplist.get("stateDescCHS"));
//					else if (getResources().getConfiguration().locale
//							.getCountry().equals("TW"))
//						reson.setText(maplist.get("stateDescTRADITIONAL"));
//					else if (getResources().getConfiguration().locale
//							.getCountry().equals("UK")
//							|| getResources().getConfiguration().locale
//									.getCountry().equals("US"))
//						reson.setText(maplist.get("stateDescENG"));
//				}
//			}

			dlvOrgName = map.get("dlvOrgName").toString();
			dlvOrgPostCode= map.get("dlvOrgPostCode").toString();
			dlvAddress = map.get("dlvAddress").toString();
			operationTime=map.get("operationTime").toString();
			
//			fllowdao = DaoFactory.getInstance().getFollowActionDao(
//					Mail_DlvDetailN.this);
//			followlist = fllowdao.FindFollowAction();
			ProjReasonDao projdao1=DaoFactory.getInstance().getProjReasonDao(Mail_DlvDetailN.this);
			undlvNextModeCode = map.get("undlvNextModeCode").toString();
			String repro=projdao1.findProReasons(undlvNextModeCode,true);
			Log.i("tgxx",repro+"xiangmu" );
			project = (TextView) findViewById(R.id.send_action_mail);
			project.setText(repro);
			
			String nextStr="";
			for(int h=0;h<6;h++){
				if(nextCd[h].equals(map.get("undlvfollowCode").toString())){
					nextStr = nextDesc[h];
					break;
				}
			}
			send_followaction = (TextView) findViewById(R.id.send_followaction);
			send_followaction.setText(nextStr);
			
//			// 下一步动作
//			followaction = (TextView) findViewById(R.id.send_action_mail);
//			undlvNextModeCode = map.get("undlvNextModeCode").toString();
//			for (Map<String, String> maplist : followlist) {
//				if (undlvNextModeCode.equals(maplist.get("followAction"))) {
//					if (getResources().getConfiguration().locale.getCountry()
//							.equals("CN"))
//						followaction.setText(maplist.get("actionDescCHS"));
//					else if (getResources().getConfiguration().locale
//							.getCountry().equals("TW"))
//						followaction.setText(maplist
//								.get("actionDescTRADITIONAL"));
//					else if (getResources().getConfiguration().locale
//							.getCountry().equals("UK")
//							|| getResources().getConfiguration().locale
//									.getCountry().equals("US"))
//						followaction.setText(maplist.get("actionDescENG"));
//				}
//			}
			undlvfollowCode = map.get("undlvfollowCode").toString();
			send_remark = (TextView) findViewById(R.id.send_remark);
			send_remark.setText(map.get("remark").toString());
			
		}else{
			Intent intent = new Intent();
			setResult(2, intent);
			finish();
			Toast.makeText(this, getString(R.string.uploaded), 1000).show();
		}
		} catch (Exception e) {
		}
	}
	
	String[] nextCd={"","1","2","3","4","5","8"};
	String[] nextDesc={"","重新对客户进行预约","安排再次配送","转为自提","退回收寄局",
			"退无着","报险及理赔"};
	// 保存值
	private String is_upload = "", role = "", sname = "", interFlag = "",
			dlvOrgName = "", dlvOrgPostCode = "", signStsCode = "",operationTime ="",
			undlvCauseCode = "", undlvNextModeCode = "", dlvAddress = "",undlvfollowCode="",undlvCauseDesc="";
	private double actualGoodsFee = 0.0, actualFee = 0.0, actualTax = 0.0, otherFee = 0.0;
	private String signatureImg = "";

	// 保存数据
	private boolean deleteEnter() {
		// is_upload, role, operationMode, dlvStsCode, undlvCauseCode,
		// undlvNextModeCode,dlvAddress, signatureImg
		boolean isSave = false;
		try {
			if (params == null)
				params = new JSONObject();
			BaiduGps baiduGps = BaiduGps.getInstance(this);
			baiduGps.getLocation();
			
			maildao = DaoFactory.getInstance().getMailDao(Mail_DlvDetailN.this);
			
			TelephonyManager telephonemanage = (TelephonyManager) getWindow()
	                .getContext().getSystemService(Context.TELEPHONY_SERVICE);

			params.put("IS_UPLOAD", "0");

			params.put("deviceNumber", telephonemanage.getDeviceId());// Tm.getDeviceId());//手机设备号
			params.put("orgCode", getorgCode());// 机构号
			params.put("userCode", getlogName());// 工号

			params.put("role", role);
			params.put("operationMode", "D");// 保存
			params.put("mailCode", mailno);// 邮件号

			params.put("dlvOrgCode",getorgCode());// 机构号
			params.put("dlvOrgName", dlvOrgName);// 机构名称
			params.put("dlvOrgPostCode", dlvOrgPostCode); // 投递机构邮编

			params.put("dlvStsCode", "H");
			params.put("signStsCode", signStsCode);// 签收情况代码

			params.put("actualGoodsFee", actualGoodsFee);// 实收货款
			params.put("actualTax", actualTax);
			params.put("actualFee", actualFee);// 实收资费
			params.put("otherFee", otherFee);

			params.put("dlvUserCode", getlogName());// 工号
			params.put("dlvUserName", Global.NAME);// 投递员姓名
			params.put("undlvCauseCode", undlvCauseCode);
			params.put("undlvCauseDesc", undlvCauseDesc);
			params.put("undlvNextModeCode", undlvNextModeCode);
			params.put("undlvfollowCode", undlvfollowCode);// 下一步动作
			params.put("signerName", sname);// 签收人姓名
			params.put("interFlag", interFlag);// 国际标识 国内国际是1，0
			params.put("createDate", DateFormat.format("yyyyMMddkkmmss", new Date()));
			params.put("operationTime", operationTime);// 操作时间
			params.put("dlvAddress", dlvAddress);
			params.put("signatureImg", signatureImg);
			
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

			isSave = maildao.SaveMail(params);
		} catch (Exception e) {

		}
		return isSave;
	}
}

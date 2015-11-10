package com.cn.net.cnpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.db.dao.DlvStateDao;
import com.cn.net.cnpl.db.dao.MailDao;
import com.cn.net.cnpl.tools.BASE64Decoder;
import com.cn.net.cnpl.tools.BaiduGps;
import com.cn.net.cnpl.tools.BaseActivity;
import com.cn.net.cnpl.tools.MyCode;
import com.cn.net.cnpl.tools.MyDialog;

public class Mail_DlvDetail extends BaseActivity {
	private TextView title;
	private Button back;
	private Button codeButton = null;
	// 邮件号
	private TextView mail_no = null;

	// 邮件类型
	private TextView mail_type = null;

	// 签收类型
	private TextView retype = null;

	// 签收人
	private TextView name = null;

	// 收款类型
	private TextView feetype = null;

	// 收款金额
	private TextView fee = null;

	// 图片
	private ImageView img = null;

	// 删除
	private Button delbutton = null;
	private JSONObject params = null;

	private Map<String, Object> map = null;
	private MailDao maildao = null;
	DlvStateDao dlvdao = null;
	private List<Map<String, String>> list = null;
	String mailno = "";
	String create_time = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.mail_dlvdt_new);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bg_new);
		initTitle();

		// 邮件号
		mail_no = (TextView) findViewById(R.id.send_name_mail);
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

		showData(mailno, is_upload, create_time);
		/*
		codeButton = (Button) findViewById(R.id.btncode);
		codeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyCode.Builder builder = new MyCode.Builder(
						Mail_DlvDetail.this);
				builder.setTitle("二维码:");
				builder.setPositiveButton("", null);
				builder.create().show();
			}

		});
		*/
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
				Mail_DlvDetail.this.finish();
			}
		});
	}
private void setlistener(){
	   delbutton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyDialog.Builder builder = new MyDialog.Builder(Mail_DlvDetail.this);
				builder.setTitle(getString(R.string.hint));
				builder.setMessage(getString(R.string.shifoudelete));
				builder.setPositiveButton("", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

						if (is_upload.equals(Global.UPLOAD)) {
							// 回到MailDlvyActivity
							if("".equals(maildao.Findcount(getlogName(), Global.DLVCODE, mailno)))
							{
								Toast.makeText(Mail_DlvDetail.this,getString(R.string.isdelete), 1000)
								.show();
							}else{
							if (deleteEnter()){
								Intent serviceIntent = new Intent(Mail_DlvDetail.this.getApplicationContext(), DlvUploadService.class); 
								startService(serviceIntent); 
								Intent intent = new Intent();
								setResult(2, intent);
								finish();
							}else
								Toast.makeText(Mail_DlvDetail.this,getString(R.string.delete_fail) , 1000)
										.show();
							}
						} else if (is_upload.equals(Global.UNUPLOAD)) {
							maildao = DaoFactory.getInstance().getMailDao( Mail_DlvDetail.this);
							maildao.deleteMail(mailno, getlogName(), Global.DLVCODE);// 删除信息
							Intent serviceIntent = new Intent(Mail_DlvDetail.this.getApplicationContext(), DlvUploadService.class); 
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
   }
	// 显示上传，未上传，保存信息
	private void showData(String mailno, String upload, String createData) {
		try {
			maildao = DaoFactory.getInstance().getMailDao(Mail_DlvDetail.this);
			map = maildao.FindMail(mailno, getlogName(),
					Global.DLVCODE, upload, createData);
			if(map != null && map.size()>0){
			// 邮件类型
			mail_type = (TextView) findViewById(R.id.mailtypeid);
			String type_str = "";
			interFlag = map.get("interFlag").toString();
			if (interFlag.equals(Global.INTERNATIONAL))
				type_str = getString(R.string.international); 
			else
				type_str = getString(R.string.internal);
//			mail_type.setText(type_str);
			dlvdao = DaoFactory.getInstance().getDlvStateDao(
					Mail_DlvDetail.this);
			list = dlvdao.FindDlvStateBystateType(Global.DLVCODE);
			// 签收类型
			retype = (TextView) findViewById(R.id.dtretype);
			String stscod_str = (String) map.get("signStsCode");
			for (Map<String, String> maplist : list) {
				if (stscod_str.equals(maplist.get("stateCode"))) {
					if (getResources().getConfiguration().locale.getCountry()
							.equals("CN"))
						retype.setText(maplist.get("stateDescCHS"));
					else if (getResources().getConfiguration().locale
							.getCountry().equals("TW"))
						retype.setText(maplist.get("stateDescTRADITIONAL"));
					else if (getResources().getConfiguration().locale
							.getCountry().equals("UK")
							|| getResources().getConfiguration().locale
									.getCountry().equals("US"))
						retype.setText(maplist.get("stateDescENG"));
				}
			}

			// 签收人
			name = (TextView) findViewById(R.id.dtname);
			if (map.get("signerName") != null && map.get("signerName") != ""){
				sname = map.get("signerName").toString();
				name.setText(sname);
			}
			dlvOrgName = map.get("dlvOrgName").toString();
			dlvOrgPostCode= map.get("dlvOrgPostCode").toString();
			signStsCode = map.get("signStsCode").toString();
			dlvAddress = map.get("dlvAddress").toString();
			operationTime=map.get("operationTime").toString();
			// 收款类型
			feetype = (TextView) findViewById(R.id.dtfeetype);

			// 删除按钮
			delbutton = (Button) findViewById(R.id.delmail);

			// 收款金额
			fee = (TextView) findViewById(R.id.dtfee);
			actualGoodsFee = (Double)map.get("actualGoodsFee");
			actualFee = (Double)map.get("actualFee");
			if ( actualGoodsFee != 0.0) {
				feetype.setText(getString(R.string.substitute_fee));
				//Log.e("3333", "" + actualGoodsFee);
				fee.setText( ""+actualGoodsFee );
//				delbutton.setVisibility(View.GONE);
			} else if (actualFee != 0.0) {
				feetype.setText(getString(R.string.receive_fee));
				fee.setText( ""+actualFee );
//				delbutton.setVisibility(View.GONE);
			}
//			else{
//				delbutton.setVisibility(View.VISIBLE);
//				setlistener();
//			}

			// 图片
			img = (ImageView) findViewById(R.id.dtimageView);
			
			BASE64Decoder base64decoder = new BASE64Decoder();
			byte[] imgs = base64decoder.decodeBuffer(map.get("signatureImg").toString());
			Bitmap img1=BitmapFactory.decodeByteArray(imgs, 0, imgs.length);
			img.setImageBitmap(img1);
			
			signatureImg = map.get("signatureImg").toString();
			
			}else{
				Intent intent = new Intent();
				setResult(2, intent);
				finish();
				Toast.makeText(this, getString(R.string.uploaded), 1000).show();
			}
		} catch (Exception e) {
		}
	}

	// 保存值
	private String is_upload = "", role = "", sname = "", interFlag = "",
			dlvOrgName = "", dlvOrgPostCode = "", signStsCode = "",operationTime ="",
			undlvCauseCode = "", undlvNextModeCode = "", dlvAddress = "";
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
			
			maildao = DaoFactory.getInstance().getMailDao(Mail_DlvDetail.this);
			
			TelephonyManager telephonemanage = (TelephonyManager) getWindow()
	                .getContext().getSystemService(Context.TELEPHONY_SERVICE);

			params.put("IS_UPLOAD", "0");

			params.put("deviceNumber", telephonemanage.getDeviceId());// Tm.getDeviceId());//手机设备号
			params.put("orgCode", getorgCode());// 机构号
			params.put("userCode", getlogName());// 工号

			params.put("role", role);
			params.put("operationMode", "D");// 保存
			params.put("mailCode", mailno);// 邮件号

			params.put("dlvOrgCode", getorgCode());// 机构号
			params.put("dlvOrgName", dlvOrgName);// 机构名称
			params.put("dlvOrgPostCode", dlvOrgPostCode); // 投递机构邮编

			params.put("dlvStsCode", "I");
			params.put("signStsCode", signStsCode);// 签收情况代码

			params.put("actualGoodsFee", actualGoodsFee);// 实收货款
			params.put("actualTax", actualTax);
			params.put("actualFee", actualFee);// 实收资费
			params.put("otherFee", otherFee);

			params.put("dlvUserCode", getlogName());// 工号
			params.put("dlvUserName", Global.NAME);// 投递员姓名
			params.put("undlvCauseCode", undlvCauseCode);
			params.put("undlvNextModeCode", undlvNextModeCode);
			params.put("undlvfollowCode", "");// 下一步动作
			params.put("undlvCauseDesc", "");// 原因描述
			params.put("signerName", sname);// 签收人姓名
			params.put("interFlag", interFlag);// 国际标识 国内国际是1，0
			params.put("createDate",DateFormat.format("yyyyMMddkkmmss", new Date()));
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
			Log.i("tgxx", e.toString());
		}
		return isSave;
	}
}

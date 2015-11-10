package com.cn.net.cnpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.db.dao.DlvStateDao;
import com.cn.net.cnpl.db.dao.MailDao;
import com.cn.net.cnpl.db.dao.MailHandDetailDao;
import com.cn.net.cnpl.db.dao.ResOrgDao;
import com.cn.net.cnpl.tools.BaiduGps;
import com.cn.net.cnpl.tools.BaseActivity;
import com.cn.net.cnpl.tools.MyCode;
import com.cn.net.cnpl.tools.MyDialog;
import com.cn.net.cnpl.tools.MyListView;
import com.cn.net.cnpl.tools.MySpinnerAdapter;
import com.google.zxing.client.android.CaptureActivity;

public class MailBatchDlvyEnter extends BaseActivity {
	private TextView title;
	private Button back;
	private Button codeButton = null;
	// ǩ������
	private Spinner spinner;
	private List<Map<String, String>> typeList = null;
	private ArrayList<String> arraylist = null;

	// ɨ��������ȡ�ʼ���ֵ
	private EditText mailEdit;

	// �ʼ�����
	private RadioGroup rdiaogroup = null;
	private RadioButton mailTypeButton1 = null;
	private RadioButton mailTypeButton2 = null;
	// ���ù��ʱ�ʶ ���ڹ�����1��0
	String interFlag = "1";

	// ǩ������
	private DlvStateDao dlvstatedao = null;
	// ǩ����
	private EditText revNameedit = null;
	// �տ�����
	// private Spinner spinnergathering;
	private CheckBox substitute_fee;
	private CheckBox receive_fee;
	// ArrayList<String> gathering = null;

	// �տ���
	String spinerfee = "";
	private EditText feeedit = null;
	double fee = 0.0;
	private double actualGoodsFee = 0.0;// ʵ�ջ���
	private double actualFee = 0.0;// ʵ���ʷ�
	private double actualTax = 0.0;// ʵ��˰��
	private double otherFee = 0.0;// ��������
	// ���水ť
	private TextView saveButton = null;
	MailDao maildao = null;
	List<Map<String, String>> maildaoList = null;
	// ��ֵ
	private String signStsCode = "";
	private String dlvOrgPostCode = "";
	private String dlvOrgName = "";

	// ��ȡ��ַ
	private BaiduGps baidugps = null;

	// ɨ����ʼ���
	private MyListView mailList = null;
	private List<Map<String, Object>> maillistdata = new ArrayList<Map<String, Object>>();
	private List<String> maillistStr = new ArrayList<String>();
	private SimpleAdapter adapter = null;
	private String strDnNumber = "";
	private Button addButton = null;
	private int num;

	// ����ֵ
	private String is_upload = "0", role = "1", undlvCauseCode = "",
			undlvNextModeCode = "", dlvAddress = "";
	private String signatureImg = "";
	private File fullFilename;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.mail_batch_dlventer_new);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bg_new);
		initTitle();
		File dir = new File(Environment.getExternalStorageDirectory()+ "/CNPL");
    	if(!dir.exists()){
    	    dir.mkdir();
    	}
    	fullFilename =new File(Environment.getExternalStorageDirectory()+ "/CNPL/"+Global.MAILDLV);
    	if (!fullFilename.exists()){
	    try {
		fullFilename.createNewFile();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } 
	    }
    	
		// ��ַ
			baidugps =BaiduGps.getInstance(this.getApplicationContext());
			baidugps.getLocation();
		// **�ʼ���UI
		// �������ɨ���ʼ���
		mailEdit = (EditText) findViewById(R.id.dlvmailidserch);
		mailEdit.setHint(getString(R.string.scan_hint).toString());
		/*
		codeButton = (Button) findViewById(R.id.btncode);
		codeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyCode.Builder builder = new MyCode.Builder(
						MailBatchDlvyEnter.this);
				builder.setTitle("��ά��:");
				builder.setPositiveButton("", null);
				builder.create().show();
			}

		});
		*/
		// ɨ�谴ť
		ImageView camera = (ImageView) findViewById(R.id.dlvcamera);
		camera.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MailBatchDlvyEnter.this,
						CaptureActivity.class);
				startActivityForResult(intent, 1);
			}
		});

		// ** ǩ������
		spinner = (Spinner) findViewById(R.id.arrType);

		dlvstatedao = DaoFactory.getInstance().getDlvStateDao(
				MailBatchDlvyEnter.this);
		typeList = dlvstatedao.FindDlvStateBystateType(Global.DLVCODE);
		for (Map<String, String> map : typeList) {
			String xk = map.get("stateDescCHS");
			if (getResources().getConfiguration().locale.getCountry().equals(
					"CN")) {
				xk = map.get("stateDescCHS");
			} else if (getResources().getConfiguration().locale.getCountry()
					.equals("TW")) {
				xk = map.get("stateDescTRADITIONAL");
			} else if (getResources().getConfiguration().locale.getCountry()
					.equals("UK")
					|| getResources().getConfiguration().locale.getCountry()
							.equals("US")) {
				xk = map.get("stateDescENG");
			}
			if (arraylist == null)
				arraylist = new ArrayList<String>();
			arraylist.add(xk);
		}

		MySpinnerAdapter<String> typeadapter = new MySpinnerAdapter<String>(
				this, R.layout.my_spinner, R.id.name_text, arraylist);
		typeadapter.setDropDownViewResource(R.layout.my_spinner_drpdown_item);
		spinner.setAdapter(typeadapter);
		spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				signStsCode =typeList.get(arg2).get("stateCode");
//				String spineritem = (String) spinner.getSelectedItem();
//				String str = "";
//				for (Map<String, String> map : typeList) {
//					if (getResources().getConfiguration().locale.getCountry()
//							.equals("CN")) {
//						str = map.get("stateDescCHS");
//					} else if (getResources().getConfiguration().locale
//							.getCountry().equals("TW")) {
//						str = map.get("stateDescTRADITIONAL");
//					} else if (getResources().getConfiguration().locale
//							.getCountry().equals("UK")
//							|| getResources().getConfiguration().locale
//									.getCountry().equals("US")) {
//						str = map.get("stateDescENG");
//					}
//					if (spineritem.equals(str)) {
//						signStsCode = map.get("stateCode");
//						break;
//					}
//				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		// **�ʼ�����
		setMailType();

		// ** ǩ����
		revNameedit = (EditText) findViewById(R.id.dlventername);
		revNameedit.setHint(getString(R.string.revname_hint).toString());

		// **�տ�����
		setFeeType();

		// **���水ť

		// ��ȡ��ֵ
		ResOrgDao resOrgDao = DaoFactory.getInstance().getResOrgDao(this.getApplicationContext());
		maildaoList = resOrgDao.FindResOrgDaoBystateType();
		for (Map<String, String> map : maildaoList) {
			dlvOrgPostCode = map.get("postcode");
			dlvOrgName = map.get("org_sname");
		}

		saveButton = (TextView) findViewById(R.id.top_save);
		saveButton.setVisibility(View.VISIBLE);
		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveData();
			}

		});

		// �ʼ��б�
		mailList = (MyListView) findViewById(R.id.mailList);
		adapter = new SimpleAdapter(this, maillistdata,
				R.layout.mail_dlventeritem, new String[] { "mail_no" },
				new int[] { R.id.dlvmailiditem });
		mailList.setAdapter(adapter);
		mailList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				num = arg2;
				TextView tv = (TextView) 	arg1.findViewById(R.id.dlvmailiditem);
				MyDialog.Builder builder = new MyDialog.Builder(
						MailBatchDlvyEnter.this);
				builder.setTitle(getString(R.string.hint));
				builder.setMessage(tv.getText() + "  "
						+ getString(R.string.shifoudelete));
				builder.setPositiveButton("",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								messageListener.sendEmptyMessage(2);
							}
						});
				builder.setNegativeButton("", null);
				builder.create().show();
				return false;
			}
		});

		// ����ʼ���
		addButton = (Button) findViewById(R.id.ndllvadd);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				strDnNumber = mailEdit.getText().toString();
				if (strDnNumber != null && !"".equals(strDnNumber)) {
					addMailListView(strDnNumber);
				} else {
					Toast.makeText(MailBatchDlvyEnter.this,
							getString(R.string.scan_hint), 1000).show();
				}
			}

		});

	}

	private void initTitle() {
		// TODO Auto-generated method stub
		title = (TextView) findViewById(R.id.new_name);
		title.setText("����¼��");
		back = (Button) findViewById(R.id.top_left);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MailBatchDlvyEnter.this.finish();
			}
		});
	}

	private Handler messageListener = new Handler() {
		public void handleMessage(Message msg) {
			LayoutParams layoutParams = mailList.getLayoutParams();
			switch (msg.what) {
			case 1:
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("mail_no", strDnNumber);
				maillistdata.add(map);
				maillistStr.add(strDnNumber);
	
				if(maillistStr.size()>10){
					layoutParams.height =10*80 ;
				}else{
					layoutParams.height =maillistStr.size()*80 ;
				}
				mailList.setLayoutParams(layoutParams);
				adapter.notifyDataSetChanged();
				break;
			case 2:
				maillistdata.remove(num);
				maillistStr.remove(num);
				if(maillistStr.size()>10){
					layoutParams.height =10*80 ;
				}else{
					layoutParams.height =maillistStr.size()*80 ;
				}
				mailList.setLayoutParams(layoutParams);
				adapter.notifyDataSetChanged();
				break;
			}
		}
	};

	private void addMailListView(String strDnNumber) {
		mailhanddetailDao = DaoFactory.getInstance().getMailHandDetailDao(this);
		List<Map<String, String>> mailNoList = mailhanddetailDao.FindMailIn();
//		boolean isjieru = false;

//		if (mailNoList != null) {
//			for (int i = 0; i < mailNoList.size(); i++) {
//				if (strDnNumber.equals(mailNoList.get(i).get("mail_num").toString())) {
//					isjieru = true;
//				}
//			}
//		}
//		if (!isjieru) {
//			Toast.makeText(MailBatchDlvyEnter.this, getString(R.string.mailnojieru),
//					1000).show();
//		} else
			if (maillistStr.contains(strDnNumber)) {// �ж��ǲ����ظ�
			Toast.makeText(MailBatchDlvyEnter.this,
					getString(R.string.mail_no_repeat), 1000).show();
		} else {
			maildao = DaoFactory.getInstance().getMailDao(
					MailBatchDlvyEnter.this);
			String data = maildao.Findcount(getlogName(), Global.DLVCODE,
					strDnNumber);
			if (data != null && !"".equals(data)) {
				try {
					SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmSS");
					String dataStr = ""
							+ DateFormat.format("yyyy-MM-dd", f.parse(data));
					String message = getString(R.string.issave1) + dataStr
							+ getString(R.string.issave2);
					MyDialog.Builder builder = new MyDialog.Builder(
							MailBatchDlvyEnter.this);
					builder.setTitle(getString(R.string.hint));
					builder.setMessage(message);
					builder.setPositiveButton("",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									messageListener.sendEmptyMessage(1);
								}
							});
					builder.setNegativeButton("", null);
					builder.create().show();
				} catch (Exception e) {

				}
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("mail_no", strDnNumber);
				maillistdata.add(map);
				maillistStr.add(strDnNumber);
				LayoutParams layoutParams = mailList.getLayoutParams();
				if(maillistStr.size()>10){
					layoutParams.height =10*80 ;
				}else{
					layoutParams.height =maillistStr.size()*80 ;
				}
				mailList.setLayoutParams(layoutParams);
				adapter.notifyDataSetChanged();
			}
		}

	}

	private void saveData() {
		if ((spinerfee.equals(getString(R.string.substitute_fee)) || spinerfee
				.equals(getString(R.string.receive_fee)))
				&& "".equals(feeedit.getText().toString().trim())) {
			Toast.makeText(MailBatchDlvyEnter.this,
					getString(R.string.feeshikong), 1000).show();
		} else {
			MyDialog.Builder builder = new MyDialog.Builder(
					MailBatchDlvyEnter.this);
			builder.setTitle(getString(R.string.hint));
			builder.setMessage(getString(R.string.shifousave));
			builder.setPositiveButton("",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							try {
								fee = Double.parseDouble(feeedit.getText()
										.toString().trim());
								if (spinerfee
										.equals(getString(R.string.substitute_fee))) {
									actualGoodsFee = fee;
								} else if (spinerfee
										.equals(getString(R.string.receive_fee)))
									actualFee = fee;
							} catch (NumberFormatException e) {
								Log.e("spiner", "" + 11111);
							}
							if (saveEnter()) {
								// ����ɹ�����ԭ��ҳ��
								Intent serviceIntent = new Intent(
										MailBatchDlvyEnter.this
												.getApplicationContext(),
										DlvUploadService.class);
								startService(serviceIntent);
								Intent intent = new Intent();
								setResult(1, intent);
								finish();
							} else {
								Toast.makeText(MailBatchDlvyEnter.this,
										getString(R.string.save_fail), 1000)
										.show();
							}
						}
					});
			builder.setNegativeButton("", null);
			builder.create().show();

		}
	}

	// ͨ��ɨ���õ��ʼ���
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == 1) {
			super.onActivityResult(requestCode, resultCode, intent);
			if (resultCode == 1) {
				// ��ȡ��ת��Դ
				Bundle bundle = intent.getExtras();
				strDnNumber = bundle.getString("txtResult");
				if (strDnNumber != null && !"".equals(strDnNumber)) {
					addMailListView(strDnNumber);
				}

			}
		}
	}

	// �ʼ�����
	private void setMailType() {

		rdiaogroup = (RadioGroup) findViewById(R.id.radioGrouplei);

		mailTypeButton1 = (RadioButton) findViewById(R.id.radioin);
		mailTypeButton1.setText(getString(R.string.internal).toString());

		mailTypeButton2 = (RadioButton) findViewById(R.id.radioout);
		mailTypeButton2.setText(getString(R.string.international).toString());

		rdiaogroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						if (checkedId == mailTypeButton1.getId()) {
							interFlag = "1";
						} else if (checkedId == mailTypeButton2.getId()) {
							interFlag = "0";
						}
					}

				});
	}

	// �տ�����
	private void setFeeType() {

		// �տ���
		feeedit = (EditText) findViewById(R.id.dlventermoney);
		feeedit.setHint(getString(R.string.fee_hint).toString());
		lock();
		substitute_fee = (CheckBox) findViewById(R.id.substitute_fee);
		substitute_fee
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							Unlock();
							substitute_fee.setChecked(true);
							receive_fee.setChecked(false);
							spinerfee = getString(R.string.substitute_fee);
						} else {
							substitute_fee.setChecked(false);
							spinerfee = "";
							if (!receive_fee.isChecked()) {
								feeedit.setText("");
								lock();
							}
						}

					}
				});
		receive_fee = (CheckBox) findViewById(R.id.receive_fee);
		receive_fee.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					Unlock();
					receive_fee.setChecked(true);
					substitute_fee.setChecked(false);
					spinerfee = getString(R.string.receive_fee);
				} else {
					receive_fee.setChecked(false);
					spinerfee = "";
					if (!substitute_fee.isChecked()) {
						feeedit.setText("");
						lock();
					}

				}

			}
		});

	}

	// ��EditText��ɿɱ༭״̬
	private void Unlock() {

		feeedit.setFilters(new InputFilter[] { new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {

				return null;
			}
		} });

	}

	// ��EditText��ɲ��ɱ༭״̬
	private void lock() {

		feeedit.setFilters(new InputFilter[] { new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {

				return source.length() < 1 ? dest.subSequence(dstart, dend)
						: "";
			}
		} });

	}
	private MailHandDetailDao mailhanddetailDao = null;
	// ��������
	private boolean saveEnter() {
		// is_upload, role, operationMode, dlvStsCode, undlvCauseCode,
		// undlvNextModeCode,dlvAddress, signatureImg
		boolean isSave = false;
		try {
				BaiduGps baiduGps = BaiduGps.getInstance(this);
				baiduGps.getLocation();
				
			for (int i = 0; i < maillistdata.size(); i++) {
					JSONObject	params = new JSONObject();
				maildao = DaoFactory.getInstance().getMailDao(
						MailBatchDlvyEnter.this);

				TelephonyManager telephonemanage = (TelephonyManager) getWindow()
						.getContext().getSystemService(
								Context.TELEPHONY_SERVICE);
				if(!"".equals(getorgCode())&&!"".equals(getlogName())){
				dlvAddress = baidugps.getAddress();

				params.put("IS_UPLOAD", is_upload);

				params.put("deviceNumber", telephonemanage.getDeviceId());// Tm.getDeviceId());//�ֻ��豸��
				params.put("orgCode", getorgCode());// ������
				params.put("userCode", getlogName());// ����

				params.put("role", role);
				params.put("operationMode", "I");// ����
				params.put("mailCode", maillistdata.get(i).get("mail_no"));// �ʼ���

				params.put("dlvOrgCode", getorgCode());// ������
				params.put("dlvOrgName", dlvOrgName);// ��������
				params.put("dlvOrgPostCode", StringFormate(dlvOrgPostCode)); // Ͷ�ݻ����ʱ�

				params.put("dlvStsCode", "I");
				params.put("signStsCode", signStsCode);// ǩ���������

				params.put("actualGoodsFee", actualGoodsFee);// ʵ�ջ���
				params.put("actualTax", actualTax);
				params.put("actualFee", actualFee);// ʵ���ʷ�
				params.put("otherFee", otherFee);

				params.put("dlvUserCode", getlogName());// ����
				params.put("dlvUserName", Global.NAME);// Ͷ��Ա����
				params.put("undlvCauseCode", undlvCauseCode);
				params.put("undlvNextModeCode", undlvNextModeCode);
				params.put("undlvCauseDesc", "");// ԭ������
				params.put("undlvfollowCode", "");// ��һ������
				params.put("signerName", revNameedit.getText());// ǩ��������
				params.put("interFlag", interFlag);// ���ʱ�ʶ ���ڹ�����1��0
				params.put("createDate",
						DateFormat.format("yyyyMMddkkmmss", new Date()));
				// 2013-4-22 15:06:08
				params.put("operationTime", "");// ����ʱ��
				params.put("dlvAddress", dlvAddress);
				params.put("signatureImg", signatureImg);
				if( baiduGps != null && baiduGps.getBdLocation() != null ){
					params.put("longitude", ""+baiduGps.getBdLocation().getLongitude());// ����
					params.put("latitude", ""+baiduGps.getBdLocation().getLatitude());// γ��
					params.put("province", ""+baiduGps.getBdLocation().getProvince());
					params.put("city", ""+baiduGps.getBdLocation().getCity());
					params.put("county", ""+baiduGps.getBdLocation().getDistrict());
					params.put("street", ""+baiduGps.getBdLocation().getStreet());
				}else{
					params.put("longitude", "");// ����
					params.put("latitude", "");// γ��
					params.put("province", "");
					params.put("city", "");
					params.put("county", "");
					params.put("street", "");
				}
					isSave = maildao.SaveMail(params);
					if(isSave){
					FileOutputStream fos = new FileOutputStream(fullFilename,true);
					String message = "I"+"\t\t"+maillistdata.get(i).get("mail_no").toString()+"\t\t"+is_upload+"\n";
					fos.write(message.getBytes());
					fos.close();
					}
				}
			}
		} catch (Exception e) {

		}
		return isSave;
	}


}

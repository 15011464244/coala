package com.cn.net.cnpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cn.net.cnpl.tools.BaseActivity;
import com.cn.net.cnpl.tools.BaseCommand;
import com.cn.net.cnpl.tools.CodeDictionary;
import com.cn.net.cnpl.tools.MyCode;
import com.cn.net.cnpl.tools.MyDialog;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.CaptureActivity;

public class BeginConnect extends BaseActivity {
	//��ť
	private Button codeButton = null;
	private Button cancelButton = null;
	private Button nextButton = null;
	
	//ɨ��������ȡת��������
	private EditText mailEdit;
/*
	//ת��������
	private RadioGroup rdiaogroup = null;
	private RadioButton mailTypeButton1 = null;
	private RadioButton mailTypeButton2 = null;
	*/
	
	String org_type = "1";
	
	public static List<Activity> activities=new ArrayList<Activity>();
	
	public static void allfinish(){
		for(int i=0;i<activities.size();i++){
			if(activities.get(i) != null){
			activities.get(i).finish();
			}
		}
		 activities.clear();
	}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.begin_connect);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bg_new);
		
		BeginConnect.activities.add(BeginConnect.this);
		
		//**ת��������
		setType();
		
		//**ת��������
		mailEdit = (EditText) findViewById(R.id.dlvmailidserch);
		// ��ά��ɨ��
		Button camera = (Button) findViewById(R.id.dlvcamera);
		camera.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(BeginConnect.this,
						CaptureActivity.class);
				startActivityForResult(intent, 1);
			}
		});

		// **ȡ������һ����ť
		cancelButton = (Button) findViewById(R.id.cancelbtn);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}

		});
		nextButton = (Button) findViewById(R.id.nextbtn);
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String str = mailEdit.getText().toString();
				if("".equals(str))
				{
					Toast.makeText(BeginConnect.this, getString(R.string.out_hint), 1000).show();
				}else {
					String s1="";
					String s2="";
					Date date=new Date();
					Intent intent = new Intent();
					intent.putExtra("org_type", org_type);
					intent.putExtra("out_code", mailEdit.getText().toString());
					s1 =""+date.getTime();
					s2 =""+DateFormat.format("yyyy-MM-dd kk:mm:ss", date);
					intent.putExtra("sid_time",s1 );
					intent.putExtra("begin_time",s2 );
					intent.putExtra("jieru", "con");
					intent.setClass(BeginConnect.this, MailConnectInfo.class);
					startActivity(intent);
				}
			}

		});
		
		codeButton = (Button) findViewById(R.id.btncode);
		codeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyCode.Builder builder = new MyCode.Builder(
						BeginConnect.this);
				builder.setTitle("��ά��:");
				builder.setPositiveButton("", null);
				builder.create().show();
			}

		});
		
		
	}

	// ͨ��ɨ���õ�����
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == 1) {
			// ��ȡ��ת��Դ
			Bundle bundle = intent.getExtras();
			String strDnNumber = bundle.getString("txtResult");
			Map<String, String> result= CodeDictionary.decodeOrgCode(strDnNumber);
				mailEdit.setText(result.get("code"));
				/*
				if(Global.ORGANIZATION.equals(result.get("type"))){
					mailTypeButton1.setChecked(true);
				}
				if(Global.CARRY.equals(result.get("type"))){
					mailTypeButton2.setChecked(true);
				}*/
		}
	}
	// �ʼ�����
	private void setType() {
/*
		rdiaogroup = (RadioGroup) findViewById(R.id.radioGrouplei);
		mailTypeButton1 = (RadioButton) findViewById(R.id.radioin);
		mailTypeButton2 = (RadioButton) findViewById(R.id.radioout);
		
		rdiaogroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						if (checkedId == mailTypeButton1.getId()) {
							org_type = Global.ORGANIZATION;
						} else if (checkedId == mailTypeButton2.getId()) {
							org_type = Global.CARRY;
						}
					}

				});*/
	}
}

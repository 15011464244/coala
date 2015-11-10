package com.cn.net.cnpl;

import java.util.Date;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cn.net.cnpl.db.dao.MailHandDao;
import com.cn.net.cnpl.tools.BaseActivity;
import com.cn.net.cnpl.tools.BaseCommand;
import com.cn.net.cnpl.tools.CodeDictionary;
import com.cn.net.cnpl.tools.MyCode;
import com.google.zxing.client.android.CaptureActivity;

public class BeginOut extends BaseActivity {
	// 按钮
	private Button codeButton = null;
	private Button cancelButton = null;
	private Button nextButton = null;

	// 扫描或输入获取转出方代码
	private EditText mailEdit;
	//private EnterDao enterdao = null;
	private MailHandDao mailhanddao = null;
/*
	// 转出方类型
	private RadioGroup rdiaogroup = null;
	private RadioButton mailTypeButton1 = null;
	private RadioButton mailTypeButton2 = null;
	*/
	
	String type = "1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.begin_out);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bg_new);
		
		BeginConnect.activities.add(BeginOut.this);
		
		// **接入方类型
		setType();

		// **接入方代码
		mailEdit = (EditText) findViewById(R.id.dlvmailidserch);
		// 扫描按钮
		Button camera = (Button) findViewById(R.id.dlvcamera);
		camera.setText(getString(R.string.scan).toString());
		camera.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(BeginOut.this,
						CaptureActivity.class);
				startActivityForResult(intent, 1);
			}
		});

		// **取消，下一步按钮
		cancelButton = (Button) findViewById(R.id.cancelbtn);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}

		});
		nextButton = (Button) findViewById(R.id.nextbtn);
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String str = mailEdit.getText().toString();
				//List<Map<String, Object>> rList = null;
				//mailhanddao = DaoFactory.getInstance().getMailHandDao(BeginOut.this);
				//rList = mailhanddao.FindMail(Global.HANDIN,Global.MAILCOM,-1);
				if("".equals(str))
				{
					Toast.makeText(BeginOut.this, getString(R.string.in_hint), 1000).show();
				//}else if(rList.size()<=0){
				//	Toast.makeText(BeginOut.this, getString(R.string.no_out), 1000).show();
				}else{
					Date date = new Date();
					Intent intent = new Intent();
					intent.putExtra("in_type", type);
					intent.putExtra("in_code", mailEdit.getText().toString());
					intent.putExtra("sid_time", ""+date.getTime());
					intent.putExtra("begin_time", ""+DateFormat.format("yyyy-MM-dd kk:mm:ss", date));
					intent.setClass(BeginOut.this, MailOutSelect.class);
					startActivityForResult(intent,2);
				}
			}

		});
		
		codeButton = (Button) findViewById(R.id.btncode);
		codeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyCode.Builder builder = new MyCode.Builder(
						BeginOut.this);
				builder.setTitle("二维码:");
				builder.setPositiveButton("", null);
				builder.create().show();
			}

		});
		
	}

	// 通过扫描后得到代码
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == 1) {
			// 获取跳转来源
			Bundle bundle = intent.getExtras();
			String strDnNumber = bundle.getString("txtResult");
			Map<String, String> result= CodeDictionary.decodeOrgCode(strDnNumber);
			mailEdit.setText(result.get("code"));/*
			if(Global.ORGANIZATION.equals(result.get("type"))){
				mailTypeButton1.setChecked(true);
			}
			if(Global.CARRY.equals(result.get("type"))){
				mailTypeButton2.setChecked(true);
			}*/
		}else if(resultCode == 2){
			Toast.makeText(BeginOut.this, getString(R.string.no_out), 1000).show();
		}
	}
	// 邮件类型
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
							type = "1";
						} else if (checkedId == mailTypeButton2.getId()) {
							type = "2";
						}
					}

				});*/
	}
}

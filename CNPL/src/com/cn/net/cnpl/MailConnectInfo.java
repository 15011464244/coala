package com.cn.net.cnpl;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.db.dao.MailHandDao;
import com.cn.net.cnpl.db.dao.MailHandDetailDao;
import com.cn.net.cnpl.tools.BASE64Encoder;
import com.cn.net.cnpl.tools.BaiduGps;
import com.cn.net.cnpl.tools.BaseActivity;
import com.cn.net.cnpl.tools.BaseCommand;
import com.cn.net.cnpl.tools.CodeDictionary;
import com.cn.net.cnpl.tools.MyCode;
import com.cn.net.cnpl.tools.MyDatePickerDialog;
import com.cn.net.cnpl.tools.MyDialog;
import com.cn.net.cnpl.tools.MySimpleAdapter;
import com.google.zxing.client.android.CaptureActivity;

public class MailConnectInfo extends BaseActivity implements View.OnTouchListener {

	private Button codeButton = null;
	private Button beforeButton = null;
	private Button nextButton = null;
	
	private Button scanButton = null;
	private Button okbtn = null;
	private Button deletebtn = null;
	
	ImageView imgview = null;
	private Button imgbutton = null;
		
	public ProgressDialog myDialog = null;
	private ListView listView = null;
	private MySimpleAdapter adapter = null;
	private ArrayList<HashMap<String, Object>> sourceList = null;
	
	private EditText mailidserch = null;
	
	private JSONObject params = null;
	private MailHandDao mailhanddao = null;
	private MailHandDetailDao mailhanddetaildao = null;
	
	private CheckBox substitute_fee;
	//private CheckBox receive_fee;
	String spinerfee = "";
	
	private LinearLayout linear = null;
	private EditText respserch = null;
	private Button respcamera = null;
	int typeFlag = 0;
	int btntype = 0;
	String org_type="";
	String out_code="";
	String sid_time="";
	String begin_time="";
	String createDate="";
	String jieru="";
	
	private String signatureImg = "";
	int tempI=0;
	
	private String principal_type="";
	
	//查询
	private EditText reqdate = null;
	private MyDatePickerDialog my_datePickerDialog;
	private int my_Year;
	private int my_Month;
	private int my_Day;
	Calendar my_Calendar;
	
	
	private static final int PHOTO_GRAPH = 2;// 拍照
	private static final int PHOTO_RESOULT = 3;// 结果
	private static final String IMAGE_FILE_LOCATION = "file:///sdcard/temp.jpg";//temp file
	private static final Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);//The Uri to store the big bitmap
	
	private ScrollView scrollView1;
	
	/*
	//责任方类型
	private RadioGroup rdiaogroup = null;
	private RadioButton mailTypeButton1 = null;
	private RadioButton mailTypeButton2 = null;
	*/
	private 	BaiduGps baiduGps ;
	
//	private void initDate() {
//		/* 从Calendar抽象基类获得实例对象，并设置成中国时区 */
//		my_Calendar = Calendar.getInstance(Locale.CHINA);
//		/* 从日历对象中获取当前的：年、月、日、时、分 */
//		my_Year = my_Calendar.get(Calendar.YEAR);
//		my_Month = my_Calendar.get(Calendar.MONTH) + 1;
//		my_Day = my_Calendar.get(Calendar.DAY_OF_MONTH);
//		reqdate.setText(new StringBuffer().append(my_Year).append("-")
//				.append(FormatString(my_Month)).append("-")
//				.append(FormatString(my_Day)));
//	}
	
	/* 日期改变设置事件监听器 */
	private OnDateSetListener myDateSetListener = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			/* 把设置修改后的日期赋值给我的年、月、日变量 */
			my_Year = year;
			my_Month = monthOfYear + 1;
			my_Day = dayOfMonth;
			/* 显示设置后的日期*/
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
	
	private void setdialog(){
		my_datePickerDialog = new MyDatePickerDialog(
				MailConnectInfo.this, myDateSetListener, my_Year,
				my_Month - 1, my_Day);
		my_datePickerDialog.show();
		my_datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
				new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
		});
	}
	/** 
	 * 功能描述：实现日期时间选择器 
	 *  
	 * @author android_yi
	 */  
    @Override  
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {  
  
            AlertDialog.Builder builder = new AlertDialog.Builder(this);  
            View view = View.inflate(this, R.layout.date_time_dialog, null);  
            final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);  
            final TimePicker timePicker = (android.widget.TimePicker) view.findViewById(R.id.time_picker);  
            builder.setView(view);  
  
            Calendar cal = Calendar.getInstance();  
            cal.setTimeInMillis(System.currentTimeMillis());  
            datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);  
  
            timePicker.setIs24HourView(true);  
            timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));  
            timePicker.setCurrentMinute(Calendar.MINUTE);  
  
            if (v.getId() == R.id.reqdate) {  
                final int inType = reqdate.getInputType();  
                reqdate.setInputType(InputType.TYPE_NULL);  
                reqdate.onTouchEvent(event);  
                reqdate.setInputType(inType);  
                reqdate.setSelection(reqdate.getText().length());  
                  
                builder.setTitle("选取异常时间");  
                builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {  
  
                    @Override  
                    public void onClick(DialogInterface dialog, int which) {  
                        StringBuffer sb = new StringBuffer();  
                        sb.append(String.format("%d-%02d-%02d",   
                                datePicker.getYear(),   
                                datePicker.getMonth() + 1,  
                                datePicker.getDayOfMonth()));  
                        sb.append(" ");  
                        sb.append(timePicker.getCurrentHour())  
                        .append(":").append(timePicker.getCurrentMinute()).append(":").append("00");  
  
                        reqdate.setText(sb);  
                        reqdate.requestFocus();  
                          
                        dialog.cancel();  
                    }  
                });  
                  
            }
              
            Dialog dialog = builder.create();  
            dialog.show();  
        }  
  
        return true;  
    }  
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.connect_info);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bg_new);
		
		BeginConnect.activities.add(MailConnectInfo.this);
		
		imgbutton = (Button)findViewById(R.id.imgbutton);
		imgview = (ImageView)findViewById(R.id.imageViewcamera);
		imgbutton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, PHOTO_GRAPH);
			}
			
		});
		codeButton = (Button) findViewById(R.id.btncode);
		codeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyCode.Builder builder = new MyCode.Builder(
						MailConnectInfo.this);
				builder.setTitle("二维码:");
				builder.setPositiveButton("", null);
				builder.create().show();
			}

		});
		baiduGps = BaiduGps.getInstance(this.getApplicationContext());
		baiduGps.getLocation();
		
		mailidserch = (EditText) findViewById(R.id.mailidserch);
		
		linear = (LinearLayout)findViewById(R.id.respid);
		respserch = (EditText) findViewById(R.id.respserch);
		respcamera = (Button) findViewById(R.id.respcamera);
		
		
		//查询
		reqdate = (EditText) findViewById(R.id.reqdate);
		reqdate.setOnTouchListener(this);
		/*
		reqdate.setInputType(0);
		initDate();
		reqdate.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setdialog();
				
			}
			
		});*/
		
		//获得数据
		Intent intent = getIntent();
		org_type = intent.getStringExtra("org_type");
		out_code = intent.getStringExtra("out_code");
		sid_time = intent.getStringExtra("sid_time");
		begin_time = intent.getStringExtra("begin_time");
		jieru = intent.getStringExtra("jieru");
		
		respserch.setText("");
	
		//**按钮
		substitute_fee = (CheckBox) findViewById(R.id.substitute_fee);
		substitute_fee.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					substitute_fee.setChecked(true);
					//receive_fee.setChecked(false);
					typeFlag = 1;
					linear.setVisibility(View.VISIBLE);
					respcamera.setOnClickListener(onClickListener);
				}else{
					substitute_fee.setChecked(false);
					typeFlag = 0;
					linear.setVisibility(View.GONE);
					respserch.setText("");
				}
				
			}
		});/*
		receive_fee = (CheckBox) findViewById(R.id.receive_fee);
		receive_fee.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					receive_fee.setChecked(true);
					substitute_fee.setChecked(false);
					typeFlag = 2;
					linear.setVisibility(View.VISIBLE);
					respcamera.setOnClickListener(onClickListener);
				}else{
					receive_fee.setChecked(false);
					typeFlag = 0;
					linear.setVisibility(View.GONE);
					respserch.setText("");
				}
				
			}
		});*/
		
		// **扫描
		scanButton = (Button) findViewById(R.id.camera);
		scanButton.setOnClickListener(onClickListener);
		
		sourceList = new ArrayList<HashMap<String, Object>>();
		scrollView1 = (ScrollView) findViewById(R.id.scrollView1);
		listView = (ListView) findViewById(R.id.conListView);
		
		listView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()){
				case  MotionEvent.ACTION_DOWN:
					setParentScrollAble(false);
				break;
				case  MotionEvent.ACTION_MOVE:
				break;
				case  MotionEvent.ACTION_UP:
				case  MotionEvent.ACTION_CANCEL:
					setParentScrollAble(true);
					break;
				
				}
				return false;
			}
		});
		
		adapter = new MySimpleAdapter(this, sourceList,
				R.layout.kernel_item_test2, new String[] { "displayOrderNum",
						"displayGoodsName", "displayStatus" }, new int[] {
						R.id.displayOrderNum, R.id.displayGoodsName,
						R.id.displayStatus });
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				tempI = arg2;
				MyDialog.Builder builder = new MyDialog.Builder(MailConnectInfo.this);
				builder.setTitle(getString(R.string.hint));
				builder.setMessage(getString(R.string.shifoudelete));
				builder.setPositiveButton("",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								sourceList.remove(tempI);
						        adapter.notifyDataSetChanged();
							}
						});
				builder.setNegativeButton("", null);
				builder.create().show();
				return false;
			}
			
		});
		/*
		listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
                menu.setHeaderTitle("????");
                menu.add(0, 0, 0, "???");
			}
		});*/
		listView.setAdapter(adapter);
		//setQuery();
		
		okbtn = (Button) findViewById(R.id.okbtn);
		okbtn.setOnClickListener(onClickListener);
		
		deletebtn = (Button) findViewById(R.id.deletebtn);
		deletebtn.setOnClickListener(onClickListener);

		// **上一步
		beforeButton = (Button) findViewById(R.id.beforebtn);
		if(jieru.equals("ing"))
			beforeButton.setVisibility(View.GONE);
		else{
			beforeButton.setVisibility(View.VISIBLE);
			beforeButton.setOnClickListener(onClickListener);
		}
		nextButton = (Button) findViewById(R.id.nextbtn);
		nextButton.setOnClickListener(onClickListener);
		
		
		setType();
		
		loadData();
	}
	/*
	@Override
	public boolean onContextItemSelected(MenuItem item) {
        
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();  
        int start = info.position; 
        sourceList.remove(start);
        adapter.notifyDataSetChanged();
        
        return super.onContextItemSelected(item);
	}
	*/
	
	
 private void setParentScrollAble ( boolean flag) {
	 
	 scrollView1.requestDisallowInterceptTouchEvent(!flag);//这里的parentScrollView就是listview外面的那个scrollview
 }
	
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
							principal_type = Global.ORGANIZATION;
						} else if (checkedId == mailTypeButton2.getId()) {
							principal_type = Global.CARRY;
						}
					}

				});*/
	}
	
	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.beforebtn://上一步
				finish();
				break;
			case R.id.nextbtn://下一步
				/*
				List<Map<String, Object>> rList = null;
				List<Map<String, Object>> rList1 = null;
				mailhanddao = DaoFactory.getInstance().getMailHandDao(MailConnectInfo.this);
				mailhanddetaildao = DaoFactory.getInstance().getMailHandDetailDao(
						MailConnectInfo.this);
				rList = mailhanddao.FindMail(Global.LOG_NAME,Global.HANDIN,Global.MAILCOM,-1);
				int tempsize = rList.size();
				int tempsize1 = 0;
				boolean tempbool = false;
				
				for(int i=0;i<tempsize;i++){
					rList1 = mailhanddetaildao.FindMail(rList.get(i).get("sid").toString(),Global.MAILIN);
					tempsize1 = rList1.size();
					if(tempsize1>0)
						tempbool=true;
				}
				if(tempbool){
					Toast.makeText(MailConnectInfo.this, getString(R.string.shifouup), 1000).show();
				}else{*/
				if(saveMail())
				{
					saveMail_S();
					
					disrepair = mailhanddetaildao.Findcount(sid_time, Global.MAILDISREPAIR);
					lose = mailhanddetaildao.Findcount(sid_time, Global.MAILLOSE);
					mailcnt = mailhanddetaildao.Findcount( sid_time, Global.MAILINTACT);
					mailcnt = (Integer.parseInt(mailcnt)+Integer.parseInt(disrepair)+Integer.parseInt(lose))+"";
					
					intent.putExtra("org_type", org_type);
					intent.putExtra("out_code", out_code);
					intent.putExtra("sid_time", sid_time);
					intent.putExtra("begin_time", begin_time);
					intent.putExtra("mailtotal_txt", mailcnt);
					intent.putExtra("disrepair_no_txt", disrepair);
					intent.putExtra("lose_no_txt", lose);
					intent.setClass(MailConnectInfo.this, MailConnectComp.class);
					startActivity(intent);
				}
				//}
				break;
			case R.id.camera://扫描
				btntype = 0;
				intent.setClass(MailConnectInfo.this, CaptureActivity.class);
				startActivityForResult(intent, 1);
				
				break;
			case R.id.deletebtn://删除
				btntype = 2;
				intent.setClass(MailConnectInfo.this, CaptureActivity.class);
				startActivityForResult(intent, 1);
				
				break;
			case R.id.okbtn://确认
				
				spinerfee = respserch.getText().toString();
				
				MyDialog.Builder builder = new MyDialog.Builder(MailConnectInfo.this);
				builder.setTitle(getString(R.string.hint));
				builder.setMessage(getString(R.string.shifouenter));
				builder.setPositiveButton("",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								try{
								String mail_no = mailidserch.getText().toString();
								String reqdate_str = reqdate.getText().toString();
								String str = "";
								boolean ishave=false;
								for(int i=0;i<sourceList.size();i++){
									if(mail_no.equals(sourceList.get(i).get("displayOrderNum")))
										ishave=true;
								}
								if("".equals(mail_no)){
									Toast.makeText(MailConnectInfo.this, getString(R.string.scan_hint), 1000).show();
								}else if(ishave){
									Toast.makeText(MailConnectInfo.this, getString(R.string.mail_no_repeat), 1000).show();
								}else if(("".equals(reqdate_str)||"".equals(spinerfee))&&(typeFlag == 1 || typeFlag == 2)){
									if("".equals(spinerfee))
										Toast.makeText(MailConnectInfo.this, getString(R.string.resp_hint), 1000).show();
									else
										Toast.makeText(MailConnectInfo.this, getString(R.string.date_hint), 1000).show();
								}else{
									createDate = ""+DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date());
									HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
									tempHashMap.put("displayOrderNum", mail_no);
									tempHashMap.put("displayGoodsName", createDate);
									if(typeFlag == 1)
										str = getString(R.string.disrepair);
									else if(typeFlag == 2)
										str = getString(R.string.lose);
									else if(typeFlag == 0)
										str = getString(R.string.comp);
									tempHashMap.put("displayStatus", str);
									if(typeFlag == 1 || typeFlag == 2)
									{ 
										tempHashMap.put("principal", spinerfee);
										tempHashMap.put("principal_type", principal_type);
										tempHashMap.put("abnormity_time",reqdate.getText().toString());
										tempHashMap.put("signatureImg",signatureImg);
									}
									sourceList.add(tempHashMap);
									adapter.notifyDataSetChanged();
									mailidserch.setText("");
									respserch.setText("");
									reqdate.setText("");
									signatureImg="";
									substitute_fee.setChecked(false);
									//receive_fee.setChecked(false);
								}
								}catch(Exception e){
									
								}
							}
						});
				builder.setNegativeButton("", null);
				builder.create().show();
				
				/*
				if(saveMail())
				{
					Toast.makeText(MailConnectInfo.this, getString(R.string.save_comp), 1000).show();
				}*/
				break;
			case R.id.respcamera://扫描
				btntype = 1;
				intent.setClass(MailConnectInfo.this, CaptureActivity.class);
				startActivityForResult(intent, 1);
				break;
			}
		}

	};
	String returnstr="";
	// 返回
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == 1) {
			Bundle bundle = intent.getExtras();
			String strDnNumber = bundle.getString("txtResult");
			if (strDnNumber != null && strDnNumber.length() > 0)
			{
				if(btntype == 0){
					JSONArray array= CodeDictionary.decodeCode2JsonArray(strDnNumber);
					if(array == null){//不是扫描的二维码
						//mailidserch.setText(strDnNumber);
						//respserch.setText("");
						if (typeFlag == 1) {
							mailidserch.setText(strDnNumber);
							respserch.setText("");
						} else if (typeFlag == 0) {
							boolean ishave = false;
							for (int i = 0; i < sourceList.size(); i++) {
								if (strDnNumber.equals(sourceList.get(i).get(
										"displayOrderNum"))) {
									ishave = true;
								}
							}
							if (ishave) {
								Toast.makeText(MailConnectInfo.this,
										getString(R.string.mail_no_repeat),
										1000).show();
							} else {
								HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
								createDate = ""
										+ DateFormat.format(
												"yyyy-MM-dd kk:mm:ss",
												new Date());
								tempHashMap.put("displayOrderNum", strDnNumber);

								tempHashMap.put("displayGoodsName", createDate);
								tempHashMap.put("displayStatus",
										getString(R.string.comp));
								tempHashMap.put("principal", "");
								tempHashMap.put("principal_type", "");
								tempHashMap.put("abnormity_time", "");
								tempHashMap.put("signatureImg", "");

								sourceList.add(tempHashMap);
								adapter.notifyDataSetChanged();
								mailidserch.setText("");
								respserch.setText("");
								signatureImg = "";
								substitute_fee.setChecked(false);
								// receive_fee.setChecked(false);
							}
						}
					}else{
						boolean isscan = false;
						
						for(int i=0;i<array.length();i++){
							HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
							try {
								isscan = false;
								for(int j=0;j<sourceList.size();j++){
									if(sourceList.get(j).get("displayOrderNum").equals(array.getJSONObject(i).get("mailNo"))){
										isscan = true;
									}
								}
								if(!isscan){
									createDate = ""+DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date());
								tempHashMap.put("displayOrderNum", array.getJSONObject(i).get("mailNo"));
									//邮件号
								tempHashMap.put("displayGoodsName", createDate);
								tempHashMap.put("displayStatus", getString(R.string.comp));
								tempHashMap.put("principal",  "");
								tempHashMap.put("principal_type","");
								tempHashMap.put("abnormity_time",  "");
								tempHashMap.put("signatureImg","");
								if(Global.MAILDISREPAIR.equals(array.getJSONObject(i).get("isMangle"))){
									tempHashMap.put("displayStatus", getString(R.string.disrepair));
									tempHashMap.put("principal_type", Global.CARRY);
									tempHashMap.put("principal",  array.getJSONObject(i).get("responsible"));
									tempHashMap.put("abnormity_time",  array.getJSONObject(i).get("abnormity_time"));
									tempHashMap.put("signatureImg",array.getJSONObject(i).get("signatureImg"));
								}
								if(Global.MAILLOSE.equals(array.getJSONObject(i).get("isMangle"))){
									tempHashMap.put("displayStatus", getString(R.string.lose));
									tempHashMap.put("principal_type", Global.CARRY);
									tempHashMap.put("principal",  array.getJSONObject(i).get("responsible"));
									tempHashMap.put("abnormity_time",  array.getJSONObject(i).get("abnormity_time"));
									tempHashMap.put("signatureImg",array.getJSONObject(i).get("signatureImg"));
								}
								sourceList.add(tempHashMap);
								}else{
									Toast.makeText(MailConnectInfo.this, getString(R.string.shifouscan), 1000).show();
								}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
							
						}
//						array.get(index);
//						[{"mailNo":"12121212121},{"mailNo":"12121212121"," isMangle ":"1", 
//							" responsible ":"35000100", abnormity_time :" 20132211222222"}
						adapter.notifyDataSetChanged();
						mailidserch.setText("");
						respserch.setText("");
						signatureImg="";
						substitute_fee.setChecked(false);
						//receive_fee.setChecked(false);
					
					}
				
				}else if(btntype == 1){
					respserch.setText(strDnNumber);
				}else if(btntype == 2){
					returnstr=strDnNumber;
					MyDialog.Builder builder1 = new MyDialog.Builder(MailConnectInfo.this);
					builder1.setTitle(getString(R.string.hint));
					builder1.setMessage(getString(R.string.shifoudelete));
					builder1.setPositiveButton("",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									try{
									String mail_no = returnstr;
									boolean ishave=false;
									for(int i=0;i<sourceList.size();i++){
										if(mail_no.equals(sourceList.get(i).get("displayOrderNum"))){
											tempI = i;
											ishave=true;
										}
									}
									if("".equals(mail_no)){
										Toast.makeText(MailConnectInfo.this, getString(R.string.scan_hint), 1000).show();
									}else if(!ishave){
										Toast.makeText(MailConnectInfo.this, getString(R.string.mail_repeat), 1000).show();
									}else{
										sourceList.remove(tempI);
								        adapter.notifyDataSetChanged();
								        mailidserch.setText("");
										respserch.setText("");
										reqdate.setText("");
										substitute_fee.setChecked(false);
										//receive_fee.setChecked(false);
										returnstr="";
									}
									}catch(Exception e){
										
									}
								}
							});
					builder1.setNegativeButton("", null);
					builder1.create().show();
				}
			}
		}
		// 拍照
				if (requestCode == PHOTO_GRAPH) { // 设置文件保存路径
					cropImageUri(imageUri);
				}
				// 处理结果
				if (requestCode == PHOTO_RESOULT) {
					if (imageUri != null) {
						Bitmap photo;
						try {
							photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
							byte[] imgs=	BaseCommand.compressImage(photo);
							imgview.setImageBitmap(BaseCommand.Bytes2Bimap(imgs));
							imgview.setVisibility(View.VISIBLE);
						// 压缩
						BASE64Encoder base64Encoder = new BASE64Encoder();
						signatureImg = base64Encoder.encode(imgs);
						} catch (FileNotFoundException e) {
						}
					}
				}

	}

	
	private void cropImageUri(Uri uri){
		 Intent intent = new Intent("com.android.camera.action.CROP");
		 intent.setDataAndType(uri, "image/*");
		 intent.putExtra("crop", "true");
		 intent.putExtra("aspectX", 2);
		 intent.putExtra("aspectY", 1);
		 intent.putExtra("outputX", 600);
		 intent.putExtra("outputY", 300);
		 intent.putExtra("scale", true);
		 intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		 intent.putExtra("return-data", false);
		 intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		 intent.putExtra("noFaceDetection", false); // no face detection
		 startActivityForResult(intent, PHOTO_RESOULT);
		}


	private void loadData() {
		try {
			List<Map<String, Object>> rList = null;
			mailhanddetaildao = DaoFactory.getInstance().getMailHandDetailDao(MailConnectInfo.this);
			rList = mailhanddetaildao.FindMail(sid_time,"",-1);
			
			int tempSize = rList.size();
			String str = "";
			
			for (int i = 0; i < tempSize; i++) {
				HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
				
				if(Global.MAILDISREPAIR.equals(rList.get(i).get("mail_type")))
					str = getString(R.string.disrepair);
				else if(Global.MAILLOSE.equals(rList.get(i).get("mail_type")))
					str = getString(R.string.lose);
				else if(Global.MAILINTACT.equals(rList.get(i).get("mail_type")))
					str = getString(R.string.comp);
				
				tempHashMap.put("displayOrderNum", (String) rList.get(i).get("mail_num"));
				tempHashMap.put("displayGoodsName", rList.get(i).get("create_time").toString());
				tempHashMap.put("displayStatus", str);
				tempHashMap.put("principal",rList.get(i).get("principal").toString());
				tempHashMap.put("principal_type",rList.get(i).get("principal_type").toString());
				tempHashMap.put("abnormity_time",rList.get(i).get("abnormity_time").toString());
				tempHashMap.put("signatureImg",rList.get(i).get("signatureImg").toString());
				sourceList.add(tempHashMap);
				adapter.notifyDataSetChanged();
				mailidserch.setText("");
				respserch.setText("");
				signatureImg="";
				substitute_fee.setChecked(false);
				//receive_fee.setChecked(false);
				
			}
		} catch (Exception e) {
		}
	}
	
	
	
	// 保存
	private boolean saveMail() {
		boolean isSave = false;
		try {
			if(!"".equals(getlogName())){
			if (params == null)
				params = new JSONObject();
			mailhanddetaildao=DaoFactory.getInstance().getMailHandDetailDao(MailConnectInfo.this);
			mailhanddetaildao.deleteMail(sid_time);
			
			for (int i = 0; i < sourceList.size(); i++) {
				params.put("mail_num", sourceList.get(i).get("displayOrderNum"));// 邮件号
				if(sourceList.get(i).get("displayStatus").equals(getString(R.string.disrepair)))
				{	
					params.put("mail_type", Global.MAILDISREPAIR);// 邮件情况
					params.put("principal", sourceList.get(i).get("principal").toString());// 责任方
					params.put("principal_type", sourceList.get(i).get("principal_type").toString());
					params.put("abnormity_time", sourceList.get(i).get("abnormity_time").toString());// 异常时间
					params.put("signatureImg", sourceList.get(i).get("signatureImg").toString());
				}else if(sourceList.get(i).get("displayStatus").equals(getString(R.string.lose)))
				{
					params.put("mail_type", Global.MAILLOSE);// 邮件情况
					params.put("principal", sourceList.get(i).get("principal").toString());// 责任方
					params.put("principal_type", sourceList.get(i).get("principal_type").toString());
					params.put("abnormity_time", sourceList.get(i).get("abnormity_time").toString());// 异常时间
					params.put("signatureImg", sourceList.get(i).get("signatureImg").toString());
				}
				else if(sourceList.get(i).get("displayStatus").equals(getString(R.string.comp)))
				{
					params.put("mail_type", Global.MAILINTACT);// 邮件情况
					params.put("principal", "");// 责任方
					params.put("principal_type", "");
					params.put("abnormity_time", "");// 异常时间
					params.put("signatureImg", "");
				}
				params.put("create_time",
				sourceList.get(i).get("displayGoodsName"));
				params.put("upload_time", "");// 上传时间
				params.put("sid", sid_time);// 唯一标示
				
				params.put("is_out", Global.MAILIN);// 是否转出
				params.put("out_time", "");// 转出时间
				

				params.put("code2d_num", "");// 二维码
				params.put("paper_num", "");// 纸质
				params.put("operatorType", "I");
				params.put("oldSid", "");
				
				
				isSave = mailhanddetaildao.SaveMail(params);
				
			}
			if (params != null)
				params = null;
			}
		} catch (Exception e) {

		}
		return isSave;
	}
	String mailcnt = "";
	String disrepair = "";
	String lose = "";
	String abnormity_time ="";
	
	private boolean saveMail_S() {
		boolean isSave = false;
		try {
			if (params == null)
				params = new JSONObject();
			mailhanddao = DaoFactory.getInstance().getMailHandDao(
					MailConnectInfo.this);
			

			
			params.put("sid", sid_time);// 唯一不标示
			params.put("out_code", out_code);// 转出方
			params.put("in_code", getorgCode());//接入方
			params.put("org_type", org_type);//类型
			params.put("hand_type", Global.HANDIN);// 2.转出 1.转入
			
			params.put("hand_state",Global.MAILEX);//0.已撤销1.交接中2.已完成
			params.put("begin_time", begin_time);// 开始时间
			params.put("end_time", "");// 结束时间
			
			params.put("create_by", getlogName());// 用户
			params.put("is_shift_stop", Global.STOP);// 1.是 2.否
			
			params.put("shift_time", "");// 换班时间
			params.put("certificate", Global.ELECTRON);// 1 电子  2 纸质
			
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
			
			if (params != null)
				params = null;
			
			isSave = true;
		} catch (Exception e) {

		}
		return isSave;
	}

}


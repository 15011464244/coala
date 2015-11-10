package com.cn.net.cnpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.db.dao.MailHandDao;
import com.cn.net.cnpl.db.dao.MailHandDetailDao;
import com.cn.net.cnpl.tools.BaseActivity;
import com.cn.net.cnpl.tools.MyCode;
import com.cn.net.cnpl.tools.MycheckAdapter;
import com.cn.net.cnpl.tools.MycheckAdapter.ViewHolder;

public class MailOutSelect extends BaseActivity {

	private Button codeButton = null;
	private MycheckAdapter adapter = null;
	private ListView listview = null;
	private List<Map<String, String>> list = null;
	public ProgressDialog myDialog = null;
	private int pageNo = 1;
	private boolean loadOver = true;
	
	private Button okbtn = null;
	
	private String in_type="";
	private String in_code="";
	private String begin_time="";
	private String sid_time="";
	
	private MailHandDao mailhanddao=null;
	private MailHandDetailDao mailhanddetaildao = null;
	//private EnterDao enterdao = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.out_select);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bg_new);

		BeginConnect.activities.add(MailOutSelect.this);
		
		codeButton = (Button) findViewById(R.id.btncode);
		codeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyCode.Builder builder = new MyCode.Builder(
						MailOutSelect.this);
				builder.setTitle("∂˛Œ¨¬Î:");
				builder.setPositiveButton("", null);
				builder.create().show();
			}

		});
		// Ëé∑ÂæóÊï∞ÊçÆ
		Intent intent = getIntent();
		in_type = intent.getStringExtra("in_type");
		in_code = intent.getStringExtra("in_code");
		sid_time = intent.getStringExtra("sid_time");
		begin_time = intent.getStringExtra("begin_time");
		
		listview = (ListView) findViewById(R.id.comListView);
		if (list == null)
			list = new ArrayList<Map<String, String>>();
		loadData();


		adapter = new MycheckAdapter(this, list);
		listview.setAdapter(adapter);
		listview.setItemsCanFocus(false);
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		okbtn = (Button) findViewById(R.id.okbtn);
		okbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try{
				int i =0;
				
				boolean isselcted=false;
				int count = listview.getCount(); 
				int selcnt=0;
				int discnt=0;
				int losecnt=0;
				JSONObject jsonObject = new JSONObject();
				JSONArray objArray = new JSONArray();
				String str="";
				for(i =0;i<count;i++)
				{
					if(MycheckAdapter.isSelected.get(i))
					{
						jsonObject = null;
						if(jsonObject == null)
							jsonObject = new JSONObject();
						jsonObject.put("mailNo", list.get(i).get("mailno_txt"));
						if(getString(R.string.disrepair).equals(list.get(i).get("type_txt")))
							jsonObject.put("isMangle", Global.MAILDISREPAIR);
						else if(getString(R.string.lose).equals(list.get(i).get("type_txt")))
							jsonObject.put("isMangle", Global.MAILLOSE);
						else if(getString(R.string.comp).equals(list.get(i).get("type_txt")))
							jsonObject.put("isMangle", Global.MAILINTACT);
						jsonObject.put("responsible", list.get(i).get("principal"));
						jsonObject.put("principal_type", list.get(i).get("principal_type"));
						jsonObject.put("abnormity_time", list.get(i).get("abnormity_time"));
						jsonObject.put("create_time", list.get(i).get("createDate"));
						jsonObject.put("upload_time", list.get(i).get("upload_time"));
						jsonObject.put("sid", list.get(i).get("sid"));
						jsonObject.put("oldSid", list.get(i).get("oldSid"));
						jsonObject.put("signatureImg", list.get(i).get("signatureImg"));
						objArray.put(jsonObject);
						str = list.get(i).get("type_txt");
						
						if(str.equals(Global.MAILDISREPAIR))
							discnt++;
						else if(str.equals(Global.MAILLOSE))
							losecnt++;
						selcnt++;
						isselcted=true;
					}
				}
				
				if(!isselcted){
					Toast.makeText(MailOutSelect.this, getString(R.string.out_select), 1000).show();
				}else if (count == i) {
					Intent intent = new Intent();
					intent.putExtra("in_type", in_type);
					intent.putExtra("in_code", in_code);
					intent.putExtra("jason", objArray.toString());
					intent.putExtra("selcnt", ""+selcnt);
					intent.putExtra("discnt", ""+discnt);
					intent.putExtra("losecnt", ""+losecnt);
					intent.putExtra("sid_time", sid_time);
					intent.putExtra("begin_time", begin_time);
					intent.putExtra("suc_secform", "2");
					intent.setClass(MailOutSelect.this, MailOutDetail.class);
					startActivity(intent);
				}
				if(jsonObject!=null)
					jsonObject = null;
				if(objArray!=null)
					objArray=null;
				}catch(Exception e){
					
				}
			}

		});
		

		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				ViewHolder vHolder = (ViewHolder)view.getTag();
				vHolder.cBox.toggle();
				MycheckAdapter.isSelected.put(position, vHolder.cBox.isChecked());
				
			}
		});
	}

	private void loadData() {
		try {
			mailhanddetaildao = DaoFactory.getInstance().getMailHandDetailDao(MailOutSelect.this);
			mailhanddao = DaoFactory.getInstance().getMailHandDao(MailOutSelect.this);
			List<Map<String, Object>> rList = null;
			
			rList = mailhanddetaildao.FindMail("",Global.MAILIN,-1);
			if (rList == null || rList.size() == 0) {
				loadOver = false;
				return;
			}
			int tempSize = rList.size();
			String str="";
			String sid="";
			for (int i = 0; i < tempSize; i++) {
				sid = rList.get(i).get("sid").toString();
				if(!"0".equals(mailhanddao.FindExit(getlogName(),sid, Global.MAILCOM))){
				Map<String, String> tempHashMap = new LinkedHashMap<String, String>();
				tempHashMap.put("num", "" + pageNo);
				tempHashMap.put("mailno_txt",(String) rList.get(i).get("mail_num"));
				
				if(Global.MAILDISREPAIR.equals(rList.get(i).get("mail_type")))
					str = getString(R.string.disrepair);
				else if(Global.MAILLOSE.equals(rList.get(i).get("mail_type")))
					str = getString(R.string.lose);
				else if(Global.MAILINTACT.equals(rList.get(i).get("mail_type")))
					str = getString(R.string.comp);
				
				tempHashMap.put("type_txt", str);
				tempHashMap.put("principal",(String) rList.get(i).get("principal"));
				tempHashMap.put("principal_type",(String) rList.get(i).get("principal_type"));
				tempHashMap.put("abnormity_time",(String) rList.get(i).get("abnormity_time"));
				tempHashMap.put("upload_time",(String) rList.get(i).get("upload_time"));
				tempHashMap.put("createDate",(String) rList.get(i).get("create_time"));
				tempHashMap.put("sid",(String) rList.get(i).get("sid"));
				tempHashMap.put("oldSid",(String) rList.get(i).get("oldSid"));
				tempHashMap.put("signatureImg", rList.get(i).get("signatureImg").toString());
				list.add(tempHashMap);
				pageNo++;
				}
			}
			
			if(list.size()==0){
				Intent intent = new Intent();
				setResult(2, intent);
				finish();
			}
			
			/*
			List<Map<String, Object>> rList1 = null;
			mailhanddetaildao = DaoFactory.getInstance().getMailHandDetailDao(MailOutSelect.this);
			
			List<Map<String, Object>> rList = null;
			
			mailhanddao = DaoFactory.getInstance()
					.getMailHandDao(MailOutSelect.this);
			rList = mailhanddao.FindMail(Global.HANDIN,Global.MAILCOM,-1);

			if (rList == null || rList.size() == 0) {
				loadOver = false;
				return;
			}
			int tempSize = rList.size();
			String begin_time_temp="";
			String mailcnt="";
			String disrepair="";
			String lose="";

			for (int i = 0; i < tempSize; i++) {
				Map<String, String> tempHashMap = new LinkedHashMap<String, String>();
				tempHashMap.put("num", "" + pageNo);
				tempHashMap.put("conout_txt", rList.get(i).get("out_code")
						.toString());
				
				begin_time_temp=rList.get(i).get("begin_time").toString();
				mailcnt = mailhanddetaildao.Findcount(begin_time_temp, Global.MAILINTACT);
				disrepair = mailhanddetaildao.Findcount(begin_time_temp, Global.MAILDISREPAIR);
				lose = mailhanddetaildao.Findcount(begin_time_temp, Global.MAILLOSE);
				
				
				tempHashMap.put("time_txt", begin_time_temp+"??"+rList.get(i).get("end_time").toString());
				tempHashMap.put("total_txt", mailcnt);
				tempHashMap.put("disrepair_txt", disrepair);
				tempHashMap.put("lose_txt", lose);
				tempHashMap.put("begin_time", begin_time_temp);

				if (rList1 != null)
					rList1.clear();
				rList1 = mailhanddetaildao.FindMail(begin_time_temp,Global.MAILIN,-1);
				if(rList1.size()!=0){
					tempHashMap.put("no_outmail", ""+rList1.size());
					list.add(tempHashMap);
					pageNo++;
				}
			}
			
			if(list.size()==0){
				Intent intent = new Intent();
				setResult(2, intent);
				finish();
			}

			*/
		} catch (Exception e) {
		}
	}

}

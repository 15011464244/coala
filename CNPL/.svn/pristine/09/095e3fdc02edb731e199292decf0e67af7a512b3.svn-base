package com.cn.net.cnpl;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.db.dao.MailHandDao;
import com.cn.net.cnpl.db.dao.MailHandDetailDao;
import com.cn.net.cnpl.tools.BaseActivity;
import com.cn.net.cnpl.tools.BaseCommand;
import com.cn.net.cnpl.tools.CodeDictionary;
import com.cn.net.cnpl.tools.MyCode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class MailChangeOffImg extends BaseActivity {

	private Button codeButton = null;
	private MyImgAdapter adapter = null;
	private ListView listview = null;
	private List<Map<String, Object>> list = null;
	public ProgressDialog myDialog = null;
	private boolean loadOver = true;
	private int Page=1;

	private MailHandDao mailhanddao = null;
	private MailHandDetailDao mailhanddetaildao = null;
	
	JSONObject jsonhead = null;
	//private EnterDao enterdao = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.change_offimg);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bg_new);
		
		listview = (ListView) findViewById(R.id.comListView);
		if (list == null)
			list = new ArrayList<Map<String, Object>>();

		adapter = new MyImgAdapter(this);

		listview.setAdapter(adapter);

		loadData_mail(Global.HANDIN);
		loadData_mail(Global.HANDOUT);
		
		codeButton = (Button) findViewById(R.id.btncode);
		codeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyCode.Builder builder = new MyCode.Builder(
						MailChangeOffImg.this);
				builder.setTitle("二维码:");
				builder.setPositiveButton("", null);
				builder.create().show();
			}

		});

	}
	
	// 显示信息
	private void loadData_mail(String hand) {
		try {
			List<Map<String, Object>> rList1 = null;
			List<Map<String, Object>> rList = null;
			mailhanddao = DaoFactory.getInstance().getMailHandDao(MailChangeOffImg.this);
			mailhanddetaildao = DaoFactory.getInstance().getMailHandDetailDao(
					MailChangeOffImg.this);
			rList = mailhanddao.FindMail(getlogName(),hand,Global.MAILCOM,-1);
			
			if (rList == null || rList.size() == 0) {
				loadOver = false;
				return;
			}
			int tempSize = rList.size();
			JSONObject jsonObject = new JSONObject();
			
			for (int i = 0; i < tempSize; i++) {
				if(jsonhead==null)
					jsonhead = new JSONObject();
				if(jsonObject==null)
					jsonObject = new JSONObject();
				jsonObject.put("loginName", getlogName());//
				jsonObject.put("type", rList.get(i).get("org_type"));//
				if(Global.HANDIN.equals(rList.get(i).get("hand_type")))
					jsonObject.put("code", rList.get(i).get("out_code"));//
				else
					jsonObject.put("code", rList.get(i).get("in_code"));//code
				jsonObject.put("connectType", rList.get(i).get("hand_type"));//
				jsonObject.put("connectStartTime", rList.get(i).get("begin_time"));//
				jsonObject.put("connectEndTime", rList.get(i).get("end_time"));//
				
				jsonhead.put("header", jsonObject);//
				if (rList1 != null)
					rList1.clear();
				
				if(Global.HANDIN.equals(hand))
					rList1 = mailhanddetaildao.FindChange(rList.get(i).get("sid").toString(),true);
				else 
					rList1 = mailhanddetaildao.FindChange(rList.get(i).get("sid").toString(),false);
				if(rList1.size()!=0){
					loadData(rList.get(i).get("sid").toString(),Global.HANDIN.equals(hand));
				}
				mailhanddao.updateMail(getlogName(), Global.HANDIN,Global.MAILCOM, Global.STOP);
				if(jsonObject!=null)
					jsonObject = null;
			}

			
		} catch (Exception e) {
		}
	}

	// 显示信息
	private void loadData(String sid_time,boolean ss) {
		try {
			List<Map<String, Object>> rList = null;
			List<String> rListCode = null;
			mailhanddetaildao = DaoFactory.getInstance().getMailHandDetailDao(
					MailChangeOffImg.this);
			rList = mailhanddetaildao.FindChange(sid_time,ss);

			if (rList == null || rList.size() == 0) {
				return;
			}
			int tempSize = rList.size();
			
			JSONArray objArray = new JSONArray();
			JSONObject jsonObject1 = new JSONObject();
			String str="";
			
			for (int i = 0; i < tempSize; i++) {
				jsonObject1 = null;
				if (jsonObject1 == null)
					jsonObject1 = new JSONObject();
				jsonObject1.put("mailNo", rList.get(i).get("mail_num"));
				jsonObject1.put("isMangle", rList.get(i).get("mail_type"));
				jsonObject1.put("responsible", rList.get(i).get("principal"));
				jsonObject1.put("abnormity_time", rList.get(i).get("abnormity_time"));
				jsonObject1.put("create_time", rList.get(i).get("create_time"));
				jsonObject1.put("upload_time", rList.get(i).get("upload_time"));

				objArray.put(jsonObject1);

			}
			jsonhead.put("body", objArray);
			
			rListCode = CodeDictionary.createCode2Str(jsonhead);
			
			for (int i = 0; i < rListCode.size(); i++) {
				Map<String, Object> tempHashMap = new LinkedHashMap<String, Object>();

				Bitmap bmp = null;
				str = rListCode.get(i);
				
				if (str != null && !"".equals(str)) {
					bmp = BaseCommand.CreateTwoDCode(str);
				}

				if (bmp != null) {
					tempHashMap.put("num", Page);
					tempHashMap.put("img", bmp);

					list.add(tempHashMap);
				}
				Page++;
				if(objArray!=null)
					objArray = null;
				if(jsonhead!=null)
					jsonhead = null;
			}
			
		} catch (Exception e) {
		}
	}

	private Handler messageListener = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Global.TASK_LOOP_COMPLETE:
				myDialog.dismiss();
				adapter.notifyDataSetChanged();
				break;
			}
		}
	};

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
}

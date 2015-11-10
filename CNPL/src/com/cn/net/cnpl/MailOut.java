package com.cn.net.cnpl;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.db.dao.MailHandDao;
import com.cn.net.cnpl.db.dao.MailHandDetailDao;
import com.cn.net.cnpl.tools.BaseActivity;
import com.cn.net.cnpl.tools.MyCode;

public class MailOut extends BaseActivity {

	private Button codeButton = null;
	private Button conButtonOk = null;
	
	private Button concomplete = null;
	
	private MailHandDao mailhanddao = null;
	private MailHandDetailDao mailhanddetaildao = null;
	
	private TextView completenum=null;
	private TextView completemailnum=null;
	
	private int comcnt=0;
	private int commailcnt=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.mail_out);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bg_new);
		
		conButtonOk = (Button) findViewById(R.id.conbuttonok);
		conButtonOk.setOnClickListener(onClickListener);
		concomplete = (Button) findViewById(R.id.concomplete);
		concomplete.setOnClickListener(onClickListener);
		
		completenum= (TextView)findViewById(R.id.completenum);
		completemailnum= (TextView)findViewById(R.id.completemailnum);
		
		codeButton = (Button) findViewById(R.id.btncode);
		codeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyCode.Builder builder = new MyCode.Builder(
						MailOut.this);
				builder.setTitle("二维码:");
				builder.setPositiveButton("", null);
				builder.create().show();
			}

		});
		
	}
	
	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.conbuttonok://开始接入
				intent.setClass(MailOut.this, BeginOut.class);
				startActivity(intent);
				break;
			case R.id.concomplete://完成
				if(comcnt==0)
				{
					Toast.makeText(MailOut.this, getString(R.string.no_jieru), 1000).show();
				}else{
					intent.setClass(MailOut.this, MailOutSuc.class);
					startActivity(intent);
				}
				break;
			}
		}

	};
	public int getsize(){
		int size=0;
		List<Map<String, Object>> rList = null;
		mailhanddao = DaoFactory.getInstance().getMailHandDao(MailOut.this);
		rList = mailhanddao.FindMail(getlogName(),Global.HANDOUT,Global.MAILCOM,-1);
		size = rList.size();
		
		return size;
	}
	
	public int getMailsize(){
		int size=0;
		int cnt = 0;
		List<Map<String, Object>> rList = null;
		mailhanddetaildao = DaoFactory.getInstance().getMailHandDetailDao(MailOut.this);
		mailhanddao = DaoFactory.getInstance().getMailHandDao(MailOut.this);
		rList = mailhanddao.FindMail(getlogName(),Global.HANDOUT,Global.MAILCOM,-1);
		size = rList.size();
		for(int i=0;i<size;i++){
			cnt = cnt + Integer.parseInt(mailhanddetaildao.FindcountMail(rList.get(i).get("sid").toString()));
		}
		
		return size;
	}
	
	@Override
	protected void onResume() {
		initData();
		super.onResume();
	}
	
	/**
	 * 初始化数据
	 */
	public void initData() {
		new AsyncTask<Object, Void, List<Map<String, String>>>() {

			@Override
			protected List<Map<String, String>> doInBackground(Object... params) {

				List<Map<String, String>> dataList = null;
				try {
					comcnt = getsize();
					commailcnt = getMailsize();
				} catch (Exception e) {
					dataList = null;
				}

				return dataList;
			}

			@Override
			protected void onPostExecute(List<Map<String, String>> dataList) {
				
				completenum.setText(""+comcnt);
				completemailnum.setText(""+commailcnt);
				
				super.onPostExecute(dataList);
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}
		}.execute();
	}
}

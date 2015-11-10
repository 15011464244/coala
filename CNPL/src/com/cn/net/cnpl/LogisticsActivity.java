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

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.db.dao.MailHandDao;
import com.cn.net.cnpl.db.dao.MailHandDetailDao;
import com.cn.net.cnpl.tools.BaseActivity;
import com.cn.net.cnpl.tools.MyCode;

public class LogisticsActivity extends BaseActivity {
	//按钮
	private Button codeButton = null;
	private Button conbuttonok = null;
	private Button conbuttonn = null;
	//private Button concomplete = null;
//	private Button conclean = null;
	
	private TextView incount=null;
	private TextView outcount=null;
	private TextView ncount=null;
	private TextView dlvcount=null;
	
	private MailHandDao mailhanddao = null;
	private MailHandDetailDao mailhanddetaildao = null;
	
	private int incnt=0;
	private int outcnt=0;
	private int dlvcnt=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.logistic);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bg_new);
		
		incount = (TextView) findViewById(R.id.incount);
		outcount = (TextView) findViewById(R.id.outcount);
		ncount = (TextView) findViewById(R.id.ncount);
		dlvcount = (TextView) findViewById(R.id.dlvcount);
		
		conbuttonok = (Button) findViewById(R.id.conbuttonok);
		conbuttonok.setOnClickListener(onClickListener);
		conbuttonn = (Button) findViewById(R.id.conbuttonn);
		conbuttonn.setOnClickListener(onClickListener);
		//concomplete = (Button) findViewById(R.id.concomplete);
		//concomplete.setOnClickListener(onClickListener);
//		conclean = (Button) findViewById(R.id.conclean);
//		conclean.setOnClickListener(onClickListener);

		//邮件上传
		Intent mailserviceIntent = new Intent(this.getApplicationContext(),
				PlUploadService.class);
		startService(mailserviceIntent);
		
		codeButton = (Button) findViewById(R.id.btncode);
		codeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyCode.Builder builder = new MyCode.Builder(
						LogisticsActivity.this);
				builder.setTitle("二维码:");
				builder.setPositiveButton("", null);
				builder.create().show();
			}

		});
		
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent mainIntent = null;
			switch (v.getId()) {
			case R.id.conbuttonok://邮件接入
				mainIntent = new Intent(LogisticsActivity.this,
						MailConnect.class);
				startActivity(mainIntent);
				break;
			case R.id.conbuttonn://邮件转出
				mainIntent = new Intent(LogisticsActivity.this,
						MailOut.class);
				startActivity(mainIntent);
				break;/*
			case R.id.concomplete://换班交接
				mainIntent = new Intent(LogisticsActivity.this,
						MailChange.class);
				startActivity(mainIntent);
				break;*/
//			case R.id.conclean://日志查询
//				mainIntent = new Intent(LogisticsActivity.this,
//						MailQuery.class);
//				startActivity(mainIntent);
//				break;
			}
		}

	};
	
	public int getsize(String in,String com){
		int size=0;
		int count = 0;
		List<Map<String, Object>> rList = null;
		mailhanddao = DaoFactory.getInstance().getMailHandDao(LogisticsActivity.this);
		mailhanddetaildao = DaoFactory.getInstance().getMailHandDetailDao(LogisticsActivity.this);
		rList = mailhanddao.FindMail(getlogName(),in,com,-1);
		size = rList.size();
		for(int i=0;i<size;i++){
			count = count + Integer.parseInt(mailhanddetaildao.Findcount(rList.get(i).get("sid").toString(), ""));
		}
		
		return count;
	}
	
	public int getsizeDlv(String in,String com,String is_out){
		int size=0;
		int count = 0;
		List<Map<String, Object>> rList = null;
		mailhanddao = DaoFactory.getInstance().getMailHandDao(LogisticsActivity.this);
		mailhanddetaildao = DaoFactory.getInstance().getMailHandDetailDao(LogisticsActivity.this);
		rList = mailhanddao.FindMail(getlogName(),in,com,-1);
		size = rList.size();
		for(int i=0;i<size;i++){
			count = count + Integer.parseInt(mailhanddetaildao.FindcountDlv(rList.get(i).get("sid").toString(),is_out ));
		}
		
		return count;
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
					incnt = getsize(Global.HANDIN,Global.MAILCOM);
					outcnt = getsize(Global.HANDOUT,Global.MAILCOM);
					dlvcnt = getsizeDlv(Global.HANDIN,Global.MAILCOM,Global.MAILDlV);
				} catch (Exception e) {
					dataList = null;
				}

				return dataList;
			}

			@Override
			protected void onPostExecute(List<Map<String, String>> dataList) {
				// 初始化成0
				incount.setText("0");
				outcount.setText("0");
				ncount.setText("0");
				dlvcount.setText("0");
				
				incount.setText(""+incnt);
				outcount.setText(""+outcnt);
				dlvcount.setText(""+dlvcnt);
				int cnt = incnt-outcnt-dlvcnt;
				ncount.setText(""+cnt);
				

				super.onPostExecute(dataList);
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}
		}.execute();
	}
}

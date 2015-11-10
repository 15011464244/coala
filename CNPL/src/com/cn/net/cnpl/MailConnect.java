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

public class MailConnect extends BaseActivity {
	//开始接入
	private Button codeButton = null;
	private Button conButtonOk = null;
	
	private Button conbuttonn = null;
	private Button concomplete = null;
	private Button conclean = null;
	
	private TextView exchangenum=null;
	private TextView completenum=null;
	private TextView cleannum=null;
	private TextView exchangemailnum=null;
	private TextView completemailnum=null;
	private TextView cleanmailnum=null;
	
	
	//private EnterDao enterdao = null;
	private MailHandDao mailhanddao = null;
	private MailHandDetailDao mailhanddetaildao = null;
	
	private int excnt=0;
	private int comcnt=0;
	private int cancnt=0;
	private int exmailcnt=0;
	private int commailcnt=0;
	private int canmailcnt=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.mail_connect);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bg_new);
		
		conButtonOk = (Button) findViewById(R.id.conbuttonok);
		conButtonOk.setOnClickListener(onClickListener);
		conbuttonn = (Button) findViewById(R.id.conbuttonn);
		conbuttonn.setOnClickListener(onClickListener);
		concomplete = (Button) findViewById(R.id.concomplete);
		concomplete.setOnClickListener(onClickListener);
		conclean = (Button) findViewById(R.id.conclean);
		conclean.setOnClickListener(onClickListener);
		
		exchangenum= (TextView)findViewById(R.id.exchangenum);
		completenum= (TextView)findViewById(R.id.completenum);
		cleannum= (TextView)findViewById(R.id.cleannum);
		exchangemailnum= (TextView)findViewById(R.id.exchangemailnum);
		completemailnum= (TextView)findViewById(R.id.completemailnum);
		cleanmailnum= (TextView)findViewById(R.id.cleanmailnum);
		
		codeButton = (Button) findViewById(R.id.btncode);
		codeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyCode.Builder builder = new MyCode.Builder(
						MailConnect.this);
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
				intent.setClass(MailConnect.this, BeginConnect.class);
				startActivity(intent);
				break;
			case R.id.conbuttonn://交接中
				if(excnt==0)
				{
					Toast.makeText(MailConnect.this, getString(R.string.no_exe), 1000).show();
				}else{
					intent.setClass(MailConnect.this, MailConnectList.class);
					startActivity(intent);
				}
				break;
			case R.id.concomplete://完成
				if(comcnt==0)
				{
					Toast.makeText(MailConnect.this, getString(R.string.no_jieru), 1000).show();
				}else{
					intent.setClass(MailConnect.this, MailConnectSuc.class);
					startActivity(intent);
				}
				break;
			case R.id.conclean://撤销
				if(cancnt==0)
				{
					Toast.makeText(MailConnect.this, getString(R.string.no_can), 1000).show();
				}else{
					intent.setClass(MailConnect.this, MailConnectCancel.class);
					startActivity(intent);
				}
				break;
			}
		}

	};
	public int getsize(String in,String com){
		int size=0;
		List<Map<String, Object>> rList = null;
		mailhanddao = DaoFactory.getInstance().getMailHandDao(MailConnect.this);
		rList = mailhanddao.FindMail(getlogName(),in,com,-1);
		size = rList.size();
		
		return size;
	}
	
	public int getMailsize(String in,String com){
		int size=0;
		int cnt = 0;
		List<Map<String, Object>> rList = null;
		mailhanddetaildao = DaoFactory.getInstance().getMailHandDetailDao(MailConnect.this);
		mailhanddao = DaoFactory.getInstance().getMailHandDao(MailConnect.this);
		rList = mailhanddao.FindMail(getlogName(),in,com,-1);
		size = rList.size();
		for(int i=0;i<size;i++){
			cnt = cnt + Integer.parseInt(mailhanddetaildao.FindcountMail(rList.get(i).get("sid").toString()));
		}
		
		return cnt;
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
					excnt = getsize(Global.HANDIN,Global.MAILEX);
					comcnt = getsize(Global.HANDIN,Global.MAILCOM);
					cancnt = getsize(Global.HANDIN,Global.MAILCANCEL);
					
					exmailcnt = getMailsize(Global.HANDIN,Global.MAILEX);
					commailcnt = getMailsize(Global.HANDIN,Global.MAILCOM);
					canmailcnt = getMailsize(Global.HANDIN,Global.MAILCANCEL);
				} catch (Exception e) {
					dataList = null;
				}

				return dataList;
			}

			@Override
			protected void onPostExecute(List<Map<String, String>> dataList) {
				// 初始化成0
				exchangenum.setText("0");
				completenum.setText("0");
				cleannum.setText("0");
				exchangemailnum.setText("0");
				completemailnum.setText("0");
				cleanmailnum.setText("0");
				
				exchangenum.setText(""+excnt);
				completenum.setText(""+comcnt);
				cleannum.setText(""+cancnt);
				exchangemailnum.setText(""+exmailcnt);
				completemailnum.setText(""+commailcnt);
				cleanmailnum.setText(""+canmailcnt);
				

				super.onPostExecute(dataList);
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}
		}.execute();
	}
}

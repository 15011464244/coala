package com.cn.net.cnpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.db.dao.MailHandDetailDao;
import com.cn.net.cnpl.tools.BaseActivity;
import com.cn.net.cnpl.tools.BaseCommand;
import com.cn.net.cnpl.tools.MyCode;
import com.cn.net.cnpl.tools.MySimpleAdapter;

public class Mail_CompDetail extends BaseActivity {

	private Button codeButton = null;
	
	private ListView listView = null;
	private SimpleAdapter adapter = null;
	private ArrayList<Map<String, String>> sourceList = null;

	private TextView mailcon_out_no = null;
	private TextView codetype_txt= null;
	private TextView exchange_time_txt = null;
	private TextView mailtotal_txt = null;
	private TextView disrepair_no_txt = null;
	private TextView lose_no_txt = null;
	private TextView upload_txt = null;
	private TextView no_upload_txt = null;
	
	private ImageView imgview = null;

	private String out_code = "";
	private String codetype = "";
	private String begin_time = "";
	private String total_txt = "";
	private String disrepair_txt = "";
	private String lose_txt = "";
	private String upload = "";
	private String no_upload ="";
	private String sid_time ="";
	
	private MailHandDetailDao mailhanddetaildao=null;
	//private EnterDao enterdao = null;
	public ProgressDialog myDialog = null;
	private int pageNo = 1;
	private boolean loadOver = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		try{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.con_suc_dt);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bg_new);

		// 获取数据
		Intent intent = getIntent();
		out_code = intent.getStringExtra("out_code");
		codetype = intent.getStringExtra("codetype");
		sid_time = intent.getStringExtra("sid_time");
		begin_time = intent.getStringExtra("begin_time");
		total_txt = intent.getStringExtra("total_txt");
		disrepair_txt = intent.getStringExtra("disrepair_txt");
		lose_txt = intent.getStringExtra("lose_txt");
		upload = intent.getStringExtra("upload_txt");
		no_upload = intent.getStringExtra("no_upload_txt");
		
		codeButton = (Button) findViewById(R.id.btncode);
		codeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyCode.Builder builder = new MyCode.Builder(
						Mail_CompDetail.this);
				builder.setTitle("二维码:");
				builder.setPositiveButton("", null);
				builder.create().show();
			}

		});
		imgview = (ImageView)findViewById(R.id.imageViewcamera);
		Bitmap bmp = null;
		String temp="";
		JSONObject object = new JSONObject();
		object.put("outCode", out_code);
		object.put("inCode", getorgCode());
		object.put("total", total_txt);
		temp= object.toString();
		if (temp != null && !"".equals(temp)) {
			temp = BaseCommand.encodeStr(temp);
			bmp = BaseCommand.CreateTwoDCode(temp);
			imgview.setImageBitmap((Bitmap) bmp);
		}

		mailcon_out_no = (TextView) findViewById(R.id.mailcon_out_no);
		mailcon_out_no.setText(out_code);
		codetype_txt = (TextView) findViewById(R.id.codetype);
		codetype_txt.setText(codetype);
		exchange_time_txt = (TextView) findViewById(R.id.exchange_time_txt);
		exchange_time_txt.setText(begin_time);
		mailtotal_txt= (TextView) findViewById(R.id.mailtotal_txt);
		mailtotal_txt.setText(total_txt);
		disrepair_no_txt= (TextView) findViewById(R.id.disrepair_no_txt);
		disrepair_no_txt.setText(disrepair_txt);
		lose_no_txt= (TextView) findViewById(R.id.lose_no_txt);
		lose_no_txt.setText(lose_txt);
		
		upload_txt= (TextView) findViewById(R.id.upload_txt);
		upload_txt.setText(upload);
		no_upload_txt= (TextView) findViewById(R.id.no_upload_txt);
		no_upload_txt.setText(no_upload);
		
		sourceList = new ArrayList<Map<String, String>>();
		listView = (ListView) findViewById(R.id.comListView);

		adapter = new MySimpleAdapter(this, sourceList, R.layout.con_suc_dtitem,
				new String[] { "num", "mailno_txt", "time_txt", "type_txt",
						"isup_txt", "outcode_txt" }, new int[] { R.id.num,
						R.id.mailno_txt, R.id.time_txt, R.id.type_txt,
						R.id.isup_txt, R.id.outcode_txt });

		listView.setAdapter(adapter);
		// 向下滑动
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				// 判断listview是否停止滑动并且处于底部
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& view.getLastVisiblePosition() == view.getCount() - 1) {
					// loadBool = false; // 用布尔作为开关，防止在加载数据时，出现多次启动线程加载数据 数据加载
					if (loadOver) {
						myDialog = ProgressDialog.show(Mail_CompDetail.this,
								 Global.DIALOG_NAME, getString(R.string.loading), true, true);

						new Thread() {
							public void run() {
								try {
									loadData();

								} catch (Exception e) {
									// e.printStackTrace();
								} finally {
									messageListener
											.sendEmptyMessage(Global.TASK_LOOP_COMPLETE);
								}
							}
						}.start();
					}

				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
		loadData();
		}catch(Exception e){
			
		}
	}

	// 显示信息
	private void loadData() {
		try {
			List<Map<String, Object>> rList = null;
			mailhanddetaildao = DaoFactory.getInstance()
					.getMailHandDetailDao(Mail_CompDetail.this);
			rList = mailhanddetaildao.FindMail(sid_time,"",pageNo);

			if (rList == null || rList.size() == 0) {
				loadOver = false;
				return;
			}
			int tempSize = rList.size();
			String str="";
			for (int i = 0; i < tempSize; i++) {
				Map<String, String> tempHashMap = new LinkedHashMap<String, String>();
				tempHashMap.put("num", "" + ((pageNo - 1) * 10 + 1 + i));
				tempHashMap.put("mailno_txt", rList.get(i).get("mail_num")
						.toString());
				tempHashMap.put("time_txt", rList.get(i).get("create_time")
						.toString());
				
				if (Global.MAILDISREPAIR.equals(rList.get(i).get("mail_type")))
					str = getString(R.string.disrepair);
				else if (Global.MAILLOSE.equals(rList.get(i).get("mail_type")))
					str = getString(R.string.lose);
				else if (Global.MAILINTACT.equals(rList.get(i).get("mail_type")))
					str = getString(R.string.comp);
				
				tempHashMap.put("type_txt", str);
				if("".equals(rList.get(i).get("upload_time")))
					tempHashMap.put("isup_txt", getString(R.string.no_upload));
				else
					tempHashMap.put("isup_txt", getString(R.string.upload));
				tempHashMap.put("outcode_txt", out_code);

				sourceList.add(tempHashMap);
			}

			pageNo++;
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
}

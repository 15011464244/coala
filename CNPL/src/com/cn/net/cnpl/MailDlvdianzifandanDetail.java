package com.cn.net.cnpl;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.db.dao.DlvStateDao;
import com.cn.net.cnpl.db.dao.MailDao;
import com.cn.net.cnpl.db.dao.MailUploadDao;
import com.cn.net.cnpl.tools.BASE64Decoder;
import com.cn.net.cnpl.tools.BaseActivity;

public class MailDlvdianzifandanDetail extends BaseActivity {

	// 邮件号
	private TextView mail_no = null;
	//时间
	private TextView time = null;
	// 图片
	private ImageView img = null;
	private Button top_back;
	private TextView title;
	private Map<String, Object> map = null;
	private MailDao maildao = null;
	DlvStateDao dlvdao = null;
	private List<Map<String, String>> list = null;
	String mailno = "";
	String create_time = "";
	
	MailUploadDao mailUploaddao = null;
	
	// 保存值
	private String is_upload = "";
	private String signatureImg = "";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.mail_dlventer_yimg_detail_new);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bg_new);
		initTitle();

		// 邮件号
		mail_no = (TextView) findViewById(R.id.send_name_mail);
		
		time = (TextView) findViewById(R.id.dtname);
		
		Intent intent = getIntent();
		mailno = intent.getStringExtra("mailno");
		is_upload = intent.getStringExtra("isupload");
		create_time = intent.getStringExtra("create_time");
		mail_no.setText(mailno);
		
		try{
			SimpleDateFormat f=new SimpleDateFormat("yyyyMMddkkmmss");
			create_time = DateFormat.format("yyyy-MM-dd kk:mm:ss", f.parse(create_time)).toString();
		}catch(Exception e){
			
		}
		time.setText(create_time);
		
		showData(mailno, is_upload);
		
	}
 
	private void initTitle() {
		// TODO Auto-generated method stub
		top_back = (Button) findViewById(R.id.top_left);
		top_back.setVisibility(View.VISIBLE);
		top_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MailDlvdianzifandanDetail.this.finish();
			}
		});
		title = (TextView) findViewById(R.id.new_name);
		title.setText("电子返单");
	}

	// 显示上传，未上传，保存信息
	private void showData(String mailno, String upload) {
		try {
			mailUploaddao = DaoFactory.getInstance().getMailUploadDao(
					MailDlvdianzifandanDetail.this);
			map = mailUploaddao.FindMail(getlogName(),mailno,upload);
			if(map != null && map.size()>0){
			// 图片
			img = (ImageView) findViewById(R.id.dtimageView);
			
			BASE64Decoder base64decoder = new BASE64Decoder();
			byte[] imgs = base64decoder.decodeBuffer(map.get("signatureImg").toString());
			Bitmap img1=BitmapFactory.decodeByteArray(imgs, 0, imgs.length);
			if (imgs.length!=0 && imgs!=null) {
				img.setImageBitmap(img1);
			}
			
			signatureImg = map.get("signatureImg").toString();
			
			}else{
				Intent intent = new Intent();
				setResult(2, intent);
				finish();
				Toast.makeText(this, getString(R.string.uploaded), 1000).show();
			}
		} catch (Exception e) {
		}
	}

	
}

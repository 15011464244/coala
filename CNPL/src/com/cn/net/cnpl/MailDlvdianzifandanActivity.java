package com.cn.net.cnpl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.db.dao.MailDao;
import com.cn.net.cnpl.db.dao.MailUploadDao;
import com.cn.net.cnpl.model.User;
import com.cn.net.cnpl.tools.BASE64Encoder;
import com.cn.net.cnpl.tools.BaseActivity;
import com.cn.net.cnpl.tools.BaseCommand;
import com.cn.net.cnpl.tools.MyCode;
import com.cn.net.cnpl.tools.MyDialog;
import com.google.zxing.client.android.CaptureActivity;

public class MailDlvdianzifandanActivity extends BaseActivity {

	private Button codeButton = null;
	private Button top_back;
	private TextView title;
	// 扫描或输入获取邮件号值
	private EditText mailEdit;

	// 拍照
	private Button imgbutton = null;
	ImageView imgview = null;
	// 上传
	private Button saveButton = null;
	MailUploadDao mailUploaddao = null;
	List<Map<String, String>> maildaoList = null;

	private static final int PHOTO_GRAPH = 2;// 拍照
	private static final int PHOTO_RESOULT = 3;// 结果
//	private static final Uri imageUri = Uri.parse("file:///sdcard/temp.jpg");
	private static final Uri imageUri = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "temp.jpg");
	private static final Uri uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
	ProgressDialog myDialog = null;

	// 保存值
	private String signatureImg = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.mail_dlventer_img_new);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bg_new);
		initTitle();
		// **邮件号UI
		// 请输入或扫描邮件号
		mailEdit = (EditText) findViewById(R.id.dlvmailidserch_dianzifandan);
		mailEdit.setHint(getString(R.string.scan_hint).toString());

		// 扫描按钮
		Button camera = (Button) findViewById(R.id.dlvcamera_dianzifandan);
		camera.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MailDlvdianzifandanActivity.this,
						CaptureActivity.class);
				startActivityForResult(intent, 1);
			}
		});
		/*
		codeButton = (Button) findViewById(R.id.btncode);
		codeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MyCode.Builder builder = new MyCode.Builder(
						MailDlvdianzifandanActivity.this);
				builder.setTitle("二维码:");
				builder.setPositiveButton("", null);
				builder.create().show();
			}

		});
		*/
		// **拍照
		imgbutton = (Button) findViewById(R.id.imgbutton_dianzifandan);
		imgview = (ImageView) findViewById(R.id.imageViewcamera_dianzifandan);
		imgbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, PHOTO_GRAPH);
			}

		});

		// **上传
		saveButton = (Button) findViewById(R.id.dlvEnsave_dianzifandan);
		saveButton.setText(getString(R.string.save).toString());
		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveData();
			}

		});

		// 删除过时数据
		mailUploaddao = DaoFactory.getInstance().getMailUploadDao(
				MailDlvdianzifandanActivity.this);
		mailUploaddao.deleteDisableMail();

	}

	private void initTitle() {
		// TODO Auto-generated method stub
		top_back = (Button) findViewById(R.id.top_left);
		top_back.setVisibility(View.VISIBLE);
		top_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MailDlvdianzifandanActivity.this.finish();
			}
		});
		title = (TextView) findViewById(R.id.new_name);
		title.setText("电子返单");
	}

	private void saveData() {
		// 判断是否有数据
		if (signatureImg == null && "".equals(mailEdit.getText().toString())) {
			Toast.makeText(MailDlvdianzifandanActivity.this,
					getString(R.string.save_dianzifandan_check), 1000).show();
			return;
		}
		String mail = mailEdit.getText().toString();
		mail = mail.toUpperCase();
		if ( "".equals(mail))//str.length() != 13 ||
			Toast.makeText(MailDlvdianzifandanActivity.this,
					getString(R.string.mailnocuowu), 1000).show();
		else{
		mailUploaddao = DaoFactory.getInstance().getMailUploadDao(
				MailDlvdianzifandanActivity.this);
		String data = mailUploaddao.Findcount(getlogName(), mail);
		if("".equals(data)){
		MyDialog.Builder builder = new MyDialog.Builder(
				MailDlvdianzifandanActivity.this);
		builder.setTitle(getString(R.string.hint));
		builder.setMessage(getString(R.string.shifousave));
		builder.setPositiveButton("", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (saveEnter()) {// 保存成功返回原来页面
					Intent serviceIntent = new Intent(
							MailDlvdianzifandanActivity.this
									.getApplicationContext(),
							MailImgUploadService.class);
					startService(serviceIntent);
					setResult(1, serviceIntent);
					finish();
					Toast.makeText(MailDlvdianzifandanActivity.this,
							getString(R.string.save_dianzifandan_success), 1000)
							.show();
				} else {
					Toast.makeText(MailDlvdianzifandanActivity.this,
							getString(R.string.save_dianzifandan_fail), 1000)
							.show();
				}
			}
		});
		builder.setNegativeButton("", null);
		builder.create().show();
		}else{
			try{
				SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmSS");
				String dataStr = ""
						+ DateFormat.format("yyyy-MM-dd", f.parse(data));
				String message = getString(R.string.issave1) + dataStr
						+ getString(R.string.issave3);
				
				MyDialog.Builder builder = new MyDialog.Builder(
						MailDlvdianzifandanActivity.this);
				builder.setTitle(getString(R.string.hint));
				builder.setMessage(message);
				builder.setPositiveButton("", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (saveEnter()) {// 保存成功返回原来页面
							Intent serviceIntent = new Intent(
									MailDlvdianzifandanActivity.this
											.getApplicationContext(),
									MailImgUploadService.class);
							startService(serviceIntent);
							setResult(1, serviceIntent);
							finish();
							Toast.makeText(MailDlvdianzifandanActivity.this,
									getString(R.string.save_dianzifandan_success), 1000)
									.show();
						} else {
							Toast.makeText(MailDlvdianzifandanActivity.this,
									getString(R.string.save_dianzifandan_fail), 1000)
									.show();
						}
					}
				});
				builder.setNegativeButton("", null);
				builder.create().show();
				}catch(Exception e){
					
				}
		}
		}
	}

	// 通过扫描后得到邮件号
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == 1) {
			// 获取跳转来源
			Bundle bundle = intent.getExtras();
			String strDnNumber = bundle.getString("txtResult");
			if (strDnNumber != null && strDnNumber.length() > 0)
				mailEdit.setText(strDnNumber);
		}

		// 拍照
		if (requestCode == PHOTO_GRAPH) { // 设置文件保存路径
			cropImageUri(imageUri);
		}
		// 处理结果
//		if (requestCode == PHOTO_RESOULT && resultCode != 0) {
			if (requestCode == PHOTO_RESOULT ) {
			if (uritempFile != null) {
				Bitmap photo;
				try {
//					photo = intent.getExtras().getParcelable("data");
					photo = BitmapFactory.decodeStream(getContentResolver()
							.openInputStream(uritempFile));
					byte[] imgs = BaseCommand.compressImage(photo);
					imgview.setImageBitmap(BaseCommand.Bytes2Bimap(imgs));
					// 压缩
					BASE64Encoder base64Encoder = new BASE64Encoder();
					signatureImg = base64Encoder.encode(imgs);
					// 删除照片 TODO
					deleteLatestPhoto();
				} catch (Exception e) {

				}
			}
		}

	}

	private void cropImageUri(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 2);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 600);
		intent.putExtra("outputY", 300);
		intent.putExtra("scale", true);
	    
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", false); // no face detection
		startActivityForResult(intent, PHOTO_RESOULT);
	}
	//删除图片
	private void deleteLatestPhoto() {
		String[] projection = new String[] {
				MediaStore.Images.ImageColumns._ID,
				MediaStore.Images.ImageColumns.DATE_TAKEN };
		Cursor cursor = managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
				null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
		if (cursor != null) {
			if(cursor.moveToFirst()){
				ContentResolver cr = this.getContentResolver();
				cr.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					BaseColumns._ID + "=" + cursor.getString(0), null);
			}
		}
	}

	// 保存数据
	private boolean saveEnter() {
		boolean isSave = false;
		try {
			mailUploaddao = DaoFactory.getInstance().getMailUploadDao(
					MailDlvdianzifandanActivity.this);
			Map<String, Object> map = mailUploaddao.FindMail(getlogName(),mailEdit.getText().toString(),Global.UNUPLOAD);
			
			if(map!=null&&map.size()>0){
				mailUploaddao.deleteMailRe(mailEdit.getText().toString(), getlogName(), Global.UNUPLOAD);
			}
			
			JSONObject params = new JSONObject();

			if (!"".equals(getorgCode()) && !"".equals(getlogName())) {
				params.put("mail", mailEdit.getText());// 邮件号
				params.put("signatureImg", signatureImg);
				User user = User.FindUser(this.getApplicationContext());
				params.put("userCode", user.getLoginName());
				params.put("orgCode", user.getTransitCode());
				isSave = mailUploaddao.SaveMail(params);
			}
		} catch (Exception e) {
			Log.e("aaaaaaaa", e.getMessage());

		}
		return isSave;
	}
}

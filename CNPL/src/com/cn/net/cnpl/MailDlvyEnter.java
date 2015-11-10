package com.cn.net.cnpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Context;
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
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.db.dao.DlvStateDao;
import com.cn.net.cnpl.db.dao.MailDao;
import com.cn.net.cnpl.db.dao.MailHandDetailDao;
import com.cn.net.cnpl.db.dao.ResOrgDao;
import com.cn.net.cnpl.db.dao.TransferDao;
import com.cn.net.cnpl.model.User;
import com.cn.net.cnpl.tools.BASE64Encoder;
import com.cn.net.cnpl.tools.BaiduGps;
import com.cn.net.cnpl.tools.BaseActivity;
import com.cn.net.cnpl.tools.BaseCommand;
import com.cn.net.cnpl.tools.MyCode;
import com.cn.net.cnpl.tools.MyDialog;
import com.cn.net.cnpl.tools.MySpinnerAdapter;
import com.google.zxing.client.android.CaptureActivity;

public class MailDlvyEnter extends BaseActivity {
	private TextView title;
	private Button back;
	private Button codeButton = null;
	// 签收类型
	private Spinner spinner;
	private List<Map<String, String>> typeList = null;
	private ArrayList<String> arraylist = null;

	// 扫描或输入获取邮件号值
	private EditText mailEdit;

	// 邮件类型
	private RadioGroup rdiaogroup = null;
	private RadioButton mailTypeButton1 = null;
	private RadioButton mailTypeButton2 = null;
	// 设置国际标识 国内国际是1，0
	String interFlag = "1";

	// 签收类型
	private DlvStateDao dlvstatedao = null;
	// 签收人
	private EditText revNameedit = null;
	// 收款类型
	private CheckBox substitute_fee;
	private CheckBox receive_fee;

	// 收款金额
	String spinerfee = "";
	private EditText feeedit = null;
	double fee = 0.0;
	private double actualGoodsFee = 0.0;// 实收货款
	private double actualFee = 0.0;// 实收资费
	private double actualTax = 0.0;// 实收税款
	private double otherFee = 0.0;// 其他费用
	// 拍照
	private ImageView imgbutton = null;
	ImageView imgview = null;
	// 保存按钮
	private Button saveButton = null;
	MailDao maildao = null;
	List<Map<String, String>> maildaoList = null;
	// 数值
	private String signStsCode = "";
	private String signStsStr = "";
	private String dlvOrgPostCode = "";
	private String dlvOrgName = "";
	private File fullFilename;
	private MailHandDetailDao mailhanddetailDao = null;

	// 获取地址
	private BaiduGps baidugps = null;

	private static final int PHOTO_GRAPH = 2;// 拍照
	private static final int PHOTO_RESOULT = 3;// 结果
//	private static final String IMAGE_FILE_LOCATION = "file:///sdcard/temp.jpg";// temp
	private static final String IMAGE_FILE_LOCATION = "file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "temp.jpg";// temp
	private static final Uri uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");																			// file
	private static final Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);// The
																		// Uri
																		// to
																		// store
																		// the
																		// big
																		// bitmap

	// //deviceNumber
	// final TelephonyManager Tm =
	// (TelephonyManager)getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

	// 保存值
	private String is_upload = "0", role = "1", undlvCauseCode = "",
			undlvNextModeCode = "", dlvAddress = "";
	private String signatureImg = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.mail_dlventer_new);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.top_bg_new);
		initTitle();
		File dir = new File(Environment.getExternalStorageDirectory() + "/CNPL");
		if (!dir.exists()) {
			dir.mkdir();
		}
		fullFilename = new File(Environment.getExternalStorageDirectory()
				+ "/CNPL/" + Global.MAILDLV);
		if (!fullFilename.exists()) {
			try {
				fullFilename.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// 地址
		baidugps = BaiduGps.getInstance(this.getApplicationContext());
		baidugps.getLocation();
		// **邮件号UI
		// 请输入或扫描邮件号
		mailEdit = (EditText) findViewById(R.id.dlvmailidserch);
		// 扫描按钮
		ImageView camera = (ImageView) findViewById(R.id.dlvcamera);
		camera.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MailDlvyEnter.this,
						CaptureActivity.class);
				startActivityForResult(intent, 1);
			}
		});
		/*
		 * codeButton = (Button) findViewById(R.id.btncode);
		 * codeButton.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { MyCode.Builder builder = new
		 * MyCode.Builder( MailDlvyEnter.this); builder.setTitle("二维码:");
		 * builder.setPositiveButton("", null); builder.create().show(); }
		 * 
		 * });
		 */
		// ** 签收类型
		spinner = (Spinner) findViewById(R.id.arrType);

		dlvstatedao = DaoFactory.getInstance().getDlvStateDao(
				MailDlvyEnter.this);
		typeList = dlvstatedao.FindDlvStateBystateType(Global.DLVCODE);
		for (Map<String, String> map : typeList) {
			String xk = map.get("stateDescCHS");
			if (getResources().getConfiguration().locale.getCountry().equals(
					"CN")) {
				xk = map.get("stateDescCHS");
			} else if (getResources().getConfiguration().locale.getCountry()
					.equals("TW")) {
				xk = map.get("stateDescTRADITIONAL");
			} else if (getResources().getConfiguration().locale.getCountry()
					.equals("UK")
					|| getResources().getConfiguration().locale.getCountry()
							.equals("US")) {
				xk = map.get("stateDescENG");
			}
			if (arraylist == null)
				arraylist = new ArrayList<String>();
			arraylist.add(xk);
		}

		MySpinnerAdapter<String> typeadapter = new MySpinnerAdapter<String>(
				this, R.layout.my_spinner, R.id.name_text, arraylist);
		typeadapter.setDropDownViewResource(R.layout.my_spinner_drpdown_item);
		spinner.setAdapter(typeadapter);
		spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				signStsCode = typeList.get(arg2).get("stateCode");
				signStsStr = arraylist.get(arg2).toString();
				if (arg2 == 1)
					revNameedit.setText(getString(R.string.comp_text)
							.toString());
				else if (arg2 == 2)
					revNameedit.setText("");
				else {
					User user = User.FindUser(MailDlvyEnter.this
							.getApplicationContext());
					TransferDao dao = DaoFactory.getInstance().getTransferDao(
							MailDlvyEnter.this);
					List<Map<String, String>> transfer = dao
							.findTransferByTicketNum(user.getLoginName(),
									mailEdit.getText().toString());
					if (transfer != null && transfer.size() > 0) {
						for (int j = 0; j < transfer.size(); j++) {
							revNameedit.setText(transfer.get(j).get(
									"rcverCntct"));
						}
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

		// **邮件类型
		setMailType();

		// ** 签收人
		revNameedit = (EditText) findViewById(R.id.dlventername);
		revNameedit.setHint(getString(R.string.revname_hint).toString());

		// **收款类型
		setFeeType();

		// **拍照
		imgbutton = (ImageView) findViewById(R.id.imgbutton);
		imgview = (ImageView) findViewById(R.id.imageViewcamera);
		imgbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, PHOTO_GRAPH);
			}

		});

		// **保存按钮

		// 获取数值
		ResOrgDao resOrgDao = DaoFactory.getInstance().getResOrgDao(
				this.getApplicationContext());
		maildaoList = resOrgDao.FindResOrgDaoBystateType();
		for (Map<String, String> map : maildaoList) {
			dlvOrgPostCode = map.get("postcode");
			dlvOrgName = map.get("org_sname");
		}

		saveButton = (Button) findViewById(R.id.dlvEnsave);
		saveButton.setText(getString(R.string.save).toString());
		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveData();
			}

		});

	}

	private void initTitle() {
		// TODO Auto-generated method stub
		title = (TextView) findViewById(R.id.new_name);
		title.setText("妥投录入");
		back = (Button) findViewById(R.id.top_left);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MailDlvyEnter.this.finish();
			}
		});
	}

	private void saveData() {
		String str = mailEdit.getText().toString();
		mailhanddetailDao = DaoFactory.getInstance().getMailHandDetailDao(this);
		List<Map<String, String>> mailNoList = mailhanddetailDao.FindMailIn();
		// boolean isjieru = false;

		// if (mailNoList != null) {
		// for (int i = 0; i < mailNoList.size(); i++) {
		// if (str.equals(mailNoList.get(i).get("mail_num").toString())) {
		// isjieru = true;
		// }
		// }
		// }
		if ("".equals(str)) {// str.length() != 13 ||
			Toast.makeText(MailDlvyEnter.this, getString(R.string.mailnocuowu),
					1000).show();
		} else
		// if (!isjieru) {
		// Toast.makeText(MailDlvyEnter.this, getString(R.string.mailnojieru),
		// 1000).show();
		// } else
		if ((spinerfee.equals(getString(R.string.substitute_fee)) || spinerfee
				.equals(getString(R.string.receive_fee)))
				&& "".equals(feeedit.getText().toString().trim())) {
			Toast.makeText(MailDlvyEnter.this, getString(R.string.feeshikong),
					1000).show();
		} else {
			str = str.toUpperCase();
			// 查询是否录入过
			maildao = DaoFactory.getInstance().getMailDao(MailDlvyEnter.this);
			String data = maildao.Findcount(getlogName(), Global.DLVCODE, str);
			String datare = maildao.Findcount(getlogName(), str);
			if (data != null && !"".equals(data)) {
				try {
					SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmSS");
					String dataStr = ""
							+ DateFormat.format("yyyy-MM-dd", f.parse(data));
					String message = getString(R.string.issave1) + dataStr
							+ getString(R.string.issave2);

					MyDialog.Builder builder = new MyDialog.Builder(
							MailDlvyEnter.this);
					builder.setTitle(getString(R.string.hint));
					builder.setMessage(message);
					builder.setPositiveButton("",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									try {
										fee = Double.parseDouble(feeedit
												.getText().toString().trim());
										if (spinerfee
												.equals(getString(R.string.substitute_fee))) {
											actualGoodsFee = fee;
										} else if (spinerfee
												.equals(getString(R.string.receive_fee)))
											actualFee = fee;
									} catch (NumberFormatException e) {
										Log.e("spiner", "" + 11111);
									}
									if (saveEnter()) {
										// 保存成功返回原来页面
										// mailhanddetailDao.UpdateMail_isout(eqmail,eqsid);
										Intent serviceIntent = new Intent(
												MailDlvyEnter.this
														.getApplicationContext(),
												DlvUploadService.class);
										startService(serviceIntent);
										Intent intent = new Intent();
										setResult(1, intent);
										finish();
									} else {
										Toast.makeText(MailDlvyEnter.this,
												getString(R.string.save_fail),
												1000).show();
									}
								}
							});
					builder.setNegativeButton("", null);
					builder.create().show();
				} catch (Exception e) {

				}
			} else if (datare != null && !"".equals(datare)) {
				try {
					SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmSS");
					String dataStr = ""
							+ DateFormat.format("yyyy-MM-dd", f.parse(datare));
					String message = getString(R.string.issave1) + dataStr
							+ getString(R.string.issave3);

					MyDialog.Builder builder = new MyDialog.Builder(
							MailDlvyEnter.this);
					builder.setTitle(getString(R.string.hint));
					builder.setMessage(message);
					builder.setPositiveButton("",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									try {
										fee = Double.parseDouble(feeedit
												.getText().toString().trim());
										if (spinerfee
												.equals(getString(R.string.substitute_fee))) {
											actualGoodsFee = fee;
										} else if (spinerfee
												.equals(getString(R.string.receive_fee)))
											actualFee = fee;
									} catch (NumberFormatException e) {
										Log.e("spiner", "" + 11111);
									}
									if (saveEnter()) {
										// 保存成功返回原来页面
										// mailhanddetailDao.UpdateMail_isout(eqmail,eqsid);
										Intent serviceIntent = new Intent(
												MailDlvyEnter.this
														.getApplicationContext(),
												DlvUploadService.class);
										startService(serviceIntent);
										Intent intent = new Intent();
										setResult(1, intent);
										finish();
									} else {
										Toast.makeText(MailDlvyEnter.this,
												getString(R.string.save_fail),
												1000).show();
									}
								}
							});
					builder.setNegativeButton("", null);
					builder.create().show();
				} catch (Exception e) {

				}
			} else {
				MyDialog.Builder builder = new MyDialog.Builder(
						MailDlvyEnter.this);
				builder.setTitle(getString(R.string.hint));
				builder.setMessage(getString(R.string.shifousave));
				builder.setPositiveButton("",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								try {
									fee = Double.parseDouble(feeedit.getText()
											.toString().trim());
									if (spinerfee
											.equals(getString(R.string.substitute_fee))) {
										actualGoodsFee = fee;
									} else if (spinerfee
											.equals(getString(R.string.receive_fee)))
										actualFee = fee;
								} catch (NumberFormatException e) {
									Log.e("spiner", "" + 11111);
								}
								if (saveEnter()) {
									// 保存成功返回原来页面
									Intent serviceIntent = new Intent(
											MailDlvyEnter.this
													.getApplicationContext(),
											DlvUploadService.class);
									startService(serviceIntent);
									Intent intent = new Intent();
									setResult(1, intent);
									finish();
								} else {
									Toast.makeText(MailDlvyEnter.this,
											getString(R.string.save_fail), 1000)
											.show();
								}
							}
						});
				builder.setNegativeButton("", null);
				builder.create().show();
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
			if (strDnNumber != null && strDnNumber.length() > 0) {
				mailEdit.setText(strDnNumber);
				User user = User.FindUser(this.getApplicationContext());
				TransferDao dao = DaoFactory.getInstance().getTransferDao(
						MailDlvyEnter.this);
				List<Map<String, String>> transfer = dao
						.findTransferByTicketNum(user.getLoginName(),
								strDnNumber);
				if (transfer != null && transfer.size() > 0) {
					if ("本人收".equals(signStsStr)) {
						for (int j = 0; j < transfer.size(); j++) {
							revNameedit.setText(transfer.get(j).get(
									"rcverCntct"));
						}
					} else if ("单位收发章".equals(signStsStr)) {
						revNameedit.setText(getString(R.string.comp_text)
								.toString());
					}
				}
			}
		}

		// 拍照
		if (requestCode == PHOTO_GRAPH) { // 设置文件保存路径
			cropImageUri(imageUri);
		}
		// 处理结果
		// if (requestCode == PHOTO_RESOULT && resultCode != 0) {
		if (requestCode == PHOTO_RESOULT) {
			if (uritempFile != null) {
				Bitmap photo;
				try {
//					photo = intent.getExtras().getParcelable("data");
//					photo = BitmapFactory.decodeStream(getContentResolver()
//							.openInputStream(imageUri));
					photo = BitmapFactory.decodeStream(getContentResolver()
							.openInputStream(uritempFile));
					byte[] imgs = BaseCommand.compressImage(photo);
					if (imgs.length!=0 && imgs!=null) {
						imgview.setImageBitmap(BaseCommand.Bytes2Bimap(imgs));
					}
					// 压缩
					BASE64Encoder base64Encoder = new BASE64Encoder();
					signatureImg = base64Encoder.encode(imgs);
					// 删除照片 TODO
					deleteLatestPhoto();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	// 删除图片
	private void deleteLatestPhoto() {
		String[] projection = new String[] {
				MediaStore.Images.ImageColumns._ID,
				MediaStore.Images.ImageColumns.DATE_TAKEN };
		Cursor cursor = managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
				null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				ContentResolver cr = this.getContentResolver();
				cr.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						BaseColumns._ID + "=" + cursor.getString(0), null);
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

	// 邮件类型
	private void setMailType() {

		rdiaogroup = (RadioGroup) findViewById(R.id.radioGrouplei);

		mailTypeButton1 = (RadioButton) findViewById(R.id.radioin);
		mailTypeButton1.setText(getString(R.string.internal).toString());

		mailTypeButton2 = (RadioButton) findViewById(R.id.radioout);
		mailTypeButton2.setText(getString(R.string.international).toString());

		rdiaogroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == mailTypeButton1.getId()) {
							interFlag = "1";
						} else if (checkedId == mailTypeButton2.getId()) {
							interFlag = "0";
						}
					}

				});
	}

	// 收款类型
	private void setFeeType() {

		// 收款金额
		feeedit = (EditText) findViewById(R.id.dlventermoney);
		feeedit.setHint(getString(R.string.fee_hint).toString());
		lock();
		substitute_fee = (CheckBox) findViewById(R.id.substitute_fee);
		substitute_fee
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							Unlock();
							substitute_fee.setChecked(true);
							receive_fee.setChecked(false);
							spinerfee = getString(R.string.substitute_fee);
						} else {
							substitute_fee.setChecked(false);
							spinerfee = "";
							if (!receive_fee.isChecked()) {
								feeedit.setText("");
								lock();
							}
						}

					}
				});
		receive_fee = (CheckBox) findViewById(R.id.receive_fee);
		receive_fee.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					Unlock();
					receive_fee.setChecked(true);
					substitute_fee.setChecked(false);
					spinerfee = getString(R.string.receive_fee);
				} else {
					receive_fee.setChecked(false);
					spinerfee = "";
					if (!substitute_fee.isChecked()) {
						feeedit.setText("");
						lock();
					}

				}

			}
		});

	}

	// 让EditText变成可编辑状态
	private void Unlock() {

		feeedit.setFilters(new InputFilter[] { new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {

				return null;
			}
		} });

	}

	// 让EditText变成不可编辑状态
	private void lock() {

		feeedit.setFilters(new InputFilter[] { new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {

				return source.length() < 1 ? dest.subSequence(dstart, dend)
						: "";
			}
		} });

	}

	// 保存数据
	private boolean saveEnter() {
		// is_upload, role, operationMode, dlvStsCode, undlvCauseCode,
		// undlvNextModeCode,dlvAddress, signatureImg
		boolean isSave = false;
		try {
			MailDao maildao = DaoFactory.getInstance().getMailDao(
					this.getApplicationContext());
			Map<String, Object> map = maildao.FindMail(mailEdit.getText()
					.toString(), getlogName(), Global.UNUPLOAD);

			if (map != null && map.size() > 0) {
				maildao.deleteMailRe(mailEdit.getText().toString(),
						getlogName(), Global.UNUPLOAD);
			}

			JSONObject params = new JSONObject();
			BaiduGps baiduGps = BaiduGps.getInstance(this);
			baiduGps.getLocation();

			TelephonyManager telephonemanage = (TelephonyManager) getWindow()
					.getContext().getSystemService(Context.TELEPHONY_SERVICE);
			if (!"".equals(getorgCode()) && !"".equals(getlogName())) {
				dlvAddress = baidugps.getAddress();

				params.put("IS_UPLOAD", is_upload);

				params.put("deviceNumber", telephonemanage.getDeviceId());// Tm.getDeviceId());//手机设备号

				params.put("orgCode", getorgCode());// 机构号
				params.put("userCode", getlogName());// 工号

				params.put("role", role);
				params.put("operationMode", "I");// 保存
				params.put("mailCode", mailEdit.getText());// 邮件号

				params.put("dlvOrgCode", getorgCode());// 机构号
				params.put("dlvOrgName", dlvOrgName);// 机构名称
				params.put("dlvOrgPostCode", StringFormate(dlvOrgPostCode)); // 投递机构邮编

				params.put("dlvStsCode", "I");
				params.put("signStsCode", signStsCode);// 签收情况代码

				params.put("actualGoodsFee", actualGoodsFee);// 实收货款
				params.put("actualTax", actualTax);
				params.put("actualFee", actualFee);// 实收资费
				params.put("otherFee", otherFee);

				params.put("dlvUserCode", getlogName());// 工号
				params.put("dlvUserName", Global.NAME);// 投递员姓名
				params.put("undlvCauseCode", undlvCauseCode);
				params.put("undlvNextModeCode", undlvNextModeCode);
				params.put("undlvCauseDesc", "");// 原因描述
				params.put("undlvfollowCode", "");// 下一步动作
				params.put("signerName", revNameedit.getText());// 签收人姓名
				params.put("interFlag", interFlag);// 国际标识 国内国际是1，0
				params.put("createDate",
						DateFormat.format("yyyyMMddkkmmss", new Date()));
				// 2013-4-22 15:06:08
				params.put("operationTime", "");// 操作时间
				params.put("dlvAddress", dlvAddress);
				params.put("signatureImg", signatureImg);
				if (baiduGps != null && baiduGps.getBdLocation() != null) {
					params.put("longitude", ""
							+ baiduGps.getBdLocation().getLongitude());// 经度
					params.put("latitude", ""
							+ baiduGps.getBdLocation().getLatitude());// 纬度
					params.put("province", ""
							+ baiduGps.getBdLocation().getProvince());
					params.put("city", "" + baiduGps.getBdLocation().getCity());
					params.put("county", ""
							+ baiduGps.getBdLocation().getDistrict());
					params.put("street", ""
							+ baiduGps.getBdLocation().getStreet());
				} else {
					params.put("longitude", "");// 经度
					params.put("latitude", "");// 纬度
					params.put("province", "");
					params.put("city", "");
					params.put("county", "");
					params.put("street", "");
				}
				isSave = maildao.SaveMail(params);
				if (isSave) {
					FileOutputStream fos = new FileOutputStream(fullFilename,
							true);
					String message = "I" + "\t\t"
							+ mailEdit.getText().toString() + "\t\t"
							+ is_upload + "\n";
					fos.write(message.getBytes());
					fos.close();
				}
			}
		} catch (Exception e) {

		}
		return isSave;
	}

}

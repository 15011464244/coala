package com.cn.net.cnpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.db.dao.LoginBandleDao;
import com.cn.net.cnpl.db.dao.MailHandDao;
import com.cn.net.cnpl.db.dao.MailHandDetailDao;
import com.cn.net.cnpl.model.User;
import com.cn.net.cnpl.tools.BaseActivity;
import com.cn.net.cnpl.tools.MyCode;
import com.cn.net.cnpl.tools.MyDialog;
import com.cn.net.cnpl.tools.NetHelper;

public class MailChange extends BaseActivity {

	private Button codeButton = null;
	private Button onok = null;
	private Button offok = null;

	private MailHandDao mailhanddao = null;
	private MailHandDetailDao mailhanddetaildao = null;

	private ProgressDialog myDialog2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.mail_change);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top_bg_new);
		codeButton = (Button) findViewById(R.id.btncode);
		codeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyCode.Builder builder = new MyCode.Builder(
						MailChange.this);
				builder.setTitle("二维码:");
				builder.setPositiveButton("", null);
				builder.create().show();
			}

		});
		onok = (Button) findViewById(R.id.onok);
		onok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mailhanddao = DaoFactory.getInstance().getMailHandDao(
						MailChange.this);
				mailhanddetaildao = DaoFactory.getInstance()
						.getMailHandDetailDao(MailChange.this);
				MyDialog.Builder builder = new MyDialog.Builder(MailChange.this);
				builder.setTitle(getString(R.string.hint));
				builder.setMessage(getString(R.string.shifoushangban));
				builder.setPositiveButton("",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								try {
									dialog.dismiss();
									LoginBandleDao loginBandleDao = DaoFactory
											.getInstance().getLoginBandleDao(
													MailChange.this);
									String device = loginBandleDao
											.FindLoginBandle(getlogName());
									if (device != null) {
										Toast.makeText(MailChange.this,
												getString(R.string.re_on), 1000)
												.show();
									} else {
										myDialog2 = ProgressDialog.show(
												MailChange.this,
												Global.DIALOG_NAME,
												"正在连接服务器...", true, true);
										AsyncUpdate();
									}
								} catch (Exception e) {

								}
							}
						});
				builder.setNegativeButton("", null);
				builder.create().show();
			}

		});
		offok = (Button) findViewById(R.id.offok);
		offok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					boolean ishave = false;
					List<Map<String, Object>> rList = null;
					List<Map<String, Object>> rList1 = null;
					mailhanddao = DaoFactory.getInstance().getMailHandDao(
							MailChange.this);
					mailhanddetaildao = DaoFactory.getInstance()
							.getMailHandDetailDao(MailChange.this);
					rList = mailhanddao.FindMail(getlogName(), "",
							Global.MAILCOM, -1);
					if (rList != null && rList.size() != 0) {
						int tempSize = rList.size();
						for (int i = 0; i < tempSize; i++) {
							rList1 = mailhanddetaildao.FindOff(rList.get(i)
									.get("sid").toString());
							if (rList1 != null && rList1.size() != 0) {
								Toast.makeText(MailChange.this,
										getString(R.string.no_off), 1000)
										.show();
								ishave = true;
							}
						}
					}
					if (!ishave) {
						MyDialog.Builder builder = new MyDialog.Builder(
								MailChange.this);
						builder.setTitle(getString(R.string.hint));
						builder.setMessage(getString(R.string.shifouxiaban));
						builder.setPositiveButton("",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										try {
											dialog.dismiss();
											LoginBandleDao loginBandleDao = DaoFactory
													.getInstance()
													.getLoginBandleDao(
															MailChange.this);
											String device = loginBandleDao
													.FindLoginBandle(getlogName());
											if (device == null) {
												Toast.makeText(
														MailChange.this,
														getString(R.string.off_on),
														1000).show();
											} else {
												myDialog2 = ProgressDialog
														.show(MailChange.this,
																Global.DIALOG_NAME,
																"正在连接服务器...",
																true, true);
												AsyncUpdateoff();

											}
										} catch (Exception e) {

										}
									}
								});
						builder.setNegativeButton("", null);
						builder.create().show();

					}
				} catch (Exception e) {

				}
				/*
				 * List<Map<String, Object>> rList = null; mailhanddao =
				 * DaoFactory.getInstance().getMailHandDao(MailChange.this);
				 * rList =
				 * mailhanddao.FindMail(getlogName(),Global.HANDIN,Global
				 * .MAILCOM,-1); if(rList.size()<=0){
				 * Toast.makeText(MailChange.this,
				 * getString(R.string.no_offmail), 1000).show(); }else{ Intent
				 * intent = new Intent(); intent.setClass(MailChange.this,
				 * MailChangeOffImg.class); startActivity(intent); }
				 */
			}

		});
	}

	public void AsyncUpdate() {
		new AsyncTask<Object, Integer, JSONObject>() {

			private TelephonyManager telephonemanage = (TelephonyManager) getWindow()
					.getContext().getSystemService(Context.TELEPHONY_SERVICE);

			@Override
			protected JSONObject doInBackground(Object... params23) {

				User user = DaoFactory.getInstance()
						.getUserDAO(MailChange.this).FindUser();
				NetHelper helper = new NetHelper();
				StringBuffer params = new StringBuffer();
				helper.Create(Global.BASE_URL + Global.URL_MAIL_ON);
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("id", "9");
					jsonObject.put("deviceNumber",
							telephonemanage.getDeviceId());
					jsonObject.put("orgCode", user.getTransitCode());
					jsonObject.put("userCode", user.getLoginName());
					jsonObject.put("role", "8");
					params.append("header=" + jsonObject.toString());
					JSONObject resultObj = helper.exeRequestForJsonObj(
							params.toString(), Global.BASE_URL
									+ Global.URL_MAIL_ON);
					return resultObj;
				} catch (Exception e) {
				}
				return null;
			}

			@Override
			protected void onPostExecute(JSONObject resultObj) {
				if (myDialog2.isShowing()) {
					myDialog2.dismiss();
				}
				if (resultObj != null) {
					try {
						if ("1".equals(resultObj.get("result").toString())) {
							// 保存设备号和用户关联
							LoginBandleDao loginBandleDao = DaoFactory
									.getInstance().getLoginBandleDao(
											MailChange.this);
							loginBandleDao
									.SaveLoginBandle(
											telephonemanage.getDeviceId(),
											getlogName());
							mailhanddao.deleteMail();
							mailhanddetaildao.deleteMail();
							if (!resultObj.isNull("resultList")) {// 返回有结果
								JSONArray jsonArray = resultObj
										.getJSONArray("resultList");
								for (int i = 0; i < jsonArray.length(); i++) {
									MailHandDao mailHand = DaoFactory
											.getInstance().getMailHandDao(
													MailChange.this);
									long sid = new Date().getTime();
									mailHand.SaveMail(
											jsonArray.getJSONObject(i), sid);
									MailHandDetailDao mailDetail = DaoFactory
											.getInstance()
											.getMailHandDetailDao(
													MailChange.this);
									mailDetail.SaveMail(
											jsonArray.getJSONObject(i),
											sid,
											jsonArray.getJSONObject(i)
													.get("sid").toString());
								}
							}
							Toast.makeText(MailChange.this,
									getString(R.string.ok_on), 1000).show();
						} else {
							if ("1017".equals(resultObj.get("errorCode")
									.toString()))
								Toast.makeText(MailChange.this,
										getString(R.string.re_on), 1000).show();
							else if ("1018".equals(resultObj.get("errorCode")
									.toString()))
								Toast.makeText(MailChange.this,
										getString(R.string.other_on), 1000)
										.show();
							else
								Toast.makeText(MailChange.this,
										getString(R.string.error_on), 1000)
										.show();
						}
					} catch (Exception e) {
					}
				} else {
					Toast.makeText(MailChange.this,
							getString(R.string.error_on), 1000).show();
				}

			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}
		}.execute();

	}

	public void AsyncUpdateoff() {
		new AsyncTask<Object, Integer, JSONObject>() {

			private TelephonyManager telephonemanage = (TelephonyManager) getWindow()
					.getContext().getSystemService(Context.TELEPHONY_SERVICE);

			@Override
			protected JSONObject doInBackground(Object... params23) {

				User user = DaoFactory.getInstance()
						.getUserDAO(MailChange.this).FindUser();
				NetHelper helper = new NetHelper();
				try {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					helper.Create(Global.BASE_URL + Global.URL_MAIL_OFF);
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("id", "8");
					jsonObject.put("deviceNumber", user.getTelephone());
					jsonObject.put("orgCode", user.getTransitCode());
					jsonObject.put("userCode", user.getLoginName());
					jsonObject.put("role", "8");

					params.add(new BasicNameValuePair("header", jsonObject
							.toString()));

					JSONObject resultObj = helper.execute(params);
					return resultObj;
				} catch (Exception e) {
				}
				return null;
			}

			@Override
			protected void onPostExecute(JSONObject resultObj) {
				if (myDialog2.isShowing()) {
					myDialog2.dismiss();
				}
				try {
					if (resultObj != null) {
						if ("1".equals(resultObj.get("result").toString())) {
							// 删除设备和用户关联
							LoginBandleDao loginBandleDao = DaoFactory
									.getInstance().getLoginBandleDao(
											MailChange.this);
							loginBandleDao.DelLoginBandle(getlogName());

							mailhanddao.deleteMail();
							mailhanddetaildao.deleteMail();
							Toast.makeText(MailChange.this,
									getString(R.string.ok_off), 1000).show();
						} else {
							if ("1016".equals(resultObj.get("errorCode")
									.toString()))
								Toast.makeText(MailChange.this,
										getString(R.string.off_on), 1000)
										.show();
							else
								Toast.makeText(MailChange.this,
										getString(R.string.error_off), 1000)
										.show();

						}
					} else {
						Toast.makeText(MailChange.this,
								getString(R.string.error_off), 1000).show();
					}
				} catch (Exception e) {
				}

			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}
		}.execute();

	}

}

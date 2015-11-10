package com.cn.net.cnpl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.text.format.DateFormat;

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.db.dao.MailHandDao;
import com.cn.net.cnpl.db.dao.MailHandDetailDao;
import com.cn.net.cnpl.model.User;
import com.cn.net.cnpl.tools.NetHelper;

public class PlUploadService extends IntentService {

	public PlUploadService() {
		super("PlUploadService");
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	/**
	 * 上传方法 返回true全部上传成功 返回false 有数据没成功
	 * 
	 * @throws Exception
	 */
	public boolean upload(User user) {
		boolean flag=true;
		try {
			MailHandDao dao = DaoFactory.getInstance().getMailHandDao2(this);
			MailHandDetailDao detaildao = DaoFactory.getInstance().getMailHandDetailDao3(this);
			NetHelper helper = new NetHelper();
			
			List<Map<String, Object>>  mapslist = dao.FindMail(Global.getLogin_name(this.getApplicationContext()),"",Global.MAILCOM,-1);
			if (mapslist != null&& mapslist.size()>0) {
				JSONArray datas = new JSONArray();
				for (Map<String, Object> map : mapslist) {
					List<Map<String, Object>> mapslistdt   = detaildao.FindMailByUploadIN(map.get("sid")
							.toString());
					if (mapslistdt != null && mapslistdt.size()>0) {
						
						for (Map<String, Object> mapdt : mapslistdt) {
							JSONObject data = new JSONObject();
							data.put("operationMode",StringFormate(mapdt.get("operatorType")));
							data.put("operationTime",StringFormateli(mapdt.get("create_time")));
							data.put("mailCode",
									StringFormate(mapdt.get("mail_num")));
							data.put("mailSate",
									StringFormate(mapdt.get("mail_type")));
							data.put("principal",
									StringFormate(mapdt.get("principal")));
							data.put("principalType",
									StringFormate(mapdt.get("principal_type")));
							data.put("abnormityTime",
									StringFormateli(mapdt.get("abnormity_time")));
							data.put("uploadTime",
									DateFormat.format("yyyyMMddkkmmss", new Date()).toString());
							data.put("oldSid",StringFormate(mapdt.get("oldSid")));//旧交接批次ID
							data.put("signatureImg",StringFormate(mapdt.get("signatureImg")));
							
							datas.put(data);
						}
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("id", "7");
						jsonObject.put("deviceNumber",
								StringFormate(user.getTelephone()));
						jsonObject.put("orgCode",
								StringFormate(user.getTransitCode()));
						jsonObject.put("userCode",
								StringFormate(user.getLoginName()));
						jsonObject.put("role", "8");
						jsonObject.put("outCode",
								StringFormate(map.get("out_code")));
						jsonObject.put("outType",
								StringFormate(map.get("org_type")));//
						jsonObject.put("inCode",
								StringFormate(map.get("in_code")));
						jsonObject.put("inType",
								StringFormate(map.get("org_type")));//
						jsonObject.put("beginTime",
								StringFormateli(map.get("begin_time")));
						jsonObject.put("endTime",
								StringFormateli(map.get("end_time")));
						// jsonObject.put("certificate",
						// StringFormate(map.get("certificate")));
						jsonObject.put("handType",
								StringFormate(map.get("hand_type")));
						jsonObject.put("longitude",StringFormate( map.get("longitude")));
						jsonObject.put("latitude",StringFormate( map.get("latitude")));
						jsonObject.put("province",  StringFormate(map.get("province")));
						jsonObject.put("city",   StringFormate(map.get("city")));
						jsonObject.put("conty",  StringFormate( map.get("county")));
						jsonObject.put("street",  StringFormate( map.get("street")));
			
						jsonObject.put("actualCount",  StringFormate( map.get("actualCount")));

						jsonObject.put("dataList", datas);
						
						JSONObject resultObj = helper.exeRequestGzip(jsonObject.toString(), Global.BASE_URL + Global.URL_MAIL_UP);
												
						if (resultObj != null) {
							detaildao.UpdateMail(map.get("sid").toString(),resultObj.get("sid").toString());
						} else {
							flag = false;
						}
					}
				}
				/*
				boolean resultObj = helper.mailUploadRequestGzip(datas.toString(), Global.PL_URL + Global.URL_MAIL_UP);
				
				if (resultObj) {

					detaildao.UpdateMail(mailNoList);

				} else{
					flag= false;
				}*/
			}
		} catch (Exception e) {
			flag= false;
		}
		return flag;
	}

	private String StringFormate(Object str) {
		if (str != null && !"null".equals(str)) {
			return str.toString();
		} else {
			return "";
		}
	}

	private String StringFormateli(Object str) {
		if (str != null && !"null".equals(str)) {
			str=str.toString().replace("-", "");
			str=str.toString().replace(" ", "");
			str=str.toString().replace(":", "");
			return str.toString();
		} else {
			return "";
		}
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		// 一直上传，直到成功，没有次数限制
		User user=DaoFactory.getInstance().getUserDAO2(this.getApplicationContext()).FindUser();
		if(user.getTelephone() != null){//如果没有上班,手机没有绑定
			while (!upload(user)) {
			}
		}
	}

}

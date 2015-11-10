package com.cn.net.cnpl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.db.dao.MailUploadDao;
import com.cn.net.cnpl.model.User;
import com.cn.net.cnpl.tools.NetHelper;

/**
 * 投递上传service
 * 
 * @author duaizhi
 * 
 */
public class MailImgUploadService extends IntentService {

	public MailImgUploadService() {
		super("MailImgUploadService");
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
	public boolean upload() {
		boolean flag=true;
		try {
			User user=User.FindUser(this.getApplicationContext());
			TelephonyManager telephonemanage = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
			MailUploadDao dao = DaoFactory.getInstance().getMailUploadDao(this);
			NetHelper helper = new NetHelper();
			List<Map<String, Object>> mapslist = null;
			mapslist = dao.FindMailAll(user.getLoginName());
			if (mapslist != null && mapslist.size()>0) {
				for (Map<String, Object> map : mapslist) {
					JSONArray datas = new JSONArray();
					JSONObject data = new JSONObject();
					data.put("operationMode","I");
					data.put("mailCode", StringFormate(map.get("mail")));//邮件号
					data.put("operationTime", StringFormate(map.get("createDate")));//保存时间
					data.put("uploadTime", DateFormat.format("yyyyMMddkkmmss", new Date()));//DateFormat.format("yyyyMMddkkmmss", new Date()));
//					data.put("dlvOrgCode", StringFormate(map.get("orgCode")));//机构号
//					data.put("dlvUserCode",
//							StringFormate(map.get("userCode")));//工号
					data.put("signatureImg",
							StringFormate(map.get("signatureImg"))); //图片
					datas.put(data);
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("key", user.getKey());
					
					jsonObject.put("deviceNumber",telephonemanage.getDeviceId());
					jsonObject
							.put("orgCode", StringFormate(map.get("orgCode")));
					jsonObject.put("userCode",
							StringFormate(map.get("userCode")));
//					jsonObject.put("role", "3");
					jsonObject.put("dataList", datas);
					JSONObject resultObj = helper.exeRequestGzip(
							jsonObject.toString(), Global.BASE_URL
									+ Global.SOCIETYMAILDLVIMG);
					
					if (resultObj != null) {
							dao.updateMail(StringFormate(map.get("mail")), StringFormate(map.get("userCode")));
					} else {
						flag= false;
					}

				}
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

	@Override
	protected void onHandleIntent(Intent intent) {
		// 一直上传，直到成功，没有次数限制
//			upload();
		int i=0;
		while (i <50 && !upload()) {
			i++;
		}
	}

}

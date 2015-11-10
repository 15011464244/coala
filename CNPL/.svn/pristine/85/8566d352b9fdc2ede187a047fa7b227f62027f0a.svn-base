package com.cn.net.cnpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.text.format.DateFormat;

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.db.dao.MailDao;
import com.cn.net.cnpl.db.dao.MailHandDetailDao;
import com.cn.net.cnpl.model.User;
import com.cn.net.cnpl.tools.NetHelper;

/**
 * 投递上传service
 * 
 * @author duaizhi
 * 
 */
public class DlvUploadService extends IntentService {

	 private File fullFilename;
	 private boolean save=false;
	    
	public DlvUploadService() {
		super("DlvUploadService");
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
	    fullFilename =new File(Environment.getExternalStorageDirectory()+ "/CNPL/"+Global.UPMAILDLV);
    	if (!fullFilename.exists()){
		    try {
			fullFilename.createNewFile();
		    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    } 
		    }
		boolean flag=true;
		try {
			User user=User.FindUser(this.getApplicationContext());
			MailDao dao = DaoFactory.getInstance().getMailDao2(this);
			NetHelper helper = new NetHelper();
			List<Map<String, Object>> mapslist = null;
			mapslist = dao.FindMailByUpload();
			MailHandDetailDao mailhanddetailDao  = DaoFactory.getInstance().getMailHandDetailDao4(this);
			
			if (mapslist != null && mapslist.size()>0) {
				for (Map<String, Object> map : mapslist) {
					JSONArray datas = new JSONArray();
					JSONObject data = new JSONObject();
					data.put("remark", StringFormate(map.get("remark")));
					data.put("operationMode",
							StringFormate(map.get("operationMode")));
					data.put("mailCode", StringFormate(map.get("mailCode")));
					data.put("operationTime", StringFormate(map.get("createDate")));//DateFormat.format("yyyyMMddkkmmss", new Date()));
					data.put("uploadTime", DateFormat.format("yyyyMMddkkmmss", new Date()));//DateFormat.format("yyyyMMddkkmmss", new Date()));
					data.put("dlvOrgCode", StringFormate(map.get("dlvOrgCode")));
					//data.put("dlvPsegCode", "");
					//data.put("dlvCustCode", "");
					//data.put("dlvPsegName", "");
					data.put("dlvOrgName", StringFormate(map.get("dlvOrgName")));
					//data.put("dlvOrgPostCode","");
					data.put("dlvStsCode", StringFormate(map.get("dlvStsCode")));
					data.put("signStsCode", StringFormate(map
							.get("signStsCode").toString().trim()));
					data.put("actualGoodsFee",
							StringFormate(map.get("actualGoodsFee")));
					data.put("actualTax", "");
					data.put("actualFee", StringFormate(map.get("actualFee")));
					data.put("otherFee", "");
					data.put("dlvUserCode",
							StringFormate(map.get("dlvUserCode")));
					data.put("dlvUserName",
							StringFormate(map.get("dlvUserName")));
					//data.put("signerIdTypeCode", "");
					//data.put("signerIdNum", "");
					data.put("projId",
							StringFormate(map.get("undlvNextModeCode")));
					data.put("undlvCauseCode",
							StringFormate(map.get("undlvCauseCode")));
					data.put("undlvCauseDesc",
							StringFormate(map.get("undlvCauseDesc")));
					data.put("undlvNextModeCode",
							StringFormate(map.get("undlvfollowCode")));
					//data.put("dataClctModeCode", "");
					data.put("signerName", StringFormate(map.get("signerName")));
					data.put("interFlag", StringFormate(map.get("interFlag")));
					data.put("dataSrcSys", "8");
					data.put("dlvAddress", StringFormate(map.get("dlvAddress")));
					data.put("signatureImg",
							StringFormate(map.get("signatureImg")));
					data.put("xAxis",StringFormate(map.get("longitude")));
					data.put("yAxis",StringFormate(map.get("latitude")));
					data.put("province",  StringFormate(map.get("province")));
					data.put("city",   StringFormate(map.get("city")));
					data.put("region",  StringFormate( map.get("county")));
					data.put("street",  StringFormate( map.get("street")));
//					String oldSid=mailhanddetailDao.FindMailInDlv(StringFormate(map.get("mailCode")));
//					data.put("sid",oldSid);//旧交接批次ID
					datas.put(data);
					
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("key", user.getKey());
					jsonObject.put("deviceNumber",
							StringFormate(map.get("deviceNumber")));
					jsonObject
							.put("orgCode", StringFormate(map.get("orgCode")));
					jsonObject.put("userCode",
							StringFormate(map.get("userCode")));
					jsonObject.put("role", "8");
					jsonObject.put("dataList", datas);
					JSONObject resultObj = helper.exeRequestGzip(
							jsonObject.toString(), Global.BASE_URL
									+ Global.DLV_UPLOAD);
					
					if (resultObj != null) {
							dao.updateMail(map.get("mailCode").toString(), "1",
									map.get("dlvStsCode").toString(),map.get("createDate").toString());
					} else {
						flag= false;
					}
					FileOutputStream fos = new FileOutputStream(fullFilename,true);
					String is_upload = dao.Findisupload(map.get("mailCode").toString(), map.get("userCode").toString(), map.get("dlvStsCode").toString());
					String message = map.get("dlvStsCode").toString()+"\t\t"+map.get("mailCode").toString()+"\t\t"+is_upload+"\n";
					fos.write(message.getBytes());
					fos.close();
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
		// 一直上传，直到成功，没有次数限制  50次后不成功就不传
//		upload();
		int i=0;
		while (i <50 && !upload()) {
			i++;
		}
	}

}

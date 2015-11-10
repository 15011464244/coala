package com.cn.net.cnpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.db.dao.FollowAlarmDao;
import com.cn.net.cnpl.db.dao.MailFollowDao;
import com.cn.net.cnpl.db.dao.MailHandDetailDao;
import com.cn.net.cnpl.model.User;
import com.cn.net.cnpl.tools.BaiduGps;
import com.cn.net.cnpl.tools.NetHelper;

/**
 * 邮件地理上传service
 * 
 * @author duaizhi
 * 
 */
public class MailFollowUploadService extends IntentService {
	

	public MailFollowUploadService() {
		super("MailFollowUploadService");
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
	 * 查出接入完成，没有转出的邮件，获取他们的地址信息
	 */
	public void save(){

		MailHandDetailDao handDetailDao  = DaoFactory.getInstance().getMailHandDetailDao2(this.getApplicationContext());
		
		MailFollowDao dao = DaoFactory.getInstance().getMailFollowDao(this.getApplicationContext());
		List<Map<String, String>>   dataList =new ArrayList<Map<String,String>>();
		
		BaiduGps baiduGps = BaiduGps.getInstance(this.getApplicationContext());
		baiduGps.getLocation();
		try {
			while (baiduGps.getBdLocation() ==null) { //循环到去的GPS信息
				
			}
		if( baiduGps != null && baiduGps.getBdLocation() != null ){
		List<Map<String, Object>>   mailNoList =handDetailDao.FindMailNos();
		if(mailNoList!=null){
		for(int i=0;i<mailNoList.size();i++){
			Map<String, String>   values=new HashMap<String, String>();
		values.put("mail_num", mailNoList.get(i).get("mail_num").toString());
		values.put("operatorType", mailNoList.get(i).get("operatorType").toString());
		values.put("oldSid", mailNoList.get(i).get("oldSid").toString());
		values.put("longitude",Double.toString(baiduGps.getBdLocation().getLongitude()));
		values.put("latitude", Double.toString( baiduGps.getBdLocation().getLatitude()));
		values.put("province",  baiduGps.getBdLocation().getProvince());
		values.put("city", baiduGps.getBdLocation().getCity());
		values.put("county",  baiduGps.getBdLocation().getDistrict());
		values.put("street",  baiduGps.getBdLocation().getStreet());
		dataList.add(values);
		}
		}
		dao.SaveMailFollow(dataList);
		
		}
		} catch (Exception e) {
			Log.e("保存出错", e.toString());
		}
	}
	

		
	
	/**
	 * 上传方法 返回true全部上传成功 返回false 有数据没成功
	 * 
	 * @throws Exception
	 */
	public void upload(User user) {
		try {
			List<String>   mailNoList =new ArrayList<String>();
			MailFollowDao dao = DaoFactory.getInstance().getMailFollowDao2(this);
			NetHelper helper = new NetHelper();
			List<Map<String,  String>> mapslist =  dao.FindMailFllow();
			if (mapslist != null && mapslist.size()>0) {
				JSONArray datas = new JSONArray();
				for (Map<String,  String> map : mapslist) {
					JSONObject data = new JSONObject();
					data.put("operatorType","I");//操作类型(I U D)[I 插入 U 更新 D 删除]
					data.put("mailNum", StringFormate(map.get("mail_num")));
					data.put("longitude", StringFormate(map.get("longitude")));
					data.put("latitude",  StringFormate(map.get("latitude")));
					data.put("uploadTime",   DateFormat.format("yyyyMMddkkmmss", new Date()));
					data.put("country", StringFormate(  map.get("country")));
					data.put("province",  StringFormate(map.get("province")));
					data.put("city",   StringFormate(map.get("city")));
					data.put("county",  StringFormate( map.get("county")));
					data.put("street",  StringFormate( map.get("street")));
					data.put("gpsTime", StringFormate(  map.get("gps_time")));
					data.put("sid",   StringFormate(map.get("oldSid")));
					datas.put(data);
					mailNoList.add( map.get("id"));
				}

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id", "11");
				jsonObject.put("deviceNo", StringFormate( user.getTelephone()));
				jsonObject.put("orgCode",  StringFormate(user.getTransitCode()));
				jsonObject.put("userCode", StringFormate( user.getLoginName()));
				jsonObject.put("role", "8");
				jsonObject.put("operateTime", DateFormat.format("yyyyMMddkkmmss", new Date()));
				jsonObject.put("dataList", datas);
				
				JSONObject resultObj = helper.exeRequestGzip(
						jsonObject.toString(), Global.BASE_URL + Global.URL_MAIL_FOLLOW);
				if (resultObj != null) {
					dao.UpdateMailFollow(mailNoList);
				} 
				
				/*
				boolean b = helper.mailUploadRequestGzip(datas.toString(), Global.PL_URL
								+ Global.URL_MAIL_FOLLOW);

				if (b) {
						dao.UpdateMailFollow(mailNoList);
				}*/
			}
		} catch (Exception e) {
			Log.e("上传出错", e.toString());
		}
	}
	
	


	@Override
	protected void onHandleIntent(Intent intent) {
    	     try {
    	    	 
    	    	 //需要加个表记录上次时间点
    	    	 //每次执行的时候删除上次的数据，增加新时间点（或者直接修改）
    	    	 //查询上次的执行时间，如果是大于1小时，那么就执行不是的话就不执行
    	    	 //测试，先设定时间是3分钟
    	    	 FollowAlarmDao alrm = DaoFactory.getInstance().getFollowAlarmDao(this.getApplicationContext());
    	    	 Long oldtime = alrm.FindOldTime();
    	    	 long newtime = new Date().getTime();
    	    	 long delay=0; 
    	    	 if (oldtime != null){
    	    		 delay = newtime - oldtime;
    	    	 }
    	    	 if(delay==0 || delay>=1000*60*60){
    	    	 Log.e("", "开始跟踪上传保存");
    	    	 		save();
    	    		User user=DaoFactory.getInstance().getUserDAO3(this.getApplicationContext()).FindUser();
    	    		if(user!=null&&user.getTelephone() != null){//如果没有上班,手机没有绑定
    	    			Log.e("", "开始跟踪上传");
    	    			upload(user);
    	    			Log.e("", "结束跟踪上传");
    	    		}
    	    		 Log.e("", "结束");
    	    		 //记录时间点

    	    		alrm.SaveMail(newtime);
    	    	 }
			} catch (Exception e) {
//				Log.e("ssss", e.toString());
			}
	}


	private String StringFormate(Object str) {
		if (str != null && !"null".equals(str)) {
			return str.toString();
		} else {
			return "";
		}
	}

	
}

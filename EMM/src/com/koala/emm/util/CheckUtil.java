package com.koala.emm.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bouncycastle.LICENSE;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.koala.emm.app.EmmApplication;
import com.koala.emm.basicdata.BasicDataService;
import com.koala.emm.constant.Constant;
import com.koala.emm.model.CheckBean;
import com.koala.emm.model.CheckResult;
import com.koala.emm.tools.PhoneMessageUtils;
import com.lidroid.xutils.util.LogUtils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class CheckUtil {
	
	private static ActivityManager activityManager;
	/**
	 * 判断是否安装指定软件
	 * @param mContext
	 * @param packageName
	 * @return
	 */
	public static boolean checkApkExist(Context mContext, String packageName) {
		if (packageName == null || "".equals(packageName)) {
			return false;
		}
		try {
			mContext.getPackageManager().getApplicationInfo(packageName,
					PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}
	
	/**
	 * 判断指定服务是否开启
	 * @param mContext
	 * @param serviceName
	 * @return
	 */
	public static boolean checkServiceRunning(Context mContext, String serviceName){
		
		if(null == serviceName || "".equals(serviceName)){
			return false;
		}		
		activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServices = activityManager.getRunningServices(30);
		for (RunningServiceInfo runningServiceInfo : runningServices) {
			if(serviceName.equals(runningServiceInfo.service.getClassName())){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 查询是否强制更新新，强制卸载
	 */
	public static void checkUpdate(final Context mContext){
		JSONObject params = new JSONObject();
		params.put("imei", DeviceUtil.getDeviceNo());
		params.put("package_name", mContext.getPackageName());
		final String version = (new PhoneMessageUtils(mContext)).getmAppVersion();
		params.put("version", version);
		MyRequest<CheckBean> request = new MyRequest<CheckBean>(
				Method.POST, CheckBean.class, Constant.isUpdate,
				new Listener<CheckBean>() {
					@Override
					public void onResponse(CheckBean arg0) {
						LogUtils.e("checkUpdate response"+ arg0.toString());
//						JSONObject json = JSONObject.parseObject(arg0.toString());
						Boolean state = arg0.getState();
						if(state){
							CheckResult result = arg0.getResult().get(0);
//							result.setState(5);
							if(4 == result.getState()){
//								result.setApp_edition("1.1.2");
								if(versionDetermine(version,result.getApp_edition()) > 0){
									//强制更新
									Toast.makeText(mContext, "您的应用被要求强制更新，请下载更新！", 1000).show();
									UpdateUtil util = new UpdateUtil(mContext);
									util.downLoadApk(result.getUpdate_url(),UpdateUtil.APP_UPDATE_FORCED);
								}else{
									//一切正常，不再查询后台状态
									SpfsUtil.setCheckState(false);
								}
								
							}else if(5 == result.getState()){
								//强制卸载
								Toast.makeText(mContext, "您的应用被要求强制卸载，请联系管理员！", 1000).show();
								//调用卸载的框
								Uri packageUri = Uri.parse("package:"+mContext.getPackageName());
						        Intent intent = new Intent(Intent.ACTION_DELETE,packageUri);
						        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								mContext.startActivity(intent);
								if (mContext.getPackageName().equals(result.getPackage_name())) {
									System.exit(0);
									mContext.stopService(new Intent(mContext,BasicDataService.class));
								}
//								System.exit(0);
//								mContext.stopService(new Intent(mContext,BasicDataService.class));
							}else{
								//一切正常，不再查询后台状态
								SpfsUtil.setCheckState(false);
							}
							
						}else{
							//一切正常，不再查询后台状态
							SpfsUtil.setCheckState(false);
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						Toast.makeText(mContext, "您的网络不好，请检测您的网络！", 1000).show();
						System.exit(0);
						mContext.stopService(new Intent(mContext,BasicDataService.class));
					}
				}, params.toString());
		EmmApplication.sQueue.add(request);
	}
	/**
	 * 测试wangmingsheng
	 * 
	 */
	public static void checkUpdate2(final Context mContext){
		if(SpfsUtil.getCheckState()){
			if(SpfsUtil.getUninstall()){
				//强制卸载
				Toast.makeText(mContext, "您的应用被要求强制卸载，请联系管理员！", 1000).show();
				//调用卸载的框
				Uri packageUri = Uri.parse("package:"+mContext.getPackageName());
		        Intent intent = new Intent(Intent.ACTION_DELETE,packageUri);
		        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
				System.exit(0);
				mContext.stopService(new Intent(mContext,BasicDataService.class));
			}else if(SpfsUtil.getUpdateForce()){
				int versionDetermine = versionDetermine((new PhoneMessageUtils(mContext)).getmAppVersion(), SpfsUtil.getUpdateVersion());
				if(versionDetermine > 0){
					//强制更新
					Toast.makeText(mContext, "您的应用被要求强制更新，请下载更新！", 1000).show();
					UpdateUtil util = new UpdateUtil(mContext);
					util.downLoadApk(SpfsUtil.getUpdateUrl(),UpdateUtil.APP_UPDATE_FORCED);
				}else{
					SpfsUtil.setUpdateForce(false);
					SpfsUtil.setUpdateUrl("");
					SpfsUtil.setUpdateVersion("");
					SpfsUtil.setCheckState(false);
				}
			}else{
				SpfsUtil.setUninstall(false);
				SpfsUtil.setUpdateForce(false);
				SpfsUtil.setUpdateUrl("");
				SpfsUtil.setUpdateVersion("");
				SpfsUtil.setCheckState(false);
			}
		}
	}
	
	/**
	 * 版本号判断
	 * @param currentVersion
	 * @param updateVersion
	 * @return
	 */
	public static int versionDetermine(String currentVersion,String updateVersion){
		
		if(currentVersion == null || updateVersion == null || "" .equals(currentVersion.trim()) || "".equals(updateVersion.trim())){
			return 0;
		}
		int[] cuVer = str2IntArray(currentVersion);
		int[] upVer = str2IntArray(updateVersion);
		int length = (upVer.length < cuVer.length ? upVer.length : cuVer.length);
	    for (int i = 0; i <length  ; i++) {
			if(upVer[i] > cuVer[1]){
				return 1;
			}else if(upVer[i] < cuVer[1]){
				return -1;
			}
		}
	    if(upVer.length < cuVer.length){
	    	return -1;
	    }else if(upVer.length > cuVer.length){
	    	return 1;
	    }else{
	    	return 0;
	    }
	    
	}

	
	/**
	 * 版本号转int[]
	 * @param v
	 * @return
	 */
	public static int[] str2IntArray(String v){
		String tmpStr = "";
		if(v.length()>0){
			for(int i=0;i<v.length();i++){
				String tmp=""+v.charAt(i);
				if((tmp).matches("[0-9.]")){
						tmpStr+=tmp;
				}
			}
		}
		String[] strA =  tmpStr.split("\\.");
		int[] intA = new int[strA.length];
		 for (int i = 0; i <strA.length; i++)
		  {
			 intA[i] = Integer.parseInt(strA[i]);
		  }
		 return intA;
	}
	
	
}

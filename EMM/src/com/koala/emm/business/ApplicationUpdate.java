/**
 * 
 */
package com.koala.emm.business;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.arvin.koalapush.util.ToastUtil;
import com.koala.emm.app.EmmApplication;
import com.koala.emm.basicdata.BasicDataService;
import com.koala.emm.model.WarnPushMessage;
import com.koala.emm.tools.PhoneMessageUtils;
import com.koala.emm.util.CheckUtil;
import com.koala.emm.util.UpdateUtil;
import com.koala.emm.util.DeviceUtil;
import com.koala.emm.util.SpfsUtil;
import com.lidroid.xutils.util.LogUtils;

import android.content.Context;
import android.util.Log;

/**
 * @author wanjun  2015年6月5日,下午2:31:06
 * 应用更新处理类
 */
public class ApplicationUpdate {
	private Context mContext;
	
	public ApplicationUpdate(Context mContext) {
		this.mContext = mContext;
	}
	
	/**
	 * 强制更新
	 * 
	 * @param wpm
	 */
	public void forecedUpdate(WarnPushMessage wpm) {
		try {
			String updateUrl = wpm.getUpdateUrl();
			UpdateUtil util = new UpdateUtil(mContext);
			if (DeviceUtil.isWifiNetwork()) {
				util.downLoadApk(updateUrl, UpdateUtil.APP_UPDATE_FORCED);
			} else if (DeviceUtil.isMobileNetwork()) {
				// util.updateConfirm(updateUrl, AppUpdateUtil.UPDATE_FORCED);
				util.downLoadApk(updateUrl, UpdateUtil.APP_UPDATE_FORCED);
			} else {
				ToastUtil.show(mContext, "没有网络！");
			}
			// 判断是否更新本应用
			if (mContext.getPackageName().equals(
					wpm.getPackageName())) {
				// 存入强制更新的版本
				SpfsUtil.setUpdateVersion(wpm.getApp_edition());
				// 存入强制更新的url
				SpfsUtil.setUpdateUrl(updateUrl);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.e("强制更新：数据有问题");
			ToastUtil.show(mContext, "强制更新：数据有问题");
		}

	}

	/**
	 * 非强制更新
	 * 
	 * @param wpm
	 */
	public void notForecedUpdate(WarnPushMessage wpm) {
		try {
			
			String updateUrl = wpm.getUpdateUrl();
			
			UpdateUtil util = new UpdateUtil(mContext);
			PhoneMessageUtils utils = new PhoneMessageUtils(mContext);
			String currentVersion = utils.getmAppVersion();
			String updateVersion = wpm.getApp_edition();
//			boolean versionDetermine = versionDetermine(currentVersion, updateVersion);
			int versionDetermine2 = CheckUtil.versionDetermine(currentVersion, updateVersion);
			if(versionDetermine2 > 0){
				
				if (DeviceUtil.isWifiNetwork()) {
					
					util.downLoadApk(updateUrl,
							UpdateUtil.APP_UPDATE_NOT_FORCED);
				} else if (DeviceUtil.isMobileNetwork()) {
					util.updateConfirm(updateUrl,
							UpdateUtil.APP_UPDATE_NOT_FORCED);
				} else {
					ToastUtil.show(mContext, "没有网络！");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.e("非强制更新：数据有问题");
			ToastUtil.show(mContext, "非强制更新：数据有问题");
		}
	}
	
	/**
	 * 版本号判断
	 * @param currentVersion
	 * @param updateVersion
	 * @return
	 */
	private boolean versionDetermine(String currentVersion,String updateVersion){
		
		if(currentVersion == null || updateVersion == null || "" .equals(currentVersion.trim()) || "".equals(updateVersion.trim())){
			ToastUtil.show(mContext, "版本号为空");
			LogUtils.e("版本号为空");
			return false;
		}
		
		Pattern pattern = Pattern.compile("[0-9]{1,}");  
	    Matcher matcher = pattern.matcher((CharSequence) updateVersion.replace(".", ""));  
	    if(!matcher.matches()){
	    	LogUtils.e("版本号格式不对");
	    	ToastUtil.show(mContext, "版本号格式不对");
	    	return false;  
	    }
	    int cuVer = Integer.valueOf(currentVersion.replace(".", ""));
	    int upVer = Integer.valueOf(updateVersion.replace(".", ""));
	    if(cuVer > upVer){
	    	return true;
	    }else if(cuVer == upVer){
	    	ToastUtil.show(mContext, "安装的是最新版本");
	    	return false;
	    }else{
	    	ToastUtil.show(mContext, "已安装更新的版本");
	    	return false;
	    }

	}

}

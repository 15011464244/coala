/**
 * 
 */
package com.koala.emm.business;

import com.arvin.koalapush.util.ToastUtil;
import com.koala.emm.model.WarnPushMessage;
import com.koala.emm.tools.PhoneMessageUtils;
import com.koala.emm.util.UpdateUtil;
import com.koala.emm.util.DeviceUtil;
import com.koala.emm.util.SpfsUtil;
import com.lidroid.xutils.util.LogUtils;

import android.content.Context;

/**
 * @author wanjun  2015年6月5日,下午2:30:27
 * 数据更新处理类
 */
public class DataUpdate {
	private Context mContext;
	public DataUpdate(Context mContext) {
		this.mContext = mContext;
	}
	
	/**
	 * 数据强制更新
	 */
	public void forecedUpdate(WarnPushMessage wpm){
		try {
			String updateUrl = wpm.getUpdateUrl();
			UpdateUtil util = new UpdateUtil(mContext);
			if (DeviceUtil.isWifiNetwork()) {
				util.downLoadFile(updateUrl, UpdateUtil.DATA_UPDATE_FORCED);
			} else if (DeviceUtil.isMobileNetwork()) {
				// util.updateConfirm(updateUrl, AppUpdateUtil.UPDATE_FORCED);
				util.downLoadFile(updateUrl, UpdateUtil.DATA_UPDATE_FORCED);
			} else {
				ToastUtil.show(mContext, "没有网络！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.e(e.toString());
			LogUtils.e("强制更新：数据有问题");
			ToastUtil.show(mContext, "强制更新：数据有问题");
		}
	}
	
	
	
	/**
	 * 数据非强制更新
	 */
	public void notForecedUpdate(WarnPushMessage wpm){
		try {
			String updateUrl = wpm.getUpdateUrl();
			
			UpdateUtil util = new UpdateUtil(mContext);
			
			PhoneMessageUtils utils = new PhoneMessageUtils(mContext);

			String currentVersion = utils.getmAppVersion();
			String updateVersion = wpm.getApp_edition();
			
//			boolean versionDetermine = versionDetermine(currentVersion, updateVersion);
//			if(versionDetermine){
				if (DeviceUtil.isWifiNetwork()) {
					util.downLoadFile(updateUrl,
							UpdateUtil.DATA_UPDATE_NOT_FORCED);
				} else if (DeviceUtil.isMobileNetwork()) {
					util.updateConfirm(updateUrl,
							UpdateUtil.DATA_UPDATE_NOT_FORCED);
				} else {
					ToastUtil.show(mContext, "没有网络！");
				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.e("非强制更新：数据有问题");
			LogUtils.e(e.toString());
			ToastUtil.show(mContext, "非强制更新：数据有问题");
		}
	}
	
	
	
}

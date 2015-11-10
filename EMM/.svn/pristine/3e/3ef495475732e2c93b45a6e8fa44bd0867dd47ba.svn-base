package com.koala.emm.business;



import java.util.Timer;
import java.util.TimerTask;

import com.koala.emm.receiver.DeviceAdmin;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;



public class SafeUtil {
	private Context mContext;
	private DevicePolicyManager devicePolicyManager;
	private ComponentName componentName;
	private boolean isAdminActive;
	
	public SafeUtil(Context mContext) {
		this.mContext = mContext;
		//获取系统权限管理
		devicePolicyManager = (DevicePolicyManager) this.mContext.getSystemService(Context.DEVICE_POLICY_SERVICE);
		//申请权限
		componentName = new ComponentName(mContext, DeviceAdmin.class);
		//判断是否有权限
		isAdminActive = devicePolicyManager.isAdminActive(componentName);
	}
	
	/**
	 * 申请系统权限（激活）
	 */
	public void getAdminActive(){
		if(!isAdminActive){
			Intent intent = new Intent();
			//指定动作
			intent.setAction(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			//指定给哪个组件授权
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
			mContext.startActivity(intent);
		}
		
	}
	
	/**
	 * 强制激活
	 */
	public void getAdminActiveForce(){
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				if(!isAdminActive){
					getAdminActive();
				}else{
					timer.cancel();
				}
				
			}
		}, 1000*10);
	}
	
	/**
	 * 移除权限
	 */
	public void removeAdminActive(){
		devicePolicyManager.removeActiveAdmin(componentName);
	}
	
	/**
	 * 是否拥有系统权限
	 * @return
	 */
	public boolean isAllow(){
		return isAdminActive;
	}
	
	/**
	 * 锁屏
	 */
	public void lockScreen(){
		if(isAdminActive){
			devicePolicyManager.lockNow();
		}else{
			Toast.makeText(mContext, "您还没有获取权限", 100).show();
			getAdminActive();
		}
		
	}
	
	/**
	 * 重置锁屏密码
	 * @param pwd 锁屏密码(用4位数字，不然解锁困难)
	 */
	public void resetPwd(String pwd){
		if(isAdminActive){
			devicePolicyManager.resetPassword(pwd, 0);
		}else {
			Toast.makeText(mContext, "您还没有获取权限", 100).show();
			getAdminActive();
		}
		
	}
	
	/**
	 * 恢复出场设置
	 */
	public void wipeDevice(){
		if(isAdminActive){
			//恢复出厂设置（建议不要真机测试）
			devicePolicyManager.wipeData(0);
		}else{
			Toast.makeText(mContext, "您还没有获取权限", 100).show();
			getAdminActive();
		}
	}
	
	

}

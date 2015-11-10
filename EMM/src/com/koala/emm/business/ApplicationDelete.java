/**
 * 
 */
package com.koala.emm.business;

import com.arvin.koalapush.util.ToastUtil;
import com.koala.emm.basicdata.BasicDataService;
import com.koala.emm.model.WarnPushMessage;
import com.koala.emm.util.SpfsUtil;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * @author wanjun  2015年6月5日,下午2:31:23
 * 应用删除处理类
 */
public class ApplicationDelete {
	private Context mContext;
	public ApplicationDelete(Context mContext) {
		this.mContext = mContext;
	}
	
	/**
	 * 卸载并关闭
	 */
	public void uninstall(WarnPushMessage wpm) {
		String packageName = wpm.getPackageName();
//		packageName = "com.ems.express";
		// packageName = "com.koala.emm";
		if(null == packageName || "null".equals(packageName) || "".equals(packageName)){
			Toast.makeText(mContext, "包名为空", 1000).show();
			return;
		}
		SafeUtil safeUtil = new SafeUtil(mContext);
		safeUtil.removeAdminActive();
		Uri packageUri = Uri.parse("package:" + packageName);
		Intent intent = new Intent(Intent.ACTION_DELETE, packageUri);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
		if (mContext.getPackageName().equals(packageName)) {
			SpfsUtil.setUninstall(true);
			System.exit(0);
			mContext.stopService(new Intent(mContext,BasicDataService.class));
		}
	}

}

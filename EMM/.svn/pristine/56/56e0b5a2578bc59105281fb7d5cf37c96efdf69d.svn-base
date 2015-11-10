package com.koala.emm.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.koala.emm.basicdata.BasicDataService;

public class TestDialogUtil {
	public static void TestDialog(Context context,Boolean isShow,String content){
		if(isShow){
			WarningDialogUtil.createSystemAlertDialog(
					context, "测试", content, "确定",
							new OnClickListener() {
						
						@Override
						public void onClick(
								DialogInterface dialog,
								int which) {
							WarningDialogUtil.dismiss();
						}
					}, null, null);
		}
	}
}

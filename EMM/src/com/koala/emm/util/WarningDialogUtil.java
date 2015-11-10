package com.koala.emm.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.view.WindowManager;

public class WarningDialogUtil {

	private static AlertDialog.Builder builder;
	private static AlertDialog d ;

	public static void createSystemAlertDialog(Context context, String title,
			String message, String positveName, OnClickListener positiveButton,
			String negativeName, OnClickListener negativeButton) {
		if (builder == null) {
			builder = new AlertDialog.Builder(context);
		}
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setPositiveButton(positveName, positiveButton);
		builder.setNegativeButton(negativeName, negativeButton);
		d = builder.create();
		d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		d.setCancelable(false);
		d.show();
	}
	
	public static void dismiss(){
		if(d!=null){
			d.dismiss();
		}
	}
	

}

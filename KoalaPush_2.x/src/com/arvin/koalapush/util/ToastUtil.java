package com.arvin.koalapush.util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

public class ToastUtil {
	private static Boolean DEBUG = true;

	/**
	 * @param dEBUG the dEBUG to set
	 */
	public static void isToast(Boolean toast) {
		DEBUG = toast;
	}

	public static void show(Context mContext, String text, int duration) {
		if (DEBUG) {
			Toast.makeText(mContext, text, duration).show();
		}
	}

	public static void show(Context mContext, String text) {
		show(mContext, text, 1);
	}
	public static void showTip(Context mContext, String text) {
		Toast toast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
		View view = toast.getView(); 
		view.setBackgroundColor(Color.parseColor("#ffffff"));
		toast.setView(view); 
		toast.show(); 
	}
}

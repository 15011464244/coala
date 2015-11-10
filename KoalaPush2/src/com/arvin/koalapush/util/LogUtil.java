package com.arvin.koalapush.util;

import android.util.Log;

public class LogUtil {

	// private static HashMap<String, String> map;

	public static Boolean PRINT = false;
	public static Boolean SYSO = true;
	public final static String TAG = "PushKoala";
	public final static String ERROR = "PushError";
	public final static String MESSAGE = "PushMsg";

	public static void print(String str) {
		if (PRINT) {
			Log.d(TAG, str);
		}
	}
	public static void e(String str) {
			Log.e(ERROR, str);
	}

	public static void w(String str) {
			Log.w(MESSAGE, str);
	}
	public static void i(String str) {
			Log.i(MESSAGE, str);
	}

	public static void v(String str) {
			Log.v(TAG, str);
	}


	public static void isDebug(Boolean dEBUG) {
		PRINT = dEBUG;
	}

}

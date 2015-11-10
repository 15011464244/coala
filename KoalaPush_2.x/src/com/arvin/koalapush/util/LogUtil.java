package com.arvin.koalapush.util;

import android.util.Log;

public class LogUtil {

	// private static HashMap<String, String> map;

	public static Boolean PRINT = true;
	public static Boolean SYSO = true;
	public final static String TAG = "PushKoala";
	public final static String ERROR = "PushError";
	public final static String MESSAGE = "PushMsg";
	
	public static void d(String str) {
		if (PRINT) {
			Log.d(TAG, str);
		}
	}
	public static void e(String str) {
		if(PRINT){
			Log.e(ERROR, str);
		}
	}

	public static void w(String str) {
		if(PRINT){
			Log.w(MESSAGE, str);
		}
	}
	public static void i(String str) {
		if(PRINT){
			Log.i(MESSAGE, str);
		}
	}

	public static void v(String str) {
		if(PRINT){
			Log.v(TAG, str);
		}
	}


	public static void isDebug(Boolean dEBUG) {
		PRINT = dEBUG;
	}

}

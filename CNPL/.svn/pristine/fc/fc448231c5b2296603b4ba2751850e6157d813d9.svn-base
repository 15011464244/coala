package com.cn.net.cnpl;

import android.app.Application;

import com.cn.net.cnpl.exception.CrashHandler;

public class CrashApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(this);
		crashHandler.sendPreviousReportsToServer();  
	}
}

package com.cn.net.cnpl.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class LoginBandleDaoHelper extends BaseSQLiteOpenHelper implements DatabaseConstants{

	protected static final String TABLE_NAME = "soci_mobile_band_logname";
	
	
	
	public LoginBandleDaoHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		onCreate(getWritableDatabase());
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		if(!tabIsExist(db,TABLE_NAME)){
			db.execSQL("CREATE TABLE "+TABLE_NAME+" (DEVICE        VARCHAR2(200),  LOG_NAME      VARCHAR2(100));");
		}
		
	}

}

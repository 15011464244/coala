package com.cn.net.cnpl.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.cn.net.cnpl.db.LoginBandleDaoHelper;

public class LoginBandleDao extends LoginBandleDaoHelper {

	public LoginBandleDao(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public LoginBandleDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public synchronized String FindLoginBandle(String loginName) {
		Cursor cursor = null;
		String device = null;
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(TABLE_NAME, new String[] { "DEVICE" }, " LOG_NAME=? ", new String[]{loginName},
					null, null, null,null);
			if (cursor.getCount() > 0) {
				if (cursor.moveToNext()) {
					device = cursor.getString(0);
				}
			}
		} catch (Exception e) {
		} finally {
			closeCursor(cursor);
			return device;
		}

	}
	
	public synchronized long DelLoginBandle(String loginName) {
		
		SQLiteDatabase db = getWritableDatabase();
		long result = 0;
		result=db.delete(TABLE_NAME," LOG_NAME=? ", new String[]{loginName});
		return result;
	}
	
	public synchronized long SaveLoginBandle(String device ,String loginName) {
		SQLiteDatabase db = getWritableDatabase();
		long result = 0;
		ContentValues values = null;
			values = new ContentValues();
			values.put("DEVICE", device);
			values.put("LOG_NAME",loginName);
			result = db.insert(TABLE_NAME, null, values);
		return result;
	}

	protected void closeCursor(Cursor cursor) {
		if (cursor != null) {
			cursor.close();
		}
	}

}

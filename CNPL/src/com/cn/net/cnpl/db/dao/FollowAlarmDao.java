package com.cn.net.cnpl.db.dao;

import org.json.JSONException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.cn.net.cnpl.db.FollowAlarmDaoHelper;

public class FollowAlarmDao extends FollowAlarmDaoHelper {

	public FollowAlarmDao(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public FollowAlarmDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	public synchronized boolean SaveMail(Long params)
			throws JSONException {
		
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = null;
		if (params != null) {
			db.delete(TABLE_NAME, null, null);
			values = new ContentValues();
			values.put("uptime", params);
			
			db.insert(TABLE_NAME, null, values);
				return true;
		}
		return false;
	}
	
	public synchronized Long FindOldTime() {
		Cursor cursor = null;
		Long str =null ;
		String[] colums = { "uptime" };		
		try {
			SQLiteDatabase db = getReadableDatabase();
			
			cursor = db.query(TABLE_NAME, colums,null,null, null,
					null, " uptime  asc ");
			
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					str = cursor.getLong(0);
				}
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
		} finally {
			closeCursor(cursor);
		}
		
		return str;
	}
	
	protected void closeCursor(Cursor cursor) {
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
	}
	
}

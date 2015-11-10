package com.cn.net.cnpl.db.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.text.format.DateFormat;

import com.cn.net.cnpl.db.MailFollowDaoHelper;

public class MailFollowDao extends MailFollowDaoHelper {

	public MailFollowDao(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public MailFollowDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public List<Map<String, String>> FindMailFllow() {
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		Map<String, String> paramsMap = null;
		Cursor cursor = null;
//		邮件号	mail_num	varchar(20)	20		FALSE	FALSE	FALSE
//		经度	longitude	NUMBER			FALSE	FALSE	FALSE
//		纬度	latitude	NUMBER			FALSE	FALSE	FALSE
//		上传时间	upload_time	date			FALSE	FALSE	FALSE
//		国家	country	varchar(200)	200		FALSE	FALSE	FALSE
//		省	province	varchar(100)	100		FALSE	FALSE	FALSE
//		市	city	varchar(100)	100		FALSE	FALSE	FALSE
//		县	county	varchar(100)	100		FALSE	FALSE	FALSE
//		街道	street	varchar(300)	300		FALSE	FALSE	FALSE
//		创建时间	create_time	date			FALSE	FALSE	FALSE
//		id	id	long			FALSE	FALSE	FALSE
		String[] colums = { "mail_num", "longitude", "latitude",   "province", "city","county","street","id","create_time","operatorType","oldSid" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(TABLE_NAME, colums, "upload_time is null ",
					new String[] {  }, null, null, null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, String>();
					paramsMap.put("mail_num", cursor.getString(0));
					paramsMap.put("longitude", cursor.getString(1));
					paramsMap.put("latitude", cursor.getString(2));
					paramsMap.put("province", cursor.getString(3));
					paramsMap.put("city", cursor.getString(4));
					paramsMap.put("county", cursor.getString(5));
					paramsMap.put("street", cursor.getString(6));
					paramsMap.put("id", cursor.getString(7));
					paramsMap.put("gps_time", cursor.getString(8));
					paramsMap.put("operatorType", cursor.getString(9));
					paramsMap.put("oldSid", cursor.getString(10));
					
					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return dataList;
	}

	/**
	 * 只保存接入完成，没有转出的邮件
	 * @param dataList
	 * @throws JSONException
	 */
	public synchronized void SaveMailFollow(List<Map<String, String>> dataList)  {

		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = null;
		if (dataList != null && dataList.size()>0) {
			for (Map<String, String> params : dataList) {
				values = new ContentValues();
				values.put("mail_num", params.get("mail_num"));
				values.put("longitude", params.get("longitude"));
				values.put("latitude", params.get("latitude"));
				values.put("province", params.get("province"));
				values.put("city", params.get("city"));
				values.put("county", params.get("county"));
				values.put("street", params.get("street"));
				values.put("create_time",  DateFormat.format("yyyyMMddkkmmss", new Date()).toString());
				values.put("operatorType", params.get("operatorType"));
				values.put("oldSid", params.get("oldSid"));
				values.put("id", new Date().getTime());
				db.insert(TABLE_NAME, null, values);
			}
		}
	}
	
	/**
	 * 修改未 已上传
	 * @param dataList
	 * @throws JSONException
	 */
	public synchronized void UpdateMailFollow(List<String> dataList) throws JSONException {

		SQLiteDatabase db = getWritableDatabase();
		if (dataList != null) {
			for (String id : dataList) {
				ContentValues contentValues = new ContentValues();
				contentValues.put("upload_time", DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date()).toString());
				db.update(TABLE_NAME, contentValues,
						"id=?  ", new String[] {id});
			}
		}
		deleteDisableMail();
	}
	
	
	public synchronized void deleteDisableMail() {
		// 删除5天之前的数据
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_NAME, "upload_time is  not null " , null);
	}


}

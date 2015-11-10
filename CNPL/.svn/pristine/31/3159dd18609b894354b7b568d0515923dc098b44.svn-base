package com.cn.net.cnpl.db.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.text.format.DateFormat;

import com.cn.net.cnpl.db.MailUploadDaoHelpler;

public class MailUploadDao extends MailUploadDaoHelpler {
	public MailUploadDao(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public MailUploadDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	public synchronized boolean SaveMail(JSONObject params)
			throws JSONException {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = null;
		if (params != null) {
			db.delete(TABLE_NAME, " userCode=? and mail=? ", new String[]{params.getString("userCode"), params.getString("mail")});
			values = new ContentValues();
			values.put("sid", (new Date()).getTime());
			values.put("IS_UPLOAD","0");
			values.put("userCode", params.getString("userCode"));
			values.put("mail", params.getString("mail"));
			values.put("signatureImg", params.getString("signatureImg"));
			values.put("orgCode", params.getString("orgCode"));
			values.put("createDate",DateFormat.format("yyyyMMddkkmmss",new Date()).toString());
			db.insert(TABLE_NAME, null, values);
				return true;
		}
		return false;
	}

	public synchronized List<Map<String, Object>> FindMail(String userCode) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;
		String[] colums = {  "mail","signatureImg","createDate","IS_UPLOAD","upload_time","userCode","orgCode"};
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(TABLE_NAME, colums," userCode=?",new String[]{userCode}, null,
						null, "upload_time  desc ",null);
			
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("mail", cursor.getString(0));
					paramsMap.put("signatureImg", cursor.getString(1));
					paramsMap.put("createDate", cursor.getString(2));
					paramsMap.put("IS_UPLOAD", cursor.getString(3));
					paramsMap.put("upload_time", cursor.getString(4));
					paramsMap.put("userCode", cursor.getString(5));
					paramsMap.put("orgCode", cursor.getString(6));
					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}
	public synchronized Map<String, Object> FindMail(String userCode,String mail,String IS_UPLOAD) {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		Cursor cursor = null;
		String[] colums = {  "mail","signatureImg","createDate","IS_UPLOAD","upload_time","userCode","orgCode"};
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(TABLE_NAME, colums," userCode=? and mail=? and IS_UPLOAD=?",new String[]{userCode,mail,IS_UPLOAD}, null,
						null, "createDate  desc ",null);
			
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap.put("mail", cursor.getString(0));
					paramsMap.put("signatureImg", cursor.getString(1));
					paramsMap.put("createDate", cursor.getString(2));
					paramsMap.put("IS_UPLOAD", cursor.getString(3));
					paramsMap.put("upload_time", cursor.getString(4));
					paramsMap.put("userCode", cursor.getString(5));
					paramsMap.put("orgCode", cursor.getString(6));
				}
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
		} finally {
			closeCursor(cursor);
		}
		return paramsMap;
	}
	public synchronized List<Map<String, Object>> IsUpFindMail(String userCode,String datelike,String IS_UPLOAD,int pageNo) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;
		String[] colums = {  "mail","signatureImg","createDate","IS_UPLOAD","upload_time","userCode","orgCode"};
		try {
			SQLiteDatabase db = getReadableDatabase();
			if(!"".equals(datelike)){
			if("1".equals(IS_UPLOAD))
				cursor = db.query(TABLE_NAME, colums," userCode=? and substr(createDate,1,8) = ? and IS_UPLOAD=? ",new String[]{userCode,datelike,IS_UPLOAD}, null,
						null, "upload_time  desc ",null);
			else
				cursor = db.query(TABLE_NAME, colums," userCode=? and substr(createDate,1,8) = ? and IS_UPLOAD=? ",new String[]{userCode,datelike,IS_UPLOAD}, null,
						null, "createDate  desc ",null);
			}else{
				if("1".equals(IS_UPLOAD))
					cursor = db.query(TABLE_NAME, colums," userCode=? and IS_UPLOAD=? ",new String[]{userCode,IS_UPLOAD}, null,
							null, "upload_time  desc ",null);
				else
					cursor = db.query(TABLE_NAME, colums," userCode=? and IS_UPLOAD=? ",new String[]{userCode,IS_UPLOAD}, null,
							null, "createDate  desc ",null);
			}
			
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("mail", cursor.getString(0));
					paramsMap.put("signatureImg", cursor.getString(1));
					paramsMap.put("createDate", cursor.getString(2));
					paramsMap.put("IS_UPLOAD", cursor.getString(3));
					paramsMap.put("upload_time", cursor.getString(4));
					paramsMap.put("userCode", cursor.getString(5));
					paramsMap.put("orgCode", cursor.getString(6));
					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}

	public synchronized List<Map<String, Object>> FindMailAll(String userCode) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;
		String[] colums = {  "mail","signatureImg","createDate","IS_UPLOAD","upload_time","userCode","orgCode"};
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(TABLE_NAME, colums,"IS_UPLOAD ='0' and userCode =?  ",new String[]{userCode}, null,
						null, "upload_time  desc ",null);
			
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("mail", cursor.getString(0));
					paramsMap.put("signatureImg", cursor.getString(1));
					paramsMap.put("createDate", cursor.getString(2));
					paramsMap.put("IS_UPLOAD", cursor.getString(3));
					paramsMap.put("upload_time", cursor.getString(4));
					paramsMap.put("userCode", cursor.getString(5));
					paramsMap.put("orgCode", cursor.getString(6));
					dataList.add(paramsMap);
				}
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}
	
	public synchronized String Findcount(String userCode, String mailCode) {
		String count = "";
		Cursor cursor = null;
		String[] colums = {  "mail","signatureImg","createDate","IS_UPLOAD","upload_time","userCode","orgCode"};
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(TABLE_NAME, colums,
					" userCode=? and mail=? and IS_UPLOAD='0'", new String[] { userCode, mailCode }, null, null, null);
			if(cursor.getCount() > 0){
				while (cursor.moveToNext()) {
					count = cursor.getString(2);
				}
			}else
				count = "";
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
			count = "";
		} finally {
			closeCursor(cursor);
		}
		return count;
	}
	
	//修改为已经上传
	public synchronized boolean updateMail(String mail,String userCode) {
		SQLiteDatabase db = getWritableDatabase();
			ContentValues contentValues = new ContentValues();
			contentValues.put("IS_UPLOAD", "1");
			contentValues.put("upload_time", DateFormat.format("yyyyMMddkkmmss",new Date()).toString());
				
			db.update(TABLE_NAME, contentValues," mail=? and userCode =? ", new String[] { mail,userCode});
		
		return true;
	}
	
	public synchronized void deleteMailRe(String mail, String userCode,String IS_UPLOAD) {
		SQLiteDatabase db = getWritableDatabase();
		if (mail != null) {
			db.delete(TABLE_NAME,
					"mail=? and userCode=? and is_upload=? ",
					new String[] { mail, userCode,IS_UPLOAD });
		}
	}
	
	// 删除1天之前的数据
	public synchronized void deleteDisableMail() {
		// 删除1天之前的数据
		Long time = new Date().getTime();
		Long wheretime = time - 1000 * 60 * 60 * 24;
		String date= DateFormat.format("yyyyMMddkkmmss",wheretime).toString();
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_NAME, "is_upload ='1' and createDate< " + date, null);
	}
	
	protected void closeCursor(Cursor cursor) {
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
	}
}

package com.cn.net.cnpl.db.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.text.format.DateFormat;
import android.util.Log;

import com.cn.net.cnpl.Global;
import com.cn.net.cnpl.db.WorkLogDaoHelper;

public class WorkLogDao extends WorkLogDaoHelper {


	public WorkLogDao(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public WorkLogDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public  synchronized List<Map<String, Object>>  FindWorkLog() {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;
		try {
			 SQLiteDatabase db = getReadableDatabase();
			 cursor = db.query(TABLE_NAME,  new String[]{"mail_num","action_date","action"}, "", null, null, null, null);
			 if (cursor.getCount() > 0) {
				 if (cursor.moveToFirst()) {
						paramsMap = new LinkedHashMap<String, Object>();
						paramsMap.put("mail_num", cursor.getString(0));
						paramsMap.put("action_date", cursor.getString(1));
						paramsMap.put("action", cursor.getString(2));
						dataList.add(paramsMap);
				 }
			 }
		}
		catch(Exception e) {
			Log.e(Global.DIALOG_NAME,e.getMessage());
		}
		finally {
			closeCursor(cursor);
		}
		return dataList;
	}
	

	public  synchronized List<Map<String, Object>>  FindWorkLogByParams(Map<String, String> params,int pageNo) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;
		try {/*
			StringBuffer sql=new StringBuffer(" select mail_num,action_date,action  from TB_MAIL_LOG  where 1=1  ");
			if(params.get("mail_num")!= null &&  "".equals(params.get("mail_num"))){
				sql.append(" and mail_num = '"+params.get("mail_num")+"' ");
			}
			if(params.get("action_date")!= null &&  "".equals(params.get("action_date"))){
				sql.append(" and substr(action_date,1,8)  = '"+params.get("action_date")+"' ");
			}
			sql.append((pageNo - 1) * 10 + " , 10");
			 SQLiteDatabase db = getReadableDatabase();
			 cursor = db.rawQuery(sql.toString(), null);
			 */
			 String[] colums = { "mail_num", "action_date","action"};
				
			 SQLiteDatabase db = getReadableDatabase();
			 if(params.get("mail_num")== null || "".equals(params.get("mail_num")) || params.get("action_date")== null || "".equals(params.get("action_date"))){
				 if(params.get("mail_num")!= null &&  !"".equals(params.get("mail_num"))){
					 cursor = db.query(TABLE_NAME, colums," mail_num=? ",
								new String[] { params.get("mail_num") }, null,
								null, null,(pageNo - 1) * 10 + " , 10 ");
				 }else if(params.get("action_date")!= null &&  !"".equals(params.get("action_date"))){
					 cursor = db.query(TABLE_NAME, colums," substr(action_date,1,10)=? ",
								new String[] { params.get("action_date") }, null,
								null, null,(pageNo - 1) * 10 + " , 10 ");
				 
				 }
			 }else{
				 cursor = db.query(TABLE_NAME, colums," mail_num=? and substr(action_date,1,10)=? ",
							new String[] { params.get("mail_num"),params.get("action_date") }, null,
							null, null,(pageNo - 1) * 10 + " , 10 ");
			 }
			 if (cursor.getCount() > 0) {
				 while (cursor.moveToNext()) {
						paramsMap = new LinkedHashMap<String, Object>();
						paramsMap.put("mail_num", cursor.getString(0));
						paramsMap.put("action_date", cursor.getString(1));
						paramsMap.put("action", cursor.getString(2));
						dataList.add(paramsMap);
				 }
			 }
		}
		catch(Exception e) {
//			Log.e(Global.DIALOG_NAME,e.getMessage());
		}
		finally {
			closeCursor(cursor);
		}
		return dataList;
	}
	
	
	public synchronized void SaveWorkLog(String mailNo,String action) {
//		SQLiteDatabase db = getWritableDatabase();
//		ContentValues values = null;
//			values = new ContentValues();
//			values.put("mail_num", mailNo);
//			values.put("action_date",DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date()).toString());
//			values.put("action",action );
//		 db.insert(TABLE_NAME, null, values);
	}
	
	protected void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }
	
}

package com.cn.net.cnpl.db.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.cn.net.cnpl.db.ProjReasonDaoHelper;
import com.cn.net.cnpl.model.User;

public class ProjReasonDao extends ProjReasonDaoHelper {

	public ProjReasonDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	
	public ProjReasonDao(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	//保存数据
	public synchronized long SaveReason(JSONObject jsonObj) throws JSONException {
		SQLiteDatabase db = getWritableDatabase();
		long result = 0;
//		"create table tb_mail_upload(projId varchar(30)  ,projNum  varchar(30) ,projCd  varchar(50)" +
//		",cd  VARCHAR2(1),desc VARCHAR2(200));";
		ContentValues values = null;
		if(jsonObj != null){
//			db.delete(TABLE_NAME,null, null);
			values = new ContentValues();
			values.put("feedBackType", jsonObj.getString("feedBackType"));
			values.put("projId", jsonObj.getString("projId"));
			values.put("projName", jsonObj.getString("projName"));
			values.put("projCd",jsonObj.getString("projCd"));
			values.put("cd", jsonObj.getString("cd"));
			values.put("desc", jsonObj.getString("desc"));
			result = db.insert(TABLE_NAME, null, values);
		}
		return result;
	}
	
	//查询项目信息
	public synchronized List<Map<String, String>>  findPros(){
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		Map<String, String> paramsMap = null;
		Cursor cursor = null;
		String[] colums={"projId","projName"};
		try {
			 SQLiteDatabase db = getReadableDatabase();
			 cursor = db.query(TABLE_NAME,colums ,null,null, "  projId ", null, null);
			 if (cursor.getCount() > 0) {
				 while (cursor.moveToNext()) { 
					 paramsMap = new LinkedHashMap<String, String>();
					 paramsMap.put("projId", cursor.getString(0));
					 paramsMap.put("projName", cursor.getString(1));
					 dataList.add(paramsMap);
				 }
			 }
		}
		catch(Exception e) {
		}
		finally {
			closeCursor(cursor);
		}
		return dataList;
		
	}
	//查询类型
	public synchronized String findProType(String proId){;
		String dataList = "";
		Cursor cursor = null;
		String[] colums={"feedBackType"};
		try {
			 SQLiteDatabase db = getReadableDatabase();
			 cursor = db.query(TABLE_NAME,colums ," projId =? ",new String[]{proId}, null, null, null);
			 if (cursor.getCount() > 0) {
				 while (cursor.moveToNext()) { 
					 dataList = cursor.getString(0);
				 }
			 }
		}
		catch(Exception e) {
		}
		finally {
			closeCursor(cursor);
		}
		return dataList;
		
	}	
	
	//查询项目对应的异常
	public synchronized List<Map<String, String>>  findProReasons(String proId){
//		"create table tb_mail_upload(projId varchar(30)  ,projNum  varchar(30) ,projCd  varchar(50)" +
//				",cd  VARCHAR2(1),desc VARCHAR2(200));";
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		Map<String, String> paramsMap = null;
		Cursor cursor = null;
		String[] colums={"cd","desc"};
		try {
			 SQLiteDatabase db = getReadableDatabase();
			 cursor = db.query(TABLE_NAME,colums ," projId =? ",new String[]{proId}, " cd ", null, null);
			 if (cursor.getCount() > 0) {
				 while (cursor.moveToNext()) { 
					 paramsMap = new LinkedHashMap<String, String>();
					 paramsMap.put("cd", cursor.getString(0));
					 paramsMap.put("desc", cursor.getString(1));
					 dataList.add(paramsMap);
				 }
			 }
		}
		catch(Exception e) {
		}
		finally {
			closeCursor(cursor);
		}
		return dataList;
		
	}
	
	//查询项目对应的异常
	public synchronized String  findProReasons(String cd,boolean ispro){
		String dataList = "";
		Cursor cursor = null;
		if(ispro){
		String[] colums={"projName"};
		try {
			 SQLiteDatabase db = getReadableDatabase();
			 cursor = db.query(TABLE_NAME,colums ,"projId=?",new String[]{cd}, null, null, null);
			 if (cursor.getCount() > 0) {
				 if (cursor.moveToNext()) { 
					 dataList = cursor.getString(0);
//					 Log.e("dataList", dataList);
				 }
			 }
		}
		catch(Exception e) {
		}
		finally {
			closeCursor(cursor);
		}
		}else{
			String[] colums={"desc"};
			try {
				 SQLiteDatabase db = getReadableDatabase();
				 cursor = db.query(TABLE_NAME,colums ,"cd=?",new String[]{cd}, null, null, null);
				 if (cursor.getCount() > 0) {
					 while (cursor.moveToNext()) { 
						 dataList = cursor.getString(0);
					 }
				 }
			}
			catch(Exception e) {
			}
			finally {
				closeCursor(cursor);
			}
		}
		return dataList;
		
	}
	public synchronized void deletePro() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_NAME,null,null);
	}
}

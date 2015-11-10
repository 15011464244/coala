package com.cn.net.cnpl.db.dao;

import java.util.ArrayList;
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

import com.cn.net.cnpl.db.StateFollowDaoHelper;

public class StateFollowDao extends StateFollowDaoHelper {
	
	public StateFollowDao(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public StateFollowDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public List<Map<String, String>>  FindStateFllowBystateType(String standardCode) {
		List<Map<String, String>> dataList =  new ArrayList<Map<String,String>>();
		Map<String, String> paramsMap = null;
		Cursor cursor = null;
//		standardCode":"10",      //未妥投原因代码
//		"followAction ":"A"   //下一步动作代码
		String[] colums={"standardCode","followAction"};
		try {
			 SQLiteDatabase db = getReadableDatabase();
			 cursor = db.query(TABLE_NAME,colums , "standardCode=?",new String[]{standardCode}, null, null, null);
			 if (cursor.getCount() > 0) {
				 while (cursor.moveToNext()) { 
					 paramsMap = new LinkedHashMap<String, String>();
					 paramsMap.put("standardCode", cursor.getString(0));
					 paramsMap.put("followAction", cursor.getString(1));
					 dataList.add(paramsMap);
				 }
			 }
		}
		catch(Exception e) {
		}
		finally {
			if (cursor != null) {
	            cursor.close();
	        }
		}
		return dataList;
	}
	
	public void SaveStateFollow(List<JSONObject> dataList) throws JSONException {
//		standardCode":"10",      //未妥投原因代码
//		"followAction ":"A"   //下一步动作代码
		
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = null;
		if(dataList != null){
			db.delete(TABLE_NAME,null, null);
			for(JSONObject params :dataList){
				values = new ContentValues();
				values.put("standardCode", params.getString("standardCode"));
				values.put("followAction",params.getString("followAction"));
				db.insert(TABLE_NAME, null, values);
			}
		}
	}
	
	public List<Map<String, String>>  FindStateFllowActionBystateType(String standardCode) {
		List<Map<String, String>> dataList =  new ArrayList<Map<String,String>>();
		Map<String, String> paramsMap = null;
		Cursor cursor = null;
//		standardCode":"10",      //未妥投原因代码
//		"followAction ":"A"   //下一步动作代码
		String[] colums={"standardCode","followAction"};
		try {
			String sql=" select sf.standardCode,sf.followAction,pf.actionDescCHS ,pf.actionDescENG,pf.actionDescTRADITIONAL from STATE_FOLLOW  sf  left join FOLLOW_ACTION pf on sf.followAction=pf.followAction  where sf.standardCode='"+standardCode+"' ";
			 SQLiteDatabase db = getReadableDatabase();
			 cursor = db.rawQuery(sql, null);
			 if (cursor.getCount() > 0) {
				 while (cursor.moveToNext()) { 
					 paramsMap = new LinkedHashMap<String, String>();
					 paramsMap.put("standardCode", cursor.getString(0));
					 paramsMap.put("followAction", cursor.getString(1));
					 paramsMap.put("actionDescCHS", cursor.getString(2));
					 paramsMap.put("actionDescENG", cursor.getString(3));
					 paramsMap.put("actionDescTRADITIONAL", cursor.getString(4));
					 dataList.add(paramsMap);
				 }
			 }
		}
		catch(Exception e) {
		}
		finally {
			if (cursor != null) {
	            cursor.close();
	        }
		}
		return dataList;
	}
	
}

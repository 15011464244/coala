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

import com.cn.net.cnpl.db.FollowActionDaoHelper;

public class FollowActionDao extends FollowActionDaoHelper {
	public FollowActionDao(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	

	public FollowActionDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	
	public List<Map<String, String>>  FindFollowActionByStandardCode(String followAction) {
		List<Map<String, String>> dataList =  new ArrayList<Map<String,String>>();
		Map<String, String> paramsMap = null;
		Cursor cursor = null;
//		"followAction ":"A",      //下一步动作代码
//		"actionDescCHS ":"收件人不在，今天再投",   //下一步动作中文描述
//		"actionDescENG":"",          //下一步动作英文描述
//		"actionDescTRADITIONAL":"收件人不在，今天再投"   //下一步动作繁体中文描述
//		FOLLOW_ACTION	

		
//		standardCode":"10",      //未妥投原因代码
//		"followAction ":"A"   //下一步动作代码
//		STATE_FOLLOW
		
//		String sql="select fa.followAction ,fa.actionDescCHS ,fa.actionDescENG ,fa.actionDescTRADITIONAL  from FOLLOW_ACTION fa ,STATE_FOLLOW sf where fa.followAction=sf.followAction and sf.standardCode='"+standardCode+"'";
		String[] colums={"followAction","actionDescCHS","actionDescENG","actionDescTRADITIONAL"};
		try {
			 SQLiteDatabase db = getReadableDatabase();
//			 cursor= db.rawQuery(sql, null);
			 cursor = db.query(TABLE_NAME,colums , "followAction=?",new String[]{followAction}, null, null, null);
			 if (cursor.getCount() > 0) {
				 while (cursor.moveToNext()) { 
					 paramsMap = new LinkedHashMap<String, String>();
					 paramsMap.put("followAction", cursor.getString(0));
					 paramsMap.put("actionDescCHS", cursor.getString(1));
					 paramsMap.put("actionDescENG", cursor.getString(2));
					 paramsMap.put("actionDescTRADITIONAL", cursor.getString(3));
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
	
	public List<Map<String, String>>  FindFollowAction() {
		List<Map<String, String>> dataList =  new ArrayList<Map<String,String>>();
		Map<String, String> paramsMap = null;
		Cursor cursor = null;
		try {
			 SQLiteDatabase db = getReadableDatabase();
			 cursor = db.rawQuery("select * from " + TABLE_NAME, null);
			 if (cursor.getCount() > 0) {
				 while (cursor.moveToNext()) { 
					 paramsMap = new LinkedHashMap<String, String>();
					 paramsMap.put("followAction", cursor.getString(0));
					 paramsMap.put("actionDescCHS", cursor.getString(1));
					 paramsMap.put("actionDescENG", cursor.getString(2));
					 paramsMap.put("actionDescTRADITIONAL", cursor.getString(3));
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
	
	public void SaveFollowAction(List<JSONObject> dataList) throws JSONException {
//		"followAction ":"A",      //下一步动作代码
//		"actionDescCHS ":"收件人不在，今天再投",   //下一步动作中文描述
//		"actionDescENG":"",          //下一步动作英文描述
//		"actionDescTRADITIONAL":"收件人不在，今天再投"   //下一步动作繁体中文描述
		
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = null;
		if(dataList != null){
			db.delete(TABLE_NAME,null, null);
			for(JSONObject params :dataList){
				values = new ContentValues();
				values.put("followAction", params.getString("followAction"));
				values.put("actionDescCHS",params.getString("actionDescCHS"));
				values.put("actionDescENG", params.getString("actionDescENG"));
				//values.put("stateDescCHS",params.getString("stateDescCHS"));
				values.put("actionDescTRADITIONAL",params.getString("actionDescTRADITIONAL"));
				db.insert(TABLE_NAME, null, values);
			}
		}
	}
	
	protected void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

}

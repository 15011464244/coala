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

import com.cn.net.cnpl.db.DlvStateDaoHelper;

public class DlvStateDao extends DlvStateDaoHelper {

	public DlvStateDao(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public DlvStateDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public List<Map<String, String>>  FindDlvStateBystateType(String stateType) {
		List<Map<String, String>> dataList =  new ArrayList<Map<String,String>>();
		Map<String, String> paramsMap = null;
		Cursor cursor = null;
//		"pdaCode ":"11",      //pda代码
//		"stateCode":"17",     //投递状态代码
//		"stateType ":"H",          //投递情况代码
//		"stateDescCHS":"邮件错发",    //投递状态中文描述
//		"followAction":"F",        //未妥投下一部动作
//		"stateDescENG":"Item wrongly directed",      //投递状态英文描述
//		"stateDescTRADITIONAL":"郵件錯發"     //投递状态繁体中文描述
		String[] colums={"pdaCode","stateCode","stateType","stateDescCHS","followAction","stateDescENG","stateDescTRADITIONAL"};
		try {
			 SQLiteDatabase db = getReadableDatabase();
			 cursor = db.query(TABLE_NAME,colums , "stateType=?",new String[]{stateType}, null, null, null);
			 if (cursor.getCount() > 0) {
				 while (cursor.moveToNext()) { 
					 paramsMap = new LinkedHashMap<String, String>();
					 paramsMap.put("pdaCode", cursor.getString(0));
					 paramsMap.put("stateCode", cursor.getString(1));
					 paramsMap.put("stateType", cursor.getString(2));
					 paramsMap.put("stateDescCHS", cursor.getString(3));
					 paramsMap.put("followAction", cursor.getString(4));
					 paramsMap.put("stateDescENG", cursor.getString(5));
					 paramsMap.put("stateDescTRADITIONAL", cursor.getString(6));
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
	
	
	
	public void SaveDlvState(List<JSONObject> dataList) throws JSONException {
		
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = null;
		if(dataList != null){
			db.delete(TABLE_NAME,null, null);
			for(JSONObject params :dataList){
				values = new ContentValues();
				values.put("pdaCode", params.getString("pdaCode"));
				values.put("stateCode",trimString(params.getString("stateCode")));
				values.put("stateType", params.getString("stateType"));
				values.put("stateDescCHS",params.getString("stateDescCHS"));
				values.put("followAction",params.getString("followAction"));
				values.put("stateDescENG", params.getString("stateDescENG"));
				values.put("stateDescTRADITIONAL",params.getString("stateDescTRADITIONAL"));
				db.insert(TABLE_NAME, null, values);
			}
		}
	}
	
	public String trimString(String str){
			if(str != null){
				return str.trim();
			}
			return null;
	}
	
	protected void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }
	
}

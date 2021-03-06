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

import com.cn.net.cnpl.db.TransferDaoHelper;

public class TransferDao extends TransferDaoHelper {

	public TransferDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	
	public TransferDao(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	//保存数据
	public synchronized long SaveTransfer(JSONObject jsonObj,String key) throws JSONException {
		SQLiteDatabase db = getWritableDatabase();
		long result = 0;
		ContentValues values = null;
		if(jsonObj != null){
			values = new ContentValues();
			values.put("key", key);
			values.put("dlvNum", jsonObj.getString("dlvNum"));
			values.put("ticketNum",jsonObj.getString("ticketNum"));
			values.put("custNum", jsonObj.getString("custNum"));
			values.put("projId", jsonObj.getString("projId"));
			values.put("projName", jsonObj.getString("projName"));
			values.put("projCd", jsonObj.getString("projCd"));
			values.put("rcverCntct", jsonObj.getString("rcverCntct"));
			values.put("rcverCompany", jsonObj.getString("rcverCompany"));
			result = db.insert(TABLE_NAME, null, values);
		}
		Log.e("SaveTransfer", ""+result);
		return result;
	}
	
	//删除数据
	public synchronized long DeleteTransferByDlvNum(JSONArray array,String key) throws JSONException {
		SQLiteDatabase db = getWritableDatabase();
		long result = 0;
		long temp = 0;
		if(array!=null&&array.length()>0){
			for(int i=0;i<array.length();i++){
				temp = db.delete(TABLE_NAME," dlvNum=? and key=?", new String[] { array.get(i).toString(),key});
				result = result + temp;
			}
		}
		Log.e("DeleteTransfer", ""+result);
		return result;
	}
	
	//查询项目信息
	public synchronized List<Map<String, String>> findTransfers(String key){
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		Map<String, String> paramsMap = null;
		Cursor cursor = null;
		String[] colums={"dlvNum","ticketNum","custNum","projId","projName","projCd","rcverCntct","rcverCompany"};
		try {
			 SQLiteDatabase db = getReadableDatabase();
			 cursor = db.query(TABLE_NAME,colums ," key =? ",new String[]{key}, "  projId ", null, null);
			 if (cursor.getCount() > 0) {
				 while (cursor.moveToNext()) { 
					 paramsMap = new LinkedHashMap<String, String>();
					 paramsMap.put("dlvNum", cursor.getString(0));
					 paramsMap.put("ticketNum", cursor.getString(1));
					 paramsMap.put("custNum", cursor.getString(2));
					 paramsMap.put("projId", cursor.getString(3));
					 paramsMap.put("projName", cursor.getString(4));
					 paramsMap.put("projCd", cursor.getString(5));
					 paramsMap.put("rcverCntct", cursor.getString(5));
					 paramsMap.put("rcverCompany", cursor.getString(6));
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
	
	/**
	 * 根据邮件号查询配送单信息
	 */
	public synchronized List<Map<String, String>> findTransferByTicketNum(String key,String ticketNum){
		Log.e("key", key);
		Log.e("ticketNum", ticketNum);
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		Map<String, String> paramsMap = null;
		Cursor cursor = null;
		String[] colums={"dlvNum","custNum","projId","projName","projCd","rcverCntct","rcverCompany"};
		try {
			 SQLiteDatabase db = getReadableDatabase();
			 cursor = db.query(TABLE_NAME,colums ," key = ? and ticketNum = ? ",new String[]{key,ticketNum}, null, null, null);
			 if (cursor.getCount() > 0) {
				 Log.e("", "有数据...");
				 while (cursor.moveToNext()) { 
					 paramsMap = new LinkedHashMap<String, String>();
					 paramsMap.put("dlvNum", cursor.getString(0));
					 paramsMap.put("custNum", cursor.getString(1));
					 paramsMap.put("projId", cursor.getString(2));
					 paramsMap.put("projName", cursor.getString(3));
					 paramsMap.put("projCd", cursor.getString(4));
					 paramsMap.put("rcverCntct", cursor.getString(5));
					 paramsMap.put("rcverCompany", cursor.getString(6));
					 dataList.add(paramsMap);
				 }
			 }
		}
		catch(Exception e) {
			Log.e("", e.getMessage());
		}
		finally {
			closeCursor(cursor);
		}
		return dataList;
	}
	
}

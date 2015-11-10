package com.cn.net.cnpl.db.dao;

import java.util.ArrayList;
import java.util.Date;
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

import com.cn.net.cnpl.Global;
import com.cn.net.cnpl.db.MailHandHelper;

public class MailHandDao extends MailHandHelper {
	public MailHandDao(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public MailHandDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	public synchronized boolean SaveMail(JSONObject params)
			throws JSONException {
		
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = null;
		if (params != null) {
			values = new ContentValues();
			values.put("sid", params.getString("sid"));
			values.put("out_code", params.getString("out_code"));
			values.put("in_code", params.getString("in_code"));
			values.put("org_type", "");//params.getString("org_type"));
			values.put("hand_type", params.getString("hand_type"));
			values.put("hand_state", params.getString("hand_state"));
			values.put("begin_time", params.getString("begin_time"));
			values.put("end_time", params.getString("end_time"));
			values.put("create_by", params.getString("create_by"));
			values.put("is_shift_stop", params.getString("is_shift_stop"));
			values.put("shift_time", params.getString("shift_time"));
			values.put("certificate", params.getString("certificate"));
			values.put("longitude", params.getString("longitude"));
			values.put("latitude", params.getString("latitude"));
			
			values.put("province", params.getString("province"));
			values.put("city", params.getString("city"));
			values.put("county", params.getString("county"));
			values.put("street", params.getString("street"));
			values.put("actualCount", params.getString("actualCount"));
						
			db.insert(TABLE_NAME, null, values);
				return true;
		}
		return false;
	}
//	private String formate(Object str){
//		String temp="";
//		if (str != null) {
//			temp = str.toString();
//			
//			return new StringBuffer().append(temp.substring(0,3)).append("-").append(temp.substring(4,5)).append("-")
//					.append(temp.substring(6,7)).append(" ").append(temp.substring(8,9)).append(":")
//					.append(temp.substring(10,11)).append(":").append(temp.substring(12,13)).toString();
//		} else {
//			return "";
//		}
//	}
	public synchronized boolean SaveMail(JSONObject params,long sid)
			throws JSONException {
		
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = null;
		if (params != null) {
			values = new ContentValues();
			values.put("sid", sid);
			values.put("out_code", params.getString("outCode"));
			values.put("in_code", params.getString("inCode"));
			
			values.put("org_type", "");//params.getString("inType"));
			
			values.put("hand_type", params.getString("handType"));
			values.put("hand_state", Global.MAILCOM);
			values.put("begin_time", params.getString("beginTime"));
			values.put("end_time", params.getString("endTime"));
			values.put("create_by", params.getString("uoloadUserCode"));
			
			values.put("is_shift_stop", Global.UP);//params.getString("is_shift_stop"));
			values.put("shift_time", ""+DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date()));//params.getString("shift_time"));
			values.put("certificate", "");//params.getString("certificate"));
			values.put("longitude", "");
			values.put("latitude", "");
			values.put("province", "");
			values.put("city", "");
			values.put("county", "");
			values.put("street", "");
			values.put("actualCount", "");
			
			db.insert(TABLE_NAME, null, values);
			return true;
		}
		return false;
	}
	public synchronized List<Map<String, Object>> FindMail(String create_by,String hand_type,String hand_state,int pageNo) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;
		String[] colums = { "sid", "out_code","in_code","org_type","hand_type", "hand_state","begin_time",
				"end_time","create_by","is_shift_stop","shift_time", "certificate","longitude","latitude",
				"province","city","county","street","actualCount"};
		try {
			SQLiteDatabase db = getReadableDatabase();
			if("".equals(hand_type)){
				cursor = db.query(TABLE_NAME, colums," create_by=? and hand_state=? ",
						new String[] { create_by,hand_state }, "sid",
						null, "sid  desc ",null);
			}else{
				if(pageNo==-1)
					cursor = db.query(TABLE_NAME, colums," create_by=? and hand_type=? and hand_state=? ",
							new String[] { create_by,hand_type,hand_state }, "sid",
							null, "sid  desc ",null);
				else
					cursor = db.query(TABLE_NAME, colums," create_by=? and hand_type=? and hand_state=? ",
						new String[] { create_by,hand_type,hand_state }, "sid",
						null, "sid  desc ",(pageNo - 1) * 10
						+ " , 10 ");
			}
			
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("sid", cursor.getString(0));
					paramsMap.put("out_code", cursor.getString(1));
					paramsMap.put("in_code", cursor.getString(2));
					paramsMap.put("org_type", cursor.getString(3));
					paramsMap.put("hand_type", cursor.getString(4));
					paramsMap.put("hand_state", cursor.getString(5));
					paramsMap.put("begin_time", cursor.getString(6));
					paramsMap.put("end_time", cursor.getString(7));
					paramsMap.put("create_by", cursor.getString(8));
					paramsMap.put("is_shift_stop", cursor.getString(9));
					paramsMap.put("shift_time", cursor.getString(10));
					paramsMap.put("certificate", cursor.getString(11));
					paramsMap.put("longitude", cursor.getString(12));
					paramsMap.put("latitude", cursor.getString(13));
					
					paramsMap.put("province", cursor.getString(14));
					paramsMap.put("city", cursor.getString(15));
					paramsMap.put("county", cursor.getString(16));
					paramsMap.put("street", cursor.getString(17));
					paramsMap.put("actualCount", cursor.getString(18));
					
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
	
	public synchronized List<Map<String, Object>> FindShift(String create_by,String begin_time,String end_time) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;
		String[] colums = { "sid", "out_code","in_code","org_type","hand_type", "hand_state","begin_time",
				"end_time","create_by","is_shift_stop","shift_time", "certificate","longitude","latitude",
				"province","city","county","street","actualCount"};
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(TABLE_NAME, colums," create_by=? and shift_time!=? and begin_time=? and end_time=?",
						new String[] { create_by,"",begin_time,end_time }, "sid",
						null, "sid  desc ",null);
			
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("sid", cursor.getString(0));
					paramsMap.put("out_code", cursor.getString(1));
					paramsMap.put("in_code", cursor.getString(2));
					paramsMap.put("org_type", cursor.getString(3));
					paramsMap.put("hand_type", cursor.getString(4));
					paramsMap.put("hand_state", cursor.getString(5));
					paramsMap.put("begin_time", cursor.getString(6));
					paramsMap.put("end_time", cursor.getString(7));
					paramsMap.put("create_by", cursor.getString(8));
					paramsMap.put("is_shift_stop", cursor.getString(9));
					paramsMap.put("shift_time", cursor.getString(10));
					paramsMap.put("certificate", cursor.getString(11));
					paramsMap.put("longitude", cursor.getString(12));
					paramsMap.put("latitude", cursor.getString(13));
					paramsMap.put("province", cursor.getString(14));
					paramsMap.put("city", cursor.getString(15));
					paramsMap.put("county", cursor.getString(16));
					paramsMap.put("street", cursor.getString(17));
					paramsMap.put("actualCount", cursor.getString(18));
					
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
	
	public synchronized String FindExit(String create_by,String sid,String hand_state) {
		String count = "0";
		Cursor cursor = null;
		String[] colums = { "count(1) as num" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			
			cursor = db.query(TABLE_NAME, colums," create_by=? and sid=? and hand_state=? ",
							new String[] { create_by,sid,hand_state }, null,
							null, null,null);
			
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					count = cursor.getString(0);
				}
			}
		} catch (Exception e) {
			count = "0";
		} finally {
			closeCursor(cursor);
		}
		return count;
	}
	
	public synchronized List<Map<String, Object>> FindOutMail(String create_by,String datelike,String mail,boolean is,int pageNo) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;
		String[] colums = { "mail_num", "mail_state","principal","principal_type","abnormity_time", "upload_time","out_code",
				"out_type","in_code","in_type","begin_time", "end_time","certificate", "hand_type","uoload_user_code","createDate","operationMode,hand_state","longitude","latitude",
				"province","city","county","street","actualCount"};
		try {
			SQLiteDatabase db = getReadableDatabase();
			if("".equals(mail))
				cursor = db.query(TABLE_NAME, colums," create_by=? and substr(begin_time,1,10) = ? ",
					new String[] { create_by,datelike }, "begin_time", null,
							"end_time  desc ", (pageNo - 1) * 10
							+ " , 10 ");
			else if("".equals(datelike))
				cursor = db.query(TABLE_NAME, colums," create_by=? and mail_num = ? ",
						new String[] { create_by,mail }, "begin_time", null,
								"end_time  desc ", (pageNo - 1) * 10
								+ " , 10 ");
			else
				cursor = db.query(TABLE_NAME, colums," create_by=? and substr(begin_time,1,10) = ? and mail_num = ?",
						new String[] { create_by,datelike,mail }, "begin_time", null,
								"end_time  desc ", (pageNo - 1) * 10
								+ " , 10 ");
			
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("sid", cursor.getString(0));
					paramsMap.put("out_code", cursor.getString(1));
					paramsMap.put("in_code", cursor.getString(2));
					paramsMap.put("org_type", cursor.getString(3));
					paramsMap.put("hand_type", cursor.getString(4));
					paramsMap.put("hand_state", cursor.getString(5));
					paramsMap.put("begin_time", cursor.getString(6));
					paramsMap.put("end_time", cursor.getString(7));
					paramsMap.put("create_by", cursor.getString(8));
					paramsMap.put("is_shift_stop", cursor.getString(9));
					paramsMap.put("shift_time", cursor.getString(10));
					paramsMap.put("certificate", cursor.getString(11));
					paramsMap.put("longitude", cursor.getString(12));
					paramsMap.put("latitude", cursor.getString(13));
					paramsMap.put("province", cursor.getString(14));
					paramsMap.put("city", cursor.getString(15));
					paramsMap.put("county", cursor.getString(16));
					paramsMap.put("street", cursor.getString(17));
					paramsMap.put("actualCount", cursor.getString(18));
					
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
	public synchronized boolean updateMail(String create_by,String sid_time, String hand_state,String end_time,String isup) {
		SQLiteDatabase db = getWritableDatabase();
		if (sid_time != null) {
			ContentValues contentValues = new ContentValues();
			contentValues.put("hand_state", hand_state);
			if(!"".equals(end_time))
				contentValues.put("end_time", end_time);
			if(!"".equals(isup))
				contentValues.put("is_shift_stop", isup);
				
			db.update(TABLE_NAME, contentValues,
					" create_by=? and sid=? ", new String[] { create_by,sid_time});
			return true;
		}
		return false;
	}
	
	public synchronized boolean updateMail(String create_by,String hand_type,String hand_state,String isup) {
		SQLiteDatabase db = getWritableDatabase();
			ContentValues contentValues = new ContentValues();
			contentValues.put("is_shift_stop", isup);
				
			db.update(TABLE_NAME, contentValues,
					" create_by=? and hand_type=? and hand_state=? ", new String[] { create_by,hand_type,hand_state});
		
		return true;
	}
	
	public synchronized void deleteMail(String create_by,String sid) {
		SQLiteDatabase db = getWritableDatabase();
		if (sid != null) {
			db.delete(TABLE_NAME,
					"create_by=? and sid=? ",
					new String[] { create_by,sid});
		}
	}
	
	public synchronized void deleteMail() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_NAME,null,null);
	}
	
	protected void closeCursor(Cursor cursor) {
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
	}
}

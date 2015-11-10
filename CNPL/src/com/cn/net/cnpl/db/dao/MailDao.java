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
import android.util.Log;

import com.cn.net.cnpl.Global;
import com.cn.net.cnpl.db.MailDaoHelper;

public class MailDao extends MailDaoHelper {

	public MailDao(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public MailDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	/**
	 * 查询邮件号是否存在，是否可以保存
	 * 
	 * @param mailCode
	 * @return
	 */
	public synchronized boolean FindIsSave(String mailCode) {
		Boolean flag = true;
		Cursor cursor = null;
		String[] colums = { "count(1) " };
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(TABLE_NAME, colums,
					"  mailCode=?  and operationMode ='I' and dlvStsCode='I' ",
					new String[] { mailCode }, null, null, null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					if (cursor.getInt(0) > 0) {
						flag = false;
					}
				}
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
			flag = false;
		} finally {
			closeCursor(cursor);
			flag = true;
		}
		return flag;
	}

	/**
	 * 查询上传数量
	 * 
	 * @param userCode
	 * @param dlvStsCode
	 * @return
	 */
	public synchronized List<Map<String, String>> FindMailUploadCountByUserCode(
			String userCode, String dlvStsCode) {
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		Map<String, String> paramsMap = null;
		Cursor cursor = null;

		try {
			if (dlvStsCode != null) {
				String[] colums = { "count(1) ", "IS_UPLOAD" };
				SQLiteDatabase db = getReadableDatabase();
				cursor = db
						.query(TABLE_NAME,
								colums,
								"  userCode=? and dlvStsCode=?  and operationMode ='I'  ",
								new String[] { userCode, dlvStsCode },
								"IS_UPLOAD", null, null);
				if (cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						paramsMap = new LinkedHashMap<String, String>();
						paramsMap.put("num", cursor.getString(0));
						paramsMap.put("IS_UPLOAD", cursor.getString(1));
						dataList.add(paramsMap);
					}
				}
			} else {
				String[] colums = { "count(1) ", "IS_UPLOAD", "dlvStsCode" };
				SQLiteDatabase db = getReadableDatabase();
				cursor = db.query(TABLE_NAME, colums,
						"  userCode=?  and operationMode ='I'  ",
						new String[] { userCode }, "IS_UPLOAD,dlvStsCode",
						null, null);
				if (cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						paramsMap = new LinkedHashMap<String, String>();
						paramsMap.put("num", cursor.getString(0));
						paramsMap.put("IS_UPLOAD", cursor.getString(1));
						paramsMap.put("dlvStsCode", cursor.getString(2));
						dataList.add(paramsMap);
					}
				}
			}
		} catch (Exception e) {
			// Log.e( Global.DIALOG_NAME, e.toString());
		} finally {
			closeCursor(cursor);
		}
		return dataList;
	}

	/**
	 * 查询投递情况数量
	 * 
	 * @param userCode
	 * @return
	 */
	public synchronized List<Map<String, String>> FindMailDlvStsCodeCountByUserCode(
			String userCode) {
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		Map<String, String> paramsMap = null;
		Cursor cursor = null;

		String[] colums = { "count(1) as num", "dlvStsCode" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(TABLE_NAME, colums,
					"  userCode=?  and operationMode ='I'  ",
					new String[] { userCode }, " dlvStsCode", null, null);

			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, String>();
					paramsMap.put("num", cursor.getString(0));
					paramsMap.put("dlvStsCode", cursor.getString(1));
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

	/**
	 * 根据投递情况和是否上传来查询 ,如果查询全部，dlvStsCode是null
	 * 
	 * @param userCode
	 * @param dlvStsCode
	 * @param IS_UPLOAD
	 * @return
	 */
	public synchronized List<Map<String, Object>> FindMailByUpload() {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;
		String[] colums = { "IS_UPLOAD", "deviceNumber", "orgCode", "userCode",
				"role", "operationMode", "mailCode", "dlvOrgCode",
				"dlvOrgName", "dlvOrgPostCode", "dlvStsCode", "signStsCode",
				"actualGoodsFee", "actualTax", "actualFee", "otherFee",
				"dlvUserCode", "dlvUserName", "undlvCauseCode",
				"undlvNextModeCode", "signerName", "interFlag",
				"operationTime", "dlvAddress", "signatureImg","createDate","longitude","latitude",
				"province","city","county","street","remark","undlvfollowCode","undlvCauseDesc" };
		try {
			SQLiteDatabase db = getReadableDatabase();

			cursor = db.query(TABLE_NAME, colums, " IS_UPLOAD='0'", null, null,
					null, "operationTime  desc ");

			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("IS_UPLOAD", cursor.getString(0));
					paramsMap.put("deviceNumber", cursor.getString(1));
					paramsMap.put("orgCode", cursor.getString(2));
					paramsMap.put("userCode", cursor.getString(3));
					paramsMap.put("role", cursor.getString(4));
					paramsMap.put("operationMode", cursor.getString(5));
					paramsMap.put("mailCode", cursor.getString(6));
					paramsMap.put("dlvOrgCode", cursor.getString(7));
					paramsMap.put("dlvOrgName", cursor.getString(8));
					paramsMap.put("dlvOrgPostCode", cursor.getString(9));
					paramsMap.put("dlvStsCode", cursor.getString(10));
					paramsMap.put("signStsCode", cursor.getString(11));
					paramsMap.put("actualGoodsFee", cursor.getDouble(12));
					paramsMap.put("actualTax", cursor.getDouble(13));
					paramsMap.put("actualFee", cursor.getDouble(14));
					paramsMap.put("otherFee", cursor.getDouble(15));
					paramsMap.put("dlvUserCode", cursor.getString(16));
					paramsMap.put("dlvUserName", cursor.getString(17));
					paramsMap.put("undlvCauseCode", cursor.getString(18));
					paramsMap.put("undlvNextModeCode", cursor.getString(19));
					paramsMap.put("signerName", cursor.getString(20));
					paramsMap.put("interFlag", cursor.getString(21));
					paramsMap.put("operationTime", cursor.getString(22));
					paramsMap.put("dlvAddress", cursor.getString(23));
					paramsMap.put("signatureImg", cursor.getString(24));// 图片流basecode64转成的字符串
					paramsMap.put("createDate", cursor.getString(25));
					paramsMap.put("longitude", cursor.getString(26));
					paramsMap.put("latitude", cursor.getString(27));
					paramsMap.put("province", cursor.getString(28));
					paramsMap.put("city", cursor.getString(29));
					paramsMap.put("county", cursor.getString(30));
					paramsMap.put("street", cursor.getString(31));
					paramsMap.put("remark", cursor.getString(32));
					paramsMap.put("undlvfollowCode", cursor.getString(33));
					paramsMap.put("undlvCauseDesc", cursor.getString(34));
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

	/**
	 * 根据投递情况和是否上传来查询 ,如果查询全部，dlvStsCode是null
	 * 
	 * @param userCode
	 * @param dlvStsCode
	 * @param IS_UPLOAD
	 * @return
	 */
	public synchronized List<Map<String, Object>> FindMailByUpload(
			String userCode, String dlvStsCode, String IS_UPLOAD, int pageNo) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;
		String[] colums = { "IS_UPLOAD", "deviceNumber", "orgCode", "userCode",
				"role", "operationMode", "mailCode", "dlvOrgCode",
				"dlvOrgName", "dlvOrgPostCode", "dlvStsCode", "signStsCode",
				"actualGoodsFee", "actualTax", "actualFee", "otherFee",
				"dlvUserCode", "dlvUserName", "undlvCauseCode",
				"undlvNextModeCode", "signerName", "interFlag",
				"operationTime", "dlvAddress", "signatureImg","createDate","longitude","latitude",
				"province","city","county","street","remark","undlvfollowCode","undlvCauseDesc" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			if (Global.UNUPLOAD.equals(IS_UPLOAD)) {
				cursor = db.query(TABLE_NAME, colums,
						"  userCode=? and dlvStsCode=? and IS_UPLOAD=?    ",
						new String[] { userCode, dlvStsCode, IS_UPLOAD }, null,
						null, "operationTime  desc ", (pageNo - 1) * 10
								+ " , 10 ");

			} else {
				cursor = db
						.query(TABLE_NAME,
								colums,
								"  userCode=? and dlvStsCode=? and IS_UPLOAD=?  and operationMode ='I'  ",
								new String[] { userCode, dlvStsCode, IS_UPLOAD },
								null, null, "operationTime  desc ",
								(pageNo - 1) * 10 + " , 10 ");

			}

			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("IS_UPLOAD", cursor.getString(0));
					paramsMap.put("deviceNumber", cursor.getString(1));
					paramsMap.put("orgCode", cursor.getString(2));
					paramsMap.put("userCode", cursor.getString(3));
					paramsMap.put("role", cursor.getString(4));
					paramsMap.put("operationMode", cursor.getString(5));
					paramsMap.put("mailCode", cursor.getString(6));
					paramsMap.put("dlvOrgCode", cursor.getString(7));
					paramsMap.put("dlvOrgName", cursor.getString(8));
					paramsMap.put("dlvOrgPostCode", cursor.getString(9));
					paramsMap.put("dlvStsCode", cursor.getString(10));
					paramsMap.put("signStsCode", cursor.getString(11));
					paramsMap.put("actualGoodsFee", cursor.getDouble(12));
					paramsMap.put("actualTax", cursor.getDouble(13));
					paramsMap.put("actualFee", cursor.getDouble(14));
					paramsMap.put("otherFee", cursor.getDouble(15));
					paramsMap.put("dlvUserCode", cursor.getString(16));
					paramsMap.put("dlvUserName", cursor.getString(17));
					paramsMap.put("undlvCauseCode", cursor.getString(18));
					paramsMap.put("undlvNextModeCode", cursor.getString(19));
					paramsMap.put("signerName", cursor.getString(20));
					paramsMap.put("interFlag", cursor.getString(21));
					paramsMap.put("operationTime", cursor.getString(22));
					paramsMap.put("dlvAddress", cursor.getString(23));
					paramsMap.put("signatureImg", cursor.getString(24));// 图片流basecode64转成的字符串
					paramsMap.put("createDate", cursor.getString(25));
					paramsMap.put("longitude", cursor.getString(26));
					paramsMap.put("latitude", cursor.getString(27));
					paramsMap.put("province", cursor.getString(28));
					paramsMap.put("city", cursor.getString(29));
					paramsMap.put("county", cursor.getString(30));
					paramsMap.put("street", cursor.getString(31));
					paramsMap.put("remark", cursor.getString(32));
					paramsMap.put("undlvfollowCode", cursor.getString(33));
					paramsMap.put("undlvCauseDesc", cursor.getString(34));
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

	/**
	 * 根据投递情况和是否上传来查询 ,如果查询全部，dlvStsCode是null
	 * 
	 * @param userCode
	 * @param dlvStsCode
	 * @param IS_UPLOAD
	 * @return
	 */
	public synchronized List<Map<String, Object>> FindMailByUpload(
			String userCode, String dlvStsCode, String IS_UPLOAD, String createDate, int pageNo) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;
		String[] colums = { "IS_UPLOAD", "deviceNumber", "orgCode", "userCode",
				"role", "operationMode", "mailCode", "dlvOrgCode",
				"dlvOrgName", "dlvOrgPostCode", "dlvStsCode", "signStsCode",
				"actualGoodsFee", "actualTax", "actualFee", "otherFee",
				"dlvUserCode", "dlvUserName", "undlvCauseCode",
				"undlvNextModeCode", "signerName", "interFlag",
				"operationTime", "dlvAddress", "signatureImg","createDate","longitude","latitude",
				"province","city","county","street","remark","undlvfollowCode","undlvCauseDesc" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			if (Global.UNUPLOAD.equals(IS_UPLOAD)) {
				cursor = db.query(TABLE_NAME, colums,
						"  userCode=? and dlvStsCode=? and IS_UPLOAD=? and substr(createDate,1,8) = ?  ",
						new String[] { userCode, dlvStsCode, IS_UPLOAD, createDate }, null,
						null, "createDate  desc ", (pageNo - 1) * 10
								+ " , 10 ");

			} else {
				cursor = db
						.query(TABLE_NAME,
								colums,
								"  userCode=? and dlvStsCode=? and IS_UPLOAD=?  and operationMode ='I' and substr(createDate,1,8) = ? ",
								new String[] { userCode, dlvStsCode, IS_UPLOAD, createDate  },
								null, null, "createDate  desc ",
								(pageNo - 1) * 10 + " , 10 ");

			}
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("IS_UPLOAD", cursor.getString(0));
					paramsMap.put("deviceNumber", cursor.getString(1));
					paramsMap.put("orgCode", cursor.getString(2));
					paramsMap.put("userCode", cursor.getString(3));
					paramsMap.put("role", cursor.getString(4));
					paramsMap.put("operationMode", cursor.getString(5));
					paramsMap.put("mailCode", cursor.getString(6));
					paramsMap.put("dlvOrgCode", cursor.getString(7));
					paramsMap.put("dlvOrgName", cursor.getString(8));
					paramsMap.put("dlvOrgPostCode", cursor.getString(9));
					paramsMap.put("dlvStsCode", cursor.getString(10));
					paramsMap.put("signStsCode", cursor.getString(11));
					paramsMap.put("actualGoodsFee", cursor.getDouble(12));
					paramsMap.put("actualTax", cursor.getDouble(13));
					paramsMap.put("actualFee", cursor.getDouble(14));
					paramsMap.put("otherFee", cursor.getDouble(15));
					paramsMap.put("dlvUserCode", cursor.getString(16));
					paramsMap.put("dlvUserName", cursor.getString(17));
					paramsMap.put("undlvCauseCode", cursor.getString(18));
					paramsMap.put("undlvNextModeCode", cursor.getString(19));
					paramsMap.put("signerName", cursor.getString(20));
					paramsMap.put("interFlag", cursor.getString(21));
					paramsMap.put("operationTime", cursor.getString(22));
					paramsMap.put("dlvAddress", cursor.getString(23));
					paramsMap.put("signatureImg", cursor.getString(24));// 图片流basecode64转成的字符串
					paramsMap.put("createDate", cursor.getString(25));
					paramsMap.put("longitude", cursor.getString(26));
					paramsMap.put("latitude", cursor.getString(27));
					paramsMap.put("province", cursor.getString(28));
					paramsMap.put("city", cursor.getString(29));
					paramsMap.put("county", cursor.getString(30));
					paramsMap.put("street", cursor.getString(31));
					paramsMap.put("remark", cursor.getString(32));
					paramsMap.put("undlvfollowCode", cursor.getString(33));
					paramsMap.put("undlvCauseDesc", cursor.getString(34));
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
	public synchronized String Findcount(String userCode, String dlvStsCode, String mailCode) {
		String count = "";
		Cursor cursor = null;
		String[] colums = { "IS_UPLOAD", "deviceNumber", "orgCode", "userCode",
				"role", "operationMode", "mailCode", "dlvOrgCode",
				"dlvOrgName", "dlvOrgPostCode", "dlvStsCode", "signStsCode",
				"actualGoodsFee", "actualTax", "actualFee", "otherFee",
				"dlvUserCode", "dlvUserName", "undlvCauseCode",
				"undlvNextModeCode", "signerName", "interFlag",
				"operationTime", "dlvAddress", "signatureImg","createDate","longitude","latitude",
				"province","city","county","street","remark","undlvfollowCode","undlvCauseDesc" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(TABLE_NAME, colums,
					" userCode=? and dlvStsCode=? and  mailCode=? ", new String[] { userCode, dlvStsCode, mailCode }, null, null, null);
			if(cursor.getCount() > 0){
				while (cursor.moveToNext()) {
					count = cursor.getString(25);
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
	
	public synchronized String Findcount(String userCode, String mailCode) {
		String count = "";
		Cursor cursor = null;
		String[] colums = { "IS_UPLOAD", "deviceNumber", "orgCode", "userCode",
				"role", "operationMode", "mailCode", "dlvOrgCode",
				"dlvOrgName", "dlvOrgPostCode", "dlvStsCode", "signStsCode",
				"actualGoodsFee", "actualTax", "actualFee", "otherFee",
				"dlvUserCode", "dlvUserName", "undlvCauseCode",
				"undlvNextModeCode", "signerName", "interFlag",
				"operationTime", "dlvAddress", "signatureImg","createDate","longitude","latitude",
				"province","city","county","street","remark","undlvfollowCode","undlvCauseDesc" };
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(TABLE_NAME, colums,
					" userCode=? and mailCode=? and IS_UPLOAD='0'", new String[] { userCode, mailCode }, null, null, null);
			if(cursor.getCount() > 0){
				while (cursor.moveToNext()) {
					count = cursor.getString(25);
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
	public synchronized Map<String, Object> FindMail(String mailCode,
			String userCode, String IS_UPLOAD) {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		Cursor cursor = null;

		String[] colums = { "IS_UPLOAD", "deviceNumber", "orgCode", "userCode",
				"role", "operationMode", "mailCode", "dlvOrgCode",
				"dlvOrgName", "dlvOrgPostCode", "dlvStsCode", "signStsCode",
				"actualGoodsFee", "actualTax", "actualFee", "otherFee",
				"dlvUserCode", "dlvUserName", "undlvCauseCode",
				"undlvNextModeCode", "signerName", "interFlag",
				"operationTime", "dlvAddress", "signatureImg","createDate","longitude","latitude",
				"province","city","county","street" ,"remark","undlvfollowCode","undlvCauseDesc"};
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db
					.query(TABLE_NAME,
							colums,
							"mailCode=? and userCode=? and IS_UPLOAD=? ",
							new String[] { mailCode, userCode, 
									IS_UPLOAD}, null, null,null, null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap.put("IS_UPLOAD", cursor.getString(0));
					paramsMap.put("deviceNumber", cursor.getString(1));
					paramsMap.put("orgCode", cursor.getString(2));
					paramsMap.put("userCode", cursor.getString(3));
					paramsMap.put("role", cursor.getString(4));
					paramsMap.put("operationMode", cursor.getString(5));
					paramsMap.put("mailCode", cursor.getString(6));
					paramsMap.put("dlvOrgCode", cursor.getString(7));
					paramsMap.put("dlvOrgName", cursor.getString(8));
					paramsMap.put("dlvOrgPostCode", cursor.getString(9));
					paramsMap.put("dlvStsCode", cursor.getString(10));
					paramsMap.put("signStsCode", cursor.getString(11));
					paramsMap.put("actualGoodsFee", cursor.getDouble(12));
					paramsMap.put("actualTax", cursor.getDouble(13));
					paramsMap.put("actualFee", cursor.getDouble(14));
					paramsMap.put("otherFee", cursor.getDouble(15));
					paramsMap.put("dlvUserCode", cursor.getString(16));
					paramsMap.put("dlvUserName", cursor.getString(17));
					paramsMap.put("undlvCauseCode", cursor.getString(18));
					paramsMap.put("undlvNextModeCode", cursor.getString(19));
					paramsMap.put("signerName", cursor.getString(20));
					paramsMap.put("interFlag", cursor.getString(21));
					paramsMap.put("operationTime", cursor.getString(22));
					paramsMap.put("dlvAddress", cursor.getString(23));
					paramsMap.put("signatureImg", cursor.getString(24));// 图片流basecode64转成的字符串
					paramsMap.put("createDate", cursor.getString(25));
					paramsMap.put("longitude", cursor.getString(26));
					paramsMap.put("latitude", cursor.getString(27));
					paramsMap.put("province", cursor.getString(28));
					paramsMap.put("city", cursor.getString(29));
					paramsMap.put("county", cursor.getString(30));
					paramsMap.put("street", cursor.getString(31));
					paramsMap.put("remark", cursor.getString(32));
					paramsMap.put("undlvfollowCode", cursor.getString(33));
					paramsMap.put("undlvCauseDesc", cursor.getString(34));
				}
			}
		} catch (Exception e) {
		} finally {
			closeCursor(cursor);
		}
		return paramsMap;
	}
	public synchronized Map<String, Object> FindMail(String mailCode,
			String userCode, String dlvStsCode, String IS_UPLOAD,String createDate) {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		Cursor cursor = null;

		String[] colums = { "IS_UPLOAD", "deviceNumber", "orgCode", "userCode",
				"role", "operationMode", "mailCode", "dlvOrgCode",
				"dlvOrgName", "dlvOrgPostCode", "dlvStsCode", "signStsCode",
				"actualGoodsFee", "actualTax", "actualFee", "otherFee",
				"dlvUserCode", "dlvUserName", "undlvCauseCode",
				"undlvNextModeCode", "signerName", "interFlag",
				"operationTime", "dlvAddress", "signatureImg","createDate","longitude","latitude",
				"province","city","county","street" ,"remark","undlvfollowCode","undlvCauseDesc"};
		try {
			SQLiteDatabase db = getReadableDatabase();
			cursor = db
					.query(TABLE_NAME,
							colums,
							"mailCode=? and userCode=? and dlvStsCode=? and IS_UPLOAD=? and createDate=? ",
							new String[] { mailCode, userCode, dlvStsCode,
									IS_UPLOAD,createDate}, null, null,
							"createDate  desc ", null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap.put("IS_UPLOAD", cursor.getString(0));
					paramsMap.put("deviceNumber", cursor.getString(1));
					paramsMap.put("orgCode", cursor.getString(2));
					paramsMap.put("userCode", cursor.getString(3));
					paramsMap.put("role", cursor.getString(4));
					paramsMap.put("operationMode", cursor.getString(5));
					paramsMap.put("mailCode", cursor.getString(6));
					paramsMap.put("dlvOrgCode", cursor.getString(7));
					paramsMap.put("dlvOrgName", cursor.getString(8));
					paramsMap.put("dlvOrgPostCode", cursor.getString(9));
					paramsMap.put("dlvStsCode", cursor.getString(10));
					paramsMap.put("signStsCode", cursor.getString(11));
					paramsMap.put("actualGoodsFee", cursor.getDouble(12));
					paramsMap.put("actualTax", cursor.getDouble(13));
					paramsMap.put("actualFee", cursor.getDouble(14));
					paramsMap.put("otherFee", cursor.getDouble(15));
					paramsMap.put("dlvUserCode", cursor.getString(16));
					paramsMap.put("dlvUserName", cursor.getString(17));
					paramsMap.put("undlvCauseCode", cursor.getString(18));
					paramsMap.put("undlvNextModeCode", cursor.getString(19));
					paramsMap.put("signerName", cursor.getString(20));
					paramsMap.put("interFlag", cursor.getString(21));
					paramsMap.put("operationTime", cursor.getString(22));
					paramsMap.put("dlvAddress", cursor.getString(23));
					paramsMap.put("signatureImg", cursor.getString(24));// 图片流basecode64转成的字符串
					paramsMap.put("createDate", cursor.getString(25));
					paramsMap.put("longitude", cursor.getString(26));
					paramsMap.put("latitude", cursor.getString(27));
					paramsMap.put("province", cursor.getString(28));
					paramsMap.put("city", cursor.getString(29));
					paramsMap.put("county", cursor.getString(30));
					paramsMap.put("street", cursor.getString(31));
					paramsMap.put("remark", cursor.getString(32));
					paramsMap.put("undlvfollowCode", cursor.getString(33));
					paramsMap.put("undlvCauseDesc", cursor.getString(34));
				}
			}
		} catch (Exception e) {
		} finally {
			closeCursor(cursor);
		}
		return paramsMap;
	}

	public synchronized List<Map<String, Object>> FindMail(String mailCode,
			String userCode, String dlvStsCode, String IS_UPLOAD, int pageNo) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = null;
		Cursor cursor = null;

		String[] colums = { "IS_UPLOAD", "deviceNumber", "orgCode", "userCode",
				"role", "operationMode", "mailCode", "dlvOrgCode",
				"dlvOrgName", "dlvOrgPostCode", "dlvStsCode", "signStsCode",
				"actualGoodsFee", "actualTax", "actualFee", "otherFee",
				"dlvUserCode", "dlvUserName", "undlvCauseCode",
				"undlvNextModeCode", "signerName", "interFlag",
				"operationTime", "dlvAddress", "signatureImg","createDate","longitude","latitude",
				"province","city","county","street","remark","undlvfollowCode","undlvCauseDesc" };
		try {

			SQLiteDatabase db = getReadableDatabase();

			cursor = db
					.query(TABLE_NAME,
							colums,
							"mailCode=? and userCode=? and dlvStsCode=? and IS_UPLOAD=?  and operationMode ='I' ",
							new String[] { mailCode, userCode, dlvStsCode,
									IS_UPLOAD }, null, null,
							"operationTime  desc ", (pageNo - 1) * 10 + ",10");
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					paramsMap = new LinkedHashMap<String, Object>();
					paramsMap.put("IS_UPLOAD", cursor.getString(0));
					paramsMap.put("deviceNumber", cursor.getString(1));
					paramsMap.put("orgCode", cursor.getString(2));
					paramsMap.put("userCode", cursor.getString(3));
					paramsMap.put("role", cursor.getString(4));
					paramsMap.put("operationMode", cursor.getString(5));
					paramsMap.put("mailCode", cursor.getString(6));
					paramsMap.put("dlvOrgCode", cursor.getString(7));
					paramsMap.put("dlvOrgName", cursor.getString(8));
					paramsMap.put("dlvOrgPostCode", cursor.getString(9));
					paramsMap.put("dlvStsCode", cursor.getString(10));
					paramsMap.put("signStsCode", cursor.getString(11));
					paramsMap.put("actualGoodsFee", cursor.getDouble(12));
					paramsMap.put("actualTax", cursor.getDouble(13));
					paramsMap.put("actualFee", cursor.getDouble(14));
					paramsMap.put("otherFee", cursor.getDouble(15));
					paramsMap.put("dlvUserCode", cursor.getString(16));
					paramsMap.put("dlvUserName", cursor.getString(17));
					paramsMap.put("undlvCauseCode", cursor.getString(18));
					paramsMap.put("undlvNextModeCode", cursor.getString(19));
					paramsMap.put("signerName", cursor.getString(20));
					paramsMap.put("interFlag", cursor.getString(21));
					paramsMap.put("operationTime", cursor.getString(22));
					paramsMap.put("dlvAddress", cursor.getString(23));
					paramsMap.put("signatureImg", cursor.getString(24));// 图片流basecode64转成的字符串
					paramsMap.put("createDate", cursor.getString(25));
					paramsMap.put("longitude", cursor.getString(26));
					paramsMap.put("latitude", cursor.getString(27));
					paramsMap.put("province", cursor.getString(28));
					paramsMap.put("city", cursor.getString(29));
					paramsMap.put("county", cursor.getString(30));
					paramsMap.put("street", cursor.getString(31));
					paramsMap.put("remark", cursor.getString(32));
					paramsMap.put("undlvfollowCode", cursor.getString(33));
					paramsMap.put("undlvCauseDesc", cursor.getString(34));
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

	
	public synchronized String Findisupload(String mailCode,
			String userCode, String dlvStsCode) {
		    String isupload = "";
		    Cursor cursor = null;
		    String[] colums = { "IS_UPLOAD"};
		    SQLiteDatabase db = getReadableDatabase();
		    cursor = db.query(TABLE_NAME,colums,"mailCode=? and userCode=? and dlvStsCode=?",new String[] { mailCode, userCode, dlvStsCode}, null, null, null,null);
		    if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
			    isupload=cursor.getString(0);
			}
		    }
		    return isupload;
		}
	public synchronized boolean SaveMail(JSONObject params)
			throws JSONException {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = null;
		if (params != null) {
			// 是否可以录入
			if (FindIsSave(params.getString("mailCode"))) {
				values = new ContentValues();
				values.put("IS_UPLOAD", params.getString("IS_UPLOAD"));
				values.put("deviceNumber", params.getString("deviceNumber"));
				values.put("orgCode", params.getString("orgCode"));
				values.put("userCode", params.getString("userCode"));
				values.put("role", params.getString("role"));
				values.put("operationMode", params.getString("operationMode"));
				values.put("mailCode", params.getString("mailCode"));
				values.put("dlvOrgCode", params.getString("dlvOrgCode"));
				values.put("dlvOrgName", params.getString("dlvOrgName"));
				values.put("dlvOrgPostCode", params.getString("dlvOrgPostCode"));
				values.put("dlvStsCode", params.getString("dlvStsCode"));
				values.put("signStsCode", params.getString("signStsCode"));
				values.put("actualGoodsFee", params.getDouble("actualGoodsFee"));
				values.put("actualTax", params.getDouble("actualTax"));
				values.put("actualFee", params.getDouble("actualFee"));
				values.put("otherFee", params.getDouble("otherFee"));
				values.put("dlvUserCode", params.getString("dlvUserCode"));
				values.put("dlvUserName", params.getString("dlvUserName"));
				values.put("undlvCauseCode", params.getString("undlvCauseCode"));
				values.put("undlvNextModeCode",
						params.getString("undlvNextModeCode"));
				values.put("signerName", params.getString("signerName"));
				values.put("interFlag", params.getString("interFlag"));
				values.put("operationTime", params.getString("operationTime"));
				values.put("dlvAddress", params.getString("dlvAddress"));
				values.put("signatureImg", params.getString("signatureImg"));// 图片流basecode64转成的字符串
				values.put("createDate", params.getString("createDate"));
				values.put("longitude", params.isNull("longitude")?"": params.getString("longitude"));
				values.put("latitude", params.isNull("latitude")?"":params.getString("latitude"));
				values.put("province",params.isNull("province")?"": params.getString("province"));
				values.put("city", params.isNull("city")?"":params.getString("city"));
				values.put("county", params.isNull("county")?"":params.getString("county"));
				values.put("street",params.isNull("street")?"": params.getString("street"));
				values.put("remark", params.isNull("remark")?"":params.getString("remark") );
				values.put("undlvfollowCode", params.isNull("undlvfollowCode")?"":params.getString("undlvfollowCode") );
				values.put("undlvCauseDesc", params.isNull("undlvCauseDesc")?"":params.getString("undlvCauseDesc"));
				db.insert(TABLE_NAME, null, values);
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * 修改是否上传
	 * 
	 * @param mailCode
	 * @param userCode
	 * @param dlvStsCode
	 * @param IS_UPLOAD
	 */
	public synchronized void updateMail(String mailCode, String IS_UPLOAD,
			String dlvStsCode,String createDate) {
		SQLiteDatabase db = getWritableDatabase();
		if (mailCode != null) {
			ContentValues contentValues = new ContentValues();
			contentValues.put("IS_UPLOAD", IS_UPLOAD);
			contentValues.put("operationTime", DateFormat.format("yyyyMMddkkmmss", new Date()).toString());
			db.update(TABLE_NAME, contentValues,
					"mailCode=?  and dlvStsCode=? and createDate= ? ", new String[] { mailCode,
							dlvStsCode ,createDate});
		}
	}

	public synchronized void deleteMail(String mailCode, String userCode,
			String dlvStsCode) {
		SQLiteDatabase db = getWritableDatabase();
		if (mailCode != null) {
			db.delete(TABLE_NAME,
					"mailCode=? and userCode=? and dlvStsCode=? ",
					new String[] { mailCode, userCode, dlvStsCode });
		}
	}
	
	public synchronized void deleteMailRe(String mailCode, String userCode,String IS_UPLOAD) {
		SQLiteDatabase db = getWritableDatabase();
		if (mailCode != null) {
			db.delete(TABLE_NAME,
					"mailCode=? and userCode=? and IS_UPLOAD=? ",
					new String[] { mailCode, userCode,IS_UPLOAD });
		}
	}

	public synchronized void deleteDisableMail(Integer days) {
		// 删除10天之前的数据
		Long time = new Date().getTime();
		Long wheretime = time - 1000 * 60 * 60 * 24 * days;
		String date= DateFormat.format("yyyyMMddkkmmss",wheretime).toString();
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_NAME, "createDate< " + date, null);
		db.delete("tb_mail_upload", "createDate< " + date, null);
	}

	protected void closeCursor(Cursor cursor) {
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
	}

}

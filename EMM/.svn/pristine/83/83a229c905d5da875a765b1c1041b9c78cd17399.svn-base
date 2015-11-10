package com.koala.emm.util;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import com.koala.emm.model.WarnPushMessage;
import com.lidroid.xutils.util.LogUtils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 建表规则
 * 1、第一位：id主键
 * 2、第二位：isEncry，0否1是
 * 3、字段所占的大小不能小于32（加密后的密文是32位）
 * 
 * @author mingsheng
 *
 */
public class DBHelper extends SQLiteOpenHelper{
	private final static int VERSION = 1; 
	
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	
	public DBHelper(Context context, String name) {
		this(context, name, null, VERSION);
	}
	
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		//删除所有的表格
		String[] tables = new String[]{"message","application"};
		for (String table : tables) {
			String deleteSQL = "drop table if exists " + table;
			db.execSQL(deleteSQL);
		}
		//创建表
		createMessageTab(db);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//删除所有的表格
				String[] tables = new String[]{"message","application"};
				for (String table : tables) {
					String deleteSQL = "drop table if exists " + table;
					db.execSQL(deleteSQL);
				}
				//创建表
				createMessageTab(db);
		
	}
	
	/**
	 * 创建消息列表
	 * @param db
	 */
	public void createMessageTab(SQLiteDatabase db){
		String sql = "create table IF NOT EXISTS message("
				+ "id integer primary key,"
				+ "isEncry varchar(32) default('0'),"
				+ "messageType varchar(32),"
				+ "updateUrl varchar(200),"
				+ "strategyUpdateType varchar(32),"
				+ "packageName varchar(50),"
				+ "receiveTime varchar(32),"
				+ "versionCode varchar(32),"
				+ "title varchar(100),"
				+ "text varchar(500)"
				+ ");";
		db.execSQL(sql);
	}
	
	/**
	 * 插入消息数据
	 * @param db
	 * @param wpm
	 */
	public void insertMessage(SQLiteDatabase db,WarnPushMessage wpm){
		String sql = "insert into message(messageType,updateUrl,strategyUpdateType,packageName,receiveTime,versionCode,title,text) values(?,?,?,?,?,?,?,?)";
		db.execSQL(sql, new String[]{wpm.getType(),wpm.getUpdateUrl(),wpm.getStrategyUpdateType(),wpm.getPackageName(),wpm.getReceiveTime(),wpm.getApp_edition(),wpm.getTitle(),wpm.getText()});
	}
	
	/**
	 * 查询所有消息
	 * @param db
	 * @return
	 */
	public List<WarnPushMessage> queryMessage(SQLiteDatabase db){
		String sql = "select * from message";
		List<WarnPushMessage> list = new ArrayList<WarnPushMessage>();
		Cursor cur = db.rawQuery(sql, null);
		while(cur.moveToNext()){
			WarnPushMessage wpm = new WarnPushMessage();
			wpm.setId(cur.getInt(cur.getColumnIndex("id")));
			wpm.setUpdateUrl(cur.getString(cur.getColumnIndex("updateUrl")));
			wpm.setType(cur.getString(cur.getColumnIndex("messageType")));
			wpm.setStrategyUpdateType(cur.getString(cur.getColumnIndex("strategyUpdateType")));
			wpm.setPackageName(cur.getString(cur.getColumnIndex("packageName")));
			wpm.setApp_edition(cur.getString(cur.getColumnIndex("versionCode")));
			wpm.setReceiveTime(cur.getString(cur.getColumnIndex("receiveTime")));
			wpm.setTitle(cur.getString(cur.getColumnIndex("title")));
			wpm.setText(cur.getString(cur.getColumnIndex("text")));
			list.add(wpm);
		}
		return list;
	}
	
	
	/**
	 * 根据type查询消息
	 * @param db
	 * @param messageType
	 * @return
	 */
	public List<WarnPushMessage> queryMessage(SQLiteDatabase db,String[] messageTypes){
		StringBuilder sql = new StringBuilder();
		sql.append("select * from message");
		if(messageTypes != null && messageTypes.length != 0){
			sql.append(" where ");
			for (String string : messageTypes) {
				sql.append(" messageType = '"+string+"'");
				sql.append(" or");
			}
			sql.setLength(sql.length() - 2);
			sql.append(" order by receiveTime desc");
		}
		
		List<WarnPushMessage> list = new ArrayList<WarnPushMessage>();
		Cursor cur = db.rawQuery(sql.toString(), null);
		while(cur.moveToNext()){
			WarnPushMessage wpm = new WarnPushMessage();
			wpm.setId(cur.getInt(cur.getColumnIndex("id")));
			wpm.setUpdateUrl(cur.getString(cur.getColumnIndex("updateUrl")));
			wpm.setType(cur.getString(cur.getColumnIndex("messageType")));
			wpm.setStrategyUpdateType(cur.getString(cur.getColumnIndex("strategyUpdateType")));
			wpm.setPackageName(cur.getString(cur.getColumnIndex("packageName")));
			wpm.setApp_edition(cur.getString(cur.getColumnIndex("versionCode")));
			wpm.setReceiveTime(cur.getString(cur.getColumnIndex("receiveTime")));
			wpm.setTitle(cur.getString(cur.getColumnIndex("title")));
			wpm.setText(cur.getString(cur.getColumnIndex("text")));
			list.add(wpm);
		}
		return list;
	}
	
	/**
	 * 根据type查询消息
	 * @param db
	 * @param messageType
	 * @return
	 */
	public void removeMessage(SQLiteDatabase db,String[] messageTypes){
		StringBuilder sql = new StringBuilder();
		sql.append("delete from message");
		if(messageTypes != null && messageTypes.length != 0){
			sql.append(" where ");
			for (String string : messageTypes) {
				sql.append(" messageType = '"+string+"'");
				sql.append(" or");
			}
			sql.setLength(sql.length() - 2);
			db.execSQL(sql.toString());
		}
	}
	
	/**
	 * 根据type查询消息
	 * @param db
	 * @param messageType
	 * @return
	 */
	public void removeMessageByIds(SQLiteDatabase db,List<Integer>ids){
		String sql  = null;
		db.beginTransaction();
		for (Integer id : ids) {
			sql  = "delete from message where id = "+ id;
			db.execSQL(sql);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}
	
	/**
	 * 获取最新消息的时间
	 * @param db
	 * @param messageTypes
	 * @return
	 */
	public String getLastTime(SQLiteDatabase db,String[] messageTypes){
		StringBuilder sql = new StringBuilder();
		sql.append("select * from message");
		if(messageTypes != null && messageTypes.length != 0){
			sql.append(" where ");
			for (String string : messageTypes) {
				sql.append(" messageType = '"+string+"'");
				sql.append(" or");
			}
			sql.setLength(sql.length() - 2);
			sql.append(" order by receiveTime desc");
			
		}
		
		Cursor cur = db.rawQuery(sql.toString(), null);
		while(cur.moveToNext()){
			return cur.getString(cur.getColumnIndex("receiveTime"));
		}
		return "no message";
	}
	/**
	 * 获取所有的表名
	 * @param db
	 * @return
	 */
	public List<String> queryTableNames(SQLiteDatabase db){
		String sql = "select name from sqlite_master where type='table' order by name";
		List<String> tabs = new ArrayList<String>();
		Cursor cur = db.rawQuery(sql, null);
		while (cur.moveToNext()) {
			tabs.add(cur.getString(cur.getColumnIndex("name")));
		}
		return tabs;
	}
	
	/**
	 * 加密所有的数据
	 * @param db
	 * @param code
	 */
	public void encryptData(SQLiteDatabase db ,String code){
		List<String> tabs = queryTableNames(db);
		for (String tab : tabs) {
			if(!tab.contains("metadata")){
			String querySql = "select * from "+tab+" where isEncry = '0'";
			Cursor cur = db.rawQuery(querySql, null);
			String[] columnNames = cur.getColumnNames();
			db.beginTransaction();
			while(cur.moveToNext()){
				//组织sql
				StringBuilder sb = new StringBuilder();
				sb.append("update " + tab +" set ");
				sb.append(columnNames[1]+" = "+"'1' ");
				sb.append(",");
				for (int i = 2; i < columnNames.length; i++) {
					//加密前
					String content = cur.getString(cur.getColumnIndex(columnNames[i]));
					if(content != null){
						//加密后
						String content2 = null;
						try {
							content2 = AESUtils3.encrypt(code, content);
						} catch (GeneralSecurityException e) {
							e.printStackTrace();
						}
						sb.append(columnNames[i]+" = '"+content2+"'");
						sb.append(",");
					}
				}
				sb.setLength(sb.length() - 1);
				sb.append(" where "+columnNames[0]+" = "+ cur.getInt(cur.getColumnIndex(columnNames[0])));
				LogUtils.e("加密SQL = "+ sb.toString());
				db.execSQL(sb.toString());
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			cur.close();
		}
		
		
	  }
	}
	
	/**
	 * 解密所有的数据
	 * @param db
	 * @param code
	 */
	public void decryptData(SQLiteDatabase db ,String code){
		List<String> tabs = queryTableNames(db);
		for (String tab : tabs) {
			if(!tab.contains("metadata")){
			String querySql = "select * from "+tab+" where isEncry = '1'";
			Cursor cur = db.rawQuery(querySql, null);
			String[] columnNames = cur.getColumnNames();
			db.beginTransaction();
			while(cur.moveToNext()){
				//组织sql
				StringBuilder sb = new StringBuilder();
				sb.append("update " + tab +" set ");
				sb.append(columnNames[1]+" = "+"'0' ");
				sb.append(",");
				for (int i = 2; i < columnNames.length; i++) {
					//解密前
					String content = cur.getString(cur.getColumnIndex(columnNames[i]));
					if(content != null){
						//解密后
						String content2 = null;
						try {
							content2 = AESUtils3.decrypt(code, content);
						} catch (GeneralSecurityException e) {
							e.printStackTrace();
						}
						sb.append(columnNames[i]+" = '"+content2+"'");
						sb.append(",");
					}
				}
				sb.setLength(sb.length() - 1);
				sb.append(" where "+columnNames[0]+" = "+ cur.getInt(cur.getColumnIndex(columnNames[0])));
				LogUtils.e("加密SQL = "+ sb.toString());
				db.execSQL(sb.toString());
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			cur.close();
		}
	  }
	}

}

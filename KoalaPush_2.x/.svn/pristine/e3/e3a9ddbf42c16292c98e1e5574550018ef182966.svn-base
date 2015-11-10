package com.arvin.koalapush.util;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.arvin.koalapush.bean.SimpleRequestBean;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper{
	private final static int VERSION = 1; 
	private static boolean bo = false;
	
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
//		String[] tables = new String[]{"message","application"};
//		for (String table : tables) {
//			String deleteSQL = "drop table if exists " + table;
//			db.execSQL(deleteSQL);
//		}
		createRequestTab(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//删除所有的表格
//		String[] tables = new String[]{"message","application"};
//		for (String table : tables) {
//			String deleteSQL = "drop table if exists " + table;
//			db.execSQL(deleteSQL);
//		}
		
	}
	
	/**
	 * 创建请求失败列表
	 * @param db
	 */
	public void createRequestTab(SQLiteDatabase db){
		String sql = "create table IF NOT EXISTS requestList("
				+ "id integer primary key,"
				+ "class_name varchar(256),"
				+ "method_name varchar(128),"
				+ "method_behavior varchar(128),"
				+ "log_content varchar(2048),"
				+ "message_id int,"
				+ "state int)";
		db.execSQL(sql);
	}
	/**
	 * 添加失败请求
	 * @param db
	 * @param params
	 */
	public void insertRequest(SQLiteDatabase db,String class_name,long method_name, String method_behavior
			,String log_content,int message_id,int state){
		if(bo){
			String sql = "insert or IGNORE INTO requestList(class_name,method_name,method_behavior,log_content,message_id,state) "
					+ "values(?,?,?,?,?,?)";
			db.execSQL(sql, new Object[]{class_name,method_name,method_behavior,log_content,message_id,state});
		}
	}
	/**
	 * 根据id删除请求
	 * @param db
	 * @param id
	 */
	public void deleteRequestById(SQLiteDatabase db){
		if(bo){
			String sql = "delete from requestList";
			db.execSQL(sql);
		}
	}
	/**
	 * 查询所有的请求
	 */
	public String queryRequest(SQLiteDatabase db){
		if(bo){
			String sql = "select * from requestList";
			List<SimpleRequestBean> list = new ArrayList<SimpleRequestBean>();
			Cursor cur = db.rawQuery(sql.toString(), null);
			while(cur.moveToNext()){
				SimpleRequestBean srb = new SimpleRequestBean();
				srb.setId(cur.getInt(cur.getColumnIndex("id")));
				srb.setClass_name(cur.getString(cur.getColumnIndex("class_name")));
				srb.setMethod_name(cur.getString(cur.getColumnIndex("method_name")));
				srb.setMethod_behavior(cur.getString(cur.getColumnIndex("method_behavior")));
				srb.setLog_content(cur.getString(cur.getColumnIndex("log_content")));
				srb.setMessage_id(cur.getInt(cur.getColumnIndex("message_id")));
				srb.setState(cur.getInt(cur.getColumnIndex("state")));
				list.add(srb);
			}
			if(list.size()<=0){
				return null;
			}
			return JSONObject.toJSONString(list);
		}else{
			return null;
		}
	}
	
	
}

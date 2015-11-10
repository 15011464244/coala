package com.cn.net.cnpl.db;

import com.cn.net.cnpl.Global;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class UserDaoHelper extends BaseSQLiteOpenHelper implements DatabaseConstants {
	
	protected static final String TABLE_NAME = "user";
	
	protected static final String MOBILE = "mobile";   
	protected static final String LOGIN_NAME = "loginName";   
	protected static final String PASSWORD = "password";    
	protected static final String ISPWD = "isPwd";   
	protected static final String ISAUTOLOGIN = "isAutoLogin";
	protected static final String FLAG = "flag";
	protected static final String USER_NAME = "userName";   
	protected static final String TRANSITCODE = "transitCode";   
	protected static final String TRANSITNAME = "transitName"; 
	protected static final String TELEPHONE = "telephone";
	protected static final String ORGCODE = "orgCode";
	protected static final String ORGNAME = "orgName"; 
	protected static final String KEY = "key"; 
	
	protected static final String[] ALL_COLUMS = {MOBILE,PASSWORD,LOGIN_NAME,ISPWD,ISAUTOLOGIN,FLAG,USER_NAME,TRANSITCODE,TRANSITNAME,ORGCODE,ORGNAME,TELEPHONE,KEY};
	
	
	public UserDaoHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		onCreate(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		if(!tabIsExist(db,TABLE_NAME)){
			db.execSQL("CREATE TABLE "+TABLE_NAME+" ("
					  +"["+MOBILE+"] VARCHAR(11) NOT NULL,"  
					  +"["+PASSWORD+"]  VARCHAR(30) NOT NULL," 
					  +"["+LOGIN_NAME+"]  VARCHAR(30)," 
					  +"["+ISPWD+"] CHAR(1)," 
					  +"["+FLAG+"] CHAR(1)," 
					  +"["+USER_NAME+"]  VARCHAR(50)," 
					  +"["+TRANSITCODE+"]  VARCHAR(30)," 
					  +"["+TRANSITNAME+"]  VARCHAR(100)," 
					  +"["+ORGCODE+"]  VARCHAR(30)," 
					  +"["+ORGNAME+"]  VARCHAR(100)," 
					  +"["+TELEPHONE+"]  VARCHAR(100)," 
					  +"["+KEY+"]  VARCHAR(100)," 
					  +"["+ISAUTOLOGIN+"] CHAR(1));");
		}
		
	}

	
	public boolean tabIsExist(SQLiteDatabase db, String tabName) {
		boolean result = false;
		if(tabName == null){
			return result;
		}
		Cursor cursor = null;
		String sql = "select count(*) as c from sqlite_master where type='table' and name = '"+tabName.trim()+"'";
		try {
			cursor = db.rawQuery(sql, null);
			if(cursor.moveToNext()){
				int count = cursor.getInt(0);
				if(count>0){
					result = true;
				}
			}
		} catch (Exception e) {
			Log.e( Global.DIALOG_NAME, e.getMessage());
			return result;
		} finally{
			if (cursor != null) {
	            cursor.close();
	        }
		}
		return result;
	}

}

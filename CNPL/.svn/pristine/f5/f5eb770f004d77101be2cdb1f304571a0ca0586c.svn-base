package com.cn.net.cnpl.db;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.cn.net.cnpl.Global;
import com.cn.net.cnpl.model.User;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

public class BaseSQLiteOpenHelper extends SQLiteOpenHelper {

	public BaseSQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		db.execSQL("DROP TABLE IF EXISTS tb_transfer ");
		db.execSQL("DROP TABLE IF EXISTS TB_PDA_APPLE_DLV_MAIL ");
//		db.execSQL("DROP TABLE IF EXISTS STATE_FOLLOW ");
//		db.execSQL("DROP TABLE IF EXISTS user ");
//		db.execSQL("DROP TABLE IF EXISTS tb_mail_fllow_alram" );
		
	}
	
	
	
	public boolean tabIsExist(SQLiteDatabase db,String tabName){
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
	
	public void readFromAsset(String fileName,SQLiteDatabase database, AssetManager assetManager) { 
		try {
			InputStream in = assetManager.open(fileName); 
		    BufferedReader bufferedReader = new BufferedReader(
		            new InputStreamReader(in,"GBK"));
		    String sqlUpdate = null;
		    database.beginTransaction();
		    while ((sqlUpdate = bufferedReader.readLine()) != null) {
		        if (!TextUtils.isEmpty(sqlUpdate)) {
		            database.execSQL(sqlUpdate);
		        }
		    }
		    database.setTransactionSuccessful();  
		    database.endTransaction();     
		    bufferedReader.close();
		    in.close();
		} catch (Exception e) {
			Log.e( Global.DIALOG_NAME, e.getMessage());
		}
       
    } 
	
	public String StringFormate(Object str) {
		if (str != null && !"null".equals(str)) {
			return str.toString();
		} else {
			return "";
		}
	}
	
	protected void closeCursor(Cursor cursor) {
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
	}
	
}

package com.cn.net.cnpl.db.dao;

import java.util.Date;
import java.util.List;

import org.json.JSONException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.text.format.DateFormat;
import android.util.Log;

import com.cn.net.cnpl.Global;
import com.cn.net.cnpl.db.UserDaoHelper;
import com.cn.net.cnpl.model.User;

public class UserDao extends UserDaoHelper {

	public UserDao(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public UserDao(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	
	public synchronized User FindUser() {
		Cursor cursor = null;
		User user = null;
		
		try {
			 SQLiteDatabase db = getReadableDatabase();
			 cursor = db.query(TABLE_NAME,  ALL_COLUMS, "", null, null, null, null);
			 if (cursor.getCount() > 0) {
				 if (cursor.moveToFirst()) {
					user  = ToModel(cursor,User.class);
				 }
			 }
		}
		catch(Exception e) {
//			Log.e(Global.DIALOG_NAME,e.getMessage());
		}
		finally {
			closeCursor(cursor);
		}
		return user;
	}
	
	public synchronized long SaveUser(User user) {
		
		SQLiteDatabase db = getWritableDatabase();
		long result = 0;
		
		ContentValues values = null;
		if(user != null){
			db.delete(TABLE_NAME,null, null);
			values = new ContentValues();
			values.put(MOBILE, user.getMobile());
			values.put(PASSWORD, user.getPassword());
			values.put(LOGIN_NAME,user.getLoginName());
			values.put(ISPWD, user.getIsPwd());
			values.put(ISAUTOLOGIN, user.getIsAutoLogin());
			values.put(FLAG, user.getFlag());
			values.put(USER_NAME, user.getUserName());
			values.put(TRANSITCODE, user.getTransitCode());
			values.put(TRANSITNAME, user.getTransitName());
			values.put(ORGCODE, user.getOrgCode());
			values.put(ORGNAME, user.getOrgName());
			values.put(ORGNAME, user.getOrgName());
			values.put(TELEPHONE, user.getTelephone());
			values.put(KEY, user.getKey());
			result = db.insert(TABLE_NAME, null, values);
		}
		return result;
	}
	/**
	 * 清除数据
	 * @return 删除失败返回0，成功则返回删除的条数
	 */
	public long DelUser() {
		
		SQLiteDatabase db = getWritableDatabase();
		long result = 0;
		result=db.delete(TABLE_NAME,null, null);
		return result;
	}
	/**
	 * 取消自动登录 
	 * @return
	 */
	public long UpdateUser() {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv=new ContentValues();
		long result = 0;
		cv.put(ISAUTOLOGIN, "1");
		result=db.update(TABLE_NAME,cv,null, null);
		return result;
	}
	
	private User ToModel(Cursor cursor,Class<?> cls) {
		User user = null;
		try 
		{
			user = new User();
			if(cursor.getString(0)!=null && !("").equals(cursor.getString(0))) {
				user.setMobile(cursor.getString(0));
		    }
			if(cursor.getString(1)!=null && !("").equals(cursor.getString(1))) {
				user.setPassword(cursor.getString(1));
		    }
			if(cursor.getString(2)!=null && !("").equals(cursor.getString(2))) {
				user.setLoginName(cursor.getString(2));
		    }
			if(cursor.getString(3)!=null && !("").equals(cursor.getString(3))) {
				user.setIsPwd(cursor.getString(3));
		    }
			if(cursor.getString(4)!=null && !("").equals(cursor.getString(4))) {
				user.setIsAutoLogin(cursor.getString(4));
		    }
			if(cursor.getString(5)!=null && !("").equals(cursor.getString(5))) {
				user.setFlag(cursor.getString(5));
		    }
			if(cursor.getString(6)!=null && !("").equals(cursor.getString(6))) {
				user.setUserName(cursor.getString(6));
		    }
			if(cursor.getString(7)!=null && !("").equals(cursor.getString(7))) {
				user.setTransitCode(cursor.getString(7));
		    }
			if(cursor.getString(8)!=null && !("").equals(cursor.getString(8))) {
				user.setTransitName(cursor.getString(8));
		    } 	
			if(cursor.getString(9)!=null && !("").equals(cursor.getString(9))) {
				user.setOrgCode(cursor.getString(9));
		    } 	
			if(cursor.getString(10)!=null && !("").equals(cursor.getString(10))) {
				user.setOrgName(cursor.getString(10));
		    }
			if(cursor.getString(11)!=null && !("").equals(cursor.getString(11))) {
				user.setTelephone(cursor.getString(11));
		    }
			if(cursor.getString(12)!=null && !("").equals(cursor.getString(12))) {
				user.setKey(cursor.getString(12));
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	
	protected void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }
	
	public void UpdateMail(String telephone) throws JSONException {

		SQLiteDatabase db = getWritableDatabase();
		if (telephone != null) {
			ContentValues contentValues = new ContentValues();
			contentValues.put(TELEPHONE, telephone);
			db.update(TABLE_NAME, contentValues,null, null);
		}
	}
}

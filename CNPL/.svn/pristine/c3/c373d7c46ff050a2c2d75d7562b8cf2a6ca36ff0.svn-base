package com.cn.net.cnpl.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class ProjReasonDaoHelper  extends BaseSQLiteOpenHelper implements DatabaseConstants  {

	protected static final String TABLE_NAME = "tb_Proj_Reason";
	
	protected static final String CREATESQL ="create table tb_Proj_Reason (projId varchar(30)  ,projName  varchar(30) ,projCd  varchar(50)" +
			",cd  VARCHAR2(1),desc VARCHAR2(200), feedBackType varchar(30));";
	
	public ProjReasonDaoHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		onCreate(getWritableDatabase());
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		if(!tabIsExist(db,TABLE_NAME)){
			db.execSQL(CREATESQL);
		}
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
		
	}
}

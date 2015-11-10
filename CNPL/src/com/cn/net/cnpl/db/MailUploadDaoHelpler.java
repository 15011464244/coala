package com.cn.net.cnpl.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class MailUploadDaoHelpler extends BaseSQLiteOpenHelper implements DatabaseConstants {

	protected static final String TABLE_NAME = "tb_mail_upload";
	
	protected static final String CREATESQL ="create table tb_mail_upload(orgCode varchar(25)  ,upload_time  varchar(25) ,sid long not null,userCode  VARCHAR2(20),IS_UPLOAD VARCHAR2(1), createDate  varchar(25) ,mail varcahr(30),signatureImg clob);";

	public MailUploadDaoHelpler(Context context, String name, CursorFactory factory,
			int version) {
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

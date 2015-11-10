package com.cn.net.cnpl.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class MailFollowDaoHelper extends BaseSQLiteOpenHelper implements
		DatabaseConstants {
//	邮件号	mail_num	varchar(20)	20		FALSE	FALSE	FALSE
//	经度	longitude	NUMBER			FALSE	FALSE	FALSE
//	纬度	latitude	NUMBER			FALSE	FALSE	FALSE
//	上传时间	upload_time	date			FALSE	FALSE	FALSE
//	国家	country	varchar(200)	200		FALSE	FALSE	FALSE
//	省	province	varchar(100)	100		FALSE	FALSE	FALSE
//	市	city	varchar(100)	100		FALSE	FALSE	FALSE
//	县	county	varchar(100)	100		FALSE	FALSE	FALSE
//	街道	street	varchar(300)	300		FALSE	FALSE	FALSE
//	创建时间	create_time	date			FALSE	FALSE	FALSE
//	id	id	long			FALSE	FALSE	FALSE

	protected static final String TABLE_NAME = "tb_mail_follow";

	protected static final String CREATESQL = "create table tb_mail_follow (  mail_num VARCHAR2(20) , longitude VARCHAR2(20) ,latitude VARCHAR2(20), upload_time VARCHAR2(30) ," +
			"country   VARCHAR2(30),province   varchar(100) ,city	varchar(100),county	varchar(100),street	varchar(300),create_time  VARCHAR2(30) ,id number,operatorType char(1),oldSid long );";
	public MailFollowDaoHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		onCreate(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (!tabIsExist(db, TABLE_NAME)) {
			db.execSQL(CREATESQL);
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);

	}

}

package com.cn.net.cnpl.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class MailHandHelper extends BaseSQLiteOpenHelper implements DatabaseConstants {

	protected static final String TABLE_NAME = "tb_mail_hand";
	
	protected static final String CREATESQL ="create table tb_mail_hand(sid long not null," +
			"out_code varcahr(20),in_code varcahr(20),org_type char(1)," +
			"hand_type char(1),hand_state char(1),begin_time varcahr(30)," +
			"end_time varcahr(30),create_by varcahr(20),is_shift_stop char(1)," +
			"shift_time varcahr(30),certificate char(1),longitude varcahr(30),latitude varcahr(30),province   varchar(100) ,city	varchar(100),county	varchar(100),street	varchar(300)," +
			"actualCount varcahr(10));";

	public MailHandHelper(Context context, String name, CursorFactory factory,
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

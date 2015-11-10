package com.cn.net.cnpl.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class WorkLogDaoHelper extends BaseSQLiteOpenHelper implements DatabaseConstants{

	protected static final String TABLE_NAME = "TB_MAIL_LOG";

//	create table "tb_mail_log"  (
//	   "mail_num"           varchar(20),
//	   "action_date"        datetime,
//	   "action"             char(1),
//	   "char(1)"            char(1)
//	);

//	comment on table "tb_mail_log" is
//	'邮件日志';
//
//	comment on column "tb_mail_log"."mail_num" is
//	'邮件号';
//
//	comment on column "tb_mail_log"."action_date" is
//	'操作时间';
//
//	comment on column "tb_mail_log"."action" is
//	'1接入，2转出，3 接入上传，4 转出上传';
//
//	comment on column "tb_mail_log"."char(1)" is
//	'  1成功   0  失败';

	protected static final String CREATESQL ="create table TB_MAIL_LOG(mail_num  varchar2(20)   NOT NULL,  action_date VARCHAR2(30)  , action char(1)) " ;	
	public WorkLogDaoHelper(Context context, String name, CursorFactory factory,
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
	
}

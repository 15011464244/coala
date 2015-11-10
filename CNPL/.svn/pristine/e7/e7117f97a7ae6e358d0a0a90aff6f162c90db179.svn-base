package com.cn.net.cnpl.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class MailDaoHelper extends BaseSQLiteOpenHelper implements DatabaseConstants{

	protected static final String TABLE_NAME = "TB_PDA_APPLE_DLV_MAIL";
	
//	role 1 揽收 2投递
//	interFlag  国际标示 国内国际是 1，0
	protected static final String CREATESQL ="create table TB_PDA_APPLE_DLV_MAIL(IS_UPLOAD CHAR(1)  NOT NULL,  deviceNumber VARCHAR2(64)  , " +
			"orgCode    VARCHAR2(20) not null,userCode  VARCHAR2(20) not null,role  char(1),operationMode char(1)," +
			"mailCode VARCHAR2(20) not null,dlvOrgCode   VARCHAR2(20),dlvOrgName  VARCHAR2(200),dlvOrgPostCode  VARCHAR2(12),dlvStsCode    VARCHAR2(1)," +
			"signStsCode  VARCHAR2(1),actualGoodsFee  NUMBER(18,2),actualTax   NUMBER(9,2),actualFee   NUMBER(18,2),otherFee  NUMBER(18,2),dlvUserCode  VARCHAR2(20)," +
			"dlvUserName   VARCHAR2(100),undlvCauseCode VARCHAR2(2),undlvNextModeCode  VARCHAR2(1), " +
			"signerName VARCHAR2(100),interFlag   VARCHAR2(1),createDate  varchar(25),operationTime varchar(25),dlvAddress varchar2(300), signatureImg clob,longitude varcahr(30),latitude varcahr(30),province   varchar(100) ,city	varchar(100),county	varchar(100),street	varchar(300),remark varchar(200),undlvfollowCode varchar(200),undlvCauseDesc varchar(200));";
	
	public MailDaoHelper(Context context, String name, CursorFactory factory,
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
	
//	@Override
//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//		onCreate(db);
//		
//	}
}

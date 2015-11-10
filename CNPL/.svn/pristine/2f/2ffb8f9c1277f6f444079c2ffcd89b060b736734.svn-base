package com.cn.net.cnpl.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class ResOrgDaoHelper extends BaseSQLiteOpenHelper implements DatabaseConstants {
	

	protected static final String TABLE_NAME = "RES_ORG";
	
//	"org_code":"36300042",      //机构代码
//	"org_sname":"漳州丹霞速递",   //机构名称
//	"prov_code":"35",          //省份代码
//	"prov_name":"福建",       //省份名称
//	"city_code":"350600",        //地市代码
//	"city_name":"漳州市",       //地市名称
//	"county_code":"350602",     //区县代码
//	"county_name":"芗城区",    //区县名称
//	"postcode":"363007",       //邮编
//	"orgTraditionalName":"363007",   //机构繁体中文名称
//	"orgEnName":"363007"       //机构英文名称

	
	protected static final String CREATESQL = "create table RES_ORG ( org_code VARCHAR2(8), org_sname   VARCHAR2(100), prov_code   VARCHAR2(2)," +
			" prov_name VARCHAR2(20),  city_code VARCHAR2(10),  city_name  VARCHAR2(50),  county_code  VARCHAR2(10),  county_name  VARCHAR2(100)," +
			" postcode  VARCHAR2(10),  orgTraditionalName  VARCHAR2(60),  orgEnName  VARCHAR2(60));";
	
//	private AssetManager assetManager = null;
	
	public ResOrgDaoHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
//		assetManager = context.getAssets();
		onCreate(getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if(!tabIsExist(db,TABLE_NAME)){// TODO Auto-generated method stub
			db.execSQL(CREATESQL);
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
		
	}

}

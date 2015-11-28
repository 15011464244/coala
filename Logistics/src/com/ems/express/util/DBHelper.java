package com.ems.express.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

import com.alibaba.fastjson.JSONArray;
import com.ems.express.App;
import com.ems.express.R;
import com.ems.express.adapter.message.ReceivePaymnetBean;
import com.ems.express.adapter.message.SendNoticeBean;
import com.ems.express.bean.ChatMessageBean;
import com.ems.express.bean.City;
import com.ems.express.bean.DeliveryMessageBean;
import com.ems.express.bean.OrderInfoBean;
import com.ems.express.bean.PeopleInfo;
import com.ems.express.net.Carrier;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	private static final int VERSION = 6;
	private Context mContext;
	private SQLiteDatabase curDb;

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		mContext = context;
	}
   
	public DBHelper(Context context, String name) {
		this(context, name, VERSION);
		mContext = context;
	}

	public DBHelper(Context context, String name, int version) {
		this(context, name, null, version);
		mContext = context;
	}
	
	public void onCreate(SQLiteDatabase db) {
		curDb = db;
		// 删除所有的表
		String [] deleteTable = new String[]{"friend","carrier","message","deliveryMessage","sendMessage","city","province","county","senderinfo","orderInfo","receiveMessage","senderMessage"};
		for(int i =0;i<deleteTable.length;i++){
			db.execSQL("drop table if exists "+deleteTable[i]);
		}
		//db.execSQL("drop database ems.db;");//删除数据库
		//db.execSQL("create database ems.db;");//创建数据库
		// 创建数据库sql语句
		// 成色列表
		String sql1 = "create table friend(userId integer primary key ,userName varchar(50),userIcon varchar(100),userState Boolean)";
//		String sql2 = "create table message(msgId integer primary key autoincrement,msgType Boolean,msgContent varchar(100),msgTime varchar(100),msgStatus Boolean,userId integer, foreign key(userId) references friend(userId))";
		String carrier = "create table carrier("
				+ "id integer primary key,"
				+ "longitude double not null,"
				+ "latitude double not null,"
				+ "people varchar(32) not null,"
				+ "mobile varchar(22) not null,"
				+ "address varchar(128),"
				+ "clientId varchar(256) not null,"
				+ "employeeNo varchar(128),"
				+ "code varchar(128),"
				+ "sid varchar(128))";
		// 执行创建数据库表操作
		db.execSQL(sql1);
//		db.execSQL(sql2);
		db.execSQL(carrier);
		createMessage(db);
		deliveryMessage(db);
		createCounty(db);
		createCity(db);
		createProvince(db);
		sendNotice(db);
		createReceiveMessage(db);
		createSenderMessage(db);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		// 删除所有的表
				String [] deleteTable = new String[]{"friend","carrier","message","deliveryMessage","sendMessage","senderinfo","receiveMessage","senderMessage"};
				for(int i =0;i<deleteTable.length;i++){
					db.execSQL("drop table if exists "+deleteTable[i]);
				}
				//db.execSQL("drop database ems.db;");//删除数据库
				//db.execSQL("create database ems.db;");//创建数据库
				// 创建数据库sql语句
				// 成色列表
				String sql1 = "create table friend(userId integer primary key ,userName varchar(50),userIcon varchar(100),userState Boolean)";
//				String sql2 = "create table message(msgId integer primary key autoincrement,msgType Boolean,msgContent varchar(100),msgTime varchar(100),msgStatus Boolean,userId integer, foreign key(userId) references friend(userId))";
				String carrier = "create table carrier("
						+ "id integer primary key,"
						+ "longitude double not null,"
						+ "latitude double not null,"
						+ "people varchar(32) not null,"
						+ "mobile varchar(22) not null,"
						+ "address varchar(128),"
						+ "clientId varchar(256) not null,"
						+ "employeeNo varchar(128),"
						+ "code varchar(128),"
						+ "sid varchar(128))";
				// 执行创建数据库表操作
				db.execSQL(sql1);
//				db.execSQL(sql2);
				db.execSQL(carrier);
				createMessage(db);
				deliveryMessage(db);
				createCounty(db);
				createCity(db);
				createProvince(db);
				sendNotice(db);
				createReceiveMessage(db);
				createSenderMessage(db);
	}
	
	/**
	 * 消息表
	 * @param db
	 */
	public void createMessage(SQLiteDatabase db){
		String sql = "create table message("
				+ "msgId integer primary key,"
				+ "employeeNo varchar(128),"
				+ "messageStatus varchar(2),"
				+ "messageSendStatus varchar(2),"
				+ "sendIsReceive varchar(2),"
				+ "messageType varchar(2),"
				+ "messageContent varchar(528),"
				+ "url varchar(200),"
				+ "messageTime date,"
				+ "serverUrl varchar(200),"
				+ "speechMessageTime integer)";
		db.execSQL(sql);
	}
	/**
	 * 接受的支付信息表
	 * @param db    
					//以下的是支付的相关的字段
	private int receiveId;
	private String orderNum;//订单号
	private double price;//应付金额
	private String mailStatus;//订单的状态
	private String mobNum;//电话号
	private String userCode;//揽投员的名字
	
	
    //	以下是积分相关的字段
	
	private String messageStatus;       //状态
	private String  integral;        // 积分
	private String invitedMobile;  // 邀请的手机号
	 */
	public void createReceiveMessage(SQLiteDatabase db){
		String sql = "create table receiveMessage("
				+ "receiveId integer primary key,"
				+ "orderNum varchar(128),"
				+ "messageStatus varchar(2),"
				+ "price varchar(8),"
				+ "messageTime date,"
				+ "mailStatus varchar(2)," 
				+ "mobNum varchar(22),"
				+"loginUser varchar(22) not null,"
				+ "userCode varchar(22),"
				+ "orgCode varchar(22),"
				+ "invitedMobile  varchar(128)," 
				+ "integral varchar(10)"
				+ ")";
		db.execSQL(sql);
	}
	/*
	 * senderId
	 * 
	 */
	
	public void createSenderMessage(SQLiteDatabase db){
		String sql = "create table IF NOT EXISTS senderMessage("
				+ "senderId integer primary key ,"
				+ "sid varchar(128),"
				+ "userId varchar(20),"
				+ "mobile varchar(20)," // 注册的手机号
				+ "userType varchar(2)," // 寄件人是1，收件人是2
				+ "userName varchar(10)," 
				+ "userTel varchar(22)," //填写的手机号
				+"provCode varchar(10),"
				+ "cityCode varchar(10),"
				+ "countyCode varchar(10),"
				+ "address  varchar(30)," 
				+ "prov varchar(10),"
				+ "city varchar(16),"
				+ "county varchar(20),"
				+ "createDate text,"
				+ "updateDate text"
				+ ")";
		db.execSQL(sql);
	}
	/**
	 *  添加一条寄件人的信息
	 * @param db
	 * @param 
	 */
	public void insertSenderMessage(SQLiteDatabase db,String sid,String userId,String mobile,String userType,String userName,String userTel,String provCode,String cityCode,String countyCode,String address,String prov,String city,String county,String createDate,String updateDate){
		String sql ="insert into senderMessage(sid,userId,mobile,userType,userName,userTel,provCode,cityCode,countyCode,address,prov,city,county,createDate,updateDate) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		db.execSQL(sql, new Object[]{sid,userId,mobile,userType,userName,userTel,provCode,cityCode,countyCode,address,prov,city,county,createDate,updateDate});
	}
	
	public List<PeopleInfo> querySenderMessage(SQLiteDatabase db,String mobile,String type){
		String sql = "select * from senderMessage where mobile = ? and userType = ?";
		Cursor cur = null;
		try {
		cur = db.rawQuery(sql, new String[]{mobile,type});
		List<PeopleInfo> list = new ArrayList<PeopleInfo>();
			if (cur.getCount() > 0) {

				while (cur.moveToNext()) {
					PeopleInfo peopleInfo = new PeopleInfo();
					peopleInfo.setId(cur.getString(cur.getColumnIndex("sid")));
					peopleInfo.setUserId(cur.getString(cur
							.getColumnIndex("userId")));
					peopleInfo.setUserId(cur.getString(cur
							.getColumnIndex("userType")));
					peopleInfo.setName(cur.getString(cur
							.getColumnIndex("userName")));
					peopleInfo.setPhone(cur.getString(cur
							.getColumnIndex("userTel")));
					peopleInfo.setProvCode(cur.getString(cur
							.getColumnIndex("provCode")));
					peopleInfo.setCityCode(cur.getString(cur
							.getColumnIndex("cityCode")));
					peopleInfo.setCountyCode(cur.getString(cur
							.getColumnIndex("countyCode")));
					peopleInfo.setLocation(cur.getString(cur
							.getColumnIndex("address")));
					peopleInfo
							.setProv(cur.getString(cur.getColumnIndex("prov")));
					peopleInfo
							.setCity(cur.getString(cur.getColumnIndex("city")));
					peopleInfo.setCounty(cur.getString(cur
							.getColumnIndex("county")));
					peopleInfo.setCreateDate(cur.getString(cur.getColumnIndex("createDate")));
					list.add(peopleInfo);
				}
				cur.close();
				return list;
			}
		} catch (Exception e) {
			Log.e("gongjie", "查询出错"+ e);
		}
		return null;
	}
	public void updateSenderMessage(SQLiteDatabase db,PeopleInfo people){
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String updateDate = format.format(date);
		String sql = "update senderMessage set userId = '"+people.getUserId()
				+"',userType='"+people.getType()
				+"',mobile='"+SpfsUtil.loadPhone()
				+"',userTel='"+people.getPhone()
				+"',userName='"+people.getName()
				+"',provCode='"+people.getProvCode()
				+"',cityCode='"+people.getCityCode()
				+"',countyCode='"+people.getCountyCode()
				+"',address='"+people.getLocation()
				+"',prov='"+people.getProv()
				+"',city='"+people.getCity()
				+"',county='"+people.getCounty()
				+"',updateDate='"+updateDate+"' where sid = '"
				+people.getId()+"'";
		db.execSQL(sql);
	} 
	public void deleteSenderMessage(SQLiteDatabase db,String createDate){
		String sql = "delete from senderMessage where createDate ='"+createDate+"'";
		Log.e("gongjie", "删除查询"+sql);
		db.execSQL(sql);
	}
	public void deleteSenderMessageByPhone(SQLiteDatabase db,String mobile,String type){
		String sql = "delete from senderMessage where mobile ="+mobile+" and userType ="+type;
		Log.e("gongjie", "删除查询"+sql);
		db.execSQL(sql);
	}
	
	/**
	 *  添加一条收件人的信息
	 * @param db
	 * @param 
	 */
	public void insertReceiverMessage(SQLiteDatabase db,String sid,String userId,String mobile,String userType,String userName,String userTel,String provCode,String cityCode,String countyCode,String address,String prov,String city,String county,Date createDate,Date updateDate){
		String sql ="insert into receiverMessage(sid,userId,mobile,userType,userName,userTel,provCode,cityCode,countyCode,address,prov,city,county,createDate,updateDate) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		db.execSQL(sql, new Object[]{sid,userId,mobile,userType,userName,userTel,provCode,cityCode,countyCode,address,prov,city,county,createDate,updateDate});
	}
	
	/*
	 * receiveId
	 * 
	 */
	
	public void createReceiverMessage(SQLiteDatabase db){
		String sql = "create table IF NOT EXISTS receiverMessage("
				+ "receiverId integer primary key AUTO_INCREMENT,"
				+ "sid varchar(128),"
				+ "userId varchar(20),"
				+ "mobile varchar(20)," // 注册的手机号
				+ "userType varchar(2)," // 寄件人是1，收件人是2
				+ "userName varchar(10)," 
				+ "userTel varchar(22)," //填写的手机号
				+ "provCode varchar(10) "
				+ "cityCode varchar(10),"
				+ "countyCode varchar(10),"
				+ "address  varchar(30)," 
				+ "prov varchar(10)"
				+ "city varchar(16)"
				+ "county varchar(20)"
				+ "createDate date"
				+ "updateDate date"
				+ ")";
		db.execSQL(sql);
	}
	
	/**
	 * 投递短信
	 * @param db
	 */
	public void deliveryMessage(SQLiteDatabase db){
		String sql = "create table deliveryMessage("
				+ "delId integer primary key,"
				+ "longitude double not null,"
				+ "latitude double not null,"
				+ "mobile varchar(22) not null,"
				+ "clientId varchar(256),"
				+ "employeeNo varchar(128),"
				+ "people varchar(32) not null,"
				+ "code varchar(128),"
				+ "sid varchar(128),"
				+ "waybill varchar(256),"
				+ "messageStatus varchar(2),"
				+ "messageTime varchar(128)," 
				+ "receiptStatus varchar(2)," 
				+ "mail_num varchar(128),"
				+ "orgcode varchar(128)," 
				+ "username text," 
				+ "loginUser varchar(22) not null,"
				+ "signStatus varchar(2))";
		db.execSQL(sql);
	}
	
	
	
	/**
	 * 	寄件通知
	 * @param db
	 */
	
	public void sendNotice(SQLiteDatabase db){
		String sql = "create table sendMessage("
				+ "sendId integer primary key,"
				+ "messageTime varchar(128) not null,"
				+ "messageStatus varchar(2)," 
				+ "longitude double," 
				+ "latitude double," 
				+ "mobile  varchar(128)," 
				// 受邀请 的手机号
				+ "invitedMobile  varchar(128)," 
				+ "clientId varchar(256)," 
				+ "employeeNo varchar(128)," 
				+ "people varchar(32)," 
				+ "code varchar(128)," 
				+ "sid  varchar(128),"
				+ "waybill varchar(256),"
				+ "orgcode text," 
				+ "username text,"
				//揽收所需要的数据
				+ "mailStatus varchar(2)," 
				+ "orderNo varchar(128)," 
				//妥投需要的数据
				+"loginUser varchar(22) not null,"
				+ "mailNum varchar(128),"
				+"serviceType varchar(2),"
				+"payment varchar(2),"
				+"price varchar(10),"
				+ "actPrice varchar(10),"
				+ "integral varchar(10)"
				+ ")";
		db.execSQL(sql);
	}
	
	/**
	 * 邮件信息表
	 * @param db
	 */
	public void creatMailMessageTable(SQLiteDatabase db){
		String sql = "create table mailMessage("
				+ "mailNum varchar(128) primary key"
				+ "messageTime varchar(128) not null,"
				+ "mailStatus varchar(2)," 
				+ "mailStatusDesc varchar(128))"; 
		db.execSQL(sql);
	}

	
	public void updateAllDeliveryMessageRed(SQLiteDatabase db){
		String sql = "update deliveryMessage set messageStatus=0";
		db.execSQL(sql);
	}
	
	/**
	 * 根据主键，将信息修改为已读
	 * @param db
	 * @param waybill
	 */
	public void updateDeliveryMessageRed(SQLiteDatabase db,String delId){
		String sql = "update deliveryMessage set messageStatus='0' where delId=?";
		db.execSQL(sql, new String[]{delId});
	}
	
	/**
	 * 根据主键，查询修改过的messageStatus
	 * @param db
	 * @param delId
	 * @return
	 */
	
	public String seleteDeliveryMessageRed(SQLiteDatabase db,String delId){
		String sql = "select messageStatus from deliveryMessage where delId = ? and loginUser = ?";
		Cursor cur = db.rawQuery(sql, new String[]{delId,SpfsUtil.loadPhone()});
		String messageStatus = null;
		if(cur.moveToFirst()){
			messageStatus = cur.getString(0);
		}
		cur.close();
		return messageStatus;
	}
	
	/**
	 * 根据主键，查询修改过的signStatus
	 * @param db
	 * @param delId
	 * @return
	 */
	public String seleteMessageRed(SQLiteDatabase db,String delId){
		String sql = "select signStatus from deliveryMessage where delId = ? and loginUser = ?";
		Cursor cur = db.rawQuery(sql, new String[]{delId,SpfsUtil.loadPhone()});
		String messageStatus = null;
		if(cur.moveToFirst()){
			messageStatus = cur.getString(0);
		}
		cur.close();
		return messageStatus;
	}
	
	
	/**
	 * 根据主键，查询修改过的mail_num
	 * @param db
	 * @param delId
	 * @return
	 */
	public String seleteMailNum(SQLiteDatabase db,String delId){
		String sql = "select mail_num from deliveryMessage where delId = ?";
		Cursor cur = db.rawQuery(sql, new String[]{delId});
		String messageStatus = null;
		if(cur.moveToFirst()){
			messageStatus = cur.getString(0);
		}
		cur.close();
		return messageStatus;
	}
	
	
	/**
	 * 查询未读的运单
	 * @param db
	 * @return
	 */
	public int queryUnRedDeliveryMessage(SQLiteDatabase db){
		String sql ="select count(*) from deliveryMessage where messageStatus=1 and loginUser = ?";
		Cursor cur = db.rawQuery(sql, new String[]{SpfsUtil.loadPhone()});
		int i = 0;
		if(cur.moveToFirst()){
			i = cur.getInt(0);
		}
		cur.close();
		return i;
	}
	
	/**
	 * 寄件查询未读
	 * @param db
	 * @return
	 */
	public int querySendNoticeMessage(SQLiteDatabase db){
		String sql ="select count(*) from sendMessage where messageStatus=1 and loginUser = ?";
		Cursor cur = db.rawQuery(sql, new String[]{SpfsUtil.loadPhone()});
		int i = 0;
		if(cur.moveToFirst()){
			i = cur.getInt(0);
		}
		cur.close();
		return i;
	}
	/**
	 * 收到的费用信息查询未读
	 * @param db
	 * @return
	 */
	public int queryUnReadMessage(SQLiteDatabase db){
		String sql ="select count(*) from receiveMessage where messageStatus=1 and loginUser = ?";
		Cursor cur = db.rawQuery(sql, new String[]{SpfsUtil.loadPhone()});
		int i = 0;
		if(cur.moveToFirst()){
			i = cur.getInt(0);
		}
		cur.close();
		return i;
	}
	
	
	/**
	 * 寄件查询未读
	 * @param db
	 * @return
	 */
	public int querySendNoticeMessage(SQLiteDatabase db,String orderStatus){
		String sql ="select count(*) from sendMessage where messageStatus=1 and loginUser = ? and mailStatus = ?";
		Cursor cur = db.rawQuery(sql, new String[]{SpfsUtil.loadPhone(),orderStatus});
		int i = 0;
		if(cur.moveToFirst()){
			i = cur.getInt(0);
		}
		cur.close();
		return i;
	}
	/**
	 * 支付消息查询未读
	 * @param db
	 * @return
	 */
	public int queryPaymentMessage(SQLiteDatabase db,String orderStatus){
		String sql ="select count(*) from receiveMessage where messageStatus=1 and loginUser = ? and mailStatus = ?";
		Cursor cur = db.rawQuery(sql, new String[]{SpfsUtil.loadPhone(),orderStatus});
		int i = 0;
		if(cur.moveToFirst()){
			i = cur.getInt(0);
		}
		cur.close();
		return i;
	}
	
	/**
	 * 查询所有时间
	 * @param db
	 * @return 
	 */
//	public List<SendNoticeBean> querySendNotice(SQLiteDatabase db){
//		String sql = "select * from sendMessage where loginUser = ? order by sendId desc";
//		Cursor cur = db.rawQuery(sql,new String[]{SpfsUtil.loadPhone()});
//		List<SendNoticeBean> list = new ArrayList<SendNoticeBean>();
//		while(cur.moveToNext()){
//			SendNoticeBean dmb = new SendNoticeBean();
//			dmb.setSendId(cur.getInt(cur.getColumnIndex("sendId")));
//			dmb.setMessageStatus(cur.getString(cur.getColumnIndex("messageStatus")));
//			dmb.setMessageTime(cur.getString(cur.getColumnIndex("messageTime")));
//			dmb.setLongitude(cur.getDouble(cur.getColumnIndex("longitude")));
//			dmb.setLatitude(cur.getDouble(cur.getColumnIndex("latitude")));
//			dmb.setMobile(cur.getString(cur.getColumnIndex("mobile")));
//			dmb.setChannelId(cur.getString(cur.getColumnIndex("clientId")));
//			dmb.setEmployeeNo(cur.getString(cur.getColumnIndex("employeeNo")));
//			dmb.setPeople(cur.getString(cur.getColumnIndex("people")));
//			dmb.setCode(cur.getString(cur.getColumnIndex("code")));
//			dmb.setSid(cur.getString(cur.getColumnIndex("sid")));
//			dmb.setWaybill(cur.getString(cur.getColumnIndex("waybill")));
//			dmb.setMailStatus(cur.getString(cur.getColumnIndex("mailStatus")));
//			dmb.setOrderNo(cur.getString(cur.getColumnIndex("orderNo")));
//			dmb.setOrgcode(cur.getString(cur.getColumnIndex("orgcode")));
//			dmb.setUsername(cur.getString(cur.getColumnIndex("username")));
//			dmb.setMailNum(cur.getString(cur.getColumnIndex("mailNum")));
//			dmb.setPrice(cur.getString(cur.getColumnIndex("price")));
//			dmb.setServiceType(cur.getString(cur.getColumnIndex("serviceType")));
//			list.add(dmb);
//		}
//		cur.close();
//		Log.e("msgggQuerySendNotice", list.toString());
//		return list;
//	}
	
	/**
	 * 查询所有时间
	 * @param db
	 * @return 
	 */
	public List<SendNoticeBean> querySendNoticeByOrderStatus(SQLiteDatabase db,String orderStatus1,String orderStatus2){
		String sql = null;
		Cursor cur = null;
		if(orderStatus2 == null){
			 sql = "select * from sendMessage where loginUser = ? and mailStatus = ? order by sendId desc";
			 cur = db.rawQuery(sql,new String[]{SpfsUtil.loadPhone(),orderStatus1});
		}else{
			 sql =  "select * from sendMessage where loginUser = ? and (mailStatus = ? or mailStatus = ?)order by sendId desc";
			 cur = db.rawQuery(sql,new String[]{SpfsUtil.loadPhone(),orderStatus1,orderStatus2});
		}
		
		List<SendNoticeBean> list = new ArrayList<SendNoticeBean>();
		while(cur.moveToNext()){
			SendNoticeBean dmb = new SendNoticeBean();
			dmb.setInvitedMobile(cur.getString(cur.getColumnIndex("invitedMobile")));
			dmb.setSendId(cur.getInt(cur.getColumnIndex("sendId")));
			dmb.setMessageStatus(cur.getString(cur.getColumnIndex("messageStatus")));
			dmb.setMessageTime(cur.getString(cur.getColumnIndex("messageTime")));
			dmb.setLongitude(cur.getDouble(cur.getColumnIndex("longitude")));
			dmb.setLatitude(cur.getDouble(cur.getColumnIndex("latitude")));
			dmb.setMobile(cur.getString(cur.getColumnIndex("mobile")));
			dmb.setChannelId(cur.getString(cur.getColumnIndex("clientId")));
			dmb.setEmployeeNo(cur.getString(cur.getColumnIndex("employeeNo")));
			dmb.setPeople(cur.getString(cur.getColumnIndex("people")));
			dmb.setCode(cur.getString(cur.getColumnIndex("code")));
			dmb.setSid(cur.getString(cur.getColumnIndex("sid")));
			dmb.setWaybill(cur.getString(cur.getColumnIndex("waybill")));
			dmb.setMailStatus(cur.getString(cur.getColumnIndex("mailStatus")));
			dmb.setOrderNo(cur.getString(cur.getColumnIndex("orderNo")));
			dmb.setOrgcode(cur.getString(cur.getColumnIndex("orgcode")));
			dmb.setUsername(cur.getString(cur.getColumnIndex("username")));
			dmb.setMailNum(cur.getString(cur.getColumnIndex("mailNum")));
			dmb.setServiceType(cur.getString(cur.getColumnIndex("serviceType")));
			dmb.setPrice(cur.getString(cur.getColumnIndex("price")));
			dmb.setPayment(cur.getString(cur.getColumnIndex("payment")));
			dmb.setActPrice(cur.getString(cur.getColumnIndex("actPrice")));
			dmb.setIntegral(cur.getString(cur.getColumnIndex("integral")));
			
			list.add(dmb);
		}
		cur.close();
		Log.e("msgggQuerySendNotice", list.toString());
		return list;
	}
	/**
	 * 查询所有时间支付
	 * @param db
	 * @return 
	 */
	public List<ReceivePaymnetBean> queryPaymentByOrderStatus(SQLiteDatabase db,String orderStatus1,String orderStatus2){
		String sql = null;
		Cursor cur = null;
		if(orderStatus2 == null){
			 sql = "select * from receiveMessage where loginUser = ? and mailStatus = ? order by receiveId desc";
			 cur = db.rawQuery(sql,new String[]{SpfsUtil.loadPhone(),orderStatus1});
		}else{
			 sql =  "select * from receiveMessage where loginUser = ? and (mailStatus = ? or mailStatus = ?)order by receiveId desc";
			 cur = db.rawQuery(sql,new String[]{SpfsUtil.loadPhone(),orderStatus1,orderStatus2});
		}
		/*
		 * 在用户没有登陆的情况下就推送过来信息的话其中的loginUser这列就会为空，应用一下的查询语句
		 * if(orderStatus2 == null){
			 sql = "select * from receiveMessage where mailStatus = ? order by receiveId desc";
			 cur = db.rawQuery(sql,new String[]{SpfsUtil.loadPhone(),orderStatus1});
		}else{
			 sql =  "select * from receiveMessage where mailStatus = ? or mailStatus = ? order by receiveId desc";
			 cur = db.rawQuery(sql,new String[]{SpfsUtil.loadPhone(),orderStatus1,orderStatus2});
		}*/
		
		List<ReceivePaymnetBean> list = new ArrayList<ReceivePaymnetBean>();
		while(cur.moveToNext()){
			ReceivePaymnetBean dmb = new ReceivePaymnetBean();
			dmb.setInvitedMobile(cur.getString(cur.getColumnIndex("invitedMobile")));
			dmb.setReceiveId(cur.getInt(cur.getColumnIndex("receiveId")));
			dmb.setMessageStatus(cur.getString(cur.getColumnIndex("messageStatus")));
			dmb.setMessageTime(cur.getString(cur.getColumnIndex("messageTime")));
			dmb.setMailStatus(cur.getString(cur.getColumnIndex("mailStatus")));
			dmb.setOrderNum(cur.getString(cur.getColumnIndex("orderNum")));
			dmb.setPrice(cur.getString(cur.getColumnIndex("price")));
			dmb.setMobNum(cur.getString(cur.getColumnIndex("mobNum")));
			dmb.setUserCode(cur.getString(cur.getColumnIndex("userCode")));
			dmb.setOrgCode(cur.getString(cur.getColumnIndex("orgCode")));
			/*dmb.setLongitude(cur.getDouble(cur.getColumnIndex("longitude")));
			dmb.setLatitude(cur.getDouble(cur.getColumnIndex("latitude")));
			dmb.setMobile(cur.getString(cur.getColumnIndex("mobile")));
			dmb.setChannelId(cur.getString(cur.getColumnIndex("clientId")));
			dmb.setEmployeeNo(cur.getString(cur.getColumnIndex("employeeNo")));
			dmb.setPeople(cur.getString(cur.getColumnIndex("people")));
			dmb.setCode(cur.getString(cur.getColumnIndex("code")));
			dmb.setSid(cur.getString(cur.getColumnIndex("sid")));
			dmb.setWaybill(cur.getString(cur.getColumnIndex("waybill")));
			dmb.setOrderNo(cur.getString(cur.getColumnIndex("orderNo")));
			dmb.setOrgcode(cur.getString(cur.getColumnIndex("orgcode")));
			dmb.setUsername(cur.getString(cur.getColumnIndex("username")));
			dmb.setMailNum(cur.getString(cur.getColumnIndex("mailNum")));
			dmb.setServiceType(cur.getString(cur.getColumnIndex("serviceType")));
			dmb.setPrice(cur.getString(cur.getColumnIndex("price")));
			dmb.setPayment(cur.getString(cur.getColumnIndex("payment")));
			dmb.setActPrice(cur.getString(cur.getColumnIndex("actPrice")));*/
			dmb.setIntegral(cur.getString(cur.getColumnIndex("integral")));
			
			list.add(dmb);
		}
		cur.close();
		Log.e("收到的支付信息或者积分信息", list.toString());
		return list;
	}
	
//	/**
//	 * 所有派单信息
//	 * @param db
//	 * @return 
//	 */
//	public List<SendNoticeBean> queryEmbraceSendNotice(SQLiteDatabase db){
//		String sql = "select * from sendMessage where loginUser = ? order by sendId desc";
//		Cursor cur = db.rawQuery(sql,new String[]{SpfsUtil.loadPhone()});
//		List<SendNoticeBean> list = new ArrayList<SendNoticeBean>();
//		while(cur.moveToNext()){
//			SendNoticeBean dmb = new SendNoticeBean();
//			dmb.setSendId(cur.getInt(cur.getColumnIndex("sendId")));
//			dmb.setMessageStatus(cur.getString(cur.getColumnIndex("messageStatus")));
//			dmb.setMessageTime(cur.getString(cur.getColumnIndex("messageTime")));
//			dmb.setLongitude(cur.getDouble(cur.getColumnIndex("longitude")));
//			dmb.setLatitude(cur.getDouble(cur.getColumnIndex("latitude")));
//			dmb.setMobile(cur.getString(cur.getColumnIndex("mobile")));
//			dmb.setChannelId(cur.getString(cur.getColumnIndex("clientId")));
//			dmb.setEmployeeNo(cur.getString(cur.getColumnIndex("employeeNo")));
//			dmb.setPeople(cur.getString(cur.getColumnIndex("people")));
//			dmb.setCode(cur.getString(cur.getColumnIndex("code")));
//			dmb.setSid(cur.getString(cur.getColumnIndex("sid")));
//			dmb.setWaybill(cur.getString(cur.getColumnIndex("waybill")));
//			dmb.setMailStatus(cur.getString(cur.getColumnIndex("mailStatus")));
//			dmb.setOrderNo(cur.getString(cur.getColumnIndex("orderNo")));
//			dmb.setOrgcode(cur.getString(cur.getColumnIndex("orgcode")));
//			dmb.setUsername(cur.getString(cur.getColumnIndex("username")));
//			dmb.setMailNum(cur.getString(cur.getColumnIndex("mailNum")));
//			list.add(dmb);
//		}
//		cur.close();
//		Log.e("msgggQuerySendNotice", list.toString());
//		return list;
//	}
//	
//	/**
//	 * 所有寄达信息
//	 * @param db
//	 * @return 
//	 */
//	public List<SendNoticeBean> queryArriveSendNotice(SQLiteDatabase db){
//		String sql = "select * from sendMessage where loginUser = ? order by sendId desc";
//		Cursor cur = db.rawQuery(sql,new String[]{SpfsUtil.loadPhone()});
//		List<SendNoticeBean> list = new ArrayList<SendNoticeBean>();
//		while(cur.moveToNext()){
//			SendNoticeBean dmb = new SendNoticeBean();
//			dmb.setSendId(cur.getInt(cur.getColumnIndex("sendId")));
//			dmb.setMessageStatus(cur.getString(cur.getColumnIndex("messageStatus")));
//			dmb.setMessageTime(cur.getString(cur.getColumnIndex("messageTime")));
//			dmb.setLongitude(cur.getDouble(cur.getColumnIndex("longitude")));
//			dmb.setLatitude(cur.getDouble(cur.getColumnIndex("latitude")));
//			dmb.setMobile(cur.getString(cur.getColumnIndex("mobile")));
//			dmb.setChannelId(cur.getString(cur.getColumnIndex("clientId")));
//			dmb.setEmployeeNo(cur.getString(cur.getColumnIndex("employeeNo")));
//			dmb.setPeople(cur.getString(cur.getColumnIndex("people")));
//			dmb.setCode(cur.getString(cur.getColumnIndex("code")));
//			dmb.setSid(cur.getString(cur.getColumnIndex("sid")));
//			dmb.setWaybill(cur.getString(cur.getColumnIndex("waybill")));
//			dmb.setMailStatus(cur.getString(cur.getColumnIndex("mailStatus")));
//			dmb.setOrderNo(cur.getString(cur.getColumnIndex("orderNo")));
//			dmb.setOrgcode(cur.getString(cur.getColumnIndex("orgcode")));
//			dmb.setUsername(cur.getString(cur.getColumnIndex("username")));
//			dmb.setMailNum(cur.getString(cur.getColumnIndex("mailNum")));
//			list.add(dmb);
//		}
//		cur.close();
//		Log.e("msgggQuerySendNotice", list.toString());
//		return list;
//	}
	
	
	/**
	 * 添加一条运单到数据库
	 * @param db
	 * @param longitude 经度
	 * @param latitude 纬度
	 * @param mobile 快递员电话号码
	 * @param clientId 推送唯一标示
	 * @param employeeNo 快递员账号
	 * @param people 快递员名称
	 * @param code 快递员机构号
	 * @param sid 快递员头像ID
	 * @param waybill 运单号
	 * @param messageStatus 0-已读，1-未读
	 * @param messageTime 运单时间
	 * 
	 */
	public void insertDeliveryMessage(SQLiteDatabase db,double longitude,double latitude,String mobile,String clientId
			,String employeeNo,String people,String code,String sid,String waybill,String messageStatus,String messageTime,String receiptStatus,String mail,String orgcode,String username, String loginUser,String signStatus){
		String sql = "insert into deliveryMessage(longitude,latitude,mobile,clientId,employeeNo,people,code,sid,waybill,messageStatus,messageTime,receiptStatus,mail_num,orgcode,username,loginUser,signStatus)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//		if(!queryWaybill(db, waybill)){
		db.execSQL(sql, new Object[]{longitude,latitude,mobile,clientId,employeeNo,people,code,sid,waybill,messageStatus,messageTime,receiptStatus,mail,orgcode,username,loginUser,signStatus});
//		}
	}
	
	public List<DeliveryMessageBean> queryAllDeliveryMessage(SQLiteDatabase db){
		String sql = "select * from deliveryMessage where loginUser = ? order by messageTime ";
		Cursor cur = db.rawQuery(sql,new String[]{SpfsUtil.loadPhone()});
		List<DeliveryMessageBean> list = new ArrayList<DeliveryMessageBean>();
		while(cur.moveToNext()){
			DeliveryMessageBean dmb = new DeliveryMessageBean();
			dmb.setDelId(cur.getInt(cur.getColumnIndex("delId")));
			dmb.setLongitude(cur.getDouble(cur.getColumnIndex("longitude")));
			dmb.setLatitude(cur.getDouble(cur.getColumnIndex("latitude")));
			dmb.setMobile(cur.getString(cur.getColumnIndex("mobile")));
			dmb.setChannelId(cur.getString(cur.getColumnIndex("clientId")));
			dmb.setEmployeeNo(cur.getString(cur.getColumnIndex("employeeNo")));
			dmb.setPeople(cur.getString(cur.getColumnIndex("people")));
			dmb.setCode(cur.getString(cur.getColumnIndex("code")));
			dmb.setSid(cur.getString(cur.getColumnIndex("sid")));
			dmb.setWaybill(cur.getString(cur.getColumnIndex("waybill")));
			dmb.setMessageStatus(cur.getString(cur.getColumnIndex("messageStatus")));
			dmb.setReceiptStatus(cur.getString(cur.getColumnIndex("receiptStatus")));
			dmb.setMessageTime(cur.getString(cur.getColumnIndex("messageTime")));
			dmb.setMailNum(cur.getString(cur.getColumnIndex("mail_num")));
			dmb.setOrgcode(cur.getString(cur.getColumnIndex("orgcode")));
			dmb.setUsername(cur.getString(cur.getColumnIndex("username")));
			dmb.setSignStatus(cur.getString(cur.getColumnIndex("signStatus")));
			list.add(dmb);
		}
		cur.close();
		return list;
	}
	
	/**
	 * 修改mail_num 数据
	 * @param db
	 * @param id
	 * @param messageSendStatus
	 */
	public void updateMailNum(SQLiteDatabase db,int id,String mail){
		String sql = "update deliveryMessage set mail_num=? where msgId=?";
		db.execSQL(sql, new Object[]{mail,id});
	}
	
	/**
	 *  添加一条时间数据(已派送)
	 * @param db
	 * @param data 寄件收到时间
	 */
	public void insertSendNotice(SQLiteDatabase db,String data,String status,double longitude,double latitude, String mobile,String clientId,String employeeNo,String people,String code,String sid,String waybill ,String mailStatus,String orderNo,String orgcode, String loginUser,String username){
		String sql ="insert into sendMessage(messageTime,messageStatus,longitude,latitude,mobile,clientId,employeeNo,people,code,sid,waybill,mailStatus,orderNo,orgcode,loginUser,username) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		db.execSQL(sql, new Object[]{data,status,longitude,latitude,mobile,clientId,employeeNo,people,code,sid,waybill,mailStatus,orderNo,orgcode,loginUser,username});
	}
	
	/**
	 *  添加一条时间数据(已揽收)
	 * @param db
	 * @param data 寄件收到时间
	 */
	public void insertSendNotice3(SQLiteDatabase db,String orderNo,String embraceTime,String messageStatus,String orderStatus,String mailNum,String loginUser,String employName,String employPhone,String serviceType,String price,String payment,String actPrice,String integral){
		String sql ="insert into sendMessage(orderNo,messageTime,messageStatus,mailStatus,mailNum,loginUser,people,mobile,serviceType,price,payment,actPrice,integral) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		db.execSQL(sql, new Object[]{orderNo,embraceTime,messageStatus,orderStatus,mailNum,loginUser,employName,employPhone,serviceType,price,payment,actPrice,integral});
	}
	
	/**
	 *  添加一条时间数据(已妥投)
	 * @param db
	 * @param data 寄件收到时间
	 */
	public void insertSendNotice2(SQLiteDatabase db,String data,String messageStatus,String mailStatus,String mailNum,String loginUser){
		String sql ="insert into sendMessage(messageTime,messageStatus,mailStatus,mailNum,loginUser) values(?,?,?,?,?)";
		db.execSQL(sql, new Object[]{data,messageStatus,mailStatus,mailNum,loginUser});
	}
	
	/**
	 *  添加一条其他信息里的积分数据（受邀请所得）
	 * @param db
	 * @param 
	 */
	public void insertSendNoticeInvitation(SQLiteDatabase db,String messageStatus,String mssageTime,String invitedMobile,String mailStatus,String integral,String loginUser){
		String sql ="insert into receiveMessage(messageStatus,messageTime,invitedMobile,mailStatus,integral,loginUser) values(?,?,?,?,?,?)";
		db.execSQL(sql, new Object[]{messageStatus,mssageTime,invitedMobile,mailStatus,integral,loginUser});
	}
	
	/**
	 *  添加一条其他信息里的支付数据
	 * @param db
	 * @param 
	 */
	public void insertReceivePaymentMessage(SQLiteDatabase db,String messageStatus,String mailStatus,String messageTime,String loginUser,String orderNum,double price,String mobNum,String userCode,String orgCode){
		String sql ="insert into receiveMessage(messageStatus,mailStatus,messageTime,loginUser,orderNum,price,mobNum,userCode,orgCode) values(?,?,?,?,?,?,?,?,?)";
		db.execSQL(sql, new Object[]{messageStatus,mailStatus,messageTime,loginUser,orderNum,price,mobNum,userCode,orgCode});
	}
	/**
	 * 查询表里是否有这条订单号
	 * @param db
	 * @param 
	 */
	public Boolean seleteReceivePaymentMessage(SQLiteDatabase db,String orderNum){
		String sql ="select price from receiveMessage where orderNum = ?";
		Cursor c = db.rawQuery(sql, new String[]{orderNum});
		if(c.moveToFirst()){
			return true;
		}
		c.close();
		return false;
	}
	
	
	
	/**
	 * 查询是否存在此运单号
	 * @param db
	 * @param employeeNo 运单号
	 * @return
	 */
	public boolean queryWaybill(SQLiteDatabase db,String waybill){
		String sql ="select employeeNo from deliveryMessage where waybill = ?";
		Cursor c = db.rawQuery(sql, new String[]{waybill});
		if(c.moveToFirst()){
			return true;
		}
		c.close();
		return false;
	}
	
	
	
	/**
	 * 添加一条消息到数据库
	 * @param employeeNo快递员编号
	 * @param messageStatus消息状态 0-已读，1-未读
	 * @param messageSendStatus消息发送状态 0-成功，1-失败，2-发送中
	 * @param sendIsReceive消息是接收方还是发送方  0接收，1发送
	 * @param messageType消息类型  0-普通文字信息，1-图片，2-语音信息
	 * @param messageContent消息内容
	 * @param url文件地址
	 * @param ServerUrl服务器文件地址
	 * @param speechMessageTime录音文件时长
	 * @return 本地消息的ID
	 */
	public int insertMessage(SQLiteDatabase db,String employeeNo,String messageStatus,String messageSendStatus,String sendIsReceive
			,String messageType,String messageContent,String url,Date messageTime,String serverUrl,int speechMessageTime){
		String sql = "insert into message(employeeNo,messageStatus,messageSendStatus,sendIsReceive,messageType,messageContent,url,messageTime,serverUrl,speechMessageTime) values(?,?,?,?,?,?,?,?,?,?)";
		db.execSQL(sql, new Object[]{employeeNo,messageStatus,messageSendStatus,sendIsReceive,messageType,messageContent,url,messageTime,serverUrl,speechMessageTime});
		Cursor cur = db.rawQuery("select LAST_INSERT_ROWID() msgId", null);
		int i =0;
		if(cur.moveToFirst()){
			i = cur.getInt(cur.getColumnIndex("msgId"));
		}
		cur.close();
		return i;
	}
	
	/**
	 * 修改发送的消息状态
	 * @param db
	 * @param id 发送消息的id
	 * @param messageSendStatus 修改的状态 0-成功，1-失败，2-发送中
	 */
	public void updateMessage(SQLiteDatabase db,int id,String messageSendStatus){
		String sql = "update message set messageSendStatus=? where msgId=?";
		db.execSQL(sql, new Object[]{messageSendStatus,id});
	}
	
	/**
	 * 查询与快递员的所有聊天信息
	 * @param db
	 * @param employeeNo 快递员员工编号
	 * @return 所有聊天信息
	 */
	public List<ChatMessageBean> queryAllMessage(SQLiteDatabase db,String employeeNo,String sourceIcon){
		String sql = "select * from message where employeeNo=? order by msgId";
		Cursor cur = db.rawQuery(sql, new String[]{employeeNo});
		List<ChatMessageBean> list = new ArrayList<ChatMessageBean>();
		while(cur.moveToNext()){
			ChatMessageBean bean = new ChatMessageBean();
			bean.setMessageSendStatus(cur.getString(cur.getColumnIndex("messageSendStatus")));
			bean.setContent(cur.getString(cur.getColumnIndex("messageContent")));
			bean.setContentType(cur.getString(cur.getColumnIndex("messageType")));
			bean.setMessageId(cur.getInt(cur.getColumnIndex("msgId")));
			bean.setSourceIcon(sourceIcon);
			if("0".equals(cur.getString(cur.getColumnIndex("sendIsReceive")))){
				bean.setSendIsReceive(false);
			}else{
				bean.setSendIsReceive(true);
			}
			bean.setServerPath(cur.getString(cur.getColumnIndex("serverUrl")));
			bean.setPicpath(cur.getString(cur.getColumnIndex("url")));
			bean.setSpeechMessageTime(cur.getInt(cur.getColumnIndex("speechMessageTime")));
			list.add(bean);
		}
		cur.close();
		return list;
	}
	
	
	/**
	 * 查询快递员未读的消息
	 * @param db
	 * @param employeeNo 快递员编号
	 * @return
	 */
	public int queryUnReadMessage(SQLiteDatabase db,String employeeNo){
		String sql = "select count(*) from message where employeeNo=? and messageStatus=1";
		Cursor cur = db.rawQuery(sql, new String[]{employeeNo});
		int i = 0;
		if(cur.moveToFirst()){
			i = cur.getInt(0);
		}
		cur.close();
		return i;
	}
	
	public int queryAllUnReadMessage(SQLiteDatabase db){
		String sql = "select count(*) from message where messageStatus=1 and loginUser = ?";
		Cursor cur = db.rawQuery(sql, new String[]{SpfsUtil.loadPhone()});
		int i =0;
		if(cur.moveToFirst()){
			i = cur.getInt(0);
		}
		cur.close();
		return i;
	}
	
	/**
	 * 将未读消息修改为已读
	 * @param db
	 * @param employeeNo快递员编号
	 */
	public void updateUnReadMessage(SQLiteDatabase db,String employeeNo){
		String sql = "update message set messageStatus = 0 where employeeNo=?";
		db.execSQL(sql, new Object[]{employeeNo});
	}
	
	
	/**
	 * 添加一个聊天快递员到数据库
	 * @param db
	 * @param point
	 */
	public void insertChatList(SQLiteDatabase db,Carrier point){
		String sql = "insert into carrier(longitude,latitude,people,mobile,address,clientId,employeeNo,code,sid) values(?,?,?,?,?,?,?,?,?)";
		String update = "update carrier set longitude=?,latitude=?,people=?,mobile=?,address=?,clientId=?,code=?,sid=? where employeeNo=?";
		if(!queryEmployeeNo(db,point.getEmployeeNo())){
			db.execSQL(sql, new Object[]{point.getLatitude(),point.getLatitude(),point.getPeople(),point.getMobile(),point.getAddress(),point.getClientId(),point.getEmployeeNo(),point.getCode(),point.getSID()});
		}else{
			db.execSQL(update, new Object[]{point.getLatitude(),point.getLatitude(),point.getPeople(),point.getMobile(),point.getAddress(),point.getClientId(),point.getCode(),point.getSID(),point.getEmployeeNo()});
		}
	}
	/**
	 * 根据姓名和电话查询邮递员(暂时只获取员工号和机构号，以后有需要再继续添加字段)
	 * @param db
	 * @param name
	 * @param phone
	 * @return
	 */
	public Carrier queryCarrierByNameAndPhone(SQLiteDatabase db,String name,String phone){
		String sql = "select * from carrier where people = ? and mobile = ?";
		
		Cursor cur = db.rawQuery(sql, new String[]{name,phone});
		while(cur.moveToNext()){
			Carrier bean = new Carrier();
			bean.setCode(cur.getString(cur.getColumnIndex("code")));
			bean.setEmployeeNo(cur.getString(cur.getColumnIndex("employeeNo")));
			cur.close();
			return bean;
		}
		cur.close();
		return null;
	}
	public void replaceClientId(SQLiteDatabase db,String clientId,String code,String employeeNo){
		String sql = "update carrier set clientId = ? where code = ? and employeeNo = ?";
		db.execSQL(sql, new String[]{clientId,code,employeeNo});
		
	}
	/**
	 * 查询是否存在此快递员
	 * @param db
	 * @param employeeNo 快递员编号
	 * @return
	 */
	public boolean queryEmployeeNo(SQLiteDatabase db,String employeeNo){
		String sql ="select employeeNo from carrier where employeeNo = ?";
		Cursor c = db.rawQuery(sql, new String[]{employeeNo});
		if(c.moveToFirst()){
			c.close();
			return true;
		}
		c.close();
		return false;
	}
	/**
	 * 查询所有聊天过的快递员列表
	 * @param db
	 * @return
	 */
	public List<Carrier> queryAllChatList(SQLiteDatabase db){
		String sql ="select * from carrier";
		List<Carrier> list = new ArrayList<Carrier>();
		Cursor cursor = db.rawQuery(sql, null);
		while(cursor.moveToNext()){
			int count =queryUnReadMessage(db,cursor.getString(cursor.getColumnIndex("employeeNo")));
			Carrier ca = new Carrier();
			ca.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
			ca.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
			ca.setPeople(cursor.getString(cursor.getColumnIndex("people")));
			ca.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
			ca.setAddress(cursor.getString(cursor.getColumnIndex("address")));
			ca.setClientId(cursor.getString(cursor.getColumnIndex("clientId")));
			ca.setEmployeeNo(cursor.getString(cursor.getColumnIndex("employeeNo")));
			ca.setCode(cursor.getString(cursor.getColumnIndex("code")));
			ca.setSID(cursor.getString(cursor.getColumnIndex("sid")));
			ca.setUnRedCount(count);
			list.add(ca);
		}
		cursor.close();
		return list;
	}
	
	public void deleteCurrierByClientId(SQLiteDatabase db,String clientId){
		String sql = "delete from carrier where clientId = ?";
		db.execSQL(sql, new String[]{clientId});
	}
	
	
	/**
	 * 根据clientId查询employeeNo
	 * @param db
	 * @param clientId
	 * @return
	 */
	public String queryEmployeeNoIsClientId(SQLiteDatabase db,String clientId){
		String employeeNo = null;
		if(clientId!=null&&!"".equals(clientId)){
			String sql = "select employeeNo from carrier where clientId = ?";
			Cursor cur = db.rawQuery(sql, new String[]{clientId});
			if(cur.moveToFirst()){
				employeeNo = cur.getString(0);
			}
			cur.close();
		}
		return employeeNo;
	}
	
	/**
	 * 根据消息ID 修改消息的本地文件路径
	 * @param url 本地文件路径
	 * @param messageId	消息ID
	 */
	public void updateMessageUrl(SQLiteDatabase db,String url,String messageId){
		ContentValues cv = new ContentValues();
		cv.put("url", url);
		db.update("message", cv, "msgId=?", new String[]{messageId});
	}
	
	/**
	 * 根据delId删除数据
	 * @param db
	 * @param delId
	 * 
	 */
	public void deleteDeliveryMessageIsDelId(SQLiteDatabase db,String delId){
		db.delete("deliveryMessage", "delId = ?", new String[]{delId});
	}
	/**
	 * 删除已读的收件消息数据
	 * @param db
	 * @param delId
	 * 
	 */
	public void deleteReadedDelivery(SQLiteDatabase db){
		db.delete("deliveryMessage", "messageStatus = ?", new String[]{"0"});
	}
	
	/**
	 * 根据主键修改，状态值为未读
	 * @param db
	 * @param delId
	 */
	public void updateDeliveryMessageIsDelId(SQLiteDatabase db,String delId){
		String sql = "update deliveryMessage set receiptStatus = '0' where delId=?";
		db.execSQL(sql, new String[]{delId});
	}
	/**
	 * 根据主键修改，状态值为未读
	 * @param db
	 * @param delId
	 */
	public void updateMessageIsDelId(SQLiteDatabase db,String delId){
		String sql = "update deliveryMessage set signStatus = '0' where delId=?";
		db.execSQL(sql, new String[]{delId});
	}
	
	/**
	 * 根据主键，删除消息
	 * @param db
	 * @param delId
	 */
	public void deleteSendNotice(SQLiteDatabase db,String delId){
		db.delete("sendMessage", "sendId = ?", new String[]{delId});
	}
	/**
	 * 根据主键，删除消息
	 * @param db
	 * @param delId
	 */
	public void deleteReceiveNotice(SQLiteDatabase db,String delId){
		db.delete("receiveMessage", "receiveId = ?", new String[]{delId});
	}
	/**
	 * 根据主键修改，状态值为未读
	 * @param db
	 * @param sendId
	 */
	public void updateSendNotice(SQLiteDatabase db,String sendId){
		String sql = "update sendMessage set messageStatus = '0' where sendId=?";
		db.execSQL(sql, new String[]{sendId});
	}
	/**
	 * 根据主键修改，状态值为未读
	 * @param db
	 * @param sendId
	 */
	public void updateReceiveNotice(SQLiteDatabase db,String sendId){
		String sql = "update receiveMessage set messageStatus = '0' where receiveId=?";
		db.execSQL(sql, new String[]{sendId});
	}
	
	/**
	 * 删除carrier所有消息
	 * @param db
	 * @param delId
	 */
	public void deleteAllCarrier(SQLiteDatabase db){
		db.delete("carrier", null, null);
	}
	
	/**
	 * 删除消息message所有消息
	 * @param db
	 * @param delId
	 */
	public void deleteAllMessage(SQLiteDatabase db){
		db.delete("message", null, null);
	}
	
	/**
	 * 删除消息deliveryMessage所有消息
	 * @param db
	 * @param delId
	 */
	public void deleteAllDeliveryMessage(SQLiteDatabase db){
		db.delete("deliveryMessage", null, null);
	}
	
	/**
	 * 删除消息sendMessage所有消息
	 * @param db
	 * @param delId
	 */
	public void deleteAllSendMessage(SQLiteDatabase db){
		db.delete("sendMessage", null, null);
	}
	/**
	 * 删除消息senderMessage所有消息
	 * @param db
	 * @param delId
	 */
	public void deleteAllSenderMessage(SQLiteDatabase db){
		db.delete("senderMessage", null, null);
	}
	/**
	 * 删除carrier所有消息
	 * @param db
	 * @param delId
	 */
	public void deleteAllReceiveMessage(SQLiteDatabase db){
		db.delete("receiveMessage", null, null);
	}
	/**
	 * 根据订单号查询派单信息(暂时只获取机构号和员工号)
	 * @param db
	 * @param sid
	 * @return
	 */
	public SendNoticeBean querySendMessageBySid(SQLiteDatabase db,String sid){
		String sql ="select * from sendMessage where orderNo = ?";
		Cursor cur = db.rawQuery(sql, new String[]{sid});
		while(cur.moveToNext()){
			SendNoticeBean bean = new SendNoticeBean();
			bean.setEmployeeNo(cur.getString(cur.getColumnIndex("employeeNo")));
			bean.setOrgcode(cur.getString(cur.getColumnIndex("orgcode")));
			Log.e("msggg", cur.getString(cur.getColumnIndex("employeeNo"))+"   "+cur.getString(cur.getColumnIndex("orgcode")));
			cur.close();
			return bean;
		}
		cur.close();
		return null;
	}
	
	/**
	 * 根据订单号和状态查询派单信息(暂时只获取机构号和员工号)
	 * @param db
	 * @param sid
	 * @return
	 */
	public SendNoticeBean querySendMessageBySidAndStatus(SQLiteDatabase db,String sid,String orderStatus){
		String sql ="select * from sendMessage where orderNo = ? and mailStatus = ?";
		Cursor cur = db.rawQuery(sql, new String[]{sid,orderStatus});
		while(cur.moveToNext()){
			SendNoticeBean bean = new SendNoticeBean();
			bean.setEmployeeNo(cur.getString(cur.getColumnIndex("employeeNo")));
			bean.setSendId(cur.getInt(cur.getColumnIndex("sendId")));
			bean.setOrgcode(cur.getString(cur.getColumnIndex("orgcode")));
			Log.e("msggg", cur.getString(cur.getColumnIndex("employeeNo"))+"   "+cur.getString(cur.getColumnIndex("orgcode")));
			cur.close();
			return bean;
		}
		cur.close();
		return null;
	}
	
//	/**
//	 * 更具订单号查询派单信息(暂时只获取机构号和员工号)
//	 * @param db
//	 * @param sid
//	 * @return
//	 */
//	public void updateMailStatusBySid(SQLiteDatabase db,String sid,String mailStatus){
//		String sql ="update sendMessage set mailStatus = ?,messageStatus = '0' where sendId=?";
//		db.execSQL(sql, new String[]{sid});
//	}
	
	//城市
	
	/**
	 * 创建省列表
	 * @param db
	 */
	public void createProvince(SQLiteDatabase db){
		String sql = "create table IF NOT EXISTS province("
				+ "Id integer primary key,"
				+ "code varchar(10) not null,"
				+ "name varchar(50) not null"
				+")";
		db.execSQL(sql);
	}
	
	//添加省
	public void addProvince(SQLiteDatabase db,JSONArray jsonArray/*List<City> provinceList*/) {
		//清空表中的数据
		db.execSQL("delete from province");
//		db.execSQL("update sqlite_sequence SET seq = 0 where name = province");
		
		String sql = "insert into province(code,name) values(?,?)";
		if(this.getCityCount(db, "province") > 0){
			return;
		}
		for (int i = 0; i < jsonArray.size(); i++) {
			String name = jsonArray.getJSONObject(i).getString("value");
			String code = jsonArray.getJSONObject(i).getString("key");
			db.execSQL(sql, new Object[]{code,name});
		}
	}
	//查询省列表
	public List<City> queryProvinceList(SQLiteDatabase db) {
			String sql = "select * from province";
			List<City> cityList = new ArrayList<City>();
			Cursor cur = db.rawQuery(sql, null);
			while(cur.moveToNext()){
				City city = new City();
				city.setCode(Integer.valueOf(cur.getString(cur.getColumnIndex("code"))));
				city.setName(cur.getString(cur.getColumnIndex("name")));
				cityList.add(city);
			}
			cur.close();
			
			return cityList;
	}
	
	/**
	 * 创建市列表
	 * @param db
	 */
	public void createCity(SQLiteDatabase db){
		String sql = "create table IF NOT EXISTS city("
				+ "Id integer primary key,"
				+ "code varchar(10) not null,"
				+ "name varchar(50) not null"
				+")";
		db.execSQL(sql);
	}
	//添加市
	public void addCity(SQLiteDatabase db,JSONArray jsonArray) {
		//清空表中的数据
		db.execSQL("delete from city");
//		db.execSQL("update sqlite_sequence SET seq = 0 where name = city");
		
		String sql = "insert into city(code,name) values(?,?)";
		if(this.getCityCount(db, "city") > 0){
			return;
		}
		db.beginTransaction();
		for (int i = 0; i < jsonArray.size(); i++) {
			String name = jsonArray.getJSONObject(i).getString("value");
			String code = jsonArray.getJSONObject(i).getString("key");
			db.execSQL(sql, new Object[]{code,name});
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}
	
	//查询市列表
	public List<City> queryCityList(SQLiteDatabase db,String proCode) {
		String sql = "select * from city where code like '"+proCode+"%'";
		List<City> cityList = new ArrayList<City>();
		Cursor cur = db.rawQuery(sql, null);
		while(cur.moveToNext()){
			City city = new City();
			city.setCode(Integer.valueOf(cur.getString(cur.getColumnIndex("code"))));
			city.setName(cur.getString(cur.getColumnIndex("name")));
			cityList.add(city);
		}
		cur.close();
		return cityList;
	}
	//查询固定市列表
		public String queryCodeByCity(SQLiteDatabase db,String proCode,String cityName) {
			String sql = "select * from city where code like '"+proCode+"%' and name = '"+cityName+"'";
			List<City> cityList = new ArrayList<City>();
			Cursor cur = db.rawQuery(sql, null);
			String code = null;
			while(cur.moveToNext()){
				City city = new City();
				city.setCode(Integer.valueOf(cur.getString(cur.getColumnIndex("code"))));
				city.setName(cur.getString(cur.getColumnIndex("name")));
				code = cur.getString(cur.getColumnIndex("code"));
				cityList.add(city);
			}
			cur.close();
			return code;
		}
	//根据固定地址查询code
	public String queryCodeByProvince(SQLiteDatabase db,String pro){
		String sql = "select * from province where name ='"+ pro+"'";
		List<City> cityList = new ArrayList<City>();
		Cursor cur = db.rawQuery(sql, null);
		String code = null;
		while (cur.moveToNext()) {
			City city = new City();
			city.setCode(Integer.valueOf(cur.getString(cur.getColumnIndex("code"))));
			city.setName(cur.getString(cur.getColumnIndex("name")));
			code = cur.getString(cur.getColumnIndex("code"));
			cityList.add(city);
		}
		return code;
	}
	//查询固定县列表
		public String queryCodeByCounty(SQLiteDatabase db,String cityCode,String countyName) {
			String startWith;
			if(cityCode.startsWith("11")||cityCode.startsWith("12")||cityCode.startsWith("31")||cityCode.startsWith("50")){
				 startWith = cityCode.substring(0, 3);
			}else{
				 startWith = cityCode.substring(0, 4);
			}
			
			Log.e("msggg", startWith);
			String sql = "select * from county where code like '"+startWith+"%' and name = '"+countyName+"'";
			List<City> cityList = new ArrayList<City>();
			Cursor cur = db.rawQuery(sql, null);
			String code = null;
			while(cur.moveToNext()){
				City city = new City();
				city.setCode(Integer.valueOf(cur.getString(cur.getColumnIndex("code"))));
				city.setName(cur.getString(cur.getColumnIndex("name")));
				code = cur.getString(cur.getColumnIndex("code"));
				cityList.add(city);
			}
			cur.close();
			Log.e("gongjie", cityList.toString());
			return code;
		}
	/**
	 * 创建县列表
	 * @param db
	 */
	public void createCounty(SQLiteDatabase db){
		String sql = "create table IF NOT EXISTS county("
				+ "Id integer primary key,"
				+ "code varchar(10) not null,"
				+ "name varchar(50) not null"
				+")";
		db.execSQL(sql);
	}
	
	//添加县
	public void addCounty(SQLiteDatabase db,JSONArray jsonArray) {
		//清空表中的数据
		db.execSQL("delete from county");
//		db.execSQL("update sqlite_sequence SET seq = 0 where name = county");
		String sql = "insert into county(code,name) values(?,?)";
		if(!SpfsUtil.getIsLoadingCity()){
			return;
		}
		db.beginTransaction();
		for (int i = 0; i < jsonArray.size(); i++) {
			String name = jsonArray.getJSONObject(i).getString("value");
			String code = jsonArray.getJSONObject(i).getString("key");
			db.execSQL(sql, new Object[]{code,name});
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		//改变标识状态
		SpfsUtil.isLoadingCity(false);
	}
	//查询县列表
	public List<City> queryCountyList(SQLiteDatabase db,String cityCode) {
		String startWith;
		if(cityCode.startsWith("11")||cityCode.startsWith("12")||cityCode.startsWith("31")||cityCode.startsWith("50")){
			 startWith = cityCode.substring(0, 3);
		}else{
			 startWith = cityCode.substring(0, 4);
		}
		
		Log.e("msggg", startWith);
		String sql = "select * from county where code like '"+startWith+"%'";
		List<City> cityList = new ArrayList<City>();
		Cursor cur = db.rawQuery(sql, null);
		while(cur.moveToNext()){
			City city = new City();
			city.setCode(Integer.valueOf(cur.getString(cur.getColumnIndex("code"))));
			city.setName(cur.getString(cur.getColumnIndex("name")));
			cityList.add(city);
		}
		cur.close();
		Log.e("gongjie", cityList.toString());
		return cityList;
	}
			
	//获取城市数量
	public int getCityCount(SQLiteDatabase db,String tbName){
		String sql = "select count(1) from "+tbName;
		Cursor cur = db.rawQuery(sql, null);
		int i = 0;
		if(cur.moveToFirst()){
			i = cur.getInt(0);
		}
		cur.close();
		return i;
	}
	/**
	 * 创建寄件人信息表
	 * @param db
	 */
	public void createSenderInfo(SQLiteDatabase db){
		
		String sql = "create table IF NOT EXISTS senderinfo("
				+ "Id integer primary key,"
				+ "name varchar(30) not null,"
				+ "phone varchar(15) not null,"
				+ "prov_name varchar(20) not null,"
				+ "prov_code varchar(20) not null,"
				+ "city_name varchar(20) not null,"
				+ "city_code varchar(20) not null,"
				+ "county_name varchar(20) not null,"
				+ "county_code varchar(20) not null,"
				+ "location varchar(100) not null,"
				+ "type int not null,"
				+ "isdef int not null"
				+")";
		db.execSQL(sql);
	}
	
	//添加寄件人信息
	public void addSender(SQLiteDatabase db,PeopleInfo people) {
		String sql = "insert into senderinfo(name,phone,prov_name,prov_code,city_name,city_code,county_name,county_code,location,type,isdef) values(?,?,?,?,?,?,?,?,?,?,?)";
		String name = people.getName();
		String phone = people.getPhone();
		String provName = people.getProv();
		String provCode = people.getProvCode();
		String cityName = people.getCity();
		String cityCode = people.getCityCode();
		String location = people.getLocation();
		String countyName = people.getCounty();
		String countyCode = people.getCountyCode();
		int type = people.getType();
		int isdef = people.getIsDef();			
		db.execSQL(sql, new Object[]{name,phone,provName,provCode,cityName,cityCode,countyName,countyCode,location,type,isdef});
	}
	//查询寄件人列表
	public List<PeopleInfo> querySenderList(SQLiteDatabase db,int type) {
			String sql = "select * from senderinfo where type = "+type+" order by Id desc";
			List<PeopleInfo> senderList = new ArrayList<PeopleInfo>();
			Cursor cur = db.rawQuery(sql, null);
			while(cur.moveToNext()){
				PeopleInfo si = new PeopleInfo();
				si.setId(cur.getString(cur.getColumnIndex("Id")));
				si.setName(cur.getString(cur.getColumnIndex("name")));
				si.setPhone(cur.getString(cur.getColumnIndex("phone")));
				
				si.setType(cur.getInt(cur.getColumnIndex("type")));
				si.setIsDef(cur.getInt(cur.getColumnIndex("isdef")));
				
				si.setProv(cur.getString(cur.getColumnIndex("prov_name")));
				si.setProvCode(cur.getString(cur.getColumnIndex("prov_code")));
				si.setCityCode(cur.getString(cur.getColumnIndex("city_code")));
				si.setCity(cur.getString(cur.getColumnIndex("city_name")));
				si.setCounty(cur.getString(cur.getColumnIndex("county_name")));
				si.setCountyCode(cur.getString(cur.getColumnIndex("county_code")));
				si.setLocation(cur.getString(cur.getColumnIndex("location")));
				senderList.add(si);
			}
			cur.close();
			return senderList;
		}
	//删除寄件人
	public void removeSender(SQLiteDatabase db,PeopleInfo people){
		String sql = "delete from senderinfo where Id = ?";
		db.execSQL(sql,new Object[]{people.getId()});
	}
	//修改寄件人
	public void updateSender(SQLiteDatabase db,PeopleInfo people){
		String sql = "update senderinfo set name = ?,phone = ?,prov_name = ?,prov_code = ?,city_name = ? ,city_code = ?,county_name = ?,county_code = ?,location = ? where Id = ?";
		db.execSQL(sql,new Object[]{people.getName(),people.getPhone(),people.getProv(),people.getProvCode(),people.getCity(),people.getCityCode(),people.getCounty(),people.getCountyCode(),people.getLocation(),people.getId()});
	}
	
	/**
	 * 订单表
	 * @param db
	 */
	public void creatOrderTable(SQLiteDatabase db){
		
		String sql = "create table IF NOT EXISTS orderInfo("
				+ "Id integer primary key,"
				+ "orderNo varchar(20) not null,"
				+ "senderName varchar(20)," 
				+ "senderPhone varchar(20)," 
				+ "senderAddress varchar(50)," 
				+ "receiveName varchar(20)," 
				+ "receivePhone varchar(20)," 
				+ "receiveAddress varchar(50)," 
				+ "weight varchar(5),"
				+ "type varchar(2),"
				+ "orderTime varchar(20),"
				+ "payWay varchar(2)"
				+");"; 
		db.execSQL(sql);
		
		
	}
	/**
	 * 添加订单数据
	 * @param db
	 * @param bean
	 */
	public void insertOrderInfo(SQLiteDatabase db,OrderInfoBean bean){
		String sql = "insert into orderInfo(orderNo,senderName,senderPhone,senderAddress,receiveName,receivePhone,receiveAddress,weight,type,payWay,orderTime) values (?,?,?,?,?,?,?,?,?,?,?)";
		
		String orderNo = bean.getOrderNo();
		String senderName = bean.getSenderName();
		String senderPhone = bean.getSenderPhone();
		String senderAddress = bean.getSenderAddress();
		String reveiveName = bean.getReceiveName();
		String receivePhone = bean.getReceivePhone();
		String receiveAddress = bean.getReceiveAddress();
		String weight = bean.getWeight();
		String type = bean.getType();
		String payWay = bean.getPayWay();
		String orderTime = bean.getOrderTime();
		
		db.execSQL(sql, new Object[]{orderNo,senderName,senderPhone,senderAddress,reveiveName,receivePhone,receiveAddress,weight,type,payWay,orderTime});
		
	}
	/**
	 * 查询订单详情
	 * @param db
	 * @param orderNo
	 * @return
	 */
	public OrderInfoBean getOrderInfo(SQLiteDatabase db,String orderNo){
		String sql = "select * from orderInfo where orderNo = ?";
		OrderInfoBean bean = null;
		if(orderNo != null && !"".equals(orderNo)){
			Cursor cur = db.rawQuery(sql,new String[]{orderNo});
			if(cur.moveToFirst()){
				bean = new OrderInfoBean();
				bean.setOrderNo(cur.getString(cur.getColumnIndex("orderNo")));
				bean.setSenderName(cur.getString(cur.getColumnIndex("senderName")));
				bean.setSenderPhone(cur.getString(cur.getColumnIndex("senderPhone")));
				bean.setSenderAddress(cur.getString(cur.getColumnIndex("senderAddress")));
				
				bean.setReceiveName(cur.getString(cur.getColumnIndex("receiveName")));
				bean.setReceivePhone(cur.getString(cur.getColumnIndex("receivePhone")));
				bean.setReceiveAddress(cur.getString(cur.getColumnIndex("receiveAddress")));
				
				bean.setWeight(cur.getString(cur.getColumnIndex("weight")));
				bean.setType(cur.getString(cur.getColumnIndex("type")));
				bean.setPayWay(cur.getString(cur.getColumnIndex("payWay")));
				
				bean.setOrderTime(cur.getString(cur.getColumnIndex("orderTime")));
			}
			cur.close();
		}
		
		return bean;
	}
	/**
	 * 删除订单
	 * @param db
	 * @param orderNo
	 * @return
	 */
	public boolean deleteOrder(SQLiteDatabase db,String orderNo){
		String sql = "delete from orderInfo where orderNo = ?";
		db.execSQL(sql,new Object[]{orderNo});
		if(null == getOrderInfo(db, orderNo)){
			return true;
		}else {
			return false;
		}
		
	}
	
}

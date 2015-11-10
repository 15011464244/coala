package com.ems.express.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ems.express.adapter.message.SendNoticeBean;
import com.ems.express.bean.ChatMessageBean;
import com.ems.express.bean.DeliveryMessageBean;
import com.ems.express.net.Carrier;
import com.ems.express.ui.HomeActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	private static final int VERSION = 3;

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
   
	public DBHelper(Context context, String name) {
		this(context, name, VERSION);
	}

	public DBHelper(Context context, String name, int version) {
		this(context, name, null, version);
	}

	public void onCreate(SQLiteDatabase db) {
		// 删除所有的表
		String [] deleteTable = new String[]{"friend","carrier","message","deliveryMessage","sendMessage"};
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
		sendNotice(db);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// 删除所有的表
				String [] deleteTable = new String[]{"friend","carrier","message","deliveryMessage","sendMessage"};
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
				sendNotice(db);
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
				+ "mailNum varchar(128))";
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
		String sql = "select messageStatus from deliveryMessage where delId = ?";
		Cursor cur = db.rawQuery(sql, new String[]{delId});
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
		String sql = "select signStatus from deliveryMessage where delId = ?";
		Cursor cur = db.rawQuery(sql, new String[]{delId});
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
		String sql ="select count(*) from deliveryMessage where messageStatus=1";
		Cursor cur = db.rawQuery(sql, null);
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
		String sql ="select count(*) from sendMessage where messageStatus=1";
		Cursor cur = db.rawQuery(sql, null);
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
	public List<SendNoticeBean> querySendNotice(SQLiteDatabase db){
		String sql = "select * from sendMessage order by messageTime";
		Cursor cur = db.rawQuery(sql,null);
		List<SendNoticeBean> list = new ArrayList<SendNoticeBean>();
		while(cur.moveToNext()){
			SendNoticeBean dmb = new SendNoticeBean();
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
			list.add(dmb);
		}
		cur.close();
		Log.e("msgggQuerySendNotice", list.toString());
		return list;
	}
	
	
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
			,String employeeNo,String people,String code,String sid,String waybill,String messageStatus,String messageTime,String receiptStatus,String mail,String orgcode,String username, String signStatus){
		String sql = "insert into deliveryMessage(longitude,latitude,mobile,clientId,employeeNo,people,code,sid,waybill,messageStatus,messageTime,receiptStatus,mail_num,orgcode,username,signStatus)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//		if(!queryWaybill(db, waybill)){
		db.execSQL(sql, new Object[]{longitude,latitude,mobile,clientId,employeeNo,people,code,sid,waybill,messageStatus,messageTime,receiptStatus,mail,orgcode,username,signStatus});
//		}
	}
	
	public List<DeliveryMessageBean> queryAllDeliveryMessage(SQLiteDatabase db){
		String sql = "select * from deliveryMessage order by messageTime";
		Cursor cur = db.rawQuery(sql,null);
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
	 *  添加一条时间数据(已揽收)
	 * @param db
	 * @param data 寄件收到时间
	 */
	public void insertSendNotice(SQLiteDatabase db,String data,String status,double longitude,double latitude, String mobile,String clientId,String employeeNo,String people,String code,String sid,String waybill ,String mailStatus,String orderNo,String orgcode ,String username){
		String sql ="insert into sendMessage(messageTime,messageStatus,longitude,latitude,mobile,clientId,employeeNo,people,code,sid,waybill,mailStatus,orderNo,orgcode,username) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		db.execSQL(sql, new Object[]{data,status,longitude,latitude,mobile,clientId,employeeNo,people,code,sid,waybill,mailStatus,orderNo,orgcode,username});
	}
	
	/**
	 *  添加一条时间数据(已妥投)
	 * @param db
	 * @param data 寄件收到时间
	 */
	public void insertSendNotice2(SQLiteDatabase db,String data,String messageStatus,String mailStatus,String mailNum){
		String sql ="insert into sendMessage(messageTime,messageStatus,mailStatus,mailNum) values(?,?,?,?)";
		db.execSQL(sql, new Object[]{data,messageStatus,mailStatus,mailNum});
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
		String sql = "select count(*) from message where messageStatus=1";
		Cursor cur = db.rawQuery(sql, null);
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
//		List<SendNoticeBean> querySendNotice = this.querySendNotice(db);
//		Log.e("msggg", String.valueOf(querySendNotice.size()));
//		Log.e("msggg", querySendNotice.toString());
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
	 * 更具订单号查询派单信息(暂时只获取机构号和员工号)
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
	
	
	
}

 package com.ems.express.constant;

public class Constant {
	public static final int RESULT_CODE_PIC = 100;
	public static final int RESULT_CODE_CAMERA = 101;
	/**
	 * 接口根地址
	 */

//	 public static final String ROOT = "http://192.168.0.145:8080/chatserver";
//	 public static final String ROOT = "http://123.56.45.153:8080/chatserver";
//	 public static final String ROOT = "http://218.245.3.24:8080/pushproxy";
//	 public static final String IM = "http://218.245.3.24:8081/chatserver";
	 
	 public static final String ROOT = "http://218.245.3.16/pushproxy_cluster";
	 public static final String IM = "http://218.245.3.24:8081/chatserver";
	 
	 /*
	  * 上海
	  */
//    public static final String SERVER_ROOT = "http://211.156.193.130:8091/message-service/";
    /*
     * 江西
     */
//	 public static final String SERVER_ROOT = "http://111.75.223.93:9002/post-customer-service/";
	
	 public static final String SERVER_ROOT = "http://111.75.223.93:9008/post-customer-service/";
	/**
	 * 登录
	 */
	public static final String Login = SERVER_ROOT + "MicroPhoneAction/userLogin";
	/**
	 * 签到验证
	 */
	public static final String checkSign = SERVER_ROOT + "UtilsAction/queryIsSignIn";
	/**
	 * 签到
	 */
	public static final String sign = SERVER_ROOT + "UtilsAction/signIn";
	/**
	 * 查询积分
	 */
	public static final String jifenTotal = SERVER_ROOT + "UtilsAction/queryUserIntegral";
	/**
	 * 查询积分列表
	 */
	public static final String jifenList = SERVER_ROOT + "UtilsAction/queryUserIntegralHistory";
	
    /**
     * 注册
     */
    public static final String Regist = SERVER_ROOT + "MicroPhoneAction/userRegedit";

    /**
     * 密码重置
     */
    public static final String ResetPwd = SERVER_ROOT + "MicroPhoneAction/userResetPwd";
    
    /**
     * 签收处理
     */
    
    public static final String SignHandle = SERVER_ROOT + "PhoneAction/customerSign";
    
    /**
     * 获取验证码
     */
    public static final String ValidateCode = SERVER_ROOT + "MicroPhoneAction/sendPhoneValidateCode";
    //上海的重置密码的手机验证
//    public static final String ValidateCode = "http://116.236.177.58:38008/message-service/" + "MicroPhoneAction/sendPhoneValidateCode";
    /**
     * 修改用户信息
     */
    public static final String UpdateUserInfo = SERVER_ROOT + "MicroPhoneAction/updateUser";
	/**
	 * 名址信息查询
	 */
	public static final String QueryAddress = "http://agent.ems.com.cn/PhoneAction/autoComplete";
	/**
	 * 时效查询
	 */
	public static final String TimeQuery = SERVER_ROOT + "UtilsAction/API/time";
	/**
	 * 查询下段信息
	 */
	public static final String QueryNextSection = SERVER_ROOT +"EmployeeAction/findEmployeeMessage";
	
	/**
	 * 寄件预约
	 */
	public static final String SendMail = SERVER_ROOT + "NetworkAction/sendMail";
	/**
	 * 派单
	 */
	public static final String DeliveryMail = SERVER_ROOT + "NewemsAction/receiveMsg";
	
	/**
	 * 运费查询
	 */
//	public static final String Costquery = SERVER_ROOT + "UtilsAction/API/costquery";
//	public static final String Costquery = "http://211.156.193.130:8091/message-service/" + "UtilsAction/API/costquery";
	public static final String Costquery = "http://111.75.223.93:9008/dias/api/helperUser/queryBilling";

	/**
	 * 邮件信息
	 */
	public static final String MailInfo = SERVER_ROOT+"/EmployeeAction/findMailNumToCustomer";
	/**
	 * 运单评价
	 */
	public static final String MailComment = SERVER_ROOT+"/NetworkAction/sendAppraise";
	/**
	 * 发普通文本
	 */
	public static final String ImSendTextMessage = IM +"/message/sendText.html";
	
	/**
	 * IM发送图片语音接口
	 */
	public static final String ImSendImageMessage = IM + "/message/send.html";
	/**
	 * IM查询消息
	 */
	public static final String ImQueryMessage = IM + "/message/getAllMessage.html";
	/**
	 * 发送消息地址
	 */
	public static final String Send = ROOT + "/message/sendMsg";
	/**
	 * 查询消息地址
	 */
	public static final String Query = ROOT + "/message/pushedMsg";
	
	/**
	 * 查询订单历史记录
	 */
	public static final String QueryMailHistory = SERVER_ROOT+"NetworkAction/fingMailHistory";
	/**
	 * 查询订单历史记录
	 */
	public static final String QueryMailHistory2 = SERVER_ROOT+"UtilsAction/queryDetailStatusOrder";
	/**
	 * 查询邮件状态
	 */
	public static final String QueryParcel = SERVER_ROOT+"UtilsAction/queryParcel";
	/**
	 * 崔单
	 */
	public static final String Remind = SERVER_ROOT+"UtilsAction/remind";
	/**
	 * 退单
	 */
	public static final String TurnBackOrder =SERVER_ROOT +"UtilsAction/turnBackOrder";
	
	/**
	 * @文件上传 文件上传，录音，图片
	 */
	public static final String FileUpload = ROOT + "/file/sendBinaryMsg.html";
	/**
	 * @文件下载 文件下载，录音，图片
	 */
	public static final String FileDownload = ROOT + "/file/download.html";
	
	/**
	 * 查询寄件人
	 */
	public static final String querySenderList = SERVER_ROOT + "UtilsAction/queryUserAddress";
	
	/**
	 * 添加寄件人
	 */
	public static final String saveSender = SERVER_ROOT + "UtilsAction/saveOrUpdateUserAddress";
	/**
	 * 编辑寄件人
	 */
	public static final String editSender = SERVER_ROOT + "UtilsAction/saveOrUpdateUserAddress";
	/**
	 * 删除寄件人
	 */
	public static final String deleteSender = SERVER_ROOT + "UtilsAction/deleteUserAddress";
	/**
	 * 清空寄件人
	 */
	public static final String clearSender = SERVER_ROOT + "UtilsAction/deleteAllUserAddress";
	/*
	 * 上传更新支付方式
	 */
	public static final String updatePayment = SERVER_ROOT + "UtilsAction/userCharge";
	/*
	 * 支付的回调
	 */
	public static final String alipayNotify = SERVER_ROOT + "UtilsAction/alipayNotify";
	/*
	 * 点击获取短信验证码
	 */
	public static final String smsCode = SERVER_ROOT + "MicroPhoneAction/sendMsg";
	/*
	 * 注册的时候点击下一步对短信验证码的验证
	 */
	public static final String checkSmsCode = SERVER_ROOT + "MicroPhoneAction/checkMobileCode";
	
}

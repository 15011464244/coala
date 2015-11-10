package com.cn.net.cnpl;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.model.User;

public class Global {
		//新加PDA投递功能
	//8002是测试端口     8009
//	public static final String BASE_URL="http://192.168.1.21:8081/cnpl/service/phoneAppDlvService/";
//	public static final String BASE_URL="http://10.2.3.37:8080/cnpl/service/phoneAppDlvService/";
//	public static final String BASE_URL="http://211.156.220.98:8080/cnpl/service/phoneAppDlvService/";
//	public static final String BASE_URL="http://10.3.26.33:8080/cnpl/service/phoneAppDlvService/";
//	public static final String BASE_URL="http://211.156.220.98:9300/cnpl/service/phoneAppDlvService/";
	
//	public static final String BASE_URL="http://211.156.220.104:9100/cnpl/service/phoneAppDlvService/";//外网正式 
	
//	public static final String BASE_URL="http://10.2.3.36:8080/cnpl/service/phoneAppDlvService/";//内网测试  sd1001  123456
//	public static final String BASE_URL="http://211.156.220.102/cnpl/service/phoneAppDlvService/";//外网测试  sd1001  123456  37
	
	public static final String BASE_URL="http://211.156.220.101/cnpl/service/phoneAppDlvService/";//外网测试 sd1002  123456 36
	
	public static final String URL_LOGIN = "/userLoginByApp";//登录
	public static final String URL_ANDROIDVERSION = "checkVersionByApp"; //版本检测
	public static final String GETPROJREASONCDINFOSBYID = "getProjReasonCdInfosById"; //未妥投异常信息
	
	public static final String URL_MAIL_FOLLOW = "MailFollowUploadAction?header=11";//邮件地址跟踪
	public static final String URL_MAIL_UP = "MailHandAction?header=2";//交接信息上传接口
	public static final String URL_MAIL_OFF = "OffDutyAction";//下班
	public static final String URL_MAIL_ON = "OnDutyAction";//上班
	//数据下载
	public static final String DATA_DOWNLOAD="DataAction";
	
	public static final String UPMAILDLV="UPmaildlv.txt";
	
	public static final String MAILDLV="maildlv.txt";
	//投递数据上传
	public static final String DLV_UPLOAD="generateOrderFeedBackByApp?para=13";
	
//	public static final String EXCEPTIONACTION = "ExceptionAction?header=10";//客户端异常反馈
	
	public static final String URL_GETDLVDETAILSBYNUM = "getDlvDetailsByNum";// 配送单下载
	
	public static final String SOCIETYMAILDLVIMG="elecReverseUploadByApp"; //电子返单上传
	
	public static final String URL_CUSTMODIFYPASSWORD = "/json/BIGCUST/changepwd";
	public static final String URL_MODIFYPASSWORD = "/json/API/changepwd";//修改密码
	
	public static String M_USERFLAG = "1";
	public static String M_ACCESSID = "";
	public static String M_FUNCID = "";
	public static String M_FUNCNAME = "";
	public static String M_DIRID = "";
	public static String M_DIRNAME = "";

	public static String NAME = "";
	
	public static String DIALOG_NAME = "CNPL";
	
	//上传标示 1：上传 0：未上传
	public static final String UPLOAD = "1";
	public static final String UNUPLOAD = "0";
	
	// 国际国内  0：国际 1：国内
	public static final String INTERNAL = "1";
	public static final String INTERNATIONAL = "0";

	// 投递情况 I:投递 H:未投递
	public static final String DLVCODE = "I";
	public static final String UNDLVCODE = "H";
	
	
	//  0:未转出1:转出 2:投递
	public static final String MAILIN = "0";
	public static final String MAILOUT = "1";
	public static final String MAILDlV = "2";
	
	// '0：已撤销 1：交接中 2：已完成 '
	public static final String MAILCANCEL = "0";
	public static final String MAILEX = "1";
	public static final String MAILCOM = "2";
	
	// 1.机构 2.承运商
	public static final String ORGANIZATION = "1";
	public static final String CARRY = "3";
	
	// 1.是0.否
	public static final String  STOP= "0";
	public static final String UP = "1";
	
	// 1.是2.否
	public static final String  ELECTRON= "1";
	public static final String PAPER = "2";
	
	// 1.:转入2.:转出
	public static final String  HANDIN= "1";
	public static final String HANDOUT = "2";
	
	// '0：完整 1：破损 2：丢失'
	public static final String MAILINTACT = "0";
	public static final String MAILDISREPAIR = "1";
	public static final String MAILLOSE = "2";
	
	public final static int TASK_LOOP_COMPLETE = 0;
	
	public static int myemsFlag=0;
	public static List<Activity> activities=new ArrayList<Activity>();

	
	
	
	public static void exit(){
		allfinish();
		 logout();
	}
	
	public static void allfinish(){
		for(int i=0;i<activities.size();i++){
			if(activities.get(i) != null){
			activities.get(i).finish();
			}
		}
		 activities.clear();
	}
	
	public static void logout(){
		myemsFlag=0;
		NAME = "";
		M_ACCESSID = "";
		M_FUNCID = "";
		M_FUNCNAME = "";
		M_DIRID = "";
		M_DIRNAME = "";
		org_code="";
		login_name="";
		 }
	
	private  static String org_code = "";
	private  static String login_name = "";

	public static String getOrg_code(Context context) {
		if(org_code == null || "".equals(org_code)){
		User user=DaoFactory.getInstance().getUserDAO(context).FindUser();
				return user.getOrgCode();//获取承运商代码
		}
		return org_code;
	}

	public static void setOrg_code(String orgcode) {
		org_code = orgcode;
	}

	public static String getLogin_name(Context context) {
		if(login_name == null || "".equals(login_name)){
		User user=DaoFactory.getInstance().getUserDAO(context).FindUser();
				return user.getLoginName();
		}
		return login_name;
	}

	public static void setLogin_name(String loginname) {
		login_name = loginname;
	}
	
}

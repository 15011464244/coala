package com.cn.net.cnpl.model;

import java.io.Serializable;

import android.content.Context;

import com.cn.net.cnpl.db.DaoFactory;
import com.cn.net.cnpl.db.dao.UserDao;

public class User implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2894033453866300613L;

	private static DaoFactory daoFactory = DaoFactory.getInstance();

	
	private String loginName;
	private String mobile;
	private String password;
	private String isPwd;
	private String isAutoLogin;
	private String flag;
	private String telephone;
	private String userName;
	private String transitCode;
	private String transitName;
	private String orgCode;
	private String orgName;
	private String key;
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}


	
	
	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public User() {
		super();
	}

	public static User FindUser(Context context) {
		UserDao dao = null;
		try {
			dao = daoFactory.getUserDAO(context);
			return dao.FindUser();
		} finally {
			if (dao != null) {
				dao.close();
			}
		}
	}
	public static Long SaveUser(Context context,User user) {
		UserDao dao = null;
		try {
			dao = daoFactory.getUserDAO(context);
			dao.DelUser();
			return dao.SaveUser(user);
		} finally {
			if (dao != null) {
				dao.close();
			}
		}
	}
	public static Long DelUser(Context context) {
		UserDao dao = null;
		try {
			dao = daoFactory.getUserDAO(context);
			return dao.DelUser();
		} finally {
			if (dao != null) {
				dao.close();
			}
		}
	}
	public static Long UpdateUser(Context context) {
		UserDao dao = null;
		try {
			dao = daoFactory.getUserDAO(context);
			return dao.UpdateUser();
		} finally {
			if (dao != null) {
				dao.close();
			}
		}
	}
	
	

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}
	

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTransitCode() {
		return transitCode;
	}

	public void setTransitCode(String transitCode) {
		this.transitCode = transitCode;
	}

	public String getTransitName() {
		return transitName;
	}

	public void setTransitName(String transitName) {
		this.transitName = transitName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIsPwd() {
		return isPwd;
	}

	public void setIsPwd(String isPwd) {
		this.isPwd = isPwd;
	}

	public String getIsAutoLogin() {
		return isAutoLogin;
	}

	public void setIsAutoLogin(String isAutoLogin) {
		this.isAutoLogin = isAutoLogin;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
}

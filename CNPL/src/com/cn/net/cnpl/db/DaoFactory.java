package com.cn.net.cnpl.db;

import android.content.Context;

import com.cn.net.cnpl.db.dao.DlvStateDao;
import com.cn.net.cnpl.db.dao.FollowActionDao;
import com.cn.net.cnpl.db.dao.FollowAlarmDao;
import com.cn.net.cnpl.db.dao.LoginBandleDao;
import com.cn.net.cnpl.db.dao.MailDao;
import com.cn.net.cnpl.db.dao.MailFollowDao;
import com.cn.net.cnpl.db.dao.MailHandDao;
import com.cn.net.cnpl.db.dao.MailHandDetailDao;
import com.cn.net.cnpl.db.dao.MailUploadDao;
import com.cn.net.cnpl.db.dao.ProjReasonDao;
import com.cn.net.cnpl.db.dao.ResOrgDao;
import com.cn.net.cnpl.db.dao.StateFollowDao;
import com.cn.net.cnpl.db.dao.TransferDao;
import com.cn.net.cnpl.db.dao.UserDao;
import com.cn.net.cnpl.db.dao.WorkLogDao;

public class DaoFactory {

	private Context globalContext = null;

	private static DaoFactory instance = null;

	private UserDao userDao = null;
	private UserDao userDao2 = null;
	private UserDao userDao3 = null;

	private MailDao mailDao2 = null;
	private MailDao mailDao = null;
	private DlvStateDao dlvStateDao = null;
	private StateFollowDao stateFollowDao = null;
	private FollowActionDao followActionDao = null;
	private ResOrgDao resOrgDao = null;

	private MailHandDao mailhandDao = null;
	private MailHandDao mailhandDao2 = null;
	private MailHandDetailDao mailhanddetailDao = null;

	private MailHandDetailDao mailhanddetailDao2 = null;

	private MailHandDetailDao mailhanddetailDao3 = null;

	private MailHandDetailDao mailhanddetailDao4 = null;

	private MailFollowDao mailFollowDao = null;

	private WorkLogDao workLogDao = null;

	private LoginBandleDao loginBandleDao = null;
	
	private FollowAlarmDao fllowAlarmDao = null;
	
	private MailUploadDao mailUploadDao = null;
	
	private ProjReasonDao projReasonDao  = null;
	
	private TransferDao transferDao  = null;

	public static DaoFactory getInstance() {
		if (instance == null) {
			instance = new DaoFactory();
		}
		return instance;
	}

	public void init() {
		userDao = getInstance().getUserDAO(globalContext);
		userDao2 = getInstance().getUserDAO2(globalContext);
		userDao3 = getInstance().getUserDAO3(globalContext);
		mailDao=getInstance().getMailDao(globalContext);
		mailDao2=getInstance().getMailDao2(globalContext);
		mailhandDao=getInstance().getMailHandDao(globalContext);
		mailhandDao2=getInstance().getMailHandDao2(globalContext);
		mailFollowDao=getInstance().getMailFollowDao(globalContext);
//		stateFollowDao = getInstance().getStateFollowDao(globalContext);
//		followActionDao = getInstance().getFollowActionDao(globalContext);
		mailhanddetailDao = getInstance().getMailHandDetailDao(globalContext);
		mailhanddetailDao2 = getInstance().getMailHandDetailDao2(globalContext);
		mailhanddetailDao3 = getInstance().getMailHandDetailDao3(globalContext);
		mailhanddetailDao4 = getInstance().getMailHandDetailDao4(globalContext);
		dlvStateDao = getInstance().getDlvStateDao(globalContext);
		loginBandleDao = getInstance().getLoginBandleDao(globalContext);
		fllowAlarmDao = getInstance().getFollowAlarmDao(globalContext);
		mailUploadDao = getInstance().getMailUploadDao(globalContext);
		projReasonDao= getInstance().getProjReasonDao(globalContext);
		transferDao = getInstance().getTransferDao(globalContext);
	}

	private DaoFactory() {
	}
	
	public TransferDao getTransferDao(Context context) {
		if (transferDao == null) {
			transferDao = new TransferDao(GetContext(context));
		}
		return transferDao;
	}
	
	public ProjReasonDao getProjReasonDao(Context context) {
		
		if (projReasonDao == null) {
			projReasonDao = new ProjReasonDao(GetContext(context));
		}
		return projReasonDao;
	}
	
	public LoginBandleDao getLoginBandleDao(Context context) {
		
			if (loginBandleDao == null) {
				loginBandleDao = new LoginBandleDao(GetContext(context));
			}
			return loginBandleDao;
	}

	public WorkLogDao getWorkLogDao(Context context) {
		
			if (workLogDao == null) {
				workLogDao = new WorkLogDao(GetContext(context));
			}
			return workLogDao;
	}

	public MailFollowDao getMailFollowDao(Context context) {
		
			if (mailFollowDao == null) {
				mailFollowDao = new MailFollowDao(GetContext(context));
			}
			return mailFollowDao;
	}

	public MailFollowDao getMailFollowDao2(Context context) {
		
			if (mailFollowDao == null) {
				mailFollowDao = new MailFollowDao(GetContext(context));
			}
			return mailFollowDao;
	}

	public ResOrgDao getResOrgDao(Context context) {
		
			if (resOrgDao == null) {
				resOrgDao = new ResOrgDao(GetContext(context));
			}
			return resOrgDao;
	}

	public FollowActionDao getFollowActionDao(Context context) {
		
			if (followActionDao == null) {
				followActionDao = new FollowActionDao(GetContext(context));
			}
			return followActionDao;
	}

	public StateFollowDao getStateFollowDao(Context context) {
		
			if (stateFollowDao == null) {
				stateFollowDao = new StateFollowDao(GetContext(context));
			}
			return stateFollowDao;
	}

	public DlvStateDao getDlvStateDao(Context context) {
		
			if (dlvStateDao == null) {
				dlvStateDao = new DlvStateDao(GetContext(context));
			}
			return dlvStateDao;
	}

	public MailDao getMailDao2(Context context) {
		
			if (mailDao2 == null) {
				mailDao2 = new MailDao(GetContext(context));
			}
			return mailDao2;
	}

	public MailDao getMailDao(Context context) {
		
			if (mailDao == null) {
				mailDao = new MailDao(GetContext(context));
			}
			return mailDao;
	}

	public MailHandDao getMailHandDao(Context context) {
		
			if (mailhandDao == null) {
				mailhandDao = new MailHandDao(GetContext(context));
			}
			return mailhandDao;
	}

	public MailHandDao getMailHandDao2(Context context) {
		
			if (mailhandDao2 == null) {
				mailhandDao2 = new MailHandDao(GetContext(context));
			}
			return mailhandDao2;
	}

	public MailHandDetailDao getMailHandDetailDao(Context context) {
		
			if (mailhanddetailDao == null) {
				mailhanddetailDao = new MailHandDetailDao(GetContext(context));
			}
			return mailhanddetailDao;
	}

	public MailHandDetailDao getMailHandDetailDao2(Context context) {
		
			if (mailhanddetailDao2 == null) {
				mailhanddetailDao2 = new MailHandDetailDao(GetContext(context));
			}
			return mailhanddetailDao2;
	}

	public MailHandDetailDao getMailHandDetailDao3(Context context) {
		
			if (mailhanddetailDao3 == null) {
				mailhanddetailDao3 = new MailHandDetailDao(GetContext(context));
			}
			return mailhanddetailDao3;
	}

	public MailHandDetailDao getMailHandDetailDao4(Context context) {
		
		if (mailhanddetailDao4 == null) {
			mailhanddetailDao4 = new MailHandDetailDao(GetContext(context));
		}
		return mailhanddetailDao4;
	}

	public FollowAlarmDao getFollowAlarmDao(Context context) {
		
		if (fllowAlarmDao == null) {
			fllowAlarmDao = new FollowAlarmDao(GetContext(context));
		}
		return fllowAlarmDao;
	}
	
	public MailUploadDao getMailUploadDao(Context context) {
		
		if (mailUploadDao == null) {
			mailUploadDao = new MailUploadDao(GetContext(context));
		}
		return mailUploadDao;
	}
	
	public UserDao getUserDAO(Context context) {
		
			if (userDao == null) {
				userDao = new UserDao(GetContext(context));
			}
			return userDao;
	}

	public UserDao getUserDAO2(Context context) {
		
			if (userDao2 == null) {
				userDao2 = new UserDao(GetContext(context));
			}
			return userDao2;
	}

	public UserDao getUserDAO3(Context context) {
		
			if (userDao3 == null) {
				userDao3 = new UserDao(GetContext(context));
			}
			return userDao3;
	}

	private Context GetContext(Context context) {
		if (globalContext != null) {
			return globalContext;
		} else {
			return context;
		}
	}

	public Context getGlobalContext() {
		return globalContext;
	}

	public void setGlobalContext(Context globalContext) {
		this.globalContext = globalContext;
	}
}

package com.arvin.koalapush.keep;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

import com.arvin.koalapush.util.Log4jUtil;
import com.arvin.koalapush.util.LogUtil;

import android.util.Log;

public class ClientKeepAliveFactoryImpl implements KeepAliveMessageFactory {

	private static final String TAG = "ClientKeepAliveFactoryImpl";
	
	public static final String HEARTREQUEST = "1";
	public static final String HEARTRESPONSE = "2";
	private static Log4jUtil logger = Log4jUtil.getLogger(ClientKeepAliveFactoryImpl.class.getName());

	@Override
	public Object getRequest(IoSession arg0) {
//		Log.e(TAG, "request:请求内容"+HEARTREQUEST);
		return HEARTREQUEST;
	}

	@Override
	public Object getResponse(IoSession arg0, Object arg1) {
		LogUtil.i("response:请求内容"+HEARTRESPONSE);
		logger.info("response:请求内容"+HEARTRESPONSE);
		return null;
	}

	@Override
	public boolean isRequest(IoSession arg0, Object heartMsg) {
		LogUtil.i("isRequest：请求内容："+heartMsg);
		logger.info("isRequest：请求内容："+heartMsg);
		if(HEARTREQUEST.equals(heartMsg.toString())){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean isResponse(IoSession arg0, Object heartMsg) {
		LogUtil.i("isResponse:收到心跳包"+heartMsg);
		logger.info("isResponse:收到心跳包"+heartMsg);
		if(HEARTRESPONSE.equals(heartMsg)){
			LogUtil.i("client receive response message=" + heartMsg+";["+arg0.getRemoteAddress().toString()+"]");
			logger.info("client receive response message=" + heartMsg+";["+arg0.getRemoteAddress().toString()+"]");
			return true;
		} else {
			LogUtil.i("client receive response invalid ! [" + heartMsg + "]"+";["+arg0.getRemoteAddress().toString()+"]");
			logger.info("client receive response invalid ! [" + heartMsg + "]"+";["+arg0.getRemoteAddress().toString()+"]");
			return false;
		}
	}

}

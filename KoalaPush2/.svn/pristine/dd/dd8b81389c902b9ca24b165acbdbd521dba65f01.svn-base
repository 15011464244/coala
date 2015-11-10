package com.arvin.koalapush.keep;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import android.util.Log;

public class ClientKeepAliveFactoryImpl implements KeepAliveMessageFactory {

	private static final String TAG = "ClientKeepAliveFactoryImpl";
	
	public static final String HEARTREQUEST = "1";
	public static final String HEARTRESPONSE = "2";

	@Override
	public Object getRequest(IoSession arg0) {
//		Log.e(TAG, "request:请求内容"+HEARTREQUEST);
		return null;
	}

	@Override
	public Object getResponse(IoSession arg0, Object arg1) {
		Log.e(TAG, "response:请求内容"+HEARTRESPONSE);
		return HEARTRESPONSE;
	}

	@Override
	public boolean isRequest(IoSession arg0, Object heartMsg) {
		Log.e(TAG, "isRequest：请求内容："+heartMsg);
		if(HEARTREQUEST.equals(heartMsg.toString())){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean isResponse(IoSession arg0, Object heartMsg) {
		Log.e(TAG, "isResponse:收到心跳包"+heartMsg);
		if(HEARTREQUEST.equals(heartMsg)){
			Log.e(TAG,"client receive response message=" + heartMsg+";["+arg0.getRemoteAddress().toString()+"]");
			return true;
		} else {
			Log.e(TAG,"client receive response invalid ! [" + heartMsg + "]"+";["+arg0.getRemoteAddress().toString()+"]");
			return false;
		}
	}

}

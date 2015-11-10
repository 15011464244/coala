package com.arvin.koalapush.potter;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.arvin.koalapush.bean.ReceivedMsgBean;
import com.arvin.koalapush.bean.ReceivedSessionBean;
import com.arvin.koalapush.constant.ParamConstant;
import com.arvin.koalapush.net.resp.BaseResp;
import com.arvin.koalapush.util.LogUtil;
import com.arvin.koalapush.util.SpfsUtil;

public class MinaClientHanlder extends IoHandlerAdapter {
	private PushService mPushService;
	private int count = 0;
	
	public MinaClientHanlder(PushService mPushService) {
		super();
		this.mPushService = mPushService;
	}
	
	/**
	 * 这个方法在连接被打开时调用，它总是在sessionCreated()方法之后被调用。对于TCP 来
	 * 说，它是在连接被建立之后调用，你可以在这里执行一些认证操作、发送数据等。
	 */
	public void sessionOpened(IoSession session) throws Exception {
		LogUtil.print("sessionOpened");
		String clientId = SpfsUtil.getClientId();
		//给mina发送clientId
		Log.e("msggg",clientId);
		if(null != clientId){
			session.write("{\"clientId\":"+"\""+clientId+"\"}");
		}
		
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		super.messageSent(session, message);
		LogUtil.print("messageSent:" + message.toString());
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		count++;
		String m = spliteString(message);
		String n = m;
		LogUtil.print(count + "-json:" + m);
		BaseResp base = JSONObject.parseObject(m, BaseResp.class);
		LogUtil.w(count + "-Base:" + base.toString());
		System.out.println("m--"+m);
		Message msg = new Message();
		
		// 0：成功 1：失败 2:bind 3：验证成功
		System.out.println("code:"+base.getResCode());
		switch (Integer.parseInt(base.getResCode())) {
		case 0:
			ReceivedMsgBean rmsg = JSONObject.parseObject(n,
					ReceivedMsgBean.class);
			System.out.println("rsmg:"+rmsg.toString());
			msg.what = ParamConstant.RECEIVED;
			msg.obj = rmsg;
			mPushService.myHandler.sendMessage(msg);
			break;
		case 1:
			LogUtil.e("服务器处理失败");
			break;
//		case 2:
//			ReceivedSessionBean bean = JSONObject.parseObject(m, ReceivedSessionBean.class);
//			msg.what = mPushService.BIND;
//			msg.obj = bean;
//			mPushService.myHandler.sendMessage(msg);
//			break;
		case 3:
			LogUtil.print("收到推送已向服务器验证");
			break;
		case 4:
			LogUtil.print("连接mina成功，拿到sessionId");
			break;

		default:
			break;
		}

	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		LogUtil.e("异常：3039");
		session.close(true);
	}
	
	@Override  
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {  
//        session.write("1");
//        if(IdleStatus.READER_IDLE.equals(status)){
//        	LogUtil.w("-客户端与服务端连接[空闲] - " + status.toString());  
        	session.close(true);
//        }else if(IdleStatus.WRITER_IDLE.equals(status)){
//        	LogUtil.w("-客户端与服务端连接[空闲] - " + status.toString());  
//        }else{
//        	LogUtil.w("-客户端与服务端连接[空闲] - " + status.toString());  
//        }
        
    }  

	/**
	 * 去掉空格
	 * 
	 * @param message
	 * @return
	 */
	public String spliteString(Object message) {
		String m = (String) message;
		m = m.substring(0, m.lastIndexOf("}") + 1);
		return m;
	}

}
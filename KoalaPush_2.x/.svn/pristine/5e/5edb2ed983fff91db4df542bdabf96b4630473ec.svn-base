package com.arvin.koalapush.potter;

import java.text.SimpleDateFormat;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import android.content.Context;
import android.content.Intent;
import com.alibaba.fastjson.JSONObject;
import com.arvin.koalapush.api.KpushConfig;
import com.arvin.koalapush.bean.ReceivedMsgBean;
import com.arvin.koalapush.net.resp.BaseResp;
import com.arvin.koalapush.util.Log4jUtil;
import com.arvin.koalapush.util.LogUtil;
import com.arvin.koalapush.util.SpfsUtil;

public class MinaClientHanlder extends IoHandlerAdapter {
	private int count = 0;
	private Context mContext;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static Log4jUtil logger = Log4jUtil.getLogger(MinaClientHanlder.class.getName());
	
	//保存日志请求
//	private Intent intent;
	public MinaClientHanlder(Context context) {
		super();
		this.mContext = context;
//		if(intent==null){
//			intent = new Intent();
//		}
	}

	
	
	/**
	 * 这个方法在连接被打开时调用，它总是在sessionCreated()方法之后被调用。对于TCP 来
	 * 说，它是在连接被建立之后调用，你可以在这里执行一些认证操作、发送数据等。
	 */
	public void sessionOpened(IoSession session) throws Exception {
		LogUtil.i("sessionOpened");
		logger.info("sessionOpened");
		//给mina发送clientId
		LogUtil.i(SpfsUtil.getClientId());
		logger.info(SpfsUtil.getClientId());
		if(null != SpfsUtil.getClientId()){
			session.write("{\"clientId\":"+"\""+SpfsUtil.getClientId()+"\"}");
		}
		
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		super.messageSent(session, message);
		LogUtil.i("messageSent:" + message.toString());
		logger.info("messageSent:" + message.toString());
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		count++;
		String m = message.toString().trim();//spliteString();
		LogUtil.i(count + "-json:" + m);
		logger.info(count + "-json:" + m);
		BaseResp base = JSONObject.parseObject(m, BaseResp.class);
		LogUtil.i(count + "-Base:" + base.toString());
		logger.info(count + "-Base:" + base.toString());
//		System.out.println("m--"+m);
		// 0：成功 1：失败 2:连接成功 3：验证成功 1000:Reids绑定成功推送连接所有流程正确开始bind
//		System.out.println("code:"+base.getResCode());
		LogUtil.i("code:"+base.getResCode());
		logger.info("code:"+base.getResCode());
		switch (Integer.parseInt(base.getResCode())) {
		case 0:
			LogUtil.i("rsmg:"+m);
			logger.info("rsmg:"+m);
			ReceivedMsgBean rmsg = JSONObject.parseObject(m,
					ReceivedMsgBean.class);
			Intent intent = new Intent();
			intent.setAction(KpushConfig.RECEIVED);
			intent.putExtra("message", rmsg);
			mContext.sendBroadcast(intent);
			//上传日志
//			saveLog(rmsg);
			break;
		case 1:
			LogUtil.e("服务器处理失败");
			logger.error("服务器处理失败");
			break;
		case 2:
			LogUtil.i("rsmg:"+m);
			break;
		case 3:
			LogUtil.i("收到推送已向服务器验证");
			logger.info("收到推送已向服务器验证");
			break;
		case 4:
			LogUtil.i("连接mina成功，拿到sessionId");
			break;
		case 1000:
			Intent intent1 = new Intent();
			intent1.setAction(KpushConfig.BIND);
			mContext.sendBroadcast(intent1);
			break;
		case 1001:
			if(null != SpfsUtil.getClientId()){
				session.write("{\"clientId\":"+"\""+SpfsUtil.getClientId()+"\"}");
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		LogUtil.e("异常：3039");
//		KpushConfig.mReceivedListener.onClose("连接已断开",sdf.format(new Date()));
	}
	
	@Override  
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {  
//        session.write("1");
        if(IdleStatus.READER_IDLE.equals(status)){
        	LogUtil.w("-客户端与服务端连接[空闲] - " + status.toString());  
//        	session.close(true);
//		logger.error("客户端与服务连接出现空闲 Idle,空闲类型");
        }else if(IdleStatus.WRITER_IDLE.equals(status)){
        	LogUtil.w("-客户端与服务端连接[空闲] - " + status.toString());  
        }else{
        	LogUtil.w("-客户端与服务端连接[空闲] - " + status.toString());  
        }
        
    } 
	
	/**
	 * 存储日志
	 */
	public void saveLog(ReceivedMsgBean rmsg){
		if(null != rmsg){
			String strContent = JSONObject.toJSONString(rmsg);
			KpushConfig.dbHelper.insertRequest(KpushConfig.dbHelper.getWritableDatabase(), SpfsUtil.getClientId(),System.currentTimeMillis()/1000,"log"
					,strContent,Integer.parseInt(rmsg.getMessageEntity().getMessage_id()),1);
		}else{
			return;
		}
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
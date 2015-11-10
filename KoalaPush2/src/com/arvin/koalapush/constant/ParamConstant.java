package com.arvin.koalapush.constant;

public class ParamConstant {
	/** mina连接成功 */
	public final static int OPENED = 0;
	/** 让service 发送一个消息 */
	public final static int MSG_SEND = 1;
	/** 绑定 */
	public final static int BIND = 2;
	/** 让service 接收一个消息 */
	public final static int RECEIVED = 3;
	/** 接收错误 ，mina断开连接... */
	public final static int RECEIVED_ERROR = 4;
	/** 让service 显示一个消息的命令 */
	public final static int PUSH_INIT = 5;
	/** 登录 */
	public final static int PUSH_LOGIN = 6;
	

}

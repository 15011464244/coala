package com.arvin.koalapush.constant;

public class KpushConstant {
	public static final String http_addr = "http://218.245.3.16/pushproxy_cluster";
	public static final String sync = http_addr + "/message/syncMsgTag";
	public static final String bind = http_addr + "/device/syncConnectState";
	public static final String login = http_addr + "/device/connect";
	public static final String send = http_addr + "/message/sendMsg";
	public static final String getMsg = http_addr + "/message/pushedMsg";
	public static final String savePushLog = http_addr + "/pushLog/savePushLog";

}

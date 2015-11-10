package com.arvin.koalapush.bean;

import com.arvin.koalapush.net.resp.BaseResp;

public class ReceivedSessionBean extends BaseResp {
	private static final long serialVersionUID = 1L;
	private String sessionId;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
}

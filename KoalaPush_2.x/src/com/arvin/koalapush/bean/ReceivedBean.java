package com.arvin.koalapush.bean;

import java.io.Serializable;

import org.apache.mina.core.session.IoSession;

public class ReceivedBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private IoSession session;
	private Object message;

	public ReceivedBean(IoSession session, Object message) {
		super();
		this.session = session;
		this.message = message;
	}

	public IoSession getSession() {
		return session;
	}

	public void setSession(IoSession session) {
		this.session = session;
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ReceivedBean [session=" + session + ", message=" + message
				+ "]";
	}

}

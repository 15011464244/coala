package com.arvin.koalapush.bean;

import java.io.Serializable;

public class DeviceBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String device_id;
	private String device_type;
	private String app_key;
	private String token;
	private String client_id;

	public DeviceBean(String device_id, String device_type, String app_key,
			String client_id) {
		super();
		this.device_id = device_id;
		this.device_type = device_type;
		this.app_key = app_key;
		this.client_id = client_id;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getDevice_type() {
		return device_type;
	}

	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}

	public String getApp_key() {
		return app_key;
	}

	public void setApp_key(String app_key) {
		this.app_key = app_key;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	@Override
	public String toString() {
		return "DeviceModel [device_id=" + device_id + ", device_type="
				+ device_type + ", app_key=" + app_key + ", token=" + token
				+ ", client_id=" + client_id + "]";
	}

}

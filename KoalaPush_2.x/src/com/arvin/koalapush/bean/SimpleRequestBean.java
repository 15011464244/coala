package com.arvin.koalapush.bean;

public class SimpleRequestBean {
	private int id;
	private String class_name;
	private String method_name;
	private String method_behavior;
	private String log_content;
	private int message_id;
	private int state;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the class_name
	 */
	public String getClass_name() {
		return class_name;
	}
	/**
	 * @param class_name the class_name to set
	 */
	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}
	/**
	 * @return the method_name
	 */
	public String getMethod_name() {
		return method_name;
	}
	/**
	 * @param method_name the method_name to set
	 */
	public void setMethod_name(String method_name) {
		this.method_name = method_name;
	}
	/**
	 * @return the method_behavior
	 */
	public String getMethod_behavior() {
		return method_behavior;
	}
	/**
	 * @param method_behavior the method_behavior to set
	 */
	public void setMethod_behavior(String method_behavior) {
		this.method_behavior = method_behavior;
	}
	/**
	 * @return the log_content
	 */
	public String getLog_content() {
		return log_content;
	}
	/**
	 * @param log_content the log_content to set
	 */
	public void setLog_content(String log_content) {
		this.log_content = log_content;
	}
	/**
	 * @return the message_id
	 */
	public int getMessage_id() {
		return message_id;
	}
	/**
	 * @param message_id the message_id to set
	 */
	public void setMessage_id(int message_id) {
		this.message_id = message_id;
	}
	/**
	 * @return the state
	 */
	public int getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(int state) {
		this.state = state;
	}
}

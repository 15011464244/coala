package com.koala.emm.model;

import java.util.List;

public class CheckBean {
	private Boolean state;
	private String msg;
	private List<CheckResult> result;
	public Boolean getState() {
		return state;
	}
	@Override
	public String toString() {
		return "CheckBean [state=" + state + ", msg=" + msg + ", result="
				+ result + "]";
	}
	public void setState(Boolean state) {
		this.state = state;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public List<CheckResult> getResult() {
		return result;
	}
	public void setResult(List<CheckResult> result) {
		this.result = result;
	}

}

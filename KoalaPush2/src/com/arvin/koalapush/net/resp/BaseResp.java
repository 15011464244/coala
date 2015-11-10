package com.arvin.koalapush.net.resp;

import java.io.Serializable;

public class BaseResp implements Serializable {
	private static final long serialVersionUID = 1L;
	private String resCode;
	private String resMsg;
	private String resType;

	/**
	 * @return the resCode
	 */
	public String getResCode() {
		return resCode;
	}

	/**
	 * @param resCode
	 *            the resCode to set
	 */
	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	/**
	 * @return the resMsg
	 */
	public String getResMsg() {
		return resMsg;
	}

	/**
	 * @param resMsg
	 *            the resMsg to set
	 */
	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}

	/**
	 * @return the resType
	 */
	public String getResType() {
		return resType;
	}

	/**
	 * @param resType
	 *            the resType to set
	 */
	public void setResType(String resType) {
		this.resType = resType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BaseResp [resCode=" + resCode + ", resMsg=" + resMsg
				+ ", resType=" + resType + "]";
	}

}

package com.joyveb.redeem.common;

import lombok.Data;

/**
 * 返回信息
 * @author system
 */

@Data
public class ReturnInfo {

	private String description;
	private String retcode;
	private Object retObj;
	private boolean success;

	public ReturnInfo() {}

	public ReturnInfo(String description, String retcode, Object retObj, boolean success) {
		this.description = description;
		this.retcode = retcode;
		this.retObj = retObj;
		this.success = success;
	}

	public ReturnInfo(String money) {
		this.description = money;
		this.retObj = null;
		this.success = true;
	}
}

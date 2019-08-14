package com.joyveb.jkpmall.common;

import lombok.Data;

/**
 * @author lidongyang
 * @date 2018/6/1
 */
@Data
public class ReturnInfo {

	private String description;
	private int retcode;
	private Object retObj;
	private boolean success;


	public ReturnInfo() {}

	public static ReturnInfo Success = new ReturnInfo("SUCCESS", true);
	public static ReturnInfo Faild = new ReturnInfo("Faild", false);

	public ReturnInfo(String description, int retcode, Object retObj, boolean success) {
		this.description = description;
		this.retcode = retcode;
		this.retObj = retObj;
		this.success = success;
	}

	public ReturnInfo(boolean success) {
		this.success = success;
	}

	public ReturnInfo(String description, boolean success) {
		this.description = description;
		this.retObj = null;
		this.success = success;
	}
}

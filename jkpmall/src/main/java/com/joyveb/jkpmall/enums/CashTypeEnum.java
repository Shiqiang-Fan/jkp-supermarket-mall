package com.joyveb.jkpmall.enums;

import lombok.Getter;

/**
 * @author Saxon
 * @date 2018/7/31
 */
public enum CashTypeEnum {

	/**
	 * 机读
	 */
	MACHINE_READABLE("0"),
	/**
	 * 人工
	 */
	BY_HAND("1"),;

	@Getter
	private String type;

	CashTypeEnum(String type) {
		this.type = type;
	}
}

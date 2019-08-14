package com.joyveb.jkpmall.enums;

import lombok.Getter;

/**
 * @author Saxon
 * @date 2018/7/31
 */
public enum RedeemStatusEnum {

	/**
	 * 支付失败
	 */
	已返奖(0),
	/**
	 * 支付成功
	 */
	待返奖(1),
	/**
	 * 取消兑奖
	 */
	返奖失败(2),;

	@Getter
	private Integer type;

	RedeemStatusEnum(Integer type) {
		this.type = type;
	}

}

package com.joyveb.jkpmall.enums;

import lombok.Getter;

/**
 * @author Saxon
 * @date 2018/7/30
 */
public enum ParaSysparameEnum {

	/**
	 * 大奖最低金额
	 */
	大奖最低金额("grand.prize.min.money"),
	/**
	 * 反欺诈服务ip
	 */
	ANTI_FRAUD_SERVER_IP("anti.fraud.server.ip");
	@Getter
	private String value;

	ParaSysparameEnum(String value) {
		this.value = value;
	}
}

package com.joyveb.jkpmall.enums;

import lombok.Getter;

/**
 * @author Saxon
 * @date 2018/7/30
 */
public enum AuditStatusEnum {
	/**
	 * 待完善
	 */
	待完善(0),
	/**
	 * 审核中
	 */
	审核中(1),
	/**
	 * 待支付
	 */
	待支付(2),
	/**
	 * 兑奖成功
	 */
	兑奖成功(3),
	/**
	 * 审核失败
	 */
	审核失败(4);
	@Getter
	private Integer status;

	AuditStatusEnum(int status) {
		this.status = status;
	}
}

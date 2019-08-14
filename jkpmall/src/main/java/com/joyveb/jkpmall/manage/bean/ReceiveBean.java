package com.joyveb.jkpmall.manage.bean;

import lombok.Data;

/**
 * @author Saxon
 * @date 2018/7/21
 */
@Data
public class ReceiveBean {

	/**
	 * 微信用户id （微信转账使用）
	 */
	private String openId;
	/**
	 * 中彩渠道返奖通知 ID
	 */
	private String oprID;
	/**
	 * 中彩渠道返奖通知 userId
	 */
	private String userId;
	/**
	 * 中彩渠道返奖通知 securityCode
	 */
	private String securityCode;
	/**
	 * 中奖金额(精确小数点后两位) prizeAmount
	 */
	private String prizeAmount;
	/**
	 * 渠道编号
	 */
	private String channelId;
	/**
	 * 票所属省份
	 */
	private Integer belongtoProvince;
	/**
	 * 随机字符串
	 */
	private String nonceStr;
	/**
	 * 微信订单号
	 */
	private String partnerTradeNo;
	/**
	 * 银行流水号
	 */
	private String bankSerialNumber;
}

package com.joyveb.jkpmall.manage.bean;

import lombok.Data;

/**
 * @author Saxon
 * @date 2018/6/7
 */
@Data
public class TransferRecordBean {

	private String redeemNumber;
	private Integer cashType;
	private String userName;
	private String telePhone;
	/**
	 * 用户唯一标识(微信)
	 */
	private String openId;
	/**
	 * 返奖日期(格式 yyyy-MM-dd)
	 */
	private String redeemDate;
	/**
	 * 兑奖渠道编号
	 */
	private String prizeChannelId;
	/**
	 * 兑奖渠道名称
	 */
	private String prizeChannelName;
	/**
	 * 省码
	 */
	private String provinceNum;
	/**
	 * 兑奖笔数
	 */
	private Integer prizeCount;
	/**
	 * 兑奖金额(单位：分)
	 */
	private Integer prizeAmount;
	/**
	 * 转账状态(0 待返奖 1 已返奖 2 返奖失败 )
	 */
	private Integer redeemStatus;
	/**
	 * 更新时间
	 */
	private Double updateTime;
	/**
	 * 限额
	 */
	private Integer quota;
}

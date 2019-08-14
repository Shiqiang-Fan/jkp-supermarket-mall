package com.joyveb.jkpmall.manage.bean;

import lombok.Data;

/**
 * @author Saxon
 * @date 2018/6/6
 */
@Data
public class CashRecordBean {

	private String id;
	/**
	 * 用户唯一标识（微信唯一标识-openId）
	 */
	private String openId;
	/**
	 * 兑奖渠道编号 null
	 */
	private String prizeChannelId;
	/**
	 * 兑奖渠道名称 null
	 */
	private String prizeChannelName;
	/**
	 * 用户名 null
	 */
	private String userName;
	/**
	 * 电话 null
	 */
	private String telephone;
	/**
	 * 兑奖类型(0 公众号兑奖 1 渠道兑奖) 0
	 */
	private Integer cashType;
	/**
	 * 销售渠道编号 渠道编号：zgyc
	 */
	private String saleChannelId;
	/**
	 * 销售渠道名称 渠道名称：中国烟草
	 */
	private String saleChannelName;
	/**
	 * 游戏代码 条维码中1,5
	 */
	private String gameCode;
	/**
	 * 游戏名称
	 */
	private String gameName;
	/**
	 * 生产批次 条维码中6,10
	 */
	private String productionBatch;
	/**
	 * 本号 条维码中11,17
	 */
	private String bookNum;
	/**
	 * 票号 条维码中18,20
	 */
	private String ticketNum;
	/**
	 * 张号
	 */
	private String paperNum;
	/**
	 * 省编码 请求兑奖接口返回
	 */
	private Integer provinceId;
	/**
	 * 中奖金额(单位：分) 请求兑奖接口返回
	 */
	private Integer prizeAmount;
	/**
	 * 兑奖状态（0中奖 1 已兑奖 2 未中奖 3 大奖 4 兑奖失败） 请求兑奖接口返回
	 */
	private Integer cashStatus;
	/**
	 * 兑奖时间
	 */
	private Long cashTime;
	/**
	 * 返奖状态（0 失败 1 成功） 微信企业付款到微信账户成功与否
	 */
	private Integer redeemStatus;
	/**
	 * 返奖时间 微信企业付款到微信账户的时间，不成功时null
	 */
	private Long redeemTime;

	private Long startTime;
	private Long endTime;

}

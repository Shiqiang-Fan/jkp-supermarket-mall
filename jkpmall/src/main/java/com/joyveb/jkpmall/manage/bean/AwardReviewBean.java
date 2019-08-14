package com.joyveb.jkpmall.manage.bean;

import lombok.Data;

/**
 * @author Saxon
 * @date 2018/6/21
 */
@Data
public class AwardReviewBean {

	/**
	 * 主键
	 */
	private String id;
	/**
	 * 兑奖记录ID
	 */
	private String cashId;
	/**
	 * 用户唯一标识（微信唯一标识）
	 */
	private String openId;
	/**
	 * 票号
	 */
	private String ticketNum;
	/**
	 * 游戏代码
	 */
	private String gameCode;
	/**
	 * 游戏名称
	 */
	private String gameName;
	/**
	 * 中奖金额(单位：分)
	 */
	private Integer prizeAmount;
	/**
	 * 兑奖时间
	 */
	private Long cashTime;
	/**
	 * 审核状态 0-待完善信息 1-待审核 2-审核通过 3-审核失败
	 */
	private Integer auditStatus;
	/**
	 * 审核时间
	 */
	private Long auditTime;
	/**
	 * 审核备注
	 */
	private String auditRemark;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 身份证号
	 */
	private String idCard;
	/**
	 * 银行卡号
	 */
	private String bankCardNumber;
	/**
	 * 开户省份
	 */
	private String bankCardProvince;
	/**
	 * 开户银行
	 */
	private String accountOpeningBank;
	/**
	 * 身份证正面照片
	 */
	private String facadeOfIdCard;
	/**
	 * 身份证反面照片
	 */
	private String oppositeOfIdCard;
	/**
	 * 中奖彩票照片
	 */
	private String winningLotteryTicket;
	/**
	 * 中彩渠道返奖通知 ID
	 */
	private String oprId;
}

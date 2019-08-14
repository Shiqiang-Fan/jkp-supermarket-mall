package com.joyveb.redeem.bean;

import lombok.Data;

import java.util.Date;

/**
 * 微信提交订单接口参数
 * @author yqb
 */

@Data
public class BizOrder {
    private String openId;
    private String amount;
    //多个用逗号隔开
    private String gameCodes;
    // 订单数量多个用逗号隔开 与gameCodes 一一对应
    private String saleNums;
}
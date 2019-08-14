package com.joyveb.jkpmall.manage.bean;

import lombok.Data;

/**
 * 订单信息表
 * t_core_record
 */
@Data
public class OrderEntity {

    /**
     * 用户唯一标识
     */
    private String openId;
    /**
     * 订单编号
     */
    private String uuid;
    /**
     * 游戏编号
     */
    private String gameCode;

    /**
     * 购买数量
     */
    private String saleNum;
    /**
     * 联系电话
     */
    private String telephone;
    /**
     * 收货人
     */
    private String userName;
    /**
     * 收货地址
     */
    private String address;
    /**
     * 物流公司
     */
    private String logistics;
    /**
     * 快递编号
     */
    private String logisticsNum;
    /**
     * 订单总金额
     */
    private String amount;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 下单日期
     */
    private String dateTime;

}

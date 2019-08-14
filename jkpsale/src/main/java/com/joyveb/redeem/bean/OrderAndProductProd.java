package com.joyveb.redeem.bean;

import lombok.Data;

@Data
public class OrderAndProductProd {
    /**
     * 商品规格
     */
    private String detail;
    /**
     * 商品参数
     */
    private String parameter;
    /**
     * 附加信息
     */
    private String service;
    /**
     * 游戏编码
     */
    private String gameCode;
    /**
     * 游戏名称
     */
    private String gameName;
    /**
     * 规格（每包张数）
     */
    private Integer paperNum;
    /**
     * 单价（分）
     */
    private Integer prizeAmount;
    /**
     * 图片地址
     */
    private String picturePath;
    /**
     * 库存数量
     */
    private String storeNum;
    /**
     * 购买数量
     */
    private String saleNum;

    /**
     * 面值
     */
    private String faceValue;

}

package com.joyveb.redeem.bean;

import lombok.Data;

/**
 * 商品信息
 */
@Data
public class Lottery {
    /**
     * 商品参数
     */
    private ProductPara productPara;
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
     * 面值
     */
    private String faceValue;
    /**
     * 有参构造函数
     * @param productPara
     * @param gameCode
     * @param gameName
     * @param paperNum
     * @param prizeAmount
     * @param picturePath
     * @param storeNum
     */
    public Lottery(ProductPara productPara, String gameCode, String gameName,
                   Integer paperNum, Integer prizeAmount, String picturePath, String storeNum,String faceValue) {
        this.productPara = productPara;
        this.gameCode = gameCode;
        this.gameName = gameName;
        this.prizeAmount = prizeAmount;
        this.picturePath = picturePath;
        this.storeNum = storeNum;
        this.faceValue = faceValue;
    }
}

package com.joyveb.redeem.bean;

import lombok.Data;
@Data
public class ProductPara {
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
     * 有参构造函数
     * @param detail
     * @param parameter
     * @param service
     */
    public ProductPara(String detail,String parameter,String service){
        this.detail=detail;
        this.parameter=parameter;
        this.service=service;
    }

}

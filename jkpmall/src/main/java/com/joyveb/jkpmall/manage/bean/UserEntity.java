package com.joyveb.jkpmall.manage.bean;

import lombok.Data;

/**
 * t_user_info t_cash_record 表中需要的值
 */
@Data
public class UserEntity {

    private String openId;
    private Integer prizeAmount;
    private Integer count;
    private String realName;
    private String telephone;

}

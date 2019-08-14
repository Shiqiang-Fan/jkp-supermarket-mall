package com.joyveb.jkpmall.manage.bean;

import lombok.Data;

/**
 * @author system
 * 用户收货地址
 * t_user_address
 */
@Data
public class AddressEntity {
    private String openId;
    private String userName;
    private String telephone;
    private String address;
}
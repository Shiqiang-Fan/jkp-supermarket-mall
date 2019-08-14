package com.joyveb.redeem.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersEntity {
    private String openId;

    private String amount;

    private List<Order> orders;
}
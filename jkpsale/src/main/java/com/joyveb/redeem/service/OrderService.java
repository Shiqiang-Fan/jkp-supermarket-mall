package com.joyveb.redeem.service;

import com.joyveb.redeem.bean.AddressEntity;
import com.joyveb.redeem.bean.OrderEntity;
import com.joyveb.redeem.dao.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 保存订单信息
     * t_core_record
     */
    public void saveOrderInfo(OrderEntity orderEntity) {
        orderMapper.saveOrderInfo(orderEntity);
    }

    /**
     * 查询订单信息
     * t_core_record
     * @return
     */
    public List<OrderEntity> selectOrder() {
        List<OrderEntity> orderEntities = orderMapper.selectOrder();
        return orderEntities;
    }

    /**
     * 修改订单状态
     * @param out_trade_no
     */
    public void updateOrderStatus(String out_trade_no) {
        orderMapper.updateOrderStatus(out_trade_no);
    }

    public OrderEntity selectOrderByUuid(String orderId) {
        return orderMapper.selectOrderByUuid(orderId);
    }
}

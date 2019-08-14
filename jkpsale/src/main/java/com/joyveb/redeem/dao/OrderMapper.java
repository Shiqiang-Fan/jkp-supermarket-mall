package com.joyveb.redeem.dao;

import com.joyveb.redeem.bean.OrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface OrderMapper {

    /**
     * 保存订单信息
     * t_core_record
     */
    void saveOrderInfo(OrderEntity orderEntity);

    /**
     *  查询订单信息
     *  t_core_record
     * @return
     */
    List<OrderEntity> selectOrder();

    /**
     * 修改订单状态
     * @param out_trade_no
     */
    void updateOrderStatus(String out_trade_no);

    OrderEntity selectOrderByUuid(@Param("uuid") String orderId);
}

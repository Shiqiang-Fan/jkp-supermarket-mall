package com.joyveb.redeem.dao;

import com.joyveb.redeem.bean.OrderAndProductProd;
import com.joyveb.redeem.bean.OrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LotteryMapper {

    /**
     * 查询产品信息
     * t_core_product
     */
    OrderAndProductProd selectProductByGameCode(String gameCode);

    List<OrderAndProductProd> selectProduct();

    /**
     * |
     * 修改库存
     * t_core_product
     *
     * @param "GAME_CODE" 游戏编号
     * @return
     */
    Integer updateLottery(@Param("gameCode") String gameCode, @Param("saleNum") Integer saleNum);

    /**
     * 查询订单信息
     *
     * @param openId
     * @return
     */
    List<OrderEntity> selectByOpenId(@Param("openId") String openId);

    OrderEntity selectCoreRecordByUUID(@Param("uuid") String uuid);
}

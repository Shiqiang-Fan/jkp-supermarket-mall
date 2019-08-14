package com.joyveb.redeem.service;

import com.joyveb.redeem.bean.OrderAndProductProd;
import com.joyveb.redeem.bean.OrderEntity;
import com.joyveb.redeem.dao.LotteryMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LotteryService {
    @Autowired
    private LotteryMapper lotteryMapper;

    /**
     * 查询产品信息
     * t_core_product
     */
    public OrderAndProductProd selectProductByGameCode(String gameCode) {
        return lotteryMapper.selectProductByGameCode(gameCode);
    }

    /**
     * 查询产品信息
     * t_core_product
     */
    public List<OrderAndProductProd> selectProduct() {
        return lotteryMapper.selectProduct();
    }

    /**
     * |
     * 修改库存
     * t_core_product
     *
     * @param "GAME_CODE" 游戏编号
     * @return
     */
    public Integer updateLottery(@Param("gameCode") String gameCode, @Param("saleNum") Integer saleNum) {
        return lotteryMapper.updateLottery(gameCode, saleNum);
    }

    public List<OrderEntity> selectByOpenId(String openId) {
        return lotteryMapper.selectByOpenId(openId);
    }

    public OrderEntity selectCoreRecordByUUID(String uuid) {
        return lotteryMapper.selectCoreRecordByUUID(uuid);
    }
}

package com.joyveb.jkpmall.manage.service;

import com.joyveb.jkpmall.manage.bean.CoreProduct;
import com.joyveb.jkpmall.manage.mapper.LotteryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LotteryService {

    @Autowired
    private LotteryMapper lotteryMapper;


    public void updateProduct(CoreProduct coreProduct) {
        lotteryMapper.updateProduct(coreProduct);
    }

    public void insertProduct(CoreProduct coreProduct) {
        lotteryMapper.insertProduct(coreProduct);
    }

    public void deleteProduct(String gameCode) {
        lotteryMapper.deleteProduct(gameCode);
    }

    public CoreProduct getProduct(String gameCode) {
        return lotteryMapper.getProduct(gameCode);
    }

    public List<CoreProduct> getProducts(Map<String, Object> params) {
        return lotteryMapper.getProducts(params);
    }

    public int countCoreProduct(Map<String, Object> params) {
        return lotteryMapper.countCoreProduct();
    }
}

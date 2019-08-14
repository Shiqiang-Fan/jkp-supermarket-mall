package com.joyveb.redeem.controller;

import com.joyveb.redeem.bean.Lottery;
import com.joyveb.redeem.bean.OrderAndProductProd;
import com.joyveb.redeem.bean.OrderEntity;
import com.joyveb.redeem.bean.ProductPara;
import com.joyveb.redeem.common.ReturnInfo;
import com.joyveb.redeem.service.LotteryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * 商品控制器
 * @author system
 */
@Slf4j
@RestController
public class ProductController {
    @Autowired
    private LotteryService lotteryService;

    /**
     * 首页商品展示
     * @return
     */
    @RequestMapping(value = "/productDisplay")
    @ResponseBody
    public ReturnInfo selectLottery() {
        log.info("请求首页商品展示");
        try {
            List<OrderAndProductProd> orderAndProductProd = lotteryService.selectProduct();
            List<Lottery> lottery = new ArrayList<>();
            for (OrderAndProductProd orderAndProductProd1 : orderAndProductProd) {
                lottery.add(new Lottery(new ProductPara(orderAndProductProd1.getDetail(), orderAndProductProd1.getParameter(), orderAndProductProd1.getService()),
                        orderAndProductProd1.getGameCode(),
                        orderAndProductProd1.getGameName(),
                        orderAndProductProd1.getPaperNum(),
                        orderAndProductProd1.getPrizeAmount(),
                        orderAndProductProd1.getPicturePath(),
                        orderAndProductProd1.getStoreNum(),
                        orderAndProductProd1.getFaceValue()
                ));
            }
            log.info("响应商品请求: " + lottery);
            return new ReturnInfo("查询成功", "", lottery, true);
        } catch (Exception e) {
            log.error("查询失败:" + e);
            return new ReturnInfo("查询失败", "", e, false);
        }
    }
}

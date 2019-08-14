package com.joyveb.redeem.controller;

import com.joyveb.redeem.bean.*;
import com.joyveb.redeem.common.ReturnInfo;
import com.joyveb.redeem.service.AddressService;
import com.joyveb.redeem.service.LotteryService;
import com.joyveb.redeem.service.OrderService;
import com.joyveb.redeem.utils.DingUtil;
import com.joyveb.redeem.utils.WxPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 订单控制器
 *
 * @author system
 */
@Slf4j
@RestController
public class OrderController {

    @Autowired
    private LotteryService lotteryService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/selectOrderRecord", method = RequestMethod.GET)
    @ResponseBody
    public ReturnInfo selectRecordByOpenId(@RequestParam String openId) {

        log.info("查询{}订单", openId);
        if (StringUtils.isBlank(openId)) {
            return new ReturnInfo("订单查询失败，请求信息openId为空", "0", "", false);
        }
        List<OrderEntity> coreRecords = lotteryService.selectByOpenId(openId);
        for (OrderEntity coreRecord : coreRecords) {
            //coreRecord.setPicturePath("http://192.168.23.41:9999/image/img3.png");
        }
        log.info("订单信息: {}", coreRecords);
        return new ReturnInfo("查询成功", "", coreRecords, true);
    }

    @RequestMapping(value = "/selectOrderDetail", method = RequestMethod.GET)
    @ResponseBody
    public ReturnInfo selectCashRecordByUUID(@RequestParam String uuid) {
        log.info("查询订单号{}详情", uuid);
        if (StringUtils.isBlank(uuid)) {
            return new ReturnInfo("请求参数uuid为空", "0", "", false);
        }
        try {
            List<OrderAndProductProd> orderAndProductProds = new ArrayList<>();
            OrderEntity orderEntity = lotteryService.selectCoreRecordByUUID(uuid);
            if (null != orderEntity) {
                String[] gamecodes = orderEntity.getGameCode().split(",");
                String[] salenums = orderEntity.getSaleNum().split(",");
                for (int i = 0; i < gamecodes.length; i++) {
                    OrderAndProductProd orderAndProductProds1 = lotteryService.selectProductByGameCode(gamecodes[i]);
                    orderAndProductProds.add(orderAndProductProds1);
                }
                for (int i = 0; i < salenums.length; i++) {
                    orderAndProductProds.get(i).setSaleNum(salenums[i]);
                }
            }
            log.warn("查询订单结果:{}", orderAndProductProds);
            return new ReturnInfo("查询成功", "", orderAndProductProds, true);
        } catch (Exception e) {
            log.warn("游戏信息查询失败...");
            return new ReturnInfo("查询失败", "0", "", true);
        }
    }

    @PostMapping("/commitOrder")
    @Transactional
    public ReturnInfo wxPay(@RequestBody OrderEntity orderEntity) {
        log.info("下单请求:" + orderEntity);

        if (orderEntity == null) {
            log.info("参数为空" + orderEntity);
            return new ReturnInfo("参数为空", "", orderEntity, true);
        }

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateformat = format.format(date);
        String dateformat1 = format1.format(date);

        Random rdm = new Random();
        int number = rdm.nextInt(1000) % (1000 - 100 + 1) + 100;
        String uuid = "J" + dateformat1 + number;

        try {

            AddressEntity entity = new AddressEntity();
            entity.setOpenId(orderEntity.getOpenId());
            entity.setUserName(orderEntity.getUserName());
            entity.setTelephone(orderEntity.getTelephone());
            entity.setAddress(orderEntity.getAddress());

            orderEntity.setUuid(uuid);
            orderEntity.setDateTime(dateformat);
            orderEntity.setStatus("0");

            boolean addressSuccess = saveAddress(entity);
            if (!addressSuccess) {
                return new ReturnInfo("地址保存失败");
            }
            boolean orderSuccess = saveOrder(orderEntity);
            if (!orderSuccess) {
                return new ReturnInfo("订单保存失败");
            }

            log.info("订单入库成功~~调用微信统一下单");
            Map<String, Object> result = weChatPay(orderEntity,orderEntity.getAppId());

            log.debug("下单结果:" + result);
            return new ReturnInfo("下单成功", "", result, true);
        } catch (Exception e) {
            log.warn("下单异常", e);
            return new ReturnInfo("下单异常", "", "", false);
        }
    }

    /**
     * 单页面下单请求
     */
    @PostMapping("/singlepageCommit")
    @Transactional
    public ReturnInfo commitOrder(@RequestBody OrderEntity orderEntity) {

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateformat = format.format(date);
        String dateformat1 = format1.format(date);

        Random rdm = new Random();
        int number = rdm.nextInt(1000) % (1000 - 100 + 1) + 100;
        String uuid = "J" + dateformat1 + number;

        AddressEntity entity = new AddressEntity();
        entity.setOpenId(uuid);
        entity.setUserName(orderEntity.getUserName());
        entity.setTelephone(orderEntity.getTelephone());
        entity.setAddress(orderEntity.getAddress());

        orderEntity.setUuid(uuid);
        orderEntity.setDateTime(dateformat);
        orderEntity.setOpenId(uuid);
        orderEntity.setStatus("1");

        boolean addressSuccess = saveAddress(entity);
        if (!addressSuccess) {
            return new ReturnInfo("地址保存失败");
        }
        boolean orderSuccess = saveOrder(orderEntity);
        if (!orderSuccess) {
            return new ReturnInfo("订单保存失败");
        }

        DingUtil.noticeDingding(orderEntity);

        return new ReturnInfo("下单成功!");
    }

    /**
     * 微信支付统一下单接口调用
     * 统一下单，调用工具类请求下单方法，返回xml，用return_code判断统一下单结果,获取prepay_id等预支付成功信息
     */
    private Map<String, Object> weChatPay(OrderEntity orderEntity, String appId) throws IOException {
        Map<String, Object> map = new HashMap<>();
        String ip = WxPayUtil.getIpAddr();
        String prePayInfoXml = WxPayUtil.unifiedOrder("PRO",
                orderEntity.getUuid(),
                orderEntity.getAmount(),
                ip, orderEntity.getOpenId(),appId);
        //生成包含prepay_id的map，map传入前端
        log.info(prePayInfoXml);
        map = WxPayUtil.getPayMap(prePayInfoXml,appId);
        //将订单号放入map，用以支付后处理
        map.put("orderNo", orderEntity.getUuid());
        return map;
    }

    private boolean saveOrder(OrderEntity orderEntity) {

        try {
            orderService.saveOrderInfo(orderEntity);
            log.info("订单保存成功成功.");
            return true;
        } catch (Exception e) {
            log.warn("订单提交异常，order[{}]", orderEntity, e);
            return false;
        }
    }

    private boolean saveAddress(AddressEntity entity) {

        try {
            AddressEntity address = addressService.selectAddress(entity.getOpenId());
            if (address != null && !address.equals("")){
                addressService.updateAddress(entity);
                log.info("修改收货地址成功.");
            }else {
                addressService.saveAddress(entity);
                log.info("保存收货地址成功.");
            }
            return true;
        } catch (Exception e) {
            log.warn("地址保存失败，地址信息[{}]", entity, e);
            return false;
        }
    }
}
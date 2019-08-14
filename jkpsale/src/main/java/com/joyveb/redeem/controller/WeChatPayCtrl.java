package com.joyveb.redeem.controller;

import com.google.gson.JsonObject;
import com.joyveb.redeem.bean.OrderEntity;
import com.joyveb.redeem.service.OrderService;
import com.joyveb.redeem.utils.DingUtil;
import com.joyveb.redeem.utils.JsonParserUtils;
import com.joyveb.redeem.utils.WxPayUtil;
import com.joyveb.redeem.wx.WXJsapiticket;
import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Slf4j
@RestController
@CrossOrigin
public class WeChatPayCtrl {

    private static final String URL = "https://oapi.dingtalk.com/robot/send?access_token=ca8c2631884fea676d63377e5c12c838cd18f40ddde9206a9d605a20e1763f86";

    @Autowired
    private OrderService orderService;

    @Autowired
    private YunpianClient yunpianClient;

    /**
     * 微信验签
     *
     * @param url
     * @return
     */
    @RequestMapping(value = "/jsapisign", method = RequestMethod.GET)
    @ResponseBody
    public String jsApiSign(String url) {
        //添加微信js签名信息
        System.out.println("url------------" + url);
        Map<String, String> signMap = WXJsapiticket.jsApiSign(url);
        log.info("验签成功，"+ signMap);
        return JsonParserUtils.toJson(signMap);
    }

    /**
     * 根据code获取openId
     *
     * @param request
     * @return
     */
    @GetMapping("/get_openid")
    public void getOpenId(HttpServletRequest request, HttpServletResponse response) {

        JsonObject result = null;
        String code = request.getParameter("code");
        if (code != null || code.equals("")) {
            try {
                result = WxPayUtil.getOpenIdByCode(code);
                String openid = result.get("openid").toString();
                openid = openid.substring(1,openid.length()-1);

                if (openid == null || openid.equals("")){
                    log.error("未获取到openid");
                }
                log.info("openid=" + openid);
                String sendUrl ="https://payment.joyveb.com/shopping/index.html?openid=" + openid;
                response.sendRedirect(sendUrl);
            } catch (Exception e) {
                log.error("获取openId失败！" + e.getMessage());
            }
        }else {
            log.error("获取code失败");
            try {
                response.sendRedirect("未获取到code");
            }catch (IOException e){
                log.error("获取code失败！" + e.getMessage());
            }
        }
    }

    /**
     * 修改订单状态，获取微信回调结果
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/weixin_parent_notify.do", produces = "text/html;charset=utf-8")
    public String wxpayResult(HttpServletRequest request) {

        Map<String, String> return_data = new HashMap<>();
        String inputLine;
        String notifyXml = "";
        String resXml = "";
        try {
            while ((inputLine = request.getReader().readLine()) != null) {
                notifyXml += inputLine;
            }
            request.getReader().close();
        } catch (Exception e) {
            log.info("xml获取失败：" + e);
            e.printStackTrace();
        }
        log.info("接收到的xml：" + notifyXml);
        if (notifyXml.isEmpty()) {
            log.debug("xml为空：");
        }

        String appid = WxPayUtil.getXmlPara(notifyXml, "appid");
        ;
        String bank_type = WxPayUtil.getXmlPara(notifyXml, "bank_type");
        String cash_fee = WxPayUtil.getXmlPara(notifyXml, "cash_fee");
        String fee_type = WxPayUtil.getXmlPara(notifyXml, "fee_type");
        String is_subscribe = WxPayUtil.getXmlPara(notifyXml, "is_subscribe");
        String mch_id = WxPayUtil.getXmlPara(notifyXml, "mch_id");
        String nonce_str = WxPayUtil.getXmlPara(notifyXml, "nonce_str");
        String openid = WxPayUtil.getXmlPara(notifyXml, "openid");
        String out_trade_no = WxPayUtil.getXmlPara(notifyXml, "out_trade_no");
        String result_code = WxPayUtil.getXmlPara(notifyXml, "result_code");
        String return_code = WxPayUtil.getXmlPara(notifyXml, "return_code");
        String sign = WxPayUtil.getXmlPara(notifyXml, "sign");
        String time_end = WxPayUtil.getXmlPara(notifyXml, "time_end");
        String total_fee = WxPayUtil.getXmlPara(notifyXml, "total_fee");
        String trade_type = WxPayUtil.getXmlPara(notifyXml, "trade_type");
        String transaction_id = WxPayUtil.getXmlPara(notifyXml, "transaction_id");

        //根据返回xml计算本地签名
        String localSign =
                "appid=" + appid +
                        "&bank_type=" + bank_type +
                        "&cash_fee=" + cash_fee +
                        "&fee_type=" + fee_type +
                        "&is_subscribe=" + is_subscribe +
                        "&mch_id=" + mch_id +
                        "&nonce_str=" + nonce_str +
                        "&openid=" + openid +
                        "&out_trade_no=" + out_trade_no +
                        "&result_code=" + result_code +
                        "&return_code=" + return_code +
                        "&time_end=" + time_end +
                        "&total_fee=" + total_fee +
                        "&trade_type=" + trade_type +
                        "&transaction_id=" + transaction_id +
                        "&key=" + WxPayUtil.getKEY();//注意这里的参数要根据ASCII码 排序
        localSign = DigestUtils.md5Hex(localSign).toUpperCase();//将数据MD5加密


        log.debug("本地签名是：" + localSign);
        log.debug("微信支付签名是：" + sign);


        //本地计算签名与微信返回签名不同||返回结果为不成功
        if (localSign.equals(sign) || "SUCCESS".equals(result_code) || "SUCCESS".equals(return_code)) {

            log.info("交易成功");
            OrderEntity orderEntity = orderService.selectOrderByUuid(out_trade_no);
            if (orderEntity == null) {
                log.info("订单不存在");
                return_data.put("return_code", "FAIL");
                return_data.put("return_msg", "订单不存在");
                return GetMapToXML(return_data);
            }

            if (!total_fee.equals(orderEntity.getAmount())) {
                return_data.put("return_code", "FAIL");
                return_data.put("return_msg", "金额异常");
                log.warn("金额异常,total_fee：" + total_fee + ",amount:" + orderEntity.getAmount());
                return GetMapToXML(return_data);
            }

            //更新订单
            log.info("更新订单");
            orderService.updateOrderStatus(out_trade_no);

            //发送短信
            sendMessage(out_trade_no,orderEntity.getTelephone());

            //异步通知钉钉
            DingUtil.noticeDingding(orderEntity);

            return_data.put("return_code", "SUCCESS");
            return_data.put("return_msg", "OK");

            return GetMapToXML(return_data);

        }else {
            return_data.put("return_code", "FAIL");
            return_data.put("return_msg", "签名错误");
            return GetMapToXML(return_data);
        }
    }

    private boolean sendMessage(String orderId, String telephone) {

        //发送短信API
        log.warn("正在发送短信! 请稍等...");
        Map<String, String> param = yunpianClient.newParam(2);
        param.put(YunpianClient.MOBILE, telephone);
        param.put(YunpianClient.TEXT, "【环球鑫彩】您已下单成功，订单号为：" + orderId +
                "，若有疑问，请拨打010-5322-6934！祝您购票愉快！");
        Result<SmsSingleSend> smsSingleSendResult = yunpianClient.sms().single_send(param);
        if (smsSingleSendResult.isSucc()) {
            log.warn("发送成功! 发送结果:" + smsSingleSendResult);
            return true;
        }
        log.warn("发送失败! 发送结果:" + smsSingleSendResult);
        return false;
    }

    /**
     * Map转xml数据
     */
    public static String GetMapToXML(Map<String, String> param) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        for (Map.Entry<String, String> entry : param.entrySet()) {
            sb.append("<" + entry.getKey() + ">");
            sb.append(entry.getValue());
            sb.append("</" + entry.getKey() + ">");
        }
        sb.append("</xml>");
        return sb.toString();
    }
}
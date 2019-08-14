package com.joyveb.redeem.utils;

import com.joyveb.redeem.bean.OrderEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class DingUtil {

    private static final String URL = "https://oapi.dingtalk.com/robot/send?access_token=ca8c2631884fea676d63377e5c12c838cd18f40ddde9206a9d605a20e1763f86";

    public static void noticeDingding(OrderEntity order){
        log.info("异步钉钉通知商户处理订单......");
        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(URL);
        post.addHeader("Content-Type", "application/json; charset=utf-8");
        String textMsg = "{\"msgtype\": \"text\",\"text\": {\"content\": \"小依提醒您，您有新的订单,订单号：" +
                order.getUuid()+",下单客户:"+order.getUserName()+",下单时间："+
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+",邮寄地址："+
                order.getAddress()+",订单金额："+Double.parseDouble(order.getAmount()) /100 +"元,手机号："+order.getTelephone()+"，请及时处理(^_^)\"}}";

        log.info("异步通知钉钉消息：" + textMsg);
        StringEntity se = new StringEntity(textMsg,"utf-8");
        post.setEntity(se);

        try {
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String result= EntityUtils.toString(response.getEntity(), "utf-8");
                log.info("异步钉钉通知商户成功!!" + result);
            }
        } catch (IOException e) {
            log.info("异步钉钉发送消息失败！" + e);
            e.printStackTrace();
        }
    }
}

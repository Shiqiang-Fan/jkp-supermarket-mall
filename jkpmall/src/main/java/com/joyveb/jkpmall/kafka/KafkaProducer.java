package com.joyveb.jkpmall.kafka;

import com.unlto.twls.commonutil.msgquque.SimpleProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author: chenjia
 * @date: 2018/6/28 21:50
 * @decription:
 * @modified By:
 */
@Slf4j
@Component
public class KafkaProducer {

    @Resource
    private SimpleProducer simpleProducer;

    //发送消息方法
    public void send(String topic, String cashMessage) {
        try {
            simpleProducer.send(topic, cashMessage);
            log.debug("完成记录入kafka");
        } catch (Exception e) {
            log.error("记录入kafka异常", e);
        }
    }
}

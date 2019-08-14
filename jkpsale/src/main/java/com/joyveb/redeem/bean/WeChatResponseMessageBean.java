package com.joyveb.redeem.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * 微信事件推送响应对象
 *
 * @author wenchao
 * @date 16:40 2018/6/8
 */
@Data
public class WeChatResponseMessageBean {

    /**
     * 接收方帐号（收到的OpenID）
     */
    @XStreamAlias("ToUserName")
    private String toUserName;
    /**
     * 开发者微信号
     */
    @XStreamAlias("FromUserName")
    private String fromUserName;
    /**
     * 消息创建时间 （整型）
     */
    @XStreamAlias("CreateTime")
    private Long createTime;
    /**
     * 消息类型（text/music/news）
     */
    @XStreamAlias("MsgType")
    private String msgType;
    /**
     * 回复的消息内容
     */
    @XStreamAlias("Content")
    private String content;

    public WeChatResponseMessageBean() {
        this.createTime = System.currentTimeMillis();
        this.msgType = "text";
    }
}

package com.joyveb.jkpmall.common;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * @author yangqiju
 * @date 2018-03-12 上午11:11
 */
public enum SmsStatusEnum {

    /**
     * 发送成功
     */
    SUCCESS(1),
    /**
     * 发送失败
     */
    FAILD(2),
    /**
     * 未知状态
     */
    UNKNOW(-1);

    private static Map<Integer, SmsStatusEnum> infoMap = new HashMap<>();

    static {
        for (SmsStatusEnum info : SmsStatusEnum.values()) {
            infoMap.put(info.getCode(), info);
        }
    }

    @Getter
    private int code;

    SmsStatusEnum(int code) {
        this.code = code;
    }

    public static int type2Code(Integer type) {
        if (type == null) {
            return UNKNOW.code;
        }
        SmsStatusEnum smsStatusEnum = infoMap.get(type);
        if (smsStatusEnum == null) {
            return UNKNOW.code;
        }
        return smsStatusEnum.code;
    }
}

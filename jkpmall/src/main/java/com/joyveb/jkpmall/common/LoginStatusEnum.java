package com.joyveb.jkpmall.common;

import lombok.Getter;

/**
 * @author yangqiju
 * @date 2018-03-12 下午6:49
 */
public enum LoginStatusEnum {
    /**
     * 前台
     */
    FAILD(-1),
    /**
     * 管理端
     */
    SUCCESS(2);

    @Getter
    private int code;

    LoginStatusEnum(int code) {
        this.code = code;
    }
}

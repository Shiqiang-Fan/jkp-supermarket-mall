package com.joyveb.jkpmall.common;

import lombok.Getter;

/**
 * @author yangqiju
 * @date 2018-03-12 下午6:49
 */
public enum PlatformEnum {
    /**
     * 前台
     */
    FRONT(1),
    /**
     * 管理端
     */
    MANAGE(2);

    @Getter
    private int code;

    PlatformEnum(int code) {
        this.code = code;
    }
}

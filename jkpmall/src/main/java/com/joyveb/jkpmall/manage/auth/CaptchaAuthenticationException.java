package com.joyveb.jkpmall.manage.auth;

import org.springframework.security.core.AuthenticationException;

/**
 * @author yangqiju
 * @date 2018-03-13 下午3:10
 */
public class CaptchaAuthenticationException extends AuthenticationException {

    public CaptchaAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public CaptchaAuthenticationException(String msg) {
        super(msg);
    }

    public CaptchaAuthenticationException(String msg, Object extraInformation) {
        super(msg, extraInformation);
    }
}

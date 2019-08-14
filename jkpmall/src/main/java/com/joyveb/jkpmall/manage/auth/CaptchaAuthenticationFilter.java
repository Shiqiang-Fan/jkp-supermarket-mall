package com.joyveb.jkpmall.manage.auth;

import com.joyveb.jkpmall.auth.ctrl.CaptchaCtrl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 登录的校验
 *
 * @author yangqiju
 * @date 2018-03-07 下午4:04
 */
public class CaptchaAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String SPRING_SECURITY_FORM_CAPTCHA_KEY = "j_captcha";
    private static final String SESSION_GENERATED_CAPTCHA_KEY = "m_captcha";


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {

        CaptchaCtrl.CodeCheckSessionBean genCode = this.obtainGeneratedCaptcha(request);
        String inputCode = this.obtainCaptcha(request);
        if (genCode == null) {
            throw new CaptchaAuthenticationException("LoginAuthentication.captchaInvalid");
        }

        if (!StringUtils.equalsIgnoreCase(genCode.getCode(), inputCode)) {
            throw new CaptchaAuthenticationException("LoginAuthentication.captchaNotEquals");
        }

        return super.attemptAuthentication(request, response);
    }

    protected String obtainCaptcha(HttpServletRequest request) {
        ;
        return request.getParameter(SPRING_SECURITY_FORM_CAPTCHA_KEY);
    }

    protected CaptchaCtrl.CodeCheckSessionBean obtainGeneratedCaptcha(HttpServletRequest request) {
        return (CaptchaCtrl.CodeCheckSessionBean) request.getSession()
            .getAttribute(SESSION_GENERATED_CAPTCHA_KEY);
    }
}

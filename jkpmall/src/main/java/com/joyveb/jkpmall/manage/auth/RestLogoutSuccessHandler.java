package com.joyveb.jkpmall.manage.auth;

import com.joyveb.jkpmall.logger.OperateLogBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.marker.Markers;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * @author yangqiju
 * @date 2018-03-02 下午5:34
 */
@Slf4j
@Component("restLogoutSuccessHandler")
public class RestLogoutSuccessHandler implements LogoutSuccessHandler {

    private static final String LOGOUT_LOGGER_ACTION = "登出";

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        OperateLogBean loggerBean = new OperateLogBean();
        loggerBean.setUsername(authentication.getName());
        loggerBean.setClientIp(request.getRemoteAddr());
        loggerBean.setOperateTime(System.currentTimeMillis());
        loggerBean.setAction(LOGOUT_LOGGER_ACTION);
        log.info(Markers.appendFields(loggerBean), "登出成功");

    }
}

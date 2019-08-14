package com.joyveb.jkpmall.manage.auth;

import com.joyveb.lbos.restful.common.ReturnInfo;
import com.joyveb.lka.kafka.utils.JsonUtils;
import com.joyveb.jkpmall.logger.OperateLogBean;
import java.io.IOException;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.marker.Markers;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * @author yangqiju
 * @date 2018-03-01 下午9:17
 */
@Slf4j
@Component("restAuthenticationSuccessHandler")
public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final String LOGIN_LOGGER_ACTION = "登录";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        clearAuthenticationAttributes(request);

        ReturnInfo returnInfo = new ReturnInfo("登录成功", true);
        String username = authentication.getName();

        try {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            String resultJson = JsonUtils.toJsonString(returnInfo);
            request.getSession().setAttribute("username", username);
            this.writeJson(response, resultJson);

        } finally {
            OperateLogBean loggerBean = new OperateLogBean();
            loggerBean.setUsername(username);
            loggerBean.setClientIp(request.getRemoteAddr());
            loggerBean.setOperateTime(System.currentTimeMillis());
            loggerBean.setAction(LOGIN_LOGGER_ACTION);
            loggerBean.setResult(returnInfo);
            loggerBean.setClassName(this.getClass().getName());
            log.info(Markers.appendFields(loggerBean), "登录成功");
        }
    }

    private void writeJson(HttpServletResponse response, String msg) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        log.info("msg:{}", msg);
//        try (PrintWriter out = response.getWriter()) {
//            out.print(msg);
//        }
    }

}
package com.joyveb.jkpmall.manage.auth;

import com.joyveb.jkpmall.service.LogLoginService;
import com.joyveb.lbos.restful.common.ReturnInfo;
import com.joyveb.lka.kafka.utils.JsonUtils;
import com.joyveb.ncps.log.entity.LogLoginInfo;
import com.joyveb.jkpmall.common.LoginStatusEnum;
import com.joyveb.jkpmall.common.PlatformEnum;
import com.joyveb.jkpmall.cache.ParaSysparameCache;
import com.joyveb.jkpmall.logger.OperateLogBean;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.marker.Markers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 * @author yangqiju
 * @date 2018-03-01 下午9:17
 */
@Slf4j
@Component("restAuthenticationFailureHandler")
public class RestAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private static final String SPRING_SECURITY_FORM_USERNAME_KEY = "j_username";


	@Autowired
	private LogLoginService logLoginService;
	@Autowired
	private ParaSysparameCache paraSysparameCache;

	protected String obtainUsername(HttpServletRequest request) {
		return request.getParameter(SPRING_SECURITY_FORM_USERNAME_KEY);
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		String username = this.obtainUsername(request);
		ReturnInfo returnInfo = null;
		try {
			returnInfo = new ReturnInfo("用户名或密码错误", false);
			String faildJson;

			if (exception instanceof CaptchaAuthenticationException) {
				returnInfo.setDescription("验证码错误");
				faildJson = JsonUtils.toJsonString(returnInfo);
				this.writeJson(response, faildJson);
				return;
			}

			int count = logLoginService
				.selectTodayLoginFaild(username, PlatformEnum.MANAGE.getCode());
			if (count >= paraSysparameCache.getLoginFaildMaxTimes()) {

				returnInfo.setDescription("账户号码被冻结,请联系运维人员");
				faildJson = JsonUtils.toJsonString(returnInfo);
				this.writeJson(response, faildJson);
			} else {
				this.insertLoginLog(username);
				returnInfo.setDescription("账户或密码错误");
				faildJson = JsonUtils.toJsonString(returnInfo);
				this.writeJson(response, faildJson);
			}
		} finally {
			OperateLogBean loggerBean = new OperateLogBean();
			loggerBean.setUsername(username);
			loggerBean.setClientIp(request.getRemoteAddr());
			loggerBean.setOperateTime(System.currentTimeMillis());
			loggerBean.setAction("登录");
			loggerBean.setResult(returnInfo);
			loggerBean.setClassName(this.getClass().getName());
			log.info(Markers.appendFields(loggerBean), "登录失败");
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

	private void insertLoginLog(String username) {
		LogLoginInfo info = new LogLoginInfo();
		info.setCreateDate(System.currentTimeMillis());
		info.setPlatform(PlatformEnum.MANAGE.getCode());
		info.setStatus(LoginStatusEnum.FAILD.getCode());
		info.setUserName(username);
		try {
			logLoginService.insert(info);
		} catch (Exception e) {
			log.warn("插入日志异常", e);
		}
	}
}
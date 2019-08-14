package com.joyveb.jkpmall.auth.ctrl;

import com.joyveb.jkpmall.common.RandomImgManager;
import com.joyveb.jkpmall.common.RandomImgManager.VerifyCodeBean;
import com.joyveb.jkpmall.cache.ParaSysparameCache;
import java.io.IOException;
import java.io.Serializable;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author yangqiju
 * @date 2018-03-07 下午4:44
 */
@Slf4j
@Controller
@RequestMapping("/captcha")
public class CaptchaCtrl {

	public static final String SESSION_GENERATED_CAPTCHA_KEY = "m_captcha";

	@Autowired
	private ParaSysparameCache paraSysparameCache;

	@RequestMapping(value = "/pic_code", method = RequestMethod.GET)
	public void picCode(HttpServletRequest req, HttpServletResponse resp) {
		VerifyCodeBean bean = RandomImgManager.getVerifyCode();

		resp.setContentType("image/jpeg");
		resp.setDateHeader("expries", -1);
		resp.setHeader("Cache-Control", "no-cache");
		resp.setHeader("Pragma", "no-cache");

		CodeCheckSessionBean sessionBean = new CodeCheckSessionBean(System.currentTimeMillis(),
			bean.getCode().toLowerCase()
		);

		req.getSession(true).setAttribute(SESSION_GENERATED_CAPTCHA_KEY, sessionBean);

		try {
			ImageIO.write(bean.getBufferedImage(), "jpg", resp.getOutputStream());
			resp.getOutputStream().close();
		} catch (IOException e) {
			log.warn("刷新验证码异常", e);
		}
	}

	@Data
	public static class CodeCheckBean {

		private String code;
	}

	@Data
	@AllArgsConstructor
	public static class CodeCheckSessionBean implements Serializable {

		private long createTime;
		private String code;
	}


}

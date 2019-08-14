package com.joyveb.redeem.config;

import com.unlto.twls.commonutil.component.HttpClientComponent;
import com.yunpian.sdk.YunpianClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

/**
 * 应用配置类
 * @author system
 */

@Configuration
public class ApplicationConfig {
	@Bean(initMethod = "init", destroyMethod = "close")
	public YunpianClient yunpianClient() {
		String apikey = "2103e65e0f95605fe3a896a309e1fc4e";
		YunpianClient clnt = new YunpianClient(apikey);
		return clnt;
	}

	@Bean(initMethod = "init", destroyMethod = "destroy")
	public HttpClientComponent httpClientComponent() {
		return new HttpClientComponent();
	}


	/**
	 * 随机生成六位验证码
	 * @return
	 */
	public String getVerifyCode() {
		String verifyCode = String
				.valueOf(new Random().nextInt(899999) + 100000);
		return verifyCode;
	}
}

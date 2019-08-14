package com.joyveb.redeem.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Saxon
 * @date 2018/7/16
 */
@Data
@Configuration
@PropertySource("classpath:wechat-config.properties")
public class WeChatConfig {

	@Value("${token}")
	private String token;
	@Value("${gzhMchAppid}")
	private String gzhMchAppid;
	@Value("${xcxMchAppid}")
	private String xcxMchAppid;
	@Value("${appSecret}")
	private String appSecret;
	@Value("${accessTokenUrl}")
	private String accessTokenUrl;
	@Value("${hqxcMchid}")
	private String hqxcMchid;
	@Value("${cxhlMchid}")
	private String cxhlMchid;
	@Value("${checkName}")
	private String checkName;
	@Value("${ip}")
	private String ip;
	@Value("${hqxcCertPath}")
	private String hqxcCertPath;
	@Value("${cxhlCertPath}")
	private String cxhlCertPath;
	@Value("${hqxcKey}")
	private String hqxcKey;
	@Value("${cxhlKey}")
	private String cxhlKey;
	@Value("${payUrl}")
	private String payUrl;
	@Value("${tempMsgUrl}")
	private String tempMsgUrl;
	@Value("${tempId}")
	private String tempId;
	@Value("${webLicenseUrl}")
	private String webLicenseUrl;
	@Value("${webLoginCheckUrl}")
	private String webLoginCheckUrl;
	@Value("${webAntiFraudUrl}")
	private String webAntiFraudUrl;
}

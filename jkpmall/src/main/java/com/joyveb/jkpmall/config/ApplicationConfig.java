package com.joyveb.jkpmall.config;

import com.joyveb.jkpmall.cache.Cacheable;
import com.joyveb.jkpmall.cache.ParaSysparameCache;
import com.joyveb.jkpmall.cache.SysParaControlCache;
import com.unlto.twls.commonutil.component.HttpClientComponent;
import com.yunpian.sdk.YunpianClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Saxon
 * @date 2018/7/31
 */
@Configuration
public class ApplicationConfig {


	@Autowired
	private ParaSysparameCache sysParaCache;
	@Autowired
	private SysParaControlCache sysParaControlCache;

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


	@Bean(initMethod = "reload")
	public Cacheable cacheable() {
		return new Cacheable() {
			@Override
			public void reload() throws Exception {
				sysParaCache.reload();
				sysParaControlCache.reload();
			}
		};
	}
}

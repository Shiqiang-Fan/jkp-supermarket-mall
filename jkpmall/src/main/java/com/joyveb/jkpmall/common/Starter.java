package com.joyveb.jkpmall.common;

import java.sql.DriverManager;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mafengge
 * @description 启动类
 * @date 15:52 2018/2/28
 */
@Slf4j
public class Starter implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			log.warn("[即开票渠道兑奖管理后台]开始启动");
			log.warn("[即开票渠道兑奖管理后台]启动完成");
		} catch (Exception e) {
			log.warn("[即开票渠道兑奖管理后台]启动异常", e);
			throw new IllegalArgumentException("启动异常", e);
		}

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		try {
			while (DriverManager.getDrivers().hasMoreElements()) {
				DriverManager.deregisterDriver(DriverManager.getDrivers().nextElement());
			}
		} catch (Exception e) {
			log.error("deregisterDriver关闭连接失败！", e);
		}
		log.warn("[即开票渠道兑奖管理后台]注销完成");
	}
}

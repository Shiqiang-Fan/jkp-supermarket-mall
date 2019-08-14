package com.joyveb.redeem;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author system
 * 启动类入口
 */
@SpringBootApplication
@MapperScan("com.joyveb.redeem.dao")
@Slf4j
public class RedeemApplication {

	public static void main(String[] args) {
		log.info("即开票商城开始启动.");
		SpringApplication.run(RedeemApplication.class, args);
		log.info("即开票商城启动成功.");
	}
}
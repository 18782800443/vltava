package com.testhuamou.vltava.config;


import com.testhuamou.monitor.sdk.MonitorConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class testhuamouMonitorConfig {

	@Value("${testhuamou.dmc.projectCode}")
	private String projectCode;
	@Value("${testhuamou.dmc.appCode}")
	private String appCode;

	@Bean
	public MonitorConfig monitorConfig() {
		MonitorConfig monitorConfig = new MonitorConfig(projectCode, appCode, false);
		monitorConfig.monitorInit();
		return monitorConfig;
	}

}

package com.dmall.vltava.config;


import com.dmall.monitor.sdk.MonitorConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DmallMonitorConfig {

	@Value("${dmall.dmc.projectCode}")
	private String projectCode;
	@Value("${dmall.dmc.appCode}")
	private String appCode;

	@Bean
	public MonitorConfig monitorConfig() {
		MonitorConfig monitorConfig = new MonitorConfig(projectCode, appCode, false);
		monitorConfig.monitorInit();
		return monitorConfig;
	}

}

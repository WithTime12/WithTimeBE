package org.withtime.be.withtimebe.global.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {

	@Bean(name = "logTaskExecutor")
	public Executor logTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(1);
		executor.setMaxPoolSize(3);
		executor.setQueueCapacity(5);
		executor.setThreadNamePrefix("Executor-Log-");
		executor.initialize();
		return executor;
	}

	@Bean(name = "weatherTaskExecutor")
	public Executor weatherTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(1);
		executor.setMaxPoolSize(3);
		executor.setQueueCapacity(5);
		executor.setThreadNamePrefix("Executor-Weather-");
		executor.initialize();
		return executor;
	}
}

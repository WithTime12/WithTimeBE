package org.withtime.be.withtimebe;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import jakarta.annotation.PostConstruct;

@EnableScheduling
@EnableJpaAuditing
@EnableJpaRepositories(
    basePackages = {"org.withtime.be"},
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = "org\\.withtime\\.be\\.withtimebe\\.domain\\.log\\..*"
    ))
@SpringBootApplication
public class WithTimeBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(WithTimeBeApplication.class, args);
    }

    @PostConstruct
    public void init() { TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul")); } // JVM 기본 TimeZone 설정
}

package org.withtime.be.withtimebe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WithTimeBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(WithTimeBeApplication.class, args);
    }

}

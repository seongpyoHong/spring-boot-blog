package com.sphong;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static org.springframework.boot.SpringApplication.run;

@EnableJpaAuditing
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        run(Application.class,args);
    }
}

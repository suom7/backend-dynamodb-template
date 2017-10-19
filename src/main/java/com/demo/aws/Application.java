package com.demo.aws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.demo.aws", "com.demo.aws.dao", "com.demo.aws.db", "com.demo.aws.domain",
        "com.demo.aws.security", "com.demo.aws.service", "com.demo.aws.web"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

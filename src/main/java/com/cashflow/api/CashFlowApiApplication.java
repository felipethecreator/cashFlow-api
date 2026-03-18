package com.cashflow.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CashFlowApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CashFlowApiApplication.class, args);
    }
}

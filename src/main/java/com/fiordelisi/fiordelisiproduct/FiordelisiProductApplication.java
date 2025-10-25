package com.fiordelisi.fiordelisiproduct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class FiordelisiProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(FiordelisiProductApplication.class, args);
    }

}

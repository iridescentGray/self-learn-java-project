package com.zjy.retry;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author zjy
 */
@EnableAsync
@EnableRetry
@SpringBootApplication
public class RetryApplication {

    public static void main(String[] args) {
        SpringApplication.run(RetryApplication.class, args);
    }

}
package com.learn.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author zjy
 */
@EnableCaching
@SpringBootApplication
public class CacheLearnApplication {

    public static void main(String[] args) {
        SpringApplication.run(CacheLearnApplication.class, args);
    }

}

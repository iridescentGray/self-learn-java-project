package com.zjy.retry.service.impl;

import com.zjy.retry.service.RetryAbleService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class RetryAbleServiceImplTest {

    @Resource
    private RetryAbleService retryAbleService;

    @Test
    void testRetry() throws InterruptedException {
        retryAbleService.testRetry();
        retryAbleService.testRetry();
        retryAbleService.testRetry();
        Thread.sleep(10000);
    }
}
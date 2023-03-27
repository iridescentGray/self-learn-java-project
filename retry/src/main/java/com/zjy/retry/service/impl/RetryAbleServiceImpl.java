package com.zjy.retry.service.impl;

import com.zjy.retry.service.RetryAbleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author zjy
 */
@Slf4j
@Service
public class RetryAbleServiceImpl implements RetryAbleService {

    @Override
    @Retryable
    @Async
    public String testRetry() {
        log.info("Retry execute");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }
}

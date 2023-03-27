package com.learn.cache.service.impl;

import com.learn.cache.domain.User;
import com.learn.cache.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 *
 * @author zjy
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "student")
public class StudentServiceImpl implements StudentService {

    @Override
    @Cacheable(cacheNames = "user", key = "#id")
    public User getUserById(Long id) {
        log.info("execute getUserById, param is {}",id);
        User user = new User();
        user.setId(id);
        return user;
    }

}

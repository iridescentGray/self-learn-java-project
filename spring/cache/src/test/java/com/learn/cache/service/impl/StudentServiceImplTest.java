package com.learn.cache.service.impl;

import com.learn.cache.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class StudentServiceImplTest {

    @Resource
    private StudentService studentService;

    @Test
    void getUserById() {
        System.out.println(studentService.getUserById(1L));
        System.out.println(studentService.getUserById(1L));
        System.out.println(studentService.getUserById(1L));
    }
}
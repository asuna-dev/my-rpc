package org.zepe.rpc.springboot.example.comsumer.service;

import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zepe.rpc.example.common.model.User;

/**
 * @author zzpus
 * @datetime 2025/4/27 15:29
 * @description
 */
@SpringBootTest
class TestServiceTest {

    @Autowired
    private TestService testService;

    @Test
    public void testUser() {
        String name = "test";
        User user = testService.getUser(name);
        Assert.equals(user.getName(), name);
    }

    @Test
    public void testAdd() {
        int x = 123213;
        int y = 3422349;

        int add = testService.add(x, y);
        Assert.equals(add, x + y);
    }

}
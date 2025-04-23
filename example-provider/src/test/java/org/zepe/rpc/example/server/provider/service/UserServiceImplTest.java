package org.zepe.rpc.example.server.provider.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.zepe.rpc.example.common.model.User;
import org.zepe.rpc.example.provider.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author zzpus
 * @datetime 2025/4/23 11:43
 * @description
 */
@RunWith(MockitoJUnitRunner.class)
class UserServiceImplTest {

    UserServiceImpl userService = new UserServiceImpl();

    @Test
    void getUserByName() {
        String name = "test";
        User user = userService.getUserByName(name);
        assertEquals(name, user.getName());
    }
}
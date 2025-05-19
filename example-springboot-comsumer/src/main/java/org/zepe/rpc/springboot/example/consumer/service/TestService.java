package org.zepe.rpc.springboot.example.consumer.service;

import org.springframework.stereotype.Service;
import org.zepe.rpc.example.common.model.User;
import org.zepe.rpc.example.common.service.ComputeService;
import org.zepe.rpc.example.common.service.UserService;
import org.zepe.rpc.springboot.starter.annotation.RpcReference;

/**
 * @author zzpus
 * @datetime 2025/4/27 15:27
 * @description
 */
@Service
public class TestService {
    @RpcReference(serviceVersion = "test.UserService")
    private UserService userService;

    @RpcReference(serviceVersion = "test.ComputeService")
    private ComputeService computeService;

    public User getUser(String name) {
        return userService.getUserByName(name);
    }

    public int add(int x, int y) {
        return computeService.add(x, y);
    }
}

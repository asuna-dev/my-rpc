package org.zepe.rpc.springboot.example.provider.service;

import lombok.extern.slf4j.Slf4j;
import org.zepe.rpc.example.common.model.User;
import org.zepe.rpc.example.common.service.UserService;
import org.zepe.rpc.springboot.starter.annotation.RpcService;

/**
 * @author zzpus
 * @datetime 2025/4/27 15:19
 * @description
 */
@Slf4j
@RpcService(serviceVersion = "test.UserService")
public class UserSvcImpl implements UserService {
    @Override
    public User getUserByName(String name) {
        log.info("getUserByName name={}", name);
        return new User(name);
    }
}

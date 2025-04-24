package org.zepe.rpc.example.provider;

import lombok.extern.slf4j.Slf4j;
import org.zepe.rpc.example.common.model.User;
import org.zepe.rpc.example.common.service.UserService;

/**
 * @author zzpus
 * @datetime 2025/4/23 11:21
 * @description
 */
@Slf4j
public class UserServiceImpl implements UserService {

    @Override
    public User getUserByName(String name) {
        log.info("getUserByName: {}", name);
        return new User(name);
    }
}

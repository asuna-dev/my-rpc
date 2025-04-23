package org.zepe.rpc.example.provider;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.zepe.rpc.example.common.model.User;
import org.zepe.rpc.example.common.service.UserService;

/**
 * @author zzpus
 * @datetime 2025/4/23 11:21
 * @description
 */

public class UserServiceImpl implements UserService {
    //推荐创建不可变静态类成员变量
    private static final Log log = LogFactory.get();

    @Override
    public User getUserByName(String name) {
        log.info("getUserByName: {}", name);
        return new User(name);
    }
}

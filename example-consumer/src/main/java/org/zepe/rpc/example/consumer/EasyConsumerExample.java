package org.zepe.rpc.example.consumer;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.zepe.rpc.example.common.model.User;
import org.zepe.rpc.example.common.service.UserService;

/**
 * @author zzpus
 * @datetime 2025/4/23 11:55
 * @description
 */
public class EasyConsumerExample {
    private static final Log log = LogFactory.get();

    public static void main(String[] args) {

        //        UserService userService = new UserServiceStaticProxy();
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);

        User user = userService.getUserByName("test");

        log.info("User: {}", user);
    }
}

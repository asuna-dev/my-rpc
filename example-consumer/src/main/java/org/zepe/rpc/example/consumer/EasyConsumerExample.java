package org.zepe.rpc.example.consumer;

import lombok.extern.slf4j.Slf4j;
import org.zepe.rpc.example.common.model.User;
import org.zepe.rpc.example.common.service.UserService;
import org.zepe.rpc.proxy.ServiceProxyFactory;

/**
 * @author zzpus
 * @datetime 2025/4/23 11:55
 * @description
 */
@Slf4j
public class EasyConsumerExample {

    public static void main(String[] args) throws InterruptedException {

        //        UserService userService = new UserServiceStaticProxy();
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        //        UserService userService = ServiceProxyFactory.getMockProxy(UserService.class);

        User user = userService.getUserByName("test");

        log.info("User: {}", user);

        Thread thread = new Thread(() -> {
            while (true) {
                log.info("heartbeat");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
        thread.join();
    }
}

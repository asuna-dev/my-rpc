package org.zepe.rpc.example.consumer;

import lombok.extern.slf4j.Slf4j;
import org.zepe.rpc.bootstrap.ConsumerBootstrap;
import org.zepe.rpc.example.common.service.ComputeService;
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

        ConsumerBootstrap.init();

        UserService userService = ServiceProxyFactory.getProxy(UserService.class);

        ComputeService computeService = ServiceProxyFactory.getProxy(ComputeService.class);

        log.info("getUserByName: {}", userService.getUserByName("test"));

        log.info("add: {}", computeService.add(1, 2, 3, 4));
    }
}

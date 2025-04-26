package org.zepe.rpc.loadbalancer.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.zepe.rpc.loadbalancer.LoadBalancer;
import org.zepe.rpc.loadbalancer.LoadBalancerFactory;
import org.zepe.rpc.model.ServiceMetaInfo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zzpus
 * @datetime 2025/4/26 22:21
 * @description
 */
@Slf4j
public class LoadBalancerTest {
    static Map<String, Object> requestParams;
    static List<ServiceMetaInfo> serviceMetaInfoList;

    LoadBalancer loadBalancer;

    static // 请求参数
    {
        requestParams = new HashMap<>();
        requestParams.put("methodName", "apple");
        // 服务列表
        ServiceMetaInfo serviceMetaInfo1 = new ServiceMetaInfo();
        serviceMetaInfo1.setServiceName("myService");
        serviceMetaInfo1.setServiceVersion("1.0");
        serviceMetaInfo1.setServiceHost("localhost");
        serviceMetaInfo1.setServicePort(1234);
        ServiceMetaInfo serviceMetaInfo2 = new ServiceMetaInfo();
        serviceMetaInfo2.setServiceName("myService");
        serviceMetaInfo2.setServiceVersion("1.0");
        serviceMetaInfo2.setServiceHost("yupi.icu");
        serviceMetaInfo2.setServicePort(80);

        serviceMetaInfoList = Arrays.asList(serviceMetaInfo1, serviceMetaInfo2);
    }

    @Test
    public void testRandom() {
        loadBalancer = LoadBalancerFactory.getInstance("random");

        // 连续调用  次
        for (int i = 0; i < 5; i++) {
            ServiceMetaInfo serviceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);
            log.info("{}", serviceMetaInfo.getServiceNodeKey());
            Assert.assertNotNull(serviceMetaInfo);
        }
    }

    @Test
    public void testRoundRobin() {
        loadBalancer = LoadBalancerFactory.getInstance("roundrobin");

        // 连续调用  次
        for (int i = 0; i < 5; i++) {
            ServiceMetaInfo serviceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);
            log.info("{}", serviceMetaInfo.getServiceNodeKey());
            Assert.assertNotNull(serviceMetaInfo);
        }
    }

    @Test
    public void testConsistentHash() {
        loadBalancer = LoadBalancerFactory.getInstance("consistentHash");
        Map<String, Object> requestParams1 = new HashMap<>();
        requestParams1.put("methodName", "orange");
        // 连续调用  次
        for (int i = 0; i < 5; i++) {
            ServiceMetaInfo serviceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);
            log.info("{}", serviceMetaInfo.getServiceNodeKey());
            Assert.assertNotNull(serviceMetaInfo);

            serviceMetaInfo = loadBalancer.select(requestParams1, serviceMetaInfoList);
            log.info("{}", serviceMetaInfo.getServiceNodeKey());
            Assert.assertNotNull(serviceMetaInfo);
        }
    }

}


package org.zepe.rpc.registry;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.zepe.rpc.RpcApplication;
import org.zepe.rpc.config.RegistryConfig;
import org.zepe.rpc.model.ServiceMetaInfo;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author zzpus
 * @datetime 2025/4/24 22:03
 * @description
 */
@Slf4j
public class EtcdRegistryTest {
    Registry registry = new EtcdRegistry();

    @Before
    public void init() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setRegistry(RegistryKeys.ETCD);
        registryConfig.setAddress("http://127.0.0.1:2379");
        registry.init(registryConfig);
    }

    @After
    public void close() {
        registry.destroy();
    }

    @Test
    public void test() throws Exception {
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("test-service");
        serviceMetaInfo.setServiceVersion("1.2.3");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(9999);

        registry.register(serviceMetaInfo);

        serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("test-service");
        serviceMetaInfo.setServiceVersion("1.2.3");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(5555);
        registry.register(serviceMetaInfo);

        log.info("register");
        List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
        for (ServiceMetaInfo metaInfo : serviceMetaInfos) {
            log.info("{}", metaInfo.getServiceNodeKey());
        }

        log.info("unregister");
        serviceMetaInfo.setServicePort(5555);
        registry.unregister(serviceMetaInfo);

        serviceMetaInfos = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
        for (ServiceMetaInfo metaInfo : serviceMetaInfos) {
            log.info("{}", metaInfo.getServiceNodeKey());
        }

        Thread.sleep(60000);
    }

}
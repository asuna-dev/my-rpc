package org.zepe.rpc.registry;

import org.zepe.rpc.config.RegistryConfig;
import org.zepe.rpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * @author zzpus
 * @datetime 2025/4/24 20:36
 * @description
 */
public interface Registry {
    void init(RegistryConfig registryConfig);

    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    void unregister(ServiceMetaInfo serviceMetaInfo) throws Exception;

    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    void destroy();
}

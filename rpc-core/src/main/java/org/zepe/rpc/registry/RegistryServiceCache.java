package org.zepe.rpc.registry;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.zepe.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author zzpus
 * @datetime 2025/4/25 00:09
 * @description
 */
@Slf4j
public class RegistryServiceCache {
    Map<String, Map<String, ServiceMetaInfo>> serviceCache = new ConcurrentHashMap<>();

    public void write(ServiceMetaInfo serviceMetaInfo) {
        String serviceKey = serviceMetaInfo.getServiceKey();
        Map<String, ServiceMetaInfo> cache = serviceCache.getOrDefault(serviceKey, new ConcurrentSkipListMap<>());
        cache.put(serviceMetaInfo.getServiceNodeKey(), serviceMetaInfo);
        serviceCache.put(serviceKey, cache);
    }

    public void write(List<ServiceMetaInfo> serviceMetaInfos) {
        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfos) {
            write(serviceMetaInfo);
        }
    }

    public List<ServiceMetaInfo> read(String serviceKey) {
        Map<String, ServiceMetaInfo> serviceMetaInfos = serviceCache.get(serviceKey);
        if (CollUtil.isEmpty(serviceMetaInfos)) {
            return CollUtil.newArrayList();
        }
        return serviceMetaInfos.values().stream().toList();
    }

    public void remove(String serviceNodeKey) {
        serviceCache.computeIfPresent(ServiceMetaInfo.getSvcKeyFromSvcNodeKey(serviceNodeKey), (key, cache) -> {
            ServiceMetaInfo remove = cache.remove(serviceNodeKey);
            return cache;
        });
    }

    public void remove(ServiceMetaInfo serviceMetaInfo) {
        remove(serviceMetaInfo.getServiceNodeKey());
    }

    public void clearAll() {
        serviceCache.clear();
    }

}

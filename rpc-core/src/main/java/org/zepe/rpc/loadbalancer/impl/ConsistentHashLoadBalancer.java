package org.zepe.rpc.loadbalancer.impl;

import cn.hutool.core.collection.CollUtil;
import org.zepe.rpc.loadbalancer.LoadBalancer;
import org.zepe.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author zzpus
 * @datetime 2025/4/26 18:45
 * @description
 */
public class ConsistentHashLoadBalancer implements LoadBalancer {
    private final NavigableMap<Integer, ServiceMetaInfo> virtualNodes = new ConcurrentSkipListMap<>();
    private static final int VIRTUAL_NODE_NUM = 100;

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfos) {
        if (CollUtil.isEmpty(serviceMetaInfos)) {
            return null;
        }

        if (serviceMetaInfos.size() == 1) {
            return serviceMetaInfos.get(0);
        }

        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfos) {
            for (int i = 0; i < VIRTUAL_NODE_NUM; i++) {
                int hash = getHash(serviceMetaInfo.getServiceAddress() + "+" + i);
                virtualNodes.put(hash, serviceMetaInfo);
            }
        }

        int hash = getHash(requestParams);

        Map.Entry<Integer, ServiceMetaInfo> entry = virtualNodes.ceilingEntry(hash);

        if (entry == null) {
            entry = virtualNodes.firstEntry();
        }

        return entry.getValue();
    }

    private int getHash(Object key) {
        return key.hashCode();
    }
}

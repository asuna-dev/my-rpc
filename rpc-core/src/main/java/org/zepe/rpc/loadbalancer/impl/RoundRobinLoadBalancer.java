package org.zepe.rpc.loadbalancer.impl;

import cn.hutool.core.collection.CollUtil;
import org.zepe.rpc.loadbalancer.LoadBalancer;
import org.zepe.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zzpus
 * @datetime 2025/4/26 18:31
 * @description
 */
public class RoundRobinLoadBalancer implements LoadBalancer {

    private final AtomicInteger curIndex = new AtomicInteger(0);

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfos) {
        if (CollUtil.isEmpty(serviceMetaInfos)) {
            return null;
        }
        int size = serviceMetaInfos.size();
        if (size == 1) {
            return serviceMetaInfos.get(0);
        }

        int current = curIndex.getAndIncrement();
        if (current > Integer.MAX_VALUE - 10000) {
            curIndex.getAndSet(current + 1);
        }
        int index = current % size;

        return serviceMetaInfos.get(index);
    }
}

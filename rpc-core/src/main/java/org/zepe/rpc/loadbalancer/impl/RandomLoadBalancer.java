package org.zepe.rpc.loadbalancer.impl;

import cn.hutool.core.collection.CollUtil;
import org.zepe.rpc.loadbalancer.LoadBalancer;
import org.zepe.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author zzpus
 * @datetime 2025/4/26 18:43
 * @description
 */
public class RandomLoadBalancer implements LoadBalancer {
    private final Random random = new Random();

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfos) {
        if (CollUtil.isEmpty(serviceMetaInfos)) {
            return null;
        }
        int size = serviceMetaInfos.size();
        if (size == 1) {
            return serviceMetaInfos.get(0);
        }

        int index = random.nextInt(size);

        return serviceMetaInfos.get(index);
    }
}

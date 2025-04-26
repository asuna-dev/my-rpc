package org.zepe.rpc.loadbalancer;

import org.zepe.rpc.model.ServiceMetaInfo;

import java.util.Map;
import java.util.List;

/**
 * @author zzpus
 * @datetime 2025/4/26 18:28
 * @description
 */
public interface LoadBalancer {
    ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfos);
}

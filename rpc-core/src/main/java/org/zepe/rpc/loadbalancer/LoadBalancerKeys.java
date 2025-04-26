package org.zepe.rpc.loadbalancer;

import org.zepe.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * @author zzpus
 * @datetime 2025/4/26 18:28
 * @description
 */
public interface LoadBalancerKeys {
    String ROUND_ROBIN = "roundRobin";
    String RANDOM = "random";
    String CONSISTENT_HASH = "consistentHash";
}

package org.zepe.rpc.loadbalancer;

import org.zepe.rpc.registry.Registry;
import org.zepe.rpc.spi.SpiLoader;

/**
 * @author zzpus
 * @datetime 2025/4/26 21:59
 * @description
 */
public class LoadBalancerFactory {
    static {
        SpiLoader.load(LoadBalancer.class);
    }

    private LoadBalancerFactory() {
    }

    public static LoadBalancer getInstance(String key) {
        return SpiLoader.getInstance(LoadBalancer.class, key);
    }
}
